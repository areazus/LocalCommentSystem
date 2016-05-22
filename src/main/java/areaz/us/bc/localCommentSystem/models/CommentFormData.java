package areaz.us.bc.localCommentSystem.models;

public class CommentFormData {
	private int EmplID;
	private int year;
	private String comment;

	
	public int getEmplID() {
		return EmplID;
	}
	public void setEmplID(int emplID) {
		EmplID = emplID;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
