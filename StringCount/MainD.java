package StringCount;

public class MainD {
	
	public static void main(String[] args)
	{
		ConsoleSystem cs = new ConsoleSystem();
		FileInfo fi = new FileInfo();
		CountLetter cl = new CountLetter();
		
		String str = fi.loadFile("D:/test.txt");
		
		fi.writeFile("D:/test2.txt", cl.count(str));
		
	}

}
