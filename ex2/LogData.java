package ex2;

/**
 * 로그 정보
 * 
 * @author JuHan
 */
public class LogData {

	private String threadName;
	private String startTime;
	private String endTime;
	private String esbTranId;
	private String contentLength;
	private String galileoCalltime;
	private String beforeMarshalling;
	private String marshalling;
	private String invokingGalileo;
	private String uscs;
	
	/**
	 * 초기화 
	 */
	public void init()
	{
		this.threadName = null;
		this.startTime = null;
		this.endTime = null;
		this.esbTranId = null;
		this.contentLength = null;
		this.galileoCalltime = null;
		this.beforeMarshalling = null;
		this.marshalling = null;
		this.invokingGalileo = null;
		this.uscs = null;
	}
	
	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(this.getStartTime());
		stringBuilder.append(", ");
		stringBuilder.append(this.getEndTime());
		stringBuilder.append(", ");
		stringBuilder.append(this.getEsbTranId());
		stringBuilder.append(", ");
		stringBuilder.append(this.getContentLength());
		stringBuilder.append(", ");
		stringBuilder.append(this.getGalileoCalltime());
		stringBuilder.append(", ");
		stringBuilder.append(this.getBeforeMarshalling());
		stringBuilder.append(", ");
		stringBuilder.append(this.getMarshalling());
		stringBuilder.append(", ");
		stringBuilder.append(this.getInvokingGalileo());
		stringBuilder.append(", ");
		stringBuilder.append(this.getUscs());
		
		return stringBuilder.toString();
	}
	
	public String getThreadName() {
		return threadName;
	}
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getEsbTranId() {
		return esbTranId;
	}
	public void setEsbTranId(String esbTranId) {
		this.esbTranId = esbTranId;
	}
	public String getContentLength() {
		return contentLength;
	}
	public void setContentLength(String contentLength) {
		this.contentLength = contentLength;
	}
	public String getGalileoCalltime() {
		return galileoCalltime;
	}
	public void setGalileoCalltime(String galileoCalltime) {
		this.galileoCalltime = galileoCalltime;
	}
	public String getBeforeMarshalling() {
		return beforeMarshalling;
	}
	public void setBeforeMarshalling(String beforeMarshalling) {
		this.beforeMarshalling = beforeMarshalling;
	}
	public String getMarshalling() {
		return marshalling;
	}
	public void setMarshalling(String marshalling) {
		this.marshalling = marshalling;
	}
	public String getInvokingGalileo() {
		return invokingGalileo;
	}
	public void setInvokingGalileo(String invokingGalileo) {
		this.invokingGalileo = invokingGalileo;
	}
	public String getUscs() {
		return uscs;
	}
	public void setUscs(String uscs) {
		this.uscs = uscs;
	}
	
	
}
