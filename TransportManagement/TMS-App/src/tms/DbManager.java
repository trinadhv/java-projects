package tms;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane; 

public class DbManager {
	
private Connection con;
private Statement stmt;
private ResultSet rs;
	
	DbManager()
	{
		
		try {
			
			
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			  
			System.out.println("Connection Successful");
			stmt=con.createStatement();
			
			  
			}
			catch (Exception e)
			{
				
				showErrorMessage("There is a problem connecting database. Please check the connection");
			
				System.exit(0);
			}
	}
	
	
	public void addLocation(String loc)
	{
		
   try 
   {
	   
	   int id=0;
	   rs=stmt.executeQuery("select * from locations");
	  
	   while(rs.next()){
		   id=rs.getInt(1);
	   }
	   
	   System.out.println(id);
	  stmt.executeQuery("insert into locations values("+(id+1)+",'"+loc+"')");
	   showSuccessMessage("Location has been added");
	   
   }
   catch(Exception e)
   {
	   
	   showErrorMessage("Couldnt Insert the value");
	   e.printStackTrace();
   }

	}
	
	public String[] getLocations()
	{
		List list = new ArrayList();
		
		
		try{
		
			rs=stmt.executeQuery("select location_name from locations");
			while(rs.next())
			{
				
				list.add(rs.getString(1));
				
			}
		
			String locations[]=new String[list.size()];
			
			int i;
			for(i=0;i<list.size();i++)
			{
				
				locations[i]=list.get(i).toString();
			}
			
			return locations;
			
		}
		catch(Exception e)
		{
			
		}
		
		
		return null;
	}
	
    public String[] getDrivers()
    {
    	List list = new ArrayList();
    	list.add("");
    	try{
    		
			rs=stmt.executeQuery("select driver_name from drivers");
			while(rs.next())
			{
				
				list.add(rs.getString(1));
				
			}
		
			String drivers[]=new String[list.size()];
			
			int i;
			
			for(i=0;i<list.size();i++)
			{
				
				drivers[i]=list.get(i).toString();
			}
			
			return drivers;
			
		}
		catch(Exception e)
		{
			
		}
		
		
		return null;
    	
    	
    }
	
	public void addDriver(String dname,long license,String gender,long contact)
	{
		
		try{
			
			   int id=0;
			   rs=stmt.executeQuery("select * from drivers");
			  
			   while(rs.next()){
				   id=Integer.parseInt(rs.getString("driver_id"));
			   }
			   System.out.println(id);
			   
			 stmt.executeQuery("insert into drivers values("+(id+1)+",'"+dname+"',"+license+",'"+gender+"',"+contact+")");
			 showSuccessMessage("Driver added Successfully");			   
		}
		catch(Exception e)
		{
			
			showErrorMessage("Driver already exists");
			e.printStackTrace();
		}
		
	}
	
	
	public void editDriver(String olddname,String newdname,long license,String gender,long contact)
	{
		
		int driverId=0;
		try{
			
			rs=stmt.executeQuery("select driver_id from drivers where driver_name='"+olddname+"'");
			
			while(rs.next()){
			driverId=rs.getInt(1);
			}
			
			System.out.println(driverId);
			
			 stmt.executeUpdate("update drivers set driver_name='"+newdname+"',license_no="+license+",gender='"+gender+"',contact="+contact+" where driver_id="+driverId+"");
			 showSuccessMessage("Driver Updated Successfully");			   
		}
		catch(Exception e)
		{
			
			showErrorMessage("Driver already exists");
			e.printStackTrace();
		}
		
	}
	
	
	public String[] getDriverData(String dname)

	{
		
		String driverData[]=new String[4];
    	
    	try{
    		
			rs=stmt.executeQuery("select * from drivers where driver_name='"+dname+"'");
			while(rs.next())
			{
				
				driverData[0]=rs.getString("driver_name");
				driverData[1]=rs.getString("license_no");
				driverData[2]=rs.getString("gender");
				driverData[3]=rs.getString("contact");
			}
		
		return driverData;
			
		}
		catch(Exception e)
		{
			
		}
		
		
		return null;
    	
    	
		
	}
	
	public String getDriversInfo()
	{
		
	String output="";
	
	try{
		rs=stmt.executeQuery("select * from drivers");
		
		output+=" Name\t\tLicense No.\t\tGender\t\tContact\n";
		output+="=============================================================================================\n\n";
		while(rs.next())
		{
		 	
		output+=""+rs.getString(2)+"\t\t"+rs.getString(3)+"\t\t"+rs.getString(4)+"\t\t"+rs.getString(5)+"\n";	
		}
		
		return output;
	}
	catch(Exception e)
	{
		
		e.printStackTrace();
	}
	
	
	return output;
	}
	
	
	
	
	public void addVehicle(String vname,long regno,int capacity,String vtype,String driver)
	{
	   	
	     int driverId=0;
		try{
			
           rs=stmt.executeQuery("select driver_id from drivers where driver_name='"+driver+"'");
			
			while(rs.next()){
			driverId=rs.getInt(1);
			}
			
			int id=0;
			rs=stmt.executeQuery("select * from vehicles");
			  
			while(rs.next()){
		     id=Integer.parseInt(rs.getString("vehicle_id"));
			  }
			
			 System.out.println(id);
			stmt.executeQuery("insert into vehicles values("+(id+1)+",'"+vname+"',"+regno+","+capacity+",'"+vtype+"',"+driverId+")");
		   showSuccessMessage("Vehicle added successfully");
			
		}
		catch(Exception e)
		{
	        showErrorMessage("Vehicle records already exist. Please check");
			e.printStackTrace();
		}
		
	}
	
	
	public String[] getVehicleNames()
	{
		try{
			
			List list=new ArrayList();
			list.add("");
			
			rs=stmt.executeQuery("select vehicle_name from vehicles");
			while(rs.next())
			{
				
				list.add(rs.getString(1));
			}
			
			int i=0;
			String vehicles[]=new String[list.size()];
			
			for(i=0;i<list.size();i++)
			{
			  vehicles[i]=list.get(i).toString();	
				
			}
			
			return vehicles;
			
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void showSuccessMessage(String message)
	{
		JOptionPane.showMessageDialog(new JFrame(),
			    message,
			    "Success",
			    JOptionPane.INFORMATION_MESSAGE);
		
	}
	
	public void showErrorMessage(String message)
	{
		JOptionPane.showMessageDialog(new JFrame(),
			    message,
			    "Database Warning",
			    JOptionPane.WARNING_MESSAGE);
		
	}

}
