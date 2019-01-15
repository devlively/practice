package DataStructure;

public class Main {

	public static void main(String[] args)
	{
		LinkedList<Integer> list = new LinkedList<Integer>();
		
		//list.add(10); list.add(16); list.add(4); list.add(7);
		//list.printAll();
		
		Stack<Integer> stack = new Stack<Integer>(Integer.class, 10);
		
		//stack.push(32); stack.push(8); stack.push(12);
		//System.out.println(stack.pop());
		//System.out.println(stack.top());
		
		Queue<Integer> queue = new Queue<Integer>();
		
		//queue.put(12); queue.put(43); queue.put(5);
		//System.out.println(queue.get());
		
		int[] array = new int[5];
		array[0] = 9; array[1] = 234; array[2] = 1; array[3] = -6; array[4] = 15;
		
		Sort sort = new Sort();
		
		//sort.selectionSort(array, 5);
		//sort.InsertionSort(array, 5);
		//sort.bubbleSort(array, 5);
		
		BinarySearch bs = new BinarySearch();		
		
		//int idx = bs.search(1, array, 6);
		//System.out.println(idx);
		
	}
	
}
