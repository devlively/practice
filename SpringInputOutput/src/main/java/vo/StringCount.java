package vo;

public class StringCount implements Count
{

	@Override
	public int count(String str)
	{
		return str.length();
	}

}
