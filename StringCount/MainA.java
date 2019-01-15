package StringCount;

public class MainA {
	
	public static void main(String[] args)
	{
		ConsoleSystem cs = new ConsoleSystem();
		CountLetter cl = new CountLetter();
		
		String str = cs.read();
		cs.write(cl.count(str));
	}

}
