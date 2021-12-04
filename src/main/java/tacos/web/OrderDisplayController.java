package tacos.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import tacos.Order;
import tacos.Taco;
import tacos.data.IngredientRepository;
import tacos.data.OrderRepository;
import tacos.data.TacoRepository;

@Controller
@RequestMapping("/displayorders")
public class OrderDisplayController {

	
	@Autowired
	public OrderRepository orderRepo;
//	@Autowired 
//	public TacoRepository tacoRepo;
//	@Autowired
//	public IngredientRepository ingredientRepo;
	
	  
	  
	@GetMapping
	@ModelAttribute(name = "modelOrder")
	public String displayOrders(Model model) {
		//Create an arrayList to hold the orders for the display page. 
		List<Order> allOrders = new ArrayList<>();
		//Spring JPA automatically provides the findAll() method for the OrderRepository interface as well as any custom query method.
		orderRepo.findAll().forEach(i -> allOrders.add(i));
		
//		List<Taco> allTacos = new ArrayList<>();
//		tacoRepo.findAll().forEach(i -> allTacos.add(i));
		
		//The complete list of attributes can be added to the model with the addAllAttributes() method.
		model.addAttribute("orders" , allOrders);
		
		//model.addAttribute("tacos" , allTacos);
		
		
		
		return "design";
	}
}
