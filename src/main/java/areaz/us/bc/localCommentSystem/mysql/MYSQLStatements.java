package areaz.us.bc.localCommentSystem.mysql;

import areaz.us.bc.localCommentSystem.models.Comment;

public class MYSQLStatements {
	public final static String Version = "0.0.1";

	static final String getAllUsersStmt = "SELECT * FROM userstable";
	static final String getUsersStmt = "SELECT * FROM comments.userstable where userID = ";
	static final String getCommentByEmplIDStmt = "SELECT * FROM commentstable WHERE EmplID=";
	static final String getDataByName = "SELECT * FROM data WHERE Name=\'";
	static final String addCommentString = "INSERT INTO `comments`.`commentstable` (`EmplID`, `Date`, `Comment`, `EmployeeID`, `Year`) VALUES (?, ?, ?, ?, ?);";

	static final String addCommentString(Comment comment){
		return "INSERT INTO `comments`.`commentstable` (`EmplID`, `Date`, `Comment`, `EmployeeID`) VALUES ('"+comment.EmplID+"', '"+comment.timestamp.toString()+"', '"+comment.comment+"', '"+comment.EmployeeID+"');";
	}

} 
