package  areaz.us.bc.localCommentSystem.security;


import java.util.HashMap;
import java.util.List;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import areaz.us.bc.localCommentSystem.models.User;
import areaz.us.bc.localCommentSystem.mysql.MYSQLReader;


public class UserAuthenticator implements AuthenticationProvider{
	public final static String Version = "0.0.1";
	
	private static final BasicPasswordEncryptor passwordEncryptor;

	private static HashMap<Integer, User> users = new HashMap<Integer, User>();
	
	static{
		passwordEncryptor = new BasicPasswordEncryptor();
	}
		
	public UserAuthenticator(List<User> users){
		for(User user : users){
			UserAuthenticator.users.put(user.userID, user);
		}
	}

	
	public static String getUserPreferredName(int id){
		User user = users.get(id);
		return user==null?id+"":user.preferredName;
	}

	public static void main(String[] args){
		System.out.println(passwordEncryptor.encryptPassword("2Scari88"));
	}



	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
        String password = (String) authentication.getCredentials();
		
        try{
        	if(username!= null && password != null){
        		int empl = Integer.parseInt(username);
        		User toReturn = users.get(empl);
        		if(toReturn != null){
        			if(passwordEncryptor.checkPassword(password, toReturn.passwordHash)){
        	            return new UsernamePasswordAuthenticationToken(toReturn, password, null);
        				//return toReturn;
        			}
        		}
        	}
        	
        }catch(Exception e){
        }
		
        throw new BadCredentialsException("invalid username and/or password");
	}



	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
