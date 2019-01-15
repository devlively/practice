package DataStructure;

public class Queue<T> {

	private LinkedList<T> list;
	public int length;
	
	Queue()
	{
		list = new LinkedList<T>();
		length = 0;
	}
	
	public void put(T data)
	{
		list.add(data);
		length++;
	}
	
	public T get()
	{
		if(length > 0)
		{
			T data = list.get(0);
			list.delete(0);
			length--;

			return data;
		}
		else
			return null;
	}
	
}
