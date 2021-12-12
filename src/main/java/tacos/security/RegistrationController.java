package tacos.security;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.data.UserRepository;


//Mark this as a controller with the 'register' path.
@Controller
@RequestMapping("/register")
public class RegistrationController {
  
  private UserRepository userRepo;
  private PasswordEncoder passwordEncoder;

  public RegistrationController(
      UserRepository userRepo, PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
  }
  
  //Map the 'registration' form
  @GetMapping
  public String registerForm() {
    return "registration";
  }
  
  //Save the data to the database in the User table. The toUser() method will send the data from the registration form, passing in the passwordEncoder.
  @PostMapping
  public String processRegistration(RegistrationForm form) {
	//The passwordEncoder is the @Bean declared in SecurityConfig.java.
	// A Bean is an instance of a class managed by the Spring container.
    userRepo.save(form.toUser(passwordEncoder)); 
    return "redirect:/login";
  }
}
