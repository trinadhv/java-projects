
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class interest {

public static void main(String [] args){ 

        int initial; 
        double rate,total,temp,earned,perMonth; 
        double Rate1; 
        int years; 
       rate=0.10f; 
        Scanner in=new Scanner(System.in); 
       System.out.println("Enter the Initial investment: "); 

        initial = in.nextInt(); 
        System.out.println("Enter no. of years: "); 

        years=in.nextInt(); 

        Rate1 = 1 + rate; 

        total=initial*(1+rate); 

        for(int i=2;i<=years;i++){ 

                Rate1 = Rate1*(1+rate); 

        } 

        total=initial*Rate1; 

        System.out.println("The initial investment was $"+initial+". The "
        		+ "total amount accumulated after "+years+" years,if $"+initial+" is "
        		+ "allowed to compound with an interest rate 10.00%,comes to "+NumberFormat.getCurrencyInstance(Locale.US).format(total) +"."); 

        temp=total; 
        years=years+1; 

         Rate1 = Rate1*(1+rate); 

        total=initial*Rate1; 

        earned=total-temp; 

        System.out.println("The total amount accumulated after "
        +years+"years,if "+NumberFormat.getCurrencyInstance(Locale.US).format(initial) +" is allowed to compound "
        		+ "with an interest rate of 10%, comes to "+NumberFormat.getCurrencyInstance(Locale.US).format(total) +"."); 

        temp=total; 

        Rate1 = 1 + rate; 

        total=total*Rate1; 

        perMonth=total-temp; 

        perMonth=perMonth/12; 

        System.out.println("The interest earned during this year is "+NumberFormat.getCurrencyInstance(Locale.US).format(earned) 
        		+".If interest is withdrawn each year thereafter,my income is "
        		+NumberFormat.getCurrencyInstance(Locale.US).format(perMonth) +" per month."); 
        in.close();

} 

}