package vo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class InFile implements Input
{

	@Override
	public String in()
	{
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		String str = "";
		
		try {
			
			fis = new FileInputStream("D:/test.txt");
			isr = new InputStreamReader(fis, "euc-kr");
			br = new BufferedReader(isr);
			
			String line = ""; 
			
			while((line = br.readLine()) != null)
			{
				str += line;
			}
			
		} catch (IOException e) {
			
			System.out.println("오류가 발생했습니다.");
			return null;
			
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
	

}
