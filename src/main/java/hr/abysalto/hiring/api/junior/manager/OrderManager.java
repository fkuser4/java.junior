package hr.abysalto.hiring.api.junior.manager;

import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.model.OrderItem;
import hr.abysalto.hiring.api.junior.model.OrderStatus;
import java.util.List;

public interface OrderManager {
    Order create(CreateOrderCommand command);
    List<Order> listSortedByTotal(String sort);
    List<OrderItem> getItemsByOrderId(Long orderId);
    Order getById(Long orderId);
    void updateStatus(Long orderId, OrderStatus status);
}