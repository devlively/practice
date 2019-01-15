package DataStructure;

import java.lang.reflect.Array;

public class Stack<T> {

	private T[] stack;
	private int top;
	public int count;
	public int size;
	
	@SuppressWarnings("unchecked")
	Stack(Class<T> componentType, int size)
	{
		stack = (T[])Array.newInstance(componentType, size);
		this.size = size;
		top = -1;
		count = 0;
	}
	
	public void push(T data)
	{
		if(count < size)
		{
			top++;
			stack[top] = data;
			count++;	
		}
	}
	
	public T pop()
	{
		if(top > -1)
		{
			T result = stack[top];
			stack[top] = null;
			top--;
			count--;
			return result;
		}
		else
			return null;
	}
	
	public T top()
	{
		if(top > -1)
		{
			return stack[top];
		}
		else
			return null;
	}
	
	public boolean empty()
	{
		if(top > -1)
			return false;
		else
			return true;
	}
}
