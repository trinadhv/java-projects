package Carbon;

public class Interface {
	
	public static void main(String args[])
	{
		
		Bicycle b=new Bicycle(0);
		Car c=new Car(10.00);
		Building build=new Building(2500);
		
		System.out.println("Bicycle: "+b.getFootPrint());
		System.out.println("Building with 2500 s1ft: "+build.getFootPrint());
		System.out.println("Car that has used 10 gallons of gas: "+c.getFootPrint());
	}

}
