package tacos.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import tacos.User;

//Spring will automatically generate the implementation of this interface at runtime.
public interface UserRepository extends CrudRepository<User , Long> {
	
	User findByUsername(String username);
}
