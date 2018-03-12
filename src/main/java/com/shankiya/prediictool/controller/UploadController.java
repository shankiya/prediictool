package com.shankiya.prediictool.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shankiya.prediictool.model.ChartData;
import com.shankiya.prediictool.services.CSV;
import com.shankiya.prediictool.services.StorageService;

import rserve.JRItest;

import org.math.R.RserverConf;
import org.math.R.Rsession;
import org.rosuda.REngine.*;

@RestController
@RequestMapping("/file")
public class UploadController {

	public static final String uploadingdir = System.getProperty("user.dir") + "/upload-dir/";
	/*public static final String workbookdir = System.getProperty("user.dir") + "/workbook-dir/";
	public static final String analyticsdir = System.getProperty("user.dir") + "/analytics/";*/

	@Autowired
	StorageService storageService;

	List<String> files = new ArrayList<String>();
	
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

	@RequestMapping("/upload")
	public List<Map<String, String>> handleFileUpload(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			storageService.store(file);
			files.add(file.getOriginalFilename());

			InputStream in = new FileInputStream(uploadingdir + file.getOriginalFilename());
			CSV csv = new CSV(true, ',', in);
			List<String> fieldNames = null;
			if (csv.hasNext())
				fieldNames = new ArrayList<>(csv.next());
			List<Map<String, String>> list = new ArrayList<>();
			while (csv.hasNext()) {
				List<String> x = csv.next();
				Map<String, String> obj = new LinkedHashMap<>();
				for (int i = 0; i < fieldNames.size(); i++) {
					obj.put(fieldNames.get(i), x.get(i));
				}
				list.add(obj);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			message = "FAIL to upload " + file.getOriginalFilename() + "!";
			return null;
		}
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = storageService.loadFile(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
/*//	analytics
	
	@RequestMapping("/analytics")
	public List<Map<String, String>> analytics(@RequestBody Map map) throws Exception {
		List dataList = (List) map.get("datalist");
		List analyticsdataList = (List) map.get("analyticsdata");
		
		HashMap<String, String> anaMap = new HashMap<>();
		for(int i = 0; i<analyticsdataList.size();i++) {
			Map m = (Map) analyticsdataList.get(i);
			anaMap.put((String)m.get("header"), (String)m.get("disp"));
		}
		writeWorkBook(dataList,anaMap,map);
		writeAllDetails(true,map);
		chartInputDetails(map);
		
		System.out.println(dataList.get(0));
//		System.out.println(analyticsList.get(0));
		return null;
		
	}

	public static void writeWorkBook(List dataList,HashMap<String, String> anaMap,Map mainMap) throws Exception {
		FileWriter writer = new FileWriter(workbookdir+mainMap.get("workbookName")+".csv");
		for(int i=0;i<dataList.size();i++) {
//			System.out.println(dataList.get(i));
			
			if(i==0) {
				HashMap map = (HashMap) dataList.get(i);
				Set<Entry<String,Object>> entrySet = map.entrySet();
				String s = "";
				for (Entry<String, Object> e: entrySet) {
					s = s + anaMap.get(e.getKey());
					s=s+",";
				}
				String s1 = s.substring(0,s.length()-1);
				writer.write( s1+"\n");
			}
			
			HashMap map = (HashMap) dataList.get(i);
			Set<Entry<String,Object>> entrySet = map.entrySet();
			String s = "";
			for (Entry<String, Object> e: entrySet) {
				s = s + e.getValue();
				s=s+",";
			}
			String s1 = s.substring(0,s.length()-1);
			writer.write( s1+"\n");
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
	
	public static void writeAllDetails(boolean product,Map mainMap) throws Exception {
		try {
			HashMap<String, List<String>> rowList = new HashMap<String, List<String>>();
			String splitBy = ",";
			BufferedReader br = new BufferedReader(
					new FileReader(workbookdir+mainMap.get("workbookName")+".csv"));
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
			
			 File workBookDirectory = new File(analyticsdir+mainMap.get("workbookName")+"/scenario");
		     if (! workBookDirectory.exists()){
		    	 workBookDirectory.mkdirs();
		     }
		     
			for (Entry<String, List<String>> e : entrySet) {
				
				FileWriter fileOutputStream = new FileWriter(
						analyticsdir+mainMap.get("workbookName")+"/"+ e.getKey() + ".csv");
				fileOutputStream.write(header + "\n");
				for (String s : e.getValue()) {
					fileOutputStream.write(s + "\n");
				}
				fileOutputStream.flush();
				fileOutputStream.close();
				
				forecast.forecastSales(re, analyticsdir+mainMap.get("workbookName")+"/"+ e.getKey() + ".csv",
						analyticsdir+mainMap.get("workbookName")+"/scenario/" + e.getKey() + ".csv", " ",
						analyticsdir+mainMap.get("workbookName")+"/scenario/" + e.getKey() + ".jpg",
						analyticsdir+mainMap.get("workbookName")+"/scenario/" + e.getKey() +"- graph"+".jpg", "6", "Units",
						"12");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void chartInputDetails(Map mainMap) throws Exception {
		try {
			File folder = new File(analyticsdir+mainMap.get("workbookName")+"/");
			if(folder.isDirectory()) {
				File[] files = folder.listFiles();
				ArrayList<ChartData> arrayList = new ArrayList<>();
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					try {
						if(file.isFile()) {
							BufferedReader sourceFile = new BufferedReader(new FileReader(folder.getAbsolutePath()+"\\"+file.getName()));
							BufferedReader rFile = new BufferedReader(new FileReader(folder.getAbsolutePath()+"\\scenario\\"+file.getName()));
							arrayList = prepareData(sourceFile,rFile,arrayList);
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				Gson gson = new GsonBuilder().create();
				String s= gson.toJson(arrayList);
				System.err.println(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static ArrayList<ChartData> prepareData(BufferedReader sourceFile, BufferedReader rFile,
			ArrayList<ChartData> arrayList) {
		try {
			String sourceFileHeader = sourceFile.readLine();
			String splitBy = ",";
			int periodColumn = 0;
			int regonColumn = 0;
			String[] header = sourceFileHeader.split(splitBy);
			for (int i = 0; i < header.length; i++) {
				if (periodDetails.contains(header[i].toLowerCase())) {
					periodColumn = i;
				}
				if (regionDetails.contains(header[i].toLowerCase())) {
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
				chartData.setYaxis(region);
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

*/	
}