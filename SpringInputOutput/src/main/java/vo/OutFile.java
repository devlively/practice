package vo;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class OutFile implements Output
{
	@Override
	public void out(String str)
	{
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		
		try {
			
			fos = new FileOutputStream("D:/test2.txt");
			osw = new OutputStreamWriter(fos, "euc-kr");
			bw = new BufferedWriter(osw);
			
			
			bw.write(str);
			
		} catch (IOException e) {
			
			System.out.println("오류가 발생했습니다.");
			
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
