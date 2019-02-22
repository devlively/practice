package ex2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 로그 문자열을 분석해서 데이터를 추출
 * 
 * - 프로세스 로직
 * 파일에서 한 라인을 읽어오면 먼저 isRunningTime으로 소요시간 데이터 문자열이 나올수 있는 상황 인지 일반 데이터 문자열인지 확인합니다
 * isRunningTime = { true : 소요시간 데이터, false : 일반 데이터 }
 * 
 * 소요시간 데이터 문자열일 경우 checkRunningTimeData에서 첫 글자를 가져와 소요시간 데이터가 맞는지 확인합니다
 * (-,m,숫자)가 나오면 소요시간 데이터가 맞다고 판단하고 데이터를 분석하고 아닐경우 isRunningTime을 false로 변경합니다
 * ( 소요시간 퍼센트 키워드 ) 형식으로 소요시간 데이터를 분석하여
 * 소요시간 데이터의 쓰레드에 해당하는 runningTimeData에 데이터를 저장합니다. 
 * 
 * 일반 데이터 문자열일 경우 StringAnalysis에서
 * ( [시간] [구분] [쓰레드명 코드] - 키워드 : 데이터 ) 
 * 형식으로 로그 문자열을 분석하여 각각의 정보를 Map에 저장한 뒤
 * LogDataAnalysis에서 키워드에 맞는 작업을 처리해줍니다
 * 
 * 키워드별 작업
 * - bean start. = LogData 객체를 1개 생성해준뒤 시간데이터를 시작시간으로 저장하고
 *                 ThreadMap 에 <ThreadName, LogData> 형식으로 저장합니다
 *                 bean start일때만 ThreadMap에 저장해주기 때문에 ThreadMap에 존재하면 실행중인 쓰레드이고
 *                 존재하지 않으면 실행중이지 않은 Thread거나 무시할 데이터라고 판단 할 수 있습니다
 *                 
 *                 insertMinutelyLogData에서 LogData의 시작시간을 가져와서 기준시간(이전에 저장된 LogData의 시작시간, 분단위)과 같으면
 *                 minutelyLogData의 마지막 인덱스인 리스트에 LogData를 추가합니다
 *                 기준시간과 다를 경우 새로운 리스트객체를 만들어 LogData를 추가하고 minutelyLogData에 리스트를 저장합니다
 *                 
 * - bean end.   = ThreadMap에서 해당 쓰레드의 LogData를 가져온뒤 시간데이터를 종료시간으로 저장하고
 *                 모든 데이터가 null이 아닌지 확인한 뒤 모든 데이터가 있으면 파일1번 데이터에 추가해줍니다
 *                 ThreadMap에서 데이터를 삭제합니다
 *                    
 * - StopWatch   = runningTimeData 에 현재 쓰레드의 LogData를 저장하고 isRunningTime을 true로 변경합니다
 * 
 * - ESB_TRAN_ID, Content-Length, call time = 해당 쓰레드의 LogData에 데이터를 저장합니다
 * 
 * 모든 문자열 분석이 끝난 뒤 analyzeMinutelyLogData로 minutelyLogData에 저장된 분당 데이터를 분석하고 파일2번 데이터에 추가합니다
 * 파일1번과 파일2번을 출력합니다
 * 
 * @author JuHan
 */
public class LogAnalysis {

	//매번 객체생성을 하지 않고 하나의 객체를 재사용 하기위해 멤버변수로 선언, 전역변수로 사용
	private StringBuilder stringBuilder = null; // 함수에서 문자열 작업을 할 때 범용적으로 사용하는 객체 
	private Map<String, String> map = null; // 로그 문자열을 분석해서 나온 데이터들을 저장하는 Map 
	
	private Map<String, LogData> threadMap = null; // 1개의 쓰레드가 시작되고 끝나기 전까지 데이터가 저장 되어있는 Map
	private LogData runningTimeData = null; // 현재 소요시간 문자열의 쓰레드 데이터
	private boolean isRunningTime = false; // 현재 로그문자열이 소요시간 문자열인지 확인
	
	private List<List<LogData>> minutelyLogData = null; // 1분당 데이터리스트를 저장하는 리스트
	SimpleDateFormat sdf = null; // 분당 데이터 처리를위한 초단위 절삭
	SimpleDateFormat sdf2 = null; // String 날짜를 Date로 변환하기위한 포맷
	private Date pivotTime = null; // 분당 데이터 기준시간
	
	private StringBuilder fileOutString1 = null; // 파일 1의 결과들이 저장되는 StringBuilder
	private StringBuilder fileOutString2 = null; // 파일 2의 결과들이 저장되는 StringBuilder
	
	/**
	 * 멤버변수들을 null로 초기화
	 */
	private void init()
	{
		this.stringBuilder = null; 
		this.map = null; 
		this.threadMap = null;
		this.fileOutString1 = null;
		this.fileOutString2 = null;
		this.runningTimeData = null;
		this.isRunningTime = false;
		this.sdf = null;
		this.sdf2 = null;
		this.pivotTime = null;
		this.minutelyLogData = null;
	}
	
	/**
	 * 멤버변수들의 객체를 생성
	 */
	private void createObject()
	{
		stringBuilder = new StringBuilder(); 
		map = new HashMap<String, String>(); 
		threadMap = new HashMap<String, LogData>();
		fileOutString1 = new StringBuilder();
		fileOutString2 = new StringBuilder();
		runningTimeData = null;
		isRunningTime = false;
		sdf = new SimpleDateFormat("YYYY.MM.DD hh:mm");
		sdf2 = new SimpleDateFormat("YYYY.MM.DD hh:mm:ss");
		pivotTime = new Date();
		minutelyLogData = new ArrayList<List<LogData>>();
	}
	
	/**
	 * 로그파일을 불러와서 문자열을 분석하여 데이터 추출
	 * 
	 */
	public void analysis(String filePath)
	{
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		FileOutputStream fos1 = null;
		FileOutputStream fos2 = null;
		
		try
		{
			
			int idx = filePath.lastIndexOf('/');
			if(idx == -1)
				idx = filePath.lastIndexOf('\\');
			
			String defaultFilePath = filePath.substring(0, idx);
			String openFile = filePath.substring(idx);
			String writeFile1 = "/LogAnalysis1.log";
			String writeFile2 = "/LogAnalysis2.log";
			
			
			fis = new FileInputStream(defaultFilePath + openFile);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			fos1 = new FileOutputStream(defaultFilePath + writeFile1);
			fos2 = new FileOutputStream(defaultFilePath + writeFile2);
			
			createObject();
			String line = "";
			
			while((line = br.readLine()) != null)
			{
							
				if(this.isRunningTime)
				{
					checkRunningTimeData(line);
					if(this.isRunningTime)
						continue;
				}
				
				StringAnalysis(line);
				
				if(this.map.isEmpty()) continue;
				
				LogDataAnalysis();
				
			}

			analyzeMinutelyLogData();
			
			fos1.write(this.fileOutString1.toString().getBytes());
			fos2.write(this.fileOutString2.toString().getBytes());
			
		} catch (IndexOutOfBoundsException indexOutException)
		{
			indexOutException.printStackTrace();
			System.out.println("잘못된 파일경로");
			
		} catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("파일 불러오기/내보내기 실패");
			
		} finally
		{
			
			init();
			
			try {
				if(br != null) br.close();
				if(isr != null) isr.close();
				if(fis != null) fis.close();
				if(fos1 != null) fos1.close();
				if(fos2 != null) fos2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * [시간] [구분] [쓰레드명 코드] - 키워드 : 데이터 <br/>
	 * 위 형식의 로그 문자열을 분석하여 각각의 정보를 Map에 저장
	 * 
	 * @param log 분석할 문자열
	 */
	private void StringAnalysis(String log)
	{	
		map.clear();
		int bracketCount = 0;
		
		String time = "", name = "", keyword = "", data = "";
		
		for(int i = 0; i < log.length(); i++)
		{
			char ch = log.charAt(i);
			
			if(ch == '[')
			{
				bracketCount++;
				if(bracketCount == 1)
				{
					this.stringBuilder.setLength(0);
					while(++i < log.length() && log.charAt(i) == ' ');
					while(i < log.length() && (ch = log.charAt(i++)) != ']') this.stringBuilder.append(ch);
					time = this.stringBuilder.toString();
				}
				else if(bracketCount == 3)
				{
					this.stringBuilder.setLength(0);
					while(++i < log.length() && log.charAt(i) == ' ');
					while(i < log.length() && (ch = log.charAt(i++)) != ' ') this.stringBuilder.append(ch);
					name = this.stringBuilder.toString();
					
					if(!name.startsWith("eclipse.galileo-bean-thread")) return;
				}
				
			}
			else if(ch == '-')
			{
				this.stringBuilder.setLength(0);
				while(++i < log.length() && log.charAt(i) == ' ');
				while(i < log.length() && (ch = log.charAt(i++)) != ':') this.stringBuilder.append(ch);
				keyword = this.stringBuilder.toString();
				
				this.stringBuilder.setLength(0);
				while(i < log.length() && log.charAt(i) == ' ') { i++; }				
				while(i < log.length() && (ch = log.charAt(i++)) != ' ') this.stringBuilder.append(ch);
				data = this.stringBuilder.toString();

			}
		}
		
		this.map.put("time", time);
		this.map.put("name", name);
		this.map.put("keyword", keyword);
		this.map.put("data", data);
		this.map.put("log",log);
	}
	
	/**
	 * Map에 저장된 로그 데이터를 분석해서 쓰레드 정보 저장
	 */
	private void LogDataAnalysis()
	{
		String keyword = this.map.get("keyword").trim();
		
		if(this.threadMap.containsKey(this.map.get("name")))
		{
			LogData logData = this.threadMap.get(this.map.get("name")); 
			
			if(keyword.startsWith("StopWatch"))
			{
				this.runningTimeData = logData;
				this.isRunningTime = true;
			}
			else if(keyword.startsWith("##galileo_bean end."))
			{
				logData.setEndTime(this.map.get("time"));
				
				if(checkLogData(logData))
				{
					this.fileOutString1.append(logData.toString());
					this.fileOutString1.append('\n');
				}
				
				this.threadMap.remove(logData.getThreadName());
			}
			else if(keyword.startsWith("##galileo_bean start."))
			{
				logData.init();
				
				logData.setStartTime(this.map.get("time"));
				logData.setThreadName(this.map.get("name"));
			}
			else
			{
				if(keyword.compareTo("ESB_TRAN_ID") == 0)
					logData.setEsbTranId(this.map.get("data"));
				else if(keyword.compareTo("Content-Length") == 0)
					logData.setContentLength(this.map.get("data"));
				else if(keyword.compareTo("#galileo call time") == 0)
					logData.setGalileoCalltime(this.map.get("data"));
			}
		}
		else
		{
			if(keyword.startsWith("##galileo_bean start."))
			{
				LogData logData = new LogData();
				
				logData.setStartTime(this.map.get("time"));
				logData.setThreadName(this.map.get("name"));
				
				this.threadMap.put(logData.getThreadName(), logData);

				insertMinutelyLogData(logData);
				
			}
		}
	}
	
	/**
	 * 소요시간 문자열을 분석해서 TaskName과 소요시간을 Map에 저장
	 * 
	 * @param log 분석할 문자열
	 */
	private void timeWatchAnalysis(String log)
	{
		map.clear();
		String keyword = "", data = "", percent = "";
		int idx = 0;
		if(log.length() > 0)
		{
			char ch;
			this.stringBuilder.setLength(0);
			while(idx < log.length() && (ch = log.charAt(idx++)) != ' ') this.stringBuilder.append(ch);
			data = stringBuilder.toString();
			
			this.stringBuilder.setLength(0);
			while(idx < log.length() && log.charAt(idx++) == ' ');
			while(idx < log.length() && (ch = log.charAt(idx++)) != '%') this.stringBuilder.append(ch);
			percent = stringBuilder.toString();
			
			this.stringBuilder.setLength(0);
			while(++idx < log.length() && log.charAt(idx) == ' ');
			while(idx < log.length()) this.stringBuilder.append(log.charAt(idx++));
			keyword = stringBuilder.toString();
			
			map.put("keyword", keyword);
			map.put("data", data);
			map.put("percent", percent);
			
		}
	}
	
	/**
	 * 소요시간 로그 인지 확인 후 소요시간을 데이터에 추가
	 * 
	 * @param log 로그 문자열
	 * @return 소요시간 로그가 맞으면 true 아니면 false
	 */
	private void checkRunningTimeData(String log)
	{
		if(this.runningTimeData != null && log.length() > 0)
		{
			char ch = log.charAt(0);
			
			if(ch >= '0' && ch <= '9')
			{
				timeWatchAnalysis(log);
				
				if(!this.map.isEmpty())
				{
					String taskName = map.get("keyword");
					String ms = map.get("data");
					
					if(taskName.startsWith("1.")) this.runningTimeData.setBeforeMarshalling(ms);
					else if(taskName.startsWith("2.")) this.runningTimeData.setMarshalling(ms);
					else if(taskName.startsWith("3.")) this.runningTimeData.setInvokingGalileo(ms);
					else if(taskName.startsWith("4.")) this.runningTimeData.setUscs(ms);
				}
			}
			else if(ch != '-' && ch != 'm')
			{
				this.runningTimeData = null;
				this.isRunningTime = false;
			}
		}
		else
			this.isRunningTime = false;
	}
	
	/**
	 * 모든 데이터가 존재하는지 검사
	 * 
	 * @param logData 검사할 로그 데이터
	 * @return 모든 데이터가 존재하면 true 아니면 false
	 */
	private boolean checkLogData(LogData logData)
	{
		return (logData.getThreadName() != null && 
				logData.getStartTime() != null &&
				logData.getEndTime() != null &&
				logData.getEsbTranId() != null  && 
				logData.getContentLength() != null && 
				logData.getGalileoCalltime() != null && 
				logData.getBeforeMarshalling() != null &&
				logData.getMarshalling() != null &&
				logData.getInvokingGalileo() != null &&
				logData.getUscs() != null); 
	}
	
	/**
	 * 분 기준으로 로그데이터를 리스트에 저장<br/>
	 * 
	 * 로그데이터의 시작시간이 기준시간과 같으면 minutelyLogData의 마지막 인덱스 리스트에 추가<br/>
	 * 기준시간과 다르면 새로운 리스트 객체를 생성해서 로그데이터를 추가해주고 minutelyLogData에 리스트 추가
	 * 
	 * @param logData 로그 데이터
	 */
	private void insertMinutelyLogData(LogData logData)
	{
		try {
			Date logTime = sdf.parse(logData.getStartTime());
			
			if(this.pivotTime.getTime() == logTime.getTime())
			{
				List<LogData> list = this.minutelyLogData.get(this.minutelyLogData.size()-1);
				
				if(list != null)
					list.add(logData);
				else
				{
					list = new ArrayList<LogData>();
					list.add(logData);
					this.minutelyLogData.add(list);
				}
			}
			else
			{
				this.pivotTime = logTime;
				List<LogData> list = new ArrayList<LogData>();
				list.add(logData);
				this.minutelyLogData.add(list);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 시작 시간을 기준으로 1분당 로그데이터 분석
	 * 
	 */
	private void analyzeMinutelyLogData()
	{
		for(int i = 0; i < this.minutelyLogData.size(); i++)
		{
			List<LogData> list = this.minutelyLogData.get(i);
			fileOutString2.append(analyzeLogDataList(list));
		}
	}
	
	/**
	 * 로그 데이터 리스트를 분석<br/>
	 * 시간, 처리건수, 평균소요시간, 최소시간, 최대시간, 평균사이즈, 최소사이즈, 최대사이즈
	 * 
	 * @param logDataList
	 * @return 결과 문자열
	 */
	private String analyzeLogDataList(List<LogData> logDataList)
	{
		String result = null;
		try
		{
			int count = 0;
			long totalTime = 0, totalSize = 0;
			int minTime = -1, maxTime = 0, minSize = -1, maxSize = 0;
			
			for(int i = 0; i < logDataList.size(); i++)
			{
				LogData logData = logDataList.get(i);
					
				if(logData.getEndTime() != null && checkLogData(logData))
				{
					int threadRunningTime = (int)(sdf2.parse(logData.getEndTime()).getTime() - sdf2.parse(logData.getStartTime()).getTime());
					int size = Integer.parseInt(logData.getContentLength());
					
					totalTime += threadRunningTime;
					totalSize += size;
					
					if(minTime == -1)
						minTime = threadRunningTime;
					
					if(threadRunningTime < minTime)
						minTime = threadRunningTime;
					else if(threadRunningTime > maxTime)
						maxTime = threadRunningTime;
					
					if(minSize == -1)
						minSize = size;
					
					if(size < minSize)
						minSize = size;
					else if(size > maxSize)
						maxSize = size;
					
					count++;
				}
					
			}
			
			stringBuilder.setLength(0);
			
			String time = logDataList.get(0).getStartTime();
			stringBuilder.append(time.substring(0, time.length()-3));
			stringBuilder.append(", ");
			stringBuilder.append(count);
			stringBuilder.append(", ");
			stringBuilder.append(totalTime/count);
			stringBuilder.append(", ");
			stringBuilder.append(minTime);
			stringBuilder.append(", ");
			stringBuilder.append(maxTime);
			stringBuilder.append(", ");
			stringBuilder.append(totalSize/count);
			stringBuilder.append(", ");
			stringBuilder.append(minSize);
			stringBuilder.append(", ");
			stringBuilder.append(maxSize);
			stringBuilder.append("\n");
			
			result = stringBuilder.toString();
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
