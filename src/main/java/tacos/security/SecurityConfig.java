package tacos.security;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
    public PasswordEncoder encoder() {
		
		//The following is a Map of different encoder formats both old and new which allows for the use of any one we choose.
		//This addresses the need for future upgrades in the Spring password encryption format, and better encryption algorithms. 
	    String idForEncode = "bcrypt";
	    Map<String,PasswordEncoder> encoders = new HashMap<>();
	    encoders.put(idForEncode, new BCryptPasswordEncoder());
	    encoders.put("noop", NoOpPasswordEncoder.getInstance());
	    encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
	    encoders.put("scrypt", new SCryptPasswordEncoder()); 
	    encoders.put("sha256", new StandardPasswordEncoder());

	    //Because we are passing 'idForEncode' as the String argument the BCryptPasswordEncoder() algorithm will be used.
	    //A different algorithm could be used by passing one of the other Strings.
	    PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);
		
      return passwordEncoder;
    }

	
    @Override //used for the HttpSecurity method.
    protected void configure(HttpSecurity http) throws Exception{
		
		 http
         .authorizeRequests()
         .antMatchers("/design", "/orders")
	     .access("hasRole('ROLE_USER')")
	     .antMatchers("/", "/**").access("permitAll")
	     .and()
	     .formLogin()
	     .loginPage("/login")
	     .and()
	     .logout()
	     .logoutSuccessUrl("/")
	     .and()
	     .csrf()
	     .ignoringAntMatchers("/h2-console/**")
	     .and()  
	     .headers()
	     .frameOptions()
	     .sameOrigin();
	}
    
	
    
    @Override
	protected void configure(AuthenticationManagerBuilder auth)
	      throws Exception {

	    auth
	      .userDetailsService(userDetailsService)
	      .passwordEncoder(encoder());// This does not pass the return value of encoder(), it passes the Bean instance marked above as Password encoder.
    }
}
