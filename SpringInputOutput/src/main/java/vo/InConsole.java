package vo;

import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class InConsole implements Input
{

	@Override
	public String in()
	{
		String str = "";
		Scanner sc = new Scanner(System.in);
		str = sc.nextLine();
		sc.close();
		return str;
	}
	
}
