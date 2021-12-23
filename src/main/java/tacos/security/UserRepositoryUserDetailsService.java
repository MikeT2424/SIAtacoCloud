package tacos.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.
                                       UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tacos.User;
import tacos.data.UserRepository;

@Service //This flags the class for component scanning, Spring will declare this class as a bean automatically.
public class UserRepositoryUserDetailsService 
        implements UserDetailsService { // The implementation of UserDetailsService calls for the use of 'loadUserByUsername()'
										// which is covered by 'loadByUsername()' in the UserRepository interface. 

  private UserRepository userRepo; //create  an instance of the UserRepository.
  // private static final EmbeddedDatabaseType H2 = EmbeddedDatabaseType.H2;

  @Autowired //Spring will encounter our UserRepositoryUserDetailService class while doing a package scan, and will initialize its instance by calling the @Autowired annotated constructor.
  public UserRepositoryUserDetailsService(UserRepository userRepo) {
    this.userRepo = userRepo;
  }
  
  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    User user = userRepo.findByUsername(username);
    if (user != null) {
      return user;
    }
    throw new UsernameNotFoundException(
                    "User '" + username + "' not found");
  }
  
//  @Bean
//  public DataSource dataSource2() {
//	  return new EmbeddedDatabaseBuilder()
//			  .setType(EmbeddedDatabaseType.H2) // sets the database to the H2 type
//			  .addScript("taco_schema.sql") // sets the custom script for a new schema not named 'schema.sql'.
//			  .addScripts("user_data.sql" , "ingredient_data.sql") // sets new data scripts not named 'data.sql'.
//			  .build();
//  }

}
