package StringCount;

public class FileInfo extends FileSystem {

	private String content;
	private String filePath;
	
	public String loadFile()
	{
		content = super.loadFile(filePath);
		
		return content;
	}
	
	@Override
	public String loadFile(String filePath)
	{
		this.filePath = filePath;
		
		return loadFile();
	}
	
	public void writeFile()
	{
		super.writeFile(filePath, content);
	}
	
	@Override
	public void writeFile(String filePath, String content)
	{
		this.filePath = filePath;
		this.content = content;
		
		writeFile();
	}
	
	public void writeFile(String filePath, int num)
	{
		this.filePath = filePath;
		this.content = String.valueOf(num);
		
		writeFile();
	}
	
	public void writeFile(String content)
	{
		this.content = content;
		
		writeFile();
	}
	
	public String getContent() {
		return content;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	
	
}
