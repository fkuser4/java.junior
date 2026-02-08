package hr.abysalto.hiring.api.junior.repository;

import hr.abysalto.hiring.api.junior.model.OrderItem;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {

    @Query("SELECT * FROM order_item WHERE order_id = :orderId ORDER BY item_nr ASC")
    Iterable<OrderItem> findByOrderId(@Param("orderId") Long orderId);
}