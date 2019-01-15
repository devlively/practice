package StringCount;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileSystem implements FileIOInterface
{
	
	public String loadFile(String filePath)
	{
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		String str = "";
		
		try {
			
			fis = new FileInputStream(filePath);
			isr = new InputStreamReader(fis, "euc-kr");
			br = new BufferedReader(isr);
			
			String line = ""; 
			
			while((line = br.readLine()) != null)
			{
				str += line;
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} finally {

			try {
				
				if(br != null)
					br.close();
				
				if(isr != null)
					isr.close();
				
				if(fis != null)
					fis.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return str;
	}
	
	public void writeFile(String filePath, String content)
	{
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		
		try {
			
			fos = new FileOutputStream(filePath);
			osw = new OutputStreamWriter(fos, "euc-kr");
			bw = new BufferedWriter(osw);
			
			
			bw.write(content);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} finally {
			
			try {
				
				if(bw != null)
					bw.close();
				
				if(osw != null)
					osw.close();
				
				if(fos != null)
					fos.close();
				
			} catch (IOException e) {

				e.printStackTrace();
			}
			
		}
	}
	
}
