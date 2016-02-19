package gov.fac.cacom5.cetad.dronedetector.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Observable;
import java.util.concurrent.Semaphore;

public class DatabaseManager extends Observable {

	static Connection connection = null;
	static Statement stmt = null;
	private final Semaphore semaphore;
	
	public DatabaseManager() {
		semaphore = new Semaphore(1);
	}
	
	public void init() throws ClassNotFoundException, SQLException
	{
		try {
			semaphore.acquire();
			if(connection == null)
			{
				Class.forName("org.sqlite.JDBC");
				//connection = DriverManager.getConnection("jdbc:sqlite:coefficients.sqlite");
				connection = DriverManager.getConnection("jdbc:sqlite::resource:coefficients.sqlite");
				
				stmt = connection.createStatement();
			}
			else if(connection.isClosed())
			{
				Class.forName("org.sqlite.JDBC");
				//connection = DriverManager.getConnection("jdbc:sqlite:coefficients.sqlite");
				connection = DriverManager.getConnection("jdbc:sqlite::resource:coefficients.sqlite");
				stmt = connection.createStatement();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			semaphore.release();
		}
		
	}
	
	public void close() throws SQLException
	{
		try {
			semaphore.acquire();
			stmt.close();
			connection.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
	}
	
	public Hashtable<String, double[]> getDBData() throws SQLException 
	{
		ArrayList<double[]> coeffsArray = new ArrayList<double[]>();
		Hashtable<String, double[]> hashtable = new Hashtable<String, double[]>();
		ArrayList<String> namesArray = new ArrayList<String>();
		
		try {
			semaphore.acquire();
			//ResultSet rs = stmt.executeQuery("SELECT NAME FROM SAMPLES WHERE COEFFICIENTS = '-2.5388,2.2826,-1.8316,1.4627,-1.3016,1.2723,-1.0663,0.7069,-0.2645'");
			//ResultSet rs = stmt.executeQuery("SELECT COEFFICIENTS FROM SAMPLES");
			ResultSet rs = stmt.executeQuery( "SELECT * FROM SAMPLES" );
			while (rs.next()) 
			{
				int id = rs.getInt("id");
				String  coefficients = rs.getString("coefficients");
				
				coeffsArray.add(convertToDoubleArray(coefficients));
				
				String  name = rs.getString("name");
				
				namesArray.add(name);
				
				System.out.println( "ID = " + id );
				System.out.println( "NAME = " + name );
				System.out.println( "COEFFICIENTS = " + coefficients );
				System.out.println();
			}
			rs.close();
			
			for (int i = 0; i < namesArray.size(); i++) {
				hashtable.put(namesArray.get(i), coeffsArray.get(i));
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
		
		return hashtable;
	}
	
	public String[] getNames() throws SQLException
	{
		ArrayList<String> resultsArray = new ArrayList<>();
		String[] results = new String[]{};
		try {
			semaphore.acquire();
			ResultSet rs = stmt.executeQuery( "SELECT NAME FROM SAMPLES" );
			while (rs.next()) 
			{
				String  name = rs.getString("name");
				resultsArray.add(name);
			}
			rs.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
		
		return resultsArray.toArray(results);
	}
	
	public String getName(int pId) throws SQLException
	{
		String name = "";
		try {
			semaphore.acquire();
			ResultSet rs = stmt.executeQuery( "SELECT NAME from SAMPLES where ID=" + pId + ";" );
			while (rs.next()) 
			{
				name = rs.getString("name");
			}
			rs.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
		
		return name;
	}
	
	public Integer[] getIds() throws SQLException
	{
		ArrayList<Integer> resultsArray = new ArrayList<>();
		Integer[] results = new Integer[]{};
		
		try {
			semaphore.acquire();
			ResultSet rs = stmt.executeQuery( "SELECT ID FROM SAMPLES" );
			while (rs.next()) 
			{
				int id = rs.getInt("id");
				resultsArray.add(id);
			}
			rs.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
		
		return resultsArray.toArray(results);
	}
	
	public ArrayList<double[]> getCoefficients() throws SQLException
	{
		ArrayList<double[]> resultsArray = new ArrayList<double[]>();
	
		try {
			semaphore.acquire();
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COEFFICIENTS FROM SAMPLES");
			
			while (rs.next()) 
			{
				String coeffsString = rs.getString("coefficients");
				resultsArray.add(convertToDoubleArray(coeffsString));
			}
			rs.close();
			
			if(resultsArray.isEmpty()) throw new NullPointerException();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
		
		return resultsArray;
	}
	
	private static double[] convertToDoubleArray(String arg0)
	{
		String[] temp = arg0.split(",");
		double[] results = new double[temp.length];
		
		for (int i = 0; i < temp.length; i++) {
			results[i] = Double.parseDouble(temp[i]);
		}
		return results;
	}
	
	public void addEntry(String pName, double[] pCoeffs) throws SQLException
	{
		try {
			semaphore.acquire();
			String coeffsArrayInString = String.format(Locale.ENGLISH, "%.4f", pCoeffs[0]);

			for(int i = 1; i < pCoeffs.length; i++) { 
				coeffsArrayInString += "," + String.format(Locale.ENGLISH, "%.4f", pCoeffs[i]);
			}
			
			String sql = "INSERT INTO SAMPLES (ID,COEFFICIENTS,NAME) " + "VALUES (NULL, " + "'" + coeffsArrayInString + "'" + ", " + "'" + pName + "');"; 
			stmt.executeUpdate(sql);
			
			setChanged();
	        notifyObservers(pName);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
	}
	
	public String[] deleteEntry(String pName) throws SQLException
	{
		ArrayList<String> resultsArray = new ArrayList<>();
		String[] results = new String[]{};
				
		try {
			semaphore.acquire();
			
			String sql = "SELECT ID from SAMPLES where NAME=" + '"' + pName + '"' + ";";
			ResultSet rs = stmt.executeQuery(sql);
			
			int id = 0;
			while ( rs.next() ) 
			{
				id = rs.getInt("id");
			}
			
			sql = "DELETE from SAMPLES where ID=" + id + ";";
			stmt.executeUpdate(sql);
			
			rs = stmt.executeQuery( "SELECT NAME FROM SAMPLES;" );
			
			while ( rs.next() ) 
			{
				String  name = rs.getString("name");
				resultsArray.add(name);
			}
			rs.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
		
		return resultsArray.toArray(results);
	}

}
