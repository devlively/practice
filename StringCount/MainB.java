package StringCount;

public class MainB {
	
	public static void main(String[] args)
	{
		ConsoleSystem cs = new ConsoleSystem();
		FileInfo fi = new FileInfo();
		CountLetter cl = new CountLetter();
		
		String str = cs.read();
		fi.writeFile("D:/test.txt", cl.count(str));
		
	}

}
