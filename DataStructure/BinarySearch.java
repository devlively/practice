package DataStructure;

public class BinarySearch {

	public int search(int num, int[] array, int length)
	{
		int result = -1, left = 0, right = length - 1;
		
		while(left != right)
		{
			int mid = left + ((right - left) / 2);
			
			if(array[mid] > num)
			{
				right = mid - 1;
			}
			else if(array[mid] < num)
			{
				left = mid + 1;
			}
			else
			{
				result = mid;
				break;
			}
		}
		
		if(array[left] == num)
			result = left;
		
		return result;
	}
	
}
