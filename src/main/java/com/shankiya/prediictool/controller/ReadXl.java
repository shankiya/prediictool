/*package com.shankiya.prediictool.controller;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shankiya.prediictool.model.ChartData;
import org.math.R.RserverConf;
import org.math.R.Rsession;
import org.springframework.beans.factory.annotation.Autowired;
import rserve.JRItest;

public class ReadXl {
	
	JRItest forecast;
	public static ArrayList<String> periodDetails = new ArrayList<>();
	public static ArrayList<String> regionDetails = new ArrayList<>();
	
	static {
		periodDetails.add("period");
		periodDetails.add("date");
		regionDetails.add("region");
		regionDetails.add("state");
		regionDetails.add("country");
	}
	

public static void main(String[] args) throws Exception {
	File folder = new File("d:\\LocalData\\z022093\\Desktop\\work\\");
	if(folder.isDirectory()) {
		File[] files = folder.listFiles();
		ArrayList<ChartData> arrayList = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			try {
				if(file.isFile()) {
					BufferedReader sourceFile = new BufferedReader(new FileReader(folder.getAbsolutePath()+"\\"+file.getName()));
					BufferedReader rFile = new BufferedReader(new FileReader(folder.getAbsolutePath()+"\\Rgen\\"+file.getName()));
					arrayList = prepareData(sourceFile,rFile,arrayList);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		Gson gson = new GsonBuilder().create();
		String s= gson.toJson(arrayList);
		System.err.println(s);
		//System.out.println(arrayList.size());
	}
	writeAllDetails(false);
	
}

private static ArrayList<ChartData> prepareData(BufferedReader sourceFile, BufferedReader rFile, ArrayList<ChartData> arrayList) {
	try {
		String sourceFileHeader = sourceFile.readLine();
		String splitBy = ",";
		int periodColumn = 0;
		int regonColumn = 0;
		String[] header = sourceFileHeader.split(splitBy);
		for (int i = 0; i < header.length; i++) {
			if(periodDetails.contains(header[i].toLowerCase())) {
				periodColumn = i;
			}
			if(regionDetails.contains(header[i].toLowerCase())) {
				regonColumn = i;
			}
		}
		String sourceLine = null;
		String rLine = null;
		Date oldDate = null;
		ArrayList<Long> intervalDetailsDays = new ArrayList<>();
		Long sum = (long) 0;
		ArrayList<Date> dateList = new ArrayList<>();
		String region = "";
		while ((sourceLine = sourceFile.readLine()) !=null) {
			String[] b = sourceLine.split(splitBy);
			String period = b[periodColumn];
			SimpleDateFormat format = new SimpleDateFormat("d-MMM-yy"); 
			Date newDate = null;
			try {
				newDate = format.parse(period);
				dateList.add(newDate);
			} catch (ParseException e) {
				e.printStackTrace(); 
			}
			if(oldDate != null) {
				intervalDetailsDays.add((oldDate.getTime() - newDate.getTime())/(3600000*24));
				sum = sum + (oldDate.getTime() - newDate.getTime())/(3600000*24);
			}else {
				region = b[regonColumn];
			}
			oldDate = new Date(newDate.getTime());
		}
		
		Long avg = sum/ (dateList.size()-1);
		int it = 0 ;
		int pos = 1;
		int month = getMonthIncrementCount(avg);
		rLine = rFile.readLine();
		while ((rLine = rFile.readLine()) !=null) {
			ChartData chartData = new ChartData();
			//System.out.println(rLine);
			chartData.setValue(new BigDecimal(rLine));
			chartData.setYaxis(region);
			if(dateList.size() > it) {
				chartData.setXaxis(getStringFromDate(dateList.get(it),"MMM-YYYY"));
			}else{
				Date date = new Date(oldDate.getTime()); 
				date.setMonth(date.getMonth() + (month * pos)); 
				pos = pos+1;
				chartData.setXaxis(getStringFromDate(date,"MMM-YYYY"));
			}
			it = it+1;
			arrayList.add(chartData);
		}
		
		sourceFile.close();
		rFile.close();
		
	}catch(Exception e) {
		e.printStackTrace();
	}
	
	return arrayList;
}

private static int getMonthIncrementCount(Long avg) {
	if(avg <= 35) {
		return 1;
	}else if(avg <= 95) {
		return 3;
	}else if(avg <= 185) {
		return 6;
	}else if(avg <= 370) {
		return 12;
	}
	return 24;
}

public static String getStringFromDate(Date date, String frmt) {
	SimpleDateFormat format = new SimpleDateFormat(frmt);
	String newDate = format.format(date);
	return newDate;
}

@Autowired
public static void writeAllDetails(boolean product) throws Exception {
	try {
		HashMap<String,List<String>> rowList = new HashMap<String,List<String>>();
		String splitBy = ",";
		BufferedReader br = new BufferedReader(new FileReader("d:\\LocalData\\z022093\\Desktop\\workexcel\\region_only.csv"));
		String header = br.readLine();
		String line = null;
		while ((line = br.readLine()) !=null) {
			String[] b = line.split(splitBy);
			String file = b[0];
			if(product) {
				file = b[0]+"_"+b[1];
			}
			if(rowList.get(file) != null) {
				rowList.get(file).add(line);
			}else {
				List<String> data = new ArrayList<>();
				data.add(line);
				rowList.put(file,data);
			}
		}
		br.close();
		Set<Entry<String, List<String>>> entrySet = rowList.entrySet();
		Rsession re = Rsession.newRemoteInstance(System.out,RserverConf.parse("R://localhost:6311"));
		JRItest forecast= new JRItest();
		
		for (Entry<String, List<String>> e: entrySet) {
			FileWriter fileOutputStream = new FileWriter("d:/LocalData/z022093/Desktop/workexcel/output/"+e.getKey()+".csv");
			fileOutputStream.write(header +"\n");
			for (String s : e.getValue()) {
				fileOutputStream.write(s +"\n");
			}
			fileOutputStream.flush();
			fileOutputStream.close();
			forecast.forecastSales(re, "d:/LocalData/z022093/Desktop/workexcel/output/"+e.getKey()+".csv" , "d:/LocalData/z022093/Desktop/workexcel/output/scenario/"+e.getKey()+".csv"," ", "d:/LocalData/z022093/Desktop/workexcel/output/scenario/"+"trend-test.jpg", "d:/LocalData/z022093/Desktop/workexcel/output/scenario/"+"trend-test-one.jpg", "6", "Units", "12");
		}
		
		for (Entry<String, List<String>> e: entrySet) {
			
		}
		
		
		
	}catch(Exception e) {
		e.printStackTrace();
	}

}
}
*/