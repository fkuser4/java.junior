package hr.abysalto.hiring.api.junior.repository;

import hr.abysalto.hiring.api.junior.model.Order;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    @Query("SELECT * FROM orders ORDER BY total_price ASC")
    Iterable<Order> findAllOrderByTotalPriceAsc();

    @Query("SELECT * FROM orders ORDER BY total_price DESC")
    Iterable<Order> findAllOrderByTotalPriceDesc();

    @Modifying
    @Query("UPDATE orders SET total_price = :totalPrice WHERE order_id = :orderId")
    boolean updateTotalPrice(@Param("orderId") Long orderId, @Param("totalPrice") java.math.BigDecimal totalPrice);

    @Modifying
    @Query("UPDATE orders SET order_status = :orderStatus WHERE order_id = :orderId")
    boolean updateOrderStatus(@Param("orderId") Long orderId, @Param("orderStatus") String orderStatus);
}