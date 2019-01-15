package StringCount;

import java.util.Scanner;

public class ConsoleSystem {

	public String read()
	{
		Scanner sc = new Scanner(System.in);
		
		String input = sc.nextLine();
		
		sc.close();
		
		return input;
	}
	
	public void write(int n)
	{
		System.out.println(n);
	}
	
	public void write(String str)
	{
		System.out.println(str);
	}
	
}
