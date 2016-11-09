

public class SumOfEven {
	
	public static void main (String args[])
	{
		
		int sum=0;
		int count=0;
		int i=0;
		for(i=2;i<40;i++)
		{
			
			if(count==10)
			{
				
				break;
			}
			
			if(i%2==0)
			{
				
				sum+=i;
				System.out.println(sum);
				count++;
			}
		}
		
		
		
		
	}

}
