package com.tea.mservice.portal.commodity.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.tea.mservice.config.shiro.UserRealm;
import com.tea.mservice.config.shiro.UserRealm.ShiroUser;
import com.tea.mservice.core.util.ShiroUserUtil;
import com.tea.mservice.portal.commodity.repository.CommodityImgRepository;
import com.tea.mservice.portal.commodity.repository.CommodityRepository;
import com.tea.mservice.portal.commodity.util.SerialUtil;
import com.tea.mservice.portal.commodity.vo.CommodityVo;
import com.tea.mservice.portal.entity.Commodity;
import com.tea.mservice.portal.entity.CommodityImg;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class CommodityService {
    private static final Logger LOG = LoggerFactory.getLogger(CommodityService.class);


    
    
    @Value("${img.url}")
	private String imgUrl;
    
    @Autowired
	private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private CommodityRepository commodityRepository;
    
    @Autowired
    private CommodityImgRepository commodityImgRepository;
    
    private final String COVER = "cover";
    private final String DETAIL = "detail";
    private final String PARTICULAR = "particular";
    
    //封面路径
    private final static String COVER_PATH = "/cover/";   
    //小图
    private final static String DETAIL_PATH = "/detail/";  
    //详情
    private final static String PARTICULAR_PATH = "/particular/";  
    
    
    
    public Map<String,Object> page(Integer pageNumber,Integer pageSize,String filter){
    	
    	Map<String,Object> map = new HashMap<>();
    	String sql = "select id,commodity_num as commodityNum,trade_name as tradeName,product_num as productNum,tea_name as teaName,"
    			+ "market_price as marketPrice,product_type as productType,goods_grade as goodsGrade,purpose,"
    			+ "specification,origin_place as originPlace,expiration_data as expirationData,"
    			+ "sold_out_num as soldOutNum,update_time as updateTime,repertory_status as "
    			+ "repertoryStatus,`status` from commodity ";
    	
    	String countSql  = "select count(1) as total from commodity ";
    	StringBuilder where = new StringBuilder(" where status !=99 ");
    	String offsets = " LIMIT ? OFFSET ?";
    	List<Object> args = new ArrayList<>();
    	try{	
	    	//过滤条件
	    	if(filter != null){
	    	
	    		JSONObject jsonObject=JSONObject.fromObject(filter);
	    	
	    		
	    		if(jsonObject.get("commodityNum") != null){
	
	    			where.append(" AND commodity_num LIKE ?\n");
					args.add("%" + jsonObject.get("commodityNum") + "%");
		
	    		}
	    		
	    		if(jsonObject.get("repertoryStatus") != null){
	 
	    			where.append(" AND repertory_status = ?\n");
					args.add(jsonObject.get("repertoryStatus"));
	    		}
	    	
	    		if(jsonObject.get("status") != null){
	    			where.append(" AND status = ?\n");
					args.add(jsonObject.get("status"));
	    		}
	    	
	    	
	    	}
	    	
	    	//总数
	    	Long total  = jdbcTemplate.queryForObject(countSql + where, Long.class, args.toArray());
	
			where.append(" order by update_time desc ").append(offsets);
			args.add(pageSize);
			args.add(pageNumber * pageSize);
	    	
			
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sql+ where,args.toArray());
	    	
	    	
			map.put("data", list);
			map.put("total", total);
    	}catch(Exception e){
    		LOG.error(e.getMessage(),e);
    		throw e;
    	}
		
		return map;
    	
    }
    
	/**
	 * 更新商品
	 */
	public void edit(Long id,String commodityJson) {
		
		JSONObject jsonObject=JSONObject.fromObject(commodityJson);
    	
		
		
		Commodity cy = commodityRepository.findOne(id);
		
    	cy.setStatus(1);
    	if(jsonObject.getString("type").equals("draft")){
    		cy.setStatus(0);
    	}
		
    	ShiroUser user = ShiroUserUtil.getUser();
    	cy.setTradeName(jsonObject.getString("tradeName"));
    	cy.setTeaName(jsonObject.getString("teaName"));
    	cy.setProductType(jsonObject.getString("productType"));
    	cy.setPickYear(jsonObject.getString("pickYear"));
    	cy.setPickSeason(jsonObject.getString("pickSeason"));
    	cy.setGoodsGrade(jsonObject.getString("goodsGrade"));
    	cy.setNetContent(Integer.parseInt(jsonObject.getString("netContent")));
    	cy.setOriginPlace(jsonObject.getString("originPlace"));
    	cy.setFoodProductionLicence(jsonObject.getString("foodProductionLicence"));
    	cy.setPurpose(Integer.parseInt(jsonObject.getString("purpose")));
    	String tradeName = " 【"+cy.getTradeName() + "】"+cy.getTeaName()+" "+cy.getPickYear()+" "+ cy.getPickSeason() +
    			cy.getProductType() + " " + cy.getGoodsGrade() +" "+cy.getNetContent() +"克"+ (cy.getPurpose()==1?"自饮":"礼盒");
    	cy.setTradeName(jsonObject.getString("tradeName"));
    	cy.setSearch(tradeName);
    	
    	cy.setRepertoryStatus(Integer.parseInt(jsonObject.getString("repertory")));
    	cy.setSpecification(jsonObject.getString("specification"));
    	cy.setStoreMethod(jsonObject.getString("storeMethod"));
    	cy.setExpirationData(Integer.parseInt(jsonObject.getString("expirationData")));
    	cy.setCraft(jsonObject.getString("craft"));
    	cy.setProductNum(Long.parseLong(jsonObject.getString("productNum")));
    	cy.setMarketPrice(BigDecimal.valueOf(Double.parseDouble(jsonObject.getString("marketPrice"))));
    	cy.setPromotionPrice(BigDecimal.valueOf(Double.parseDouble(jsonObject.getString("promotionPrice"))));
    	cy.setSoldOutNum(0L);
    	cy.setPackingSpecification(jsonObject.getString("packingSpecification"));
    	Timestamp date = new Timestamp(new Date().getTime());
    	cy.setCreateTime(date);
    	cy.setUpdateTime(date);
    	cy.setCreateUserId(user.id);
    	cy.setUpdateUserId(user.id);
    	commodityRepository.save(cy);
    	
    	
    	CommodityImg img =  null;
    	
    	
    	String listSql = "select path FROM `commodity_img` where commodity_num  = ? and type =";
    	String sql = "delete FROM `commodity_img` where commodity_num  = '"+cy.getCommodityNum()+"' and type =";
    	
    	JSONArray coverArray = JSONArray.fromObject(jsonObject.getString("cover"));
    	JSONArray detailArray = JSONArray.fromObject(jsonObject.getString("detail"));
    	JSONArray particularArray = JSONArray.fromObject(jsonObject.getString("particular"));
    	
    	for(int i = 0;i<coverArray.size();i++){
    		Object coverObj = coverArray.get(i);
    		jsonObject = JSONObject.fromObject(coverObj);
    		
    		//是否是新图片上传
    		if(jsonObject.getString("path").indexOf("http")>-1){
    			break;
    		}
    		
    		if(i == 0){
    			//删除图片及数据
    			List<Map<String,Object>> list = jdbcTemplate.queryForList(listSql,new Object[]{cy.getCommodityNum(),1});
    			
    			list.stream().forEach(map -> {
    				deleteImg(map.get("path").toString());
    			});
    			
    			
    			jdbcTemplate.execute(sql+1);
    		}
    		
    		
    		
    		String path = saveImg(jsonObject.getString("path"),"cover",jsonObject.getString("name"));
    		img = new CommodityImg();
    		img.setCommodityId(cy.getId());
    		img.setCommodityNum(cy.getCommodityNum());
    		img.setType(1);
    		img.setPath(path);
    		img.setCreateTime(date);
    		img.setCreateUserId(user.id);
    		commodityImgRepository.save(img);
    		
    	}
    	
    	

    	for(int i = 0;i<detailArray.size();i++){
    		Object coverObj = detailArray.get(i);
    		jsonObject = JSONObject.fromObject(coverObj);
    		
    		if(jsonObject.getString("path").indexOf("http")>-1){
    			break;
    		}
    		
    		if(i == 0){
    			
    			//删除图片及数据
    			List<Map<String,Object>> list = jdbcTemplate.queryForList(listSql,new Object[]{cy.getCommodityNum(),2});
    			
    			list.stream().forEach(map -> {
    				deleteImg(map.get("path").toString());
    			});
    			jdbcTemplate.equals(sql+2);
    		}
    		
    		String path = saveImg(jsonObject.getString("path"),"detail",jsonObject.getString("name"));
    		img = new CommodityImg();
    		img.setCommodityId(cy.getId());
    		img.setCommodityNum(cy.getCommodityNum());
    		img.setType(2);
    		img.setPath(path);
    		img.setCreateTime(date);
    		img.setCreateUserId(user.id);
    		commodityImgRepository.save(img);
    		
    	}
    	
    	for(int i = 0;i<particularArray.size();i++){
    		Object coverObj = particularArray.get(i);
    		jsonObject = JSONObject.fromObject(coverObj);

    		if(jsonObject.getString("path").indexOf("http")>-1){
    			break;
    		}
    		
    		if(i == 0){
    			//删除图片及数据
    			List<Map<String,Object>> list = jdbcTemplate.queryForList(listSql,new Object[]{cy.getCommodityNum(),3});
    			
    			list.stream().forEach(map -> {
    				deleteImg(map.get("path").toString());
    			});
    			
    			
    			jdbcTemplate.equals(sql+3);
    		}
    		String path = saveImg(jsonObject.getString("path"),"particular",jsonObject.getString("name"));
    		img = new CommodityImg();
    		img.setCommodityId(cy.getId());
    		img.setCommodityNum(cy.getCommodityNum());
    		img.setType(3);
    		img.setPath(path);
    		img.setCreateTime(date);
    		img.setCreateUserId(user.id);
    		commodityImgRepository.save(img);
    		
    	}
    	
    	
    	
		
	}
	
	
	
	/**
	 * 上下架操作
	 * @param type
	 * @param commodityId
	 */
	public void operation(String type,Long commodityId) {
		
		Commodity commodity = commodityRepository.findOne(commodityId);
		
		if(commodity == null){
    		return;
    	}
		
		JSONObject jsonObject=JSONObject.fromObject(type);
		
		
		
		if(jsonObject.getString("type").equals("down")) {
			commodity.setStatus(2);
		}
		
		if(jsonObject.getString("type").equals("up")) {
			commodity.setStatus(1);
		}
		
		commodityRepository.save(commodity);
		
	}
    
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	public Map<String,Object> detail(Long commodityId){
		
		Map<String,Object> map = new HashMap<>();
		Commodity commodity = commodityRepository.findOne(commodityId);
		
		String sql = "SELECT path FROM `commodity_img` where commodity_num  = ? and type = ?";
		
		String commodityNum = commodity.getCommodityNum();
		List<Map<String,Object>> coverList = jdbcTemplate.queryForList(sql, new Object[]{commodityNum,1});
		List<Map<String,Object>> detailList =jdbcTemplate.queryForList(sql, new Object[]{commodityNum,2});
		List<Map<String,Object>> particularList =jdbcTemplate.queryForList(sql, new Object[]{commodityNum,3});
		
		map.put("data", commodity);
		map.put("cover", coverList);
		map.put("detail", detailList);
		map.put("particular", particularList);
		
		return map;
	}
    
	
	
 
    
    /**
     * 保存
     */
    public void saveCommodityController(String commodity){
    	
    	JSONObject jsonObject=JSONObject.fromObject(commodity);
    	
    	Commodity cy = new Commodity();
    	cy.setStatus(1);
    	if(jsonObject.getString("type").equals("draft")){
    		cy.setStatus(0);
    	}
    	ShiroUser user = ShiroUserUtil.getUser();
    	cy.setCommodityNum(SerialUtil.generateOrderSerial("MF"));
    	cy.setTeaName(jsonObject.getString("teaName"));
    	cy.setProductType(jsonObject.getString("productType"));
    	cy.setPickYear(jsonObject.getString("pickYear"));
    	cy.setPickSeason(jsonObject.getString("pickSeason"));
    	cy.setGoodsGrade(jsonObject.getString("goodsGrade"));
    	cy.setNetContent(Integer.parseInt(jsonObject.getString("netContent")));
    	cy.setPurpose(Integer.parseInt(jsonObject.getString("purpose")));
    	String tradeName = " 【"+jsonObject.getString("tradeName")+ "】"+cy.getTeaName()+" "+cy.getPickYear()+" "+ cy.getPickSeason() +
    			cy.getProductType() + " " + cy.getGoodsGrade() +" "+cy.getNetContent() +"克"+ (cy.getPurpose()==1?"自饮":"礼盒");
    	
    	cy.setTradeName(jsonObject.getString("tradeName"));
    	cy.setSearch(tradeName);
    	
    	cy.setOriginPlace(jsonObject.getString("originPlace"));
    	cy.setFoodProductionLicence(jsonObject.getString("foodProductionLicence"));
    	
    	cy.setRepertoryStatus(Integer.parseInt(jsonObject.getString("repertory")));
    	cy.setSpecification(jsonObject.getString("specification"));
    	cy.setStoreMethod(jsonObject.getString("storeMethod"));
    	cy.setExpirationData(Integer.parseInt(jsonObject.getString("expirationData")));
    	cy.setCraft(jsonObject.getString("craft"));
    	cy.setProductNum(Long.parseLong(jsonObject.getString("productNum")));
    	cy.setMarketPrice(BigDecimal.valueOf(Double.parseDouble(jsonObject.getString("marketPrice"))));
    	cy.setPromotionPrice(BigDecimal.valueOf(Double.parseDouble(jsonObject.getString("promotionPrice"))));
    	cy.setSoldOutNum(0L);
    	cy.setPackingSpecification(jsonObject.getString("packingSpecification"));
    	Timestamp date = new Timestamp(new Date().getTime());
    	cy.setCreateTime(date);
    	cy.setUpdateTime(date);
    	cy.setCreateUserId(user.id);
    	cy.setUpdateUserId(user.id);
    	commodityRepository.save(cy);
    	
    	CommodityImg img =  null;
    	
    	JSONArray coverArray = JSONArray.fromObject(jsonObject.getString("cover"));
    	JSONArray detailArray = JSONArray.fromObject(jsonObject.getString("detail"));
    	JSONArray particularArray = JSONArray.fromObject(jsonObject.getString("particular"));
    	
    	for(int i = 0;i<coverArray.size();i++){
    		Object coverObj = coverArray.get(i);
    		jsonObject = JSONObject.fromObject(coverObj);
    		String path = saveImg(jsonObject.getString("path"),"cover",jsonObject.getString("name"));
    		img = new CommodityImg();
    		img.setCommodityId(cy.getId());
    		img.setCommodityNum(cy.getCommodityNum());
    		img.setType(1);
    		img.setPath(path);
    		img.setCreateTime(date);
    		img.setCreateUserId(user.id);
    		commodityImgRepository.save(img);
    		
    	}
    	
    	for(int i = 0;i<detailArray.size();i++){
    		Object coverObj = detailArray.get(i);
    		jsonObject = JSONObject.fromObject(coverObj);
    		String path = saveImg(jsonObject.getString("path"),"detail",jsonObject.getString("name"));
    		img = new CommodityImg();
    		img.setCommodityId(cy.getId());
    		img.setCommodityNum(cy.getCommodityNum());
    		img.setType(2);
    		img.setPath(path);
    		img.setCreateTime(date);
    		img.setCreateUserId(user.id);
    		commodityImgRepository.save(img);
    		
    	}
    	
    	for(int i = 0;i<particularArray.size();i++){
    		Object coverObj = particularArray.get(i);
    		jsonObject = JSONObject.fromObject(coverObj);
    		String path = saveImg(jsonObject.getString("path"),"particular",jsonObject.getString("name"));
    		img = new CommodityImg();
    		img.setCommodityId(cy.getId());
    		img.setCommodityNum(cy.getCommodityNum());
    		img.setType(3);
    		img.setPath(path);
    		img.setCreateTime(date);
    		img.setCreateUserId(user.id);
    		commodityImgRepository.save(img);
    		
    	}
    	
    	
    }
    
    /**
     * 保存图片
     * @param imgData
     * @param type
     * @param fileName
     * @return
     */
    public String saveImg(String imgData,String type,String fileName){
    	int begin = imgData.indexOf("base64,");
		imgData = imgData.substring(begin + 7, imgData.length());
		byte[] imgByte = Base64.getDecoder().decode(imgData);
		for (int i = 0; i < imgByte.length; ++i) {
			if (imgByte[i] < 0) {// 调整异常数据
				imgByte[i] += 256;
			}
		}
		
		File path = new File(imgUrl);
		String url= "";
		if(type.indexOf(COVER) > -1){
			url = COVER_PATH+COVER;
		}else if(type.indexOf(DETAIL) >-1){
			url = DETAIL_PATH+DETAIL;
		}else if(type.indexOf(PARTICULAR) >-1){
			url = PARTICULAR_PATH+PARTICULAR;
		}
		String extName = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		fileName = url  + "-" +UUID.randomUUID().toString().replace("-", "")+extName;
		 String tempPath = path.getAbsolutePath() + fileName ;

		try {
			byte2File(imgByte, tempPath);
			
		}catch(Exception e){
			LOG.error(e.getMessage(),e);
		}
		
		return fileName;
    }
    
    private File byte2File(byte[] buf, String path) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			file = new File(path);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}

		return file;
	}
    
    /**
     * 上传
     * @param mfile 文件
     * @param type 类型
     */
	public Map<String,Object> upload(MultipartFile mfile,String type){
		try {
			//获取图片路径
			Map<String,Object> map  = new HashMap<>();
			
			File path = new File(imgUrl);
			String url= "";
			if(type.indexOf(COVER) > -1){
				url = COVER_PATH+COVER;
			}else if(type.indexOf(DETAIL) >-1){
				url = DETAIL_PATH+DETAIL;
			}else if(type.indexOf(PARTICULAR) >-1){
				url = PARTICULAR_PATH+PARTICULAR;
			}
			 String OriginalFilename[] = mfile.getOriginalFilename().split("\\.");
			 String fileName = url  + "-" +UUID.randomUUID().toString().replace("-", "")+"."+OriginalFilename[1];
			 String tempPath = path.getAbsolutePath() + fileName ;
	         File tempFile = null;
	      
	         tempFile =  new File(tempPath);
	         FileUtils.copyInputStreamToFile(mfile.getInputStream(), tempFile);
	         map.put("path", fileName);
	         map.put("res",1);
	         return map;
         }catch(Exception e){
        	 LOG.error(e.getMessage(),e);
     		 try {
				throw e;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
     		 
     		 return null;
         }

	}

	
	/**
	 * 删除图片
	 * @param path 图片路径
	 * 
	 */
	public void deleteImg(String ImgPath){
		try{
			File file = new File(imgUrl+ImgPath);
			file.delete();
		}catch(Exception e){
       	 LOG.error(e.getMessage(),e);
    		 try {
				throw e;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        }
		
	}
    
    /**
     * 删除商品
     * @param id
     */
    public void delCommodity(Long id){
    	Commodity commodity = commodityRepository.findOne(id);
    	
    	if(commodity == null){
    		return;
    	}
    	
    	commodityRepository.delete(commodity);
    	List<CommodityImg> list = commodityImgRepository.findByCommodityNum(commodity.getCommodityNum());
    	commodityImgRepository.delete(list);
    	
    	
    	for(CommodityImg c : list){
    		deleteImg(c.getPath());
    	}
    }
    
    

}
