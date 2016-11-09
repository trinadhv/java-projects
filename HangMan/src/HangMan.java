
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;

public class HangMan {
    private int numOfGuesses;
    private int length;
    private  String word;
    private String [] randomwords;
  
    private int[] revealed;
    private int count;
    public HangMan() throws FileNotFoundException{
      
        numOfGuesses=0;
        length=0;
        count=0;
        word="";
        randomwords= new String[72875];
       
        ask();
        getWord();
        startGame();
        
    }
    
    public void ask(){
       
    	
    try
    {
    	Scanner s=new Scanner(System.in);
        System.out.println("Enter Word Lenght");
        length=s.nextInt();
       
        
        if (length< 2 || length >137){
            System.out.println("Please re-enter a correct number");
            ask();
        }
      
        System.out.println("Enter number of guesses:");
        numOfGuesses=s.nextInt();
        
        if (numOfGuesses< length || numOfGuesses >= 26){
            //the alphabet is only 26 
        
            System.out.println("Please re-enter a correct number of guesses");
            ask();
        }
        
    }
    catch(Exception e)
    {
    	
    	System.out.println("invalid input , enter again");
    	ask();
    }
       
    }
    
    public void getWord() throws FileNotFoundException{
       
        File words= new File("words.txt");
        FileReader infile = new FileReader(words);
        Scanner s= new Scanner(infile);
        Random r = new Random();
        int i=0;
        int index=0;
       
        while(s.hasNext()){
            
            String secretWord=s.nextLine();
             if (secretWord.length() == length){
                 randomwords[index++]=secretWord;
                 
             }
         }
             
        word=randomwords[r.nextInt(index)];
        
     //  System.out.println(word);
       
    }
    
    public void startGame(){
        
    	int i=0;
    
    	String c;
    	revealed= new int[length];
    	char used[]=new char[numOfGuesses];
    	
    	for(i=0;i<revealed.length;i++)
    	{
    		
    		revealed[i]=-1;
    	}
    	
    	for(i=0;i<used.length;i++)
    	{
    		
    		used[i]='\0';
    	}
    	
    
    	
    for(i=0;i<numOfGuesses;i++)
    {
    	
    	Scanner input=new Scanner(System.in);
    
    	System.out.println("\nEnter a character");
    
		c=input.nextLine();
		
		if(c.length()>1||c.length()<1)
		{
			
			System.out.println("You can only enter a character");
		}
		else
		{
			
			used[i]=c.charAt(0);
			
			System.out.println("Guesses left :"+(numOfGuesses-i-1));
			System.out.print("Used Characters :");
			
			for(int l=0;l<=i;l++)
			{
				
				System.out.print(Character.toUpperCase(used[l])+", ");
			}
			
			System.out.println();
			for(int k=0;k<revealed.length;k++)
			{
				
				
			}
			 if(word.indexOf(c)==-1)
			 {
				 
				 System.out.println("Sorry, wrong guess");
				 print();
			 }
			 else
			 {
				 
			  	for(int j=0;j<word.length();j++)
			  	{
			  		
			  		if(word.charAt(j)==c.charAt(0))
			  		{
			  			
			  			
			  			if(revealed[j]==-1)
			  			{
			  			revealed[j]=word.indexOf(c);
			  			count++;
			  			}
			  			
			  			//System.out.println("Count:"+count);
			  		}
			  	
			  	}
			  	
			  	print();
			  	
			  	if(count>=word.length())
			  	{
			  		
			  		System.out.println("You have won the challenge");
			  		break;
			  	}
			  	
				 
			 }//else
			 
			
			
		}
		
		
    }
    
    if(i==numOfGuesses)
    {
    	System.out.println("You ran out of guesses");
    	
    	System.out.println("The Secret word is "+word.toUpperCase());
    }
		
    	   
    	
    }
    
    
    public void print()
    {
    	
    	
    	int i=0;
    	for(i=0;i<word.length();i++)
    	{
    		
    		
    		if(revealed[i]!=-1)
    		{
    			char c=word.charAt(i);
    			System.out.print(Character.toUpperCase(c)+" ");
    		}
    		
    		else
    		{
    			System.out.print(" - ");	
    			
    		}
    	}
    	
    	System.out.println();
    }
    
    public static void main (String[] args) throws FileNotFoundException{
         new HangMan();
        
    }
}
