package areaz.us.bc.localCommentSystem.mysql;

import static areaz.us.bc.localCommentSystem.security.Config.*;
import static areaz.us.bc.localCommentSystem.mysql.MYSQLStatements.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import areaz.us.bc.localCommentSystem.models.*;
import areaz.us.bc.localCommentSystem.security.UserAuthenticator;

public class MYSQLReader {
	public final static String Version = "0.0.1";

	private static Connection connection;
	private static Statement statement;
	private static boolean initialized = false;
	
	public static void init(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword);
			initialized = true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getData(String name){
		if(!initialized){
			throw new RuntimeException("MYSQL Reader is not initialized. Try calling MYSQLReader.init() before calling any other function");
		}
		String toReturn = "";
		try{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(getDataByName+name+"\'");
			while(rs.next()){
				toReturn = rs.getString("Data");
				break;
			}
			
		}catch(SQLException e){
			System.out.println(e);
		}
		return toReturn;
	}
	
	public static User extractUser(ResultSet rs) throws SQLException{
		int userID = rs.getInt("userID");
		int emplID = rs.getInt("emplID");
		String firstName = rs.getString("firstName");
		String lastName = rs.getString("lastName");
		String preferredName = rs.getString("preferredName");
		String passwordHash = rs.getString("passwordHash");
		boolean canRead = rs.getInt("canRead")==1?true:false;
		boolean canWrite = rs.getInt("canWrite")==1?true:false;
		int adminStatus = rs.getInt("adminRole");
		boolean isAdmin = adminStatus>0?true:false;
		boolean isSuperAdmin = adminStatus>1?true:false;
		User.Privileges privileges = new User.Privileges(canWrite, canRead, isAdmin, isSuperAdmin);
		User user = new User(userID, emplID, firstName, lastName,
				preferredName, passwordHash, privileges);
		return user;
	}
	
	
	public static User getUser(int emplID){
		if(!initialized){
			throw new RuntimeException("MYSQL Reader is not initialized. Try calling MYSQLReader.init() before calling any other function");
		}
		try{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(getUsersStmt+emplID);
			if(rs.next()){
				return extractUser(rs);
			}
		}catch(SQLException e){
			
		}
		return null;
	}
	
	
	public static List<User> getAllUsers(){
		if(!initialized){
			throw new RuntimeException("MYSQL Reader is not initialized. Try calling MYSQLReader.init() before calling any other function");
		}
		LinkedList<User> users = new LinkedList<User>();
		try{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(getAllUsersStmt);
			while(rs.next()){
				users.add(extractUser(rs));
			}
		}catch(SQLException e){
			
		}
		return users;
	}
	
	
	public static List<IfThen> getIfThenByEmplID(int emplID){
		if(!initialized){
			throw new RuntimeException("MYSQL Reader is not initialized. Try calling MYSQLReader.init() before calling any other function");
		}
		LinkedList<IfThen> ifThen = new LinkedList<IfThen>();
		try{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM comments.ifthen where userID = "+emplID);
			while(rs.next()){
				int ID = rs.getInt("ID");
				int field = rs.getInt("field");
				int condition = rs.getInt("condition");
				int highlight = rs.getInt("color");
				String forp = rs.getString("for");
				String value = rs.getString("value");
				ifThen.add(new IfThen(ID, field, forp, condition, highlight, value));
			}
		}catch(SQLException e){
			
		}
		
		return ifThen;
	}
	
	public static List<Comment> getCommentsByEmplID(int emplID){
		if(!initialized){
			throw new RuntimeException("MYSQL Reader is not initialized. Try calling MYSQLReader.init() before calling any other function");
		}
		LinkedList<Comment> comments = new LinkedList<Comment>();
		try{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(getCommentByEmplIDStmt+emplID);
			while(rs.next()){
				int EmplId = rs.getInt("EmplID");
				Timestamp ts = rs.getTimestamp("Date");
				String comment = rs.getString("Comment");
				int EmployeeId = rs.getInt("EmployeeID");
				int Year = rs.getInt("Year");
				comments.add(new Comment(EmplId, ts, comment, EmployeeId, Year));
			}
		}catch(SQLException e){
			
		}
		return comments;
	}
	
	
	public static List<Comment> getCommentsByUser(int emplID){
		if(!initialized){
			throw new RuntimeException("MYSQL Reader is not initialized. Try calling MYSQLReader.init() before calling any other function");
		}
		LinkedList<Comment> comments = new LinkedList<Comment>();
		try{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM commentstable WHERE EmployeeID="+emplID);
			while(rs.next()){
				int EmplId = rs.getInt("EmplID");
				Timestamp ts = rs.getTimestamp("Date");
				String comment = rs.getString("Comment");
				int EmployeeId = rs.getInt("EmployeeID");
				int Year = rs.getInt("Year");
				comments.add(new Comment(EmplId, ts, comment, EmployeeId, Year));
			}
		}catch(SQLException e){
			
		}
		return comments;
	}
	
	public static List<Comment> getAllComments(){
		if(!initialized){
			throw new RuntimeException("MYSQL Reader is not initialized. Try calling MYSQLReader.init() before calling any other function");
		}
		LinkedList<Comment> comments = new LinkedList<Comment>();
		try{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM commentstable");
			while(rs.next()){
				int EmplId = rs.getInt("EmplID");
				Timestamp ts = rs.getTimestamp("Date");
				String comment = rs.getString("Comment");
				int EmployeeId = rs.getInt("EmployeeID");
				int Year = rs.getInt("Year");
				comments.add(new Comment(EmplId, ts, comment, EmployeeId, Year));
			}
		}catch(SQLException e){
			
		}
		return comments;
	}
	
	public static boolean deleteIfThen(int EmplID, int IfThenID){
		if(!initialized){
			throw new RuntimeException("MYSQL Reader is not initialized. Try calling MYSQLReader.init() before calling any other function");
		}
		
		try{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM comments.ifthen where ID = "+IfThenID);
			while(rs.next()){
				int EID = rs.getInt("userID");
				System.out.println("ToDelete: "+EmplID+"\t"+IfThenID+"\tfor:"+EID);
				String stmt = "DELETE FROM `comments`.`ifthen` WHERE `ID`='"+IfThenID+"' and`userID`='"+EmplID+"';";
				System.out.println(stmt);
				if(EID == EmplID){
					statement = connection.createStatement();
					statement.executeUpdate(stmt);
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean addIfThen(int EmplId, int field, String value, int condition, int color, String equalsString){
		if(!initialized){
			throw new RuntimeException("MYSQL Reader is not initialized. Try calling MYSQLReader.init() before calling any other function");
		}
		
		if(condition == 9){
			condition = 8;
			equalsString = "^.*"+equalsString+".*$";
		}
		
		try{
			String stmt = "INSERT INTO `comments`.`ifthen` (`userID`, `field`, `for`, `condition`, `color`, `value`) VALUES (?, ?, ?, ?, ?, ?);";
			PreparedStatement st= connection.prepareStatement(stmt);
			st.setInt(1, EmplId);
			st.setInt(2, field);
			st.setString(3, value);
			st.setInt(4, condition);
			st.setInt(5, color);
			st.setString(6, equalsString);
			st.executeUpdate();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean addComment(Comment comment){
		if(!initialized){
			throw new RuntimeException("MYSQL Reader is not initialized. Try calling MYSQLReader.init() before calling any other function");
		}
		try{
			PreparedStatement st= connection.prepareStatement(addCommentString);
			st.setInt(1, comment.EmplID);
			st.setTimestamp(2, comment.timestamp);
			st.setString(3, comment.comment);
			st.setInt(4, comment.EmployeeID);
			st.setInt(5, comment.year);
			st.executeUpdate();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}
	

	public static String getCounselor(int emplId) {
		if(!initialized){
			throw new RuntimeException("MYSQL Reader is not initialized. Try calling MYSQLReader.init() before calling any other function");
		}
		try{
			PreparedStatement st= connection.prepareStatement("SELECT * FROM comments.studentcounselorsdata Where studentEmplID = ? order by id Desc Limit 1;");
			st.setInt(1, emplId);
			ResultSet rs = st.executeQuery();
			if(rs.next()){
				int counselor = rs.getInt("Counselor");
				return UserAuthenticator.getUserPreferredName(counselor);	
			}
		}catch(SQLException e){
			e.printStackTrace();
		}		
		return "None";
	}

	public static boolean addCounslors(AddCounselorsFormData form) {
		try{
			if(form.getToAdd() != null && form.getToAdd().trim().length()>0){
				String[] data =  form.getToAdd().split("\n");
				PreparedStatement statement= connection.prepareStatement("INSERT INTO `comments`.`studentcounselorsdata` (`studentEmplID`, `Counselor`) VALUES (?, ?);");
				int i = 0;
				
				for(String row : data){
					String[] colums = row.trim().split("\\s+");
					statement.setInt(1, Integer.parseInt(colums[0].trim()));
					statement.setInt(2, Integer.parseInt(colums[1].trim()));
					
					statement.addBatch();
		            i++;

		            if (i % 500 == 0 || i == data.length) {
		                statement.executeBatch(); // Execute every 500 items.
		            }
				}
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
		
		return false;
	}
	
	
	
}
