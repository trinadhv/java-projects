package Change;

import java.util.Scanner;

public class Change {
	
	public static void main (String args[])
	{
		double amount;
		int tens=0;
		int fives=0;
		int dollars=0;
		int quarters=0;
		int dimes=0;
		int nikels=0;
		int cents=0;
		
		
		double quarterValue=0.25;
		double dimeValue=0.10;
		double nikelValue=0.05;
		double centValue=0.01;
		
		Scanner s= new Scanner(System.in);
		
		System.out.println("Enter an amount:\n");
		 amount=s.nextDouble();
		 
		 while(amount!=0)
		 {
			 
			 if(amount/10!=0)
				 
			 {
				
				tens=(int)amount/10;
				amount=amount-tens*10;
				
			 }
			 
			 if(amount/5!=0)
			 {
					 
				 fives=(int)amount/5;
				 amount=amount-fives*5;
			 }
			 
			 if(amount!=0)
			 {
				
				 dollars=(int)amount;
				 amount=amount-dollars;
			 }
			 
			 if(amount/quarterValue!=0)
			 {
					
				 quarters=(int)(amount/quarterValue);
				 amount=amount-quarters*quarterValue;
				 
			 }
			 
			 if(amount/dimeValue!=0)
			 {
					
				 dimes=(int)(amount/dimeValue);
				 amount=amount-dimes*dimeValue;
			 }
			 if(amount/nikelValue!=0)
			 {
					
				 nikels=(int)(amount/nikelValue);
				 amount=amount-nikels*nikelValue;
				 
			 }
			 
			 if(amount/centValue!=0)
			 {
					
				 cents=(int)(amount/centValue);
				 amount=amount-cents*centValue;
				 break;
			 }
		 }
		 
		 System.out.println(tens+" Tens");
		 System.out.println(fives+" Fives");
		 System.out.println(dollars+" Dollas");
		 System.out.println(quarters+" Quarters");
		 System.out.println(dimes+" Dimes");
		 System.out.println(nikels+" Nikels");
		 System.out.println(cents+" Cents");
		
		
	}

}
