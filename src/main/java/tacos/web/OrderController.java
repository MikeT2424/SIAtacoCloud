package tacos.web;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;
import tacos.Order;
import tacos.User;
import tacos.data.OrderRepository;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
@ConfigurationProperties(prefix="taco.orders") //When setting the property, the parameter in the properties file will be taco.orders.pageSize
public class OrderController {
  
  private OrderRepository orderRepo;
  private OrderProps props; // Pull in the OrderProps bean, so it can be used to get properties in this class.
  
  @Autowired
  private HttpServletRequest context;

  public OrderController(OrderRepository orderRepo, OrderProps props) {
    this.orderRepo = orderRepo;
    this.props = props; //Create an instance of the OrderProps bean when the OrderController class is created.
  }
  
  @GetMapping
  public String ordersForUser(@AuthenticationPrincipal User user, Model model) {
	  Pageable pageable = PageRequest.of(0, props.getPageSize()); //This argument sets the max page size according to the property set by OrderProps.
	  model.addAttribute("orders" , orderRepo.findByUserOrderByPlacedAtDesc(user , pageable));
	  return "orderList";
  }
  
  @GetMapping("/current")
  public String orderForm(@AuthenticationPrincipal User user, 
      @ModelAttribute Order order) {
    if (order.getDeliveryName() == null) {
      order.setDeliveryName(user.getFullname());
    }
    if (order.getDeliveryStreet() == null) {
      order.setDeliveryStreet(user.getStreet());
    }
    if (order.getDeliveryCity() == null) {
      order.setDeliveryCity(user.getCity());
    }
    if (order.getDeliveryState() == null) {
      order.setDeliveryState(user.getState());
    }
    if (order.getDeliveryZip() == null) {
      order.setDeliveryZip(user.getZip());
    }
    
    //Get the token data from the session to display some information about it.
    CsrfToken token = new HttpSessionCsrfTokenRepository().loadToken(context);
    
    if(token != null) {
    	log.info("Token info: Header: " + token.getHeaderName() +" Parameter: " +  token.getParameterName() + " Token: " + token.getToken().toString());
    }
    
    return "orderForm";
  }

  // tag::processOrderWithAuthenticationPrincipal[]
  @PostMapping
  public String processOrder(@Valid Order order, Errors errors, 
      SessionStatus sessionStatus, 
      @AuthenticationPrincipal User user) {
    
    if (errors.hasErrors()) {
      return "orderForm";
    }
    
    order.setUser(user);
    
    orderRepo.save(order);
    sessionStatus.setComplete();
    
    return "redirect:/";
  }
  

}