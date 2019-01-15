package DataStructure;

public class Sort {

	public void selectionSort(int[] array, int length)
	{
		
		printAll(array, length);
		
		for(int i = 0; i < length - 1; i++)
		{
			int min = array[i];
			int idx = i;
			
			for(int j = i+1; j < length; j++)
			{
				if(min > array[j])
				{
					min = array[j];
					idx = j;
				}
			}
			
			if(idx != i)
			{
				array[idx] = array[i];
				array[i] = min;
			}
			
			printAll(array, length);
		}
	}
	
	public void InsertionSort(int[] array, int length)
	{
		printAll(array, length);
		
		for(int i = 1; i < length; i++)
		{
			for(int j = 0; j < i; j++)
			{
				if(array[i] < array[j])
				{
					int tmp = array[i];
					
					for(int k = i; k > j; k--)
						array[k] = array[k-1];
					
					array[j] = tmp;
					
					break;
				}
			}
			
			printAll(array, length);
		}
	}
	
	public void bubbleSort(int[] array, int length)
	{
		printAll(array, length);
		
		for(int i = 0; i < length; i++)
		{
			for(int j = 0; j < length - 1 - i; j++)
			{
				if(array[j] > array[j+1])
				{
					int tmp = array[j];
					array[j] = array[j+1];
					array[j+1] = tmp;
				}
			}
			printAll(array, length);
		}
	}
	
	private void printAll(int[] array, int length)
	{
		for(int i = 0; i < length; i++)
			System.out.print(array[i] + " ");
		System.out.println();
	}
	
}
