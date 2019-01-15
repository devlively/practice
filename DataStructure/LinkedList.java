package DataStructure;

public class LinkedList<T> {

	private Node<T> head;
	private Node<T> last;
	public int length;
	
	LinkedList()
	{
		head = null;
		last = null;
		length = 0;
	}
	
	public void add(T data)
	{
		Node<T> node = new Node<T>();
		
		node.data = data;
		node.nextNode = null;
		
		if(head != null)
		{
			last.nextNode = node;
			last = node;
			length++;
		}
		else
		{
			head = node;
			last = node;
			length++;
		}
		
	}
	
	public void insert(int idx, T data)
	{
		if(length > 0)
		{
			Node<T> node = new Node<T>();
			node.data = data;
			node.nextNode = null;
			
			if(idx > 0 && idx <= length)
			{
				Node<T> findNode = head;
				
				for(int i = 0; i < idx-1; i++)
					findNode = findNode.nextNode;
				
				if(findNode == last)
					last = node;
				
				node.nextNode = findNode.nextNode;
				findNode.nextNode = node;
			}
			else if(idx == 0)
			{
				node.nextNode = head;
				head = node;
			}
			
			length++;
		}
	}
	
	public void delete(int idx)
	{
		if(length > 0)
		{
			if(idx > 0 && idx < length)
			{
				Node<T> findNode = head;
				
				for(int i = 0; i < idx-1; i++)
					findNode = findNode.nextNode;
				
				if(findNode.nextNode == last)
					last = findNode;
				
				findNode.nextNode = findNode.nextNode.nextNode;

			}
			else if(idx == 0)
			{
				head = head.nextNode;
			}
			
			length--;
		}
	}
	
	public void clear()
	{
		head = null;
		last = null;
		length = 0;
	}
	
	public T get(int idx)
	{
		if(length > 0 && idx > -1 && idx < length)
		{
			Node<T> result = head;
			
			for(int i = 0; i < idx; i++)
				result = result.nextNode;
			
			return result.data;
		}
		else
			return null;
	}
	
	public void revise(int idx, T data)
	{
		if(length > 0 && idx > -1 && idx < length)
		{
			Node<T> result = head;
			
			for(int i = 0; i < idx; i++)
				result = result.nextNode;
			
			result.data = data;
		}
	}
	
	public void printAll()
	{
		Node<T> node = head;
		
		while(node != null)
		{
			System.out.print(node.data + " ");
			node = node.nextNode;
		}
		
		System.out.println();
	}
}
