package areaz.us.bc.localCommentSystem.models;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import areaz.us.bc.localCommentSystem.security.UserAuthenticator;

public class Comment {
	private static SimpleDateFormat df = new SimpleDateFormat("MM/dd");
	
	public final int EmplID, EmployeeID, year;
	public final Timestamp timestamp;
	public final String comment, employeeName;
	
	public Comment(int EmplID, Timestamp timestamp, String comment, int EmployeeID, int year){
		this.EmplID = EmplID;
		this.timestamp = timestamp;
		this.comment = comment;
		this.EmployeeID = EmployeeID;
		this.year = year;
		this.employeeName = UserAuthenticator.getUserPreferredName(EmployeeID);
		
	}
	
	
	public String getComment(){
		return this.comment;
	}
	
	public String getDate(){
		return df.format(this.timestamp);
	}
	
	public String getUser(){
		return this.employeeName;
	}

}
