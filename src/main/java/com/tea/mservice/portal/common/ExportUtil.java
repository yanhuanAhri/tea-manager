package com.tea.mservice.portal.common;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExportUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ExportUtil.class);

    public static void exportExcel(List<?> data,
                             String startTime,
                             String endTime,
                             String[] keyColumnNameHeader,
                             String[] valueColumnNameHeader,
                             String keyField,
                             String[] keyFields,
                             String statTimeField,
                             String[] statTimeFields,
                             String fileName,
                             HttpServletResponse response) {
        Map<String, Map<String, List<Object>>> rowMap = keyForRowAndStatTimeForColumnDataMap(
                data,
                startTime.length() == endTime.length() && endTime.length() == 6 ? getStatTimes(startTime, endTime) : getStatTimesForMonth(startTime, endTime),
                keyColumnNameHeader,
                valueColumnNameHeader,
                keyField,
                keyFields,
                statTimeField,
                statTimeFields);
        fileName = fileName + "(" + startTime + "_" + endTime + ").xlsx";
        exportExcel(rowDataMap(rowMap), fileName, response);
    }
    private static void exportExcel(List<List<Object>>data, String fileName, HttpServletResponse response) {
        try (ServletOutputStream sos = response.getOutputStream();
             Workbook workbook = new SXSSFWorkbook(1000)){
            Sheet sheet = workbook.createSheet();
            setContentForSheet(data, sheet);
            fileName = response.encodeURL(new String(fileName.getBytes(),"iso8859-1"));			//保存的文件名,必须和页面编码一致,否则乱码
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            workbook.write(sos);
        }catch (Exception e) {
            LOG.error("数据统计导出失败");
            LOG.error(e.getMessage(), e);
        }
    }

    private static Map<String, Map<String, List<Object>>> keyForRowAndStatTimeForColumnDataMap(List<?> res,
                                                                                               List<String> statTimes,
                                                                                        String[] keyColumnNameHeader,
                                                                                        String[] valueColumnNameHeader,
                                                                                        String keyField,
                                                                                        String[] keyFields,
                                                                                        String statTimeField,
                                                                                        String[] statTimeFields) {
        Map<String, Map<String, List<Object>>> rowMap = new LinkedHashMap<>();
        String timeKey = "statTime";
        Map<String, List<Object>> statTimeMap = rowMap.computeIfAbsent(timeKey, s -> new LinkedHashMap<>());
        statTimeMap.put(timeKey, Collections.nCopies(keyColumnNameHeader.length, ""));
        //		字段名称行
        String columnNameKey = "columnName";
        Map<String, List<Object>> columnNameMap = rowMap.computeIfAbsent(columnNameKey, s -> new LinkedHashMap<>());
        columnNameMap.put(columnNameKey, Arrays.asList(keyColumnNameHeader));

        statTimes.forEach(statTime -> {
            List<Object> list = new ArrayList<>(valueColumnNameHeader.length);
            list.add(statTime);
            for (int i = valueColumnNameHeader.length; i > 1; i--)
                list.add("");
            statTimeMap.put(statTime, list);

            columnNameMap.put(statTime, Arrays.asList(valueColumnNameHeader));
        });

        res.forEach(o -> {
            try {
                Method keyMethod = o.getClass().getDeclaredMethod("get" + captureName(keyField));
                Object rowKey = keyMethod.invoke(o);
                Map<String, List<Object>> columnGroupMap = rowMap.computeIfAbsent(rowKey.toString(), s -> {
                    Map<String, List<Object>> timeMap = new LinkedHashMap<>();
//				行的公共key
                    List<Object> keyList = new ArrayList<>(keyFields.length);
                    for (String field : keyFields) {
                        try {
                            Method method = o.getClass().getDeclaredMethod("get" + captureName(field));
                            Object value = method.invoke(o);
                            keyList.add(value == null ? "无" : value);
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                    timeMap.put(s, keyList);
//				行的日期数据
                    statTimes.forEach(s1 -> timeMap.put(s1, Collections.nCopies(statTimeFields.length, "无")));
                    return timeMap;
                });

                Method statTimeMethod = o.getClass().getDeclaredMethod("get" + captureName(statTimeField));
                Object rowStatTime = statTimeMethod.invoke(o);
                List<Object> group = new ArrayList<>(statTimeFields.length);
                for (String field : statTimeFields) {
                    Method method = o.getClass().getDeclaredMethod("get" + captureName(field));
                    Object value = method.invoke(o);
                    group.add(value);
                }
                columnGroupMap.put(rowStatTime.toString(), group);

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        return rowMap;
    }

    private static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);

    }

    /**
     *
     * @param rowMap{key:{statTime:[fields]}
     * @return
     */
    private static List<List<Object>> rowDataMap(Map<String, Map<String, List<Object>>> rowMap) {
        List<List<Object>> rowData = new ArrayList<>();
        rowMap
                .forEach((s, stringListMap) -> rowData
                        .add(stringListMap
                                .values()
                                .stream()
                                .reduce(new ArrayList<>(),(objects, objects2) -> { objects.addAll(objects2);return objects; })
                        )
                );
        return rowData;
    }

    /**
     * 获取统计时间轴
     * @param startTimeS
     * @param endTimeS
     * @return
     */
    private static List<String> getStatTimes(String startTimeS, String endTimeS) {
        List<String> statTimes = new ArrayList<>();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date startTime = format.parse(startTimeS);
            Date endTime = format.parse(endTimeS);
            for (; startTime.getTime() <= endTime.getTime() ; startTime = DateUtils.addDays(startTime, 1)) {
                String statTime = DateFormatUtils.format(startTime, "yyyyMMdd");
                statTimes.add(statTime);
            }
        } catch (ParseException e) {
            LOG.error("导出时间不符合格式=yyyyMMdd");
            LOG.error(e.getMessage(), e);

            return statTimes;
        }
        return statTimes;
    }

    private static List<String> getStatTimesForMonth(String startTimeS, String endTimeS) {
        List<String> statTimes = new ArrayList<>();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
            Date startTime = format.parse(startTimeS);
            Date endTime = format.parse(endTimeS);
            for (; startTime.getTime() <= endTime.getTime() ; startTime = DateUtils.addMonths(startTime, 1)) {
                String statTime = DateFormatUtils.format(startTime, "yyyyMM");
                statTimes.add(statTime);
            }
        } catch (ParseException e) {
            LOG.error("导出时间不符合格式=yyyyMM");
            LOG.error(e.getMessage(), e);
            return statTimes;
        }
        return statTimes;
    }

    private static void setContentForSheet(List<List<Object>> rowData, Sheet sheet) {
        int iMax = rowData.size();
        for (int i = 0; i < iMax; i++) {
            Row row = sheet.createRow(i);
            List<Object> columnData = rowData.get(i);
            if (columnData == null || columnData.isEmpty())
                continue;
            int jMax = columnData.size();
            for (int j = 0; j < jMax; j++) {
                Object o = columnData.get(j);
                //				空列丢弃
                Cell cell = row.createCell(j);
                if (o instanceof String)
                    cell.setCellValue((String) o);
                else if (o instanceof Integer)
                    cell.setCellValue((Integer) o);
                else if (o instanceof Double)
                    cell.setCellValue((Double) o);
                else if (o instanceof Long)
                    cell.setCellValue((Long) o);
                else
                    cell.setCellValue("");
            }
        }
    }
}
