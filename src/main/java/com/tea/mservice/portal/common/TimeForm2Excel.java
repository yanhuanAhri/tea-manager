package com.tea.mservice.portal.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeForm2Excel {
	
	/**
	 *计算时间区间 
	 * @throws ParseException 
	 */
	public Integer timeSection(String startTime, String endTime) throws ParseException {
		
		
		Date date1 = new Date();
		Date date2 = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

		if(startTime.length() == 10) {
			date1 = sdf1.parse(startTime);
			 
		}else if(startTime.length() == 8){
			date1 = sdf2.parse(startTime);
		}
		if(endTime.length() == 10) {
			date2 = sdf1.parse(endTime);
		}else if(endTime.length() == 8){
			date2 = sdf2.parse(endTime);
		}
		
        Integer days = (int) ((date2.getTime() - date1.getTime()) / (24*3600*1000));  
        
        return days;
			
	}
	
	
public Integer timeSection2(String startTime, String endTime) throws ParseException {
		
		
		Date date1 = new Date();
		Date date2 = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");

		date1 = sdf1.parse(startTime);
		
		date2 = sdf2.parse(endTime);
		
        Integer days = (int) ((date2.getTime() - date1.getTime()) / (24*3600*1000));  
        
        return days;
			
	}
	
	
	
	/**
	 *显示改时间区间的所有时间 
	 * @throws ParseException 
	 */
	public List<String> showTime(String startTime, String endTime) throws ParseException{
		

		
		
		
		Integer timeSection = timeSection(startTime, endTime);
		List<String> arrayList = new ArrayList<String>();
		
		int i=0;
		
		
		String[] split1 = new String[3];
		String[] split2 = new String[3];
		
		if(startTime.length() == 10) {
			split1 = startTime.split("-");
		}else if(startTime.length() == 8){
			String substring1 = startTime.substring(0, 4);
			String substring2 = startTime.substring(4, 6);
			String substring3 = startTime.substring(6, 8);
			
			split1[0] = substring1;
			split1[1] = substring2;
			split1[2] = substring3;
		}
		if(endTime.length() == 10) {
			split2 = endTime.split("-");
		}else if(endTime.length() == 8){
			String substring1 = endTime.substring(0, 4);
			String substring2 = endTime.substring(4, 6);
			String substring3 = endTime.substring(6, 8);
			
			split2[0] = substring1;
			split2[1] = substring2;
			split2[2] = substring3;
		}
		
		Integer year = Integer.parseInt(split1[0]);
		Integer mouth = Integer.parseInt(split1[1]);
		Integer day = Integer.parseInt(split1[2]);
		
		
		
		

		arrayList.add(year.toString() + "/" + mouth.toString() + "/" + day.toString());

		
		
		for(;i<timeSection;i++) {
		
			if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0 ) {
				//闰年
				
				if(mouth == 1 || mouth ==3 || mouth ==5 || mouth ==7 || mouth ==8 || mouth ==10 || mouth ==12) {
					if(day==31) {
						mouth += 1;
						day = 0;
						
					}
				}else if(mouth == 4 || mouth ==6 || mouth ==9 || mouth ==11) {
					if(day==30) {
						mouth += 1;
						day = 0;
						
					}
				}else if(mouth == 2) {
					if(day==29) {
						mouth += 1;
						day = 0;
				
					}
				}
				
				
				
			}else {
				//平年
				
				if(mouth == 1 || mouth ==3 || mouth ==5 || mouth ==7 || mouth ==8 || mouth ==10 || mouth ==12) {
					if(day==31) {
						mouth += 1;
						day = 0;
						
					}
				}else if(mouth == 4 || mouth ==6 || mouth ==9 || mouth ==11) {
					if(day==30) {
						mouth += 1;
						day = 0;
						
					}
				}else if(mouth == 2) {
					if(day==28) {
						mouth += 1;
						day = 0;
						
					}
				}
				
				
			}
			
			if(mouth == 13) {
				year += 1;
				mouth = 1;
			}
			
			day ++;
			
			String shi = year.toString() + "/" + mouth.toString() + "/" + day.toString();
			
			arrayList.add(shi);
		}
		
		
		return arrayList;
		
	}
	
	
	public String timeFarm(String str){
		String substring1 = str.substring(0, 4);
		String substring2 = str.substring(4, 6);
		String substring3 = str.substring(6, 8);
		
		Integer year = Integer.parseInt(substring1);
		Integer mouth = Integer.parseInt(substring2);
		Integer day = Integer.parseInt(substring3);
		return year + "/" + mouth + "/" + day;
		
	}
	
	

	
	public boolean compare(String startTime, String endTime) throws ParseException{
		Date date1 = new Date();
		Date date2 = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		if(startTime.length() == 10) {
			date1 = sdf1.parse(startTime);
			 
		}else if(startTime.length() == 8){
			date1 = sdf2.parse(startTime);
		}
		if(endTime.length() == 10) {
			date2 = sdf1.parse(endTime);
		}else if(endTime.length() == 8){
			date2 = sdf2.parse(endTime);
		}
		
		return (date1.equals(date2));
		
	}
	
}