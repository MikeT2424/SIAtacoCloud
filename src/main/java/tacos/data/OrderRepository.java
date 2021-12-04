package tacos.data;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import tacos.Order;

public interface OrderRepository 
         extends CrudRepository<Order, Long> {
	
	
	//A custom method to return a list of orders by searching for the delivery zip code field.
	List<Order> findByDeliveryZip(String deliveryZip);
	
	
	//This query will return a list of orders between a certain date
	List<Order> readOrdersByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startdate , Date endDate);

}
