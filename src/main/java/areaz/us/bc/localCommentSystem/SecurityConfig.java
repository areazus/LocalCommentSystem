package areaz.us.bc.localCommentSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.*;

import areaz.us.bc.localCommentSystem.mysql.MYSQLReader;
import areaz.us.bc.localCommentSystem.security.UserAuthenticator;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	public static final UserAuthenticator ua;
	
	static{
		MYSQLReader.init();
		ua = new UserAuthenticator(MYSQLReader.getAllUsers());	//initialize user authenticator with all the users
	}
	
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(ua);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.headers().frameOptions().disable();
    	
    	http
        .authorizeRequests()
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/login")
            .permitAll();
    }
}