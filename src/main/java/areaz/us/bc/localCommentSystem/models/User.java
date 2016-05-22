package areaz.us.bc.localCommentSystem.models;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String Version = "0.0.1";

	public final int userID;

	public final int emplID;
	public final String firstName, lastName, preferredName, passwordHash;
	public final Privileges privileges;
	
	public User(int userID, int emplID, String firstName, String lastName, String preferredName, String passwordHash, Privileges privileges){
		this.userID = userID;
		this.emplID = emplID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.preferredName = preferredName;
		this.passwordHash = passwordHash;
		this.privileges = privileges;
	}
	
	public static class Privileges{
		public final boolean canWriteComments;
		public final boolean canReadComments;
		public final boolean isAdmin;
		public final boolean isSuperAdmin;
		public Privileges(boolean canWriteComments, boolean canReadComments, boolean isAdmin, boolean isSuperAdmin){
			this.canWriteComments = canWriteComments;
			this.canReadComments = canReadComments;
			this.isAdmin = isAdmin;
			this.isSuperAdmin = isSuperAdmin;
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		return passwordHash;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return preferredName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
