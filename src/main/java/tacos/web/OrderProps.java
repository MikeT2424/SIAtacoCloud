
package tacos.web;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

//tag::notValidated[]
import org.springframework.boot.context.properties.
                                        ConfigurationProperties;
import org.springframework.stereotype.Component;
//end::notValidated[]
import org.springframework.validation.annotation.Validated;

//tag::notValidated[]
import lombok.Data;

@Component
@ConfigurationProperties(prefix="taco.orders")
@Data

@Validated

//This class allows the properties to be set in one class rather than all over the application classes.
public class OrderProps {
  
  //The member variable here will be the pageSize property which is limited to between 5 and 25.
  @Min(value=5, message="must be between 5 and 25")
  @Max(value=25, message="must be between 5 and 25")
  private int pageSize = 20;

}
