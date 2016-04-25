package jcse;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

public class EmployeeManager {	
	 private JCS cache; 
	 public EmployeeManager()
	  {
	    try
	    {
	      // Load the cache
	      cache = JCS.getInstance( "empCache" );
	      // Initialize the cache, Here data can be loaded during initialization
	      cache.put( "123", new Employee( "Nick", "Detroit.USA", "123" ) );
	      cache.put( "143", new Employee( "Ric",  "Seattle.USA", "143" ) );
	      cache.put( "153", new Employee( "Jhon", "Chicago.USA", "153" ) );
	      cache.put( "163", new Employee( "Dan", "Houston.USA", "163" ) ); 
	    }
	    catch( CacheException e )
	    {
	      e.printStackTrace();
	    }
	  }
	 public void addEmployee( Employee emp )
	  {
	    try
	    {
	      cache.put( emp.getEmpid(), emp );
	    }
	    catch( CacheException e )
	    {
	      e.printStackTrace();
	    }
	  }
	  public Employee getEmployee( String empid )
	  {
	    return ( Employee )cache.get( empid );
	  }
	  public void removeEmployee( String empid )
	  {
	    try
	    {
	      cache.remove( empid );
	    }
	    catch( CacheException e )
	    {
	      e.printStackTrace();
	    }
	  }	  
	  public static void main( String[] args )
	  {
	    // Create the employee manager
	    EmployeeManager empManager = new EmployeeManager();
	    // Add employees to the employee manager
	    /*empManager.addEmployee(new Employee("Name1", "address1", "empid1"));
	    empManager.addEmployee(new Employee("Name2", "address2", "empid2"));
	    empManager.addEmployee(new Employee("Name3", "address3", "empid3"));*/
	    // Get employee
	    Employee emp = empManager.getEmployee("123");
	    System.out.println( "Employee details retrieved from cache: " + emp.getName()+"-"+emp.getAddress());
	    // Remove employee
	    empManager.removeEmployee("123");
	    // After removal of employee
	    System.out.println( "Employee details after removal from cache: " + empManager.getEmployee("123") );
	  }
}
