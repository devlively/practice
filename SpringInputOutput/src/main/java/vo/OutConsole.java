package vo;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class OutConsole implements Output
{
	@Override
	public void out(String str)
	{
		System.out.println(str);
	}
	
}
