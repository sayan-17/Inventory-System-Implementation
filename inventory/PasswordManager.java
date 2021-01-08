import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PasswordManager  {
	private static boolean error = false;
	private String passkey = "";
	private String TABLE_NAME = "PASSWORD";
	private String ATTRIBUTE = "PASSKEY";
	
	private final String driver = "com.mysql.jdbc.Driver";
	private final String location = "jdbc:mysql://localhost:3306/inventorySystem";
	private final String user = "root";
	private final String password = "sayan";
	
	
	
	public boolean getKeyToVerify(String passkey){
		String storedKey = extractSavedKey();
		if (error)
			return false;
		else{
			if (passkey.equals(storedKey))
				return true;
			else
				return false;
		}
	}
	
	public String resetKey (String password, String resetPassword) throws Exception {
		if (getKeyToVerify(password)){
			passkey =  resetPassword;
			setPassword(passkey);
			return "PASSWORD UPDATED";
		}else{
			return "INVALID PASSWORD";
		}
	}
	
	public String setKey (String password) throws Exception {
		if (isAnyKeyPresent())
			return "INVALID";
		else
			setPassword(password);
		return "PASSWORD UPDATED";
	}
	
	private boolean isAnyKeyPresent() throws Exception {
		String str = getPassword();
		if (str.equals(""))
			return false;
		return true;
	}
	
	private String extractSavedKey(){
		try {
			return getPassword();
		}catch (Exception e){
			error = true;
			return "ERROR";
		}
	}
	
	private String getPassword() throws Exception {
		String command = "SELECT * FROM " + TABLE_NAME + ";";
		return executePasswordQuery(command);
	}
	
	private void setPassword(String psswrd) throws Exception{
		String command = "UPDATE " + TABLE_NAME + " SET " + ATTRIBUTE + " = '" + psswrd + "';";
		executePasswordStatement(command);
	}
	
	private String executePasswordQuery(String command) throws Exception {
		
		ResultSet set;
		Class.forName(driver);
		
		Connection con = DriverManager.getConnection(location, user, password);
		
		Statement stmt = con.createStatement();
		command = command.toLowerCase();
		System.out.println(command);
		set = stmt.executeQuery(command);
		
		if (!set.next() ){
			stmt.execute("INSERT INTO " + TABLE_NAME + " VALUES ('');" );
			set = stmt.executeQuery(command);
		}
		
		return set.getString(ATTRIBUTE);
	}
	
	public void executePasswordStatement(String command) throws Exception {
		
		Class.forName(driver);
		
		Connection con = DriverManager.getConnection(location, user, password);
		
		Statement stmt = con.createStatement();
		command = command.toLowerCase();
		System.out.println(command);
		stmt.execute(command);
	}
}
