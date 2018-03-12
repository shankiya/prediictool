package com.shankiya.prediictool.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.math.R.RserverConf;
import org.math.R.Rsession;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shankiya.prediictool.model.ChartData;

import rserve.JRItest;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

	public static final String uploadingdir = System.getProperty("user.dir") + "/upload-dir/";
	public static final String workbookdir = System.getProperty("user.dir") + "/workbook-dir/";
	public static final String analyticsdir = System.getProperty("user.dir") + "/analytics/";
	public static String valueArray;
	public static String dataString;
	List<String> files = new ArrayList<String>();
	public static ArrayList<String> periodDetails = new ArrayList<>();
	public static ArrayList<String> regionDetails = new ArrayList<>();

	static {
		periodDetails.add("period");
		periodDetails.add("date");
		regionDetails.add("region");
		regionDetails.add("state");
		regionDetails.add("country");
	}

	// analytics
	
	public String getMonthNames(int monthNumber,int year) {
		String monthNames = "";
		if(monthNumber == 0) {
			monthNames = "Jan-"+year;
		}else if(monthNumber == 1) {
			monthNames = "Feb-"+year;
		}else if(monthNumber == 2) {
			monthNames = "Mar-"+year;
		}else if(monthNumber == 3) {
			monthNames = "Apr-"+year;
		}else if(monthNumber == 4) {
			monthNames = "May-"+year;
		}else if(monthNumber == 5) {
			monthNames = "Jun-"+year;
		}else if(monthNumber == 6) {
			monthNames = "Jul-"+year;
		}else if(monthNumber == 7) {
			monthNames = "Aug-"+year;
		}else if(monthNumber == 8) {
			monthNames = "Sep-"+year;
		}else if(monthNumber == 9) {
			monthNames = "Oct-"+year;
		}else if(monthNumber == 10) {
			monthNames = "Nov-"+year;
		}else if(monthNumber == 11) {
			monthNames = "Dec-"+year;
		}
		
		return monthNames;
	}

	@RequestMapping("/prediction")
	public String analytics(@RequestBody Map map) throws Exception {
		System.out.println(map);
		List dataList = (List) map.get("datalist");
		List analyticsdataList = (List) map.get("analyticsdata");
		String startDateString = (String) map.get("startDate");
		String endDateString = (String) map.get("endDate");
		System.out.println(startDateString);
		 Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);  	
		 Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateString);  	
		 startDate.setDate(startDate.getDate()+1);
		 endDate.setDate(endDate.getDate()+1);
		 
		 System.err.println("Start Date"+startDate);
		 System.err.println("Start Date"+endDate);
			
		int prediictPeriod = (int) map.get("predictPeriod");
		String frequencyList =  (String) map.get("frequency");
		
		
		
		
		String periodString = "[";
		
		String finalPeriodString = "";
		System.err.println("start month "+startDate.getMonth());
		System.err.println("start month "+startDate.getYear()+1900);
		for(int i = startDate.getMonth();i<12;i++) {
			periodString = periodString + "\""+getMonthNames(i,startDate.getYear()+1900)+"\",";
		}
		for(int i=startDate.getYear()+1;i<endDate.getYear();i++) {
			for(int j = 0;j<=11;j++) {
				periodString = periodString + "\""+getMonthNames(j,i+1900)+"\",";
			}
		}
		
		for(int i = 0;i<=endDate.getMonth();i++) {
			periodString = periodString + "\""+getMonthNames(i,endDate.getYear()+1900)+"\",";
		}
		
		int endMonth = endDate.getMonth()+prediictPeriod;
		if(endDate.getMonth() == 11) {
			for(int i = 0;i<=prediictPeriod;i++) {
				periodString = periodString + "\""+getMonthNames(i,endDate.getYear()+1+1900)+"\",";
			}
		}else {
			if(endMonth>=12) {
				int count = 0;
				for(int i = endDate.getMonth();i<=11;i++) {
					count = count +1;
					periodString = periodString + "\""+getMonthNames(i,endDate.getYear()+1900)+"\",";
				}
				
				endMonth = endMonth - count;
				
				int monthcount = endMonth%12;
				
				for(int i = 0; i<monthcount-1;i++) {
					for(int j = 0;j<=11;j++) {
						count = count +1;
						periodString = periodString + "\""+getMonthNames(j,endDate.getYear()+1+1900)+"\",";
					}
				}
				int endMonth1 = endDate.getMonth()+prediictPeriod;
				endMonth1 = endMonth1 - count;
				for(int i = 0;i<=endMonth1 - 1;i++) {
					periodString = periodString + "\""+getMonthNames(i,endDate.getYear()+monthcount+1+1900)+"\",";
				}
				
			}else {
				for(int i = endDate.getMonth()+1;i<=endMonth+1;i++) {
					if(endDate.getMonth()+prediictPeriod >=12) {
						periodString = periodString + "\""+getMonthNames(i,endDate.getYear()+1900+1)+"\",";
					}else {
						periodString = periodString + "\""+getMonthNames(i,endDate.getYear()+1900)+"\",";
					}
				}
			}
		}
	
		
		if(periodString.length() >= 2) {
			finalPeriodString = periodString.substring(0, periodString.length() - 1);
		}
		finalPeriodString = finalPeriodString+"]";
		HashMap<String, String> anaMap = new HashMap<>();
		for (int i = 0; i < analyticsdataList.size(); i++) {
			Map m = (Map) analyticsdataList.get(i);
			anaMap.put((String) m.get("header"), (String) m.get("disp"));
		}
		boolean hasProduct = false;
		if(anaMap.get("Products") != null) {
			hasProduct =true;
		}
		writeWorkBook(dataList, anaMap, map);
		
		
		HashMap<String, List<String>> rowList = writeAllDetails(hasProduct, map,String.valueOf(prediictPeriod));
		String finalDataString = chartInputDetails(map,rowList,hasProduct);
		String finalString = "{";
		finalString = finalString + "\"period\":"+finalPeriodString+",\"data\":"+finalDataString+"}";
		
		System.out.println(finalString);
		return finalString;

	}

	public static void writeWorkBook(List dataList, HashMap<String, String> anaMap, Map mainMap) throws Exception {
		FileWriter writer = new FileWriter(workbookdir + mainMap.get("workbookName") + ".csv");
		for (int i = 0; i < dataList.size(); i++) {
			// System.out.println(dataList.get(i));

			if (i == 0) {
				HashMap map = (HashMap) dataList.get(i);
				Set<Entry<String, Object>> entrySet = map.entrySet();
				String s = "";
				for (Entry<String, Object> e : entrySet) {
					s = s + anaMap.get(e.getKey());
					s = s + ",";
				}
				String s1 = s.substring(0, s.length() - 1);
				writer.write(s1 + "\n");
			}

			HashMap map = (HashMap) dataList.get(i);
			Set<Entry<String, Object>> entrySet = map.entrySet();
			String s = "";
			for (Entry<String, Object> e : entrySet) {
				s = s + e.getValue();
				s = s + ",";
			}
			String s1 = s.substring(0, s.length() - 1);
			writer.write(s1 + "\n");
		}
		writer.close();
	}

	private static int getMonthIncrementCount(Long avg) {
		if (avg <= 35) {
			return 1;
		} else if (avg <= 95) {
			return 3;
		} else if (avg <= 185) {
			return 6;
		} else if (avg <= 370) {
			return 12;
		}
		return 24;
	}

	public static String getStringFromDate(Date date, String frmt) {
		SimpleDateFormat format = new SimpleDateFormat(frmt);
		String newDate = format.format(date);
		return newDate;
	}

	public static HashMap<String, List<String>> writeAllDetails(boolean product, Map mainMap, String predictPeriod) throws Exception {
		HashMap<String, List<String>> rowList = null;
		try {
			rowList = new HashMap<String, List<String>>();
			String splitBy = ",";
			BufferedReader br = new BufferedReader(new FileReader(workbookdir + mainMap.get("workbookName") + ".csv"));
			String header = br.readLine();
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] b = line.split(splitBy);
				String file = b[0];
				if (product) {
					file = b[0] + "_" + b[1];
				}
				if (rowList.get(file) != null) {
					rowList.get(file).add(line);
				} else {
					List<String> data = new ArrayList<>();
					data.add(line);
					rowList.put(file, data);
				}
			}
			br.close();
			Set<Entry<String, List<String>>> entrySet = rowList.entrySet();
			Rsession re = Rsession.newRemoteInstance(System.out, RserverConf.parse("R://localhost:6311"));
			JRItest forecast = new JRItest();

			File workBookDirectory = new File(analyticsdir + mainMap.get("workbookName") + "/scenario");
			if (!workBookDirectory.exists()) {
				workBookDirectory.mkdirs();
			}

			for (Entry<String, List<String>> e : entrySet) {

				FileWriter fileOutputStream = new FileWriter(
						analyticsdir + mainMap.get("workbookName") + "/" + e.getKey() + ".csv");
				fileOutputStream.write(header + "\n");
				for (String s : e.getValue()) {
					fileOutputStream.write(s + "\n");
				}
				fileOutputStream.flush();
				fileOutputStream.close();

				forecast.forecastSales(re, analyticsdir + mainMap.get("workbookName") + "/" + e.getKey() + ".csv",
						analyticsdir + mainMap.get("workbookName") + "/scenario/" + e.getKey() + ".csv", " ",
						analyticsdir + mainMap.get("workbookName") + "/scenario/" + e.getKey() + ".jpg",
						analyticsdir + mainMap.get("workbookName") + "/scenario/" + e.getKey() + "- graph" + ".jpg",
						predictPeriod, "Units", "12");
			}
			
			re.end();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowList;
	}

	public static String chartInputDetails(Map mainMap,HashMap<String, List<String>> rowList,boolean hasProducts) throws Exception {
		String endString = null;
		try {
			File folder = new File(analyticsdir + mainMap.get("workbookName") + "/");
			if (folder.isDirectory()) {
				File[] files = folder.listFiles();
				ArrayList<ChartData> arrayList = new ArrayList<>();
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					try {
						if (file.isFile()) {
							BufferedReader sourceFile = new BufferedReader(
									new FileReader(folder.getAbsolutePath() + "\\" + file.getName()));
							BufferedReader rFile = new BufferedReader(
									new FileReader(folder.getAbsolutePath() + "\\scenario\\" + file.getName()));
							arrayList = prepareData(sourceFile, rFile, arrayList,hasProducts);
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				
				
				Set<Entry<String, List<String>>> entrySet = rowList.entrySet();
				String finalString = "[";
				for (Entry<String, List<String>> e : entrySet) {
					dataString = "{";
					valueArray = "[";
					
				
					System.out.println(e.getKey());
					
					arrayList.stream().filter(chartdata -> chartdata.getYaxis().equals(e.getKey())).
					forEach(
							action -> {
									valueArray = valueArray +action.getValue().setScale(0, RoundingMode.HALF_UP)+",";
							});
					String finalvalueArray;
					
					if(valueArray.length()>=2) {
						finalvalueArray  =valueArray.substring(0, valueArray.length()-1);
					}else {
						finalvalueArray = valueArray;
					}
					finalvalueArray =finalvalueArray + "]";
					
					dataString =dataString +"\"name\":\""+e.getKey()+"\",";
					dataString =dataString + "\"data\":"+finalvalueArray+"}";
					finalString = finalString + dataString +",";
				}
				
				endString = finalString.substring(0, finalString.length()-1);
//				Gson gson = new GsonBuilder().create();
//				String s = gson.toJson(arrayList);
//				System.err.println(s);
				endString =endString+"]";
				System.out.println(endString);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return endString;
	}
	


	private static ArrayList<ChartData> prepareData(BufferedReader sourceFile, BufferedReader rFile,
			ArrayList<ChartData> arrayList,boolean hasProducts) {
		try {
			String sourceFileHeader = sourceFile.readLine();
			String splitBy = ",";
			int periodColumn = 0;
			int regonColumn = 0;
			int productColumn = 0;
			String[] header = sourceFileHeader.split(splitBy);
			for (int i = 0; i < header.length; i++) {
				if (periodDetails.contains(header[i].toLowerCase())) {
					periodColumn = i;
				}
				if (regionDetails.contains(header[i].toLowerCase())) {
					regonColumn = i;
				}
				if(hasProducts) {
					if (header[i].toLowerCase().equalsIgnoreCase("Products")) {
						productColumn = i;
					}
				}
			}
			String sourceLine = null;
			String rLine = null;
			Date oldDate = null;
			ArrayList<Long> intervalDetailsDays = new ArrayList<>();
			Long sum = (long) 0;
			ArrayList<Date> dateList = new ArrayList<>();
			String region = "";
			String product = "";
			while ((sourceLine = sourceFile.readLine()) != null) {
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
				if (oldDate != null) {
					intervalDetailsDays.add((oldDate.getTime() - newDate.getTime()) / (3600000 * 24));
					sum = sum + (oldDate.getTime() - newDate.getTime()) / (3600000 * 24);
				} else {
					region = b[regonColumn];
				}
				if(hasProducts) {
					product = b[productColumn];
				}
				oldDate = new Date(newDate.getTime());
			}

			Long avg = sum / (dateList.size() - 1);
			int it = 0;
			int pos = 1;
			int month = getMonthIncrementCount(avg);
			rLine = rFile.readLine();
			while ((rLine = rFile.readLine()) != null) {
				ChartData chartData = new ChartData();
				// System.out.println(rLine);
				chartData.setValue(new BigDecimal(rLine));
				if(hasProducts) {
					chartData.setYaxis(region+"_"+product);
				}else {
					chartData.setYaxis(region);
				}
				if (dateList.size() > it) {
					chartData.setXaxis(getStringFromDate(dateList.get(it), "MMM-YYYY"));
				} else {
					Date date = new Date(oldDate.getTime());
					date.setMonth(date.getMonth() + (month * pos));
					pos = pos + 1;
					chartData.setXaxis(getStringFromDate(date, "MMM-YYYY"));
				}
				it = it + 1;
				arrayList.add(chartData);
			}

			sourceFile.close();
			rFile.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return arrayList;
	}

}
