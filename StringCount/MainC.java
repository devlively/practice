package StringCount;

public class MainC {
	
	public static void main(String[] args)
	{
		ConsoleSystem cs = new ConsoleSystem();
		FileInfo fi = new FileInfo();
		CountLetter cl = new CountLetter();
		
		String str = fi.loadFile("D:/test.txt");
		
		cs.write(cl.count(str));

	}

}
