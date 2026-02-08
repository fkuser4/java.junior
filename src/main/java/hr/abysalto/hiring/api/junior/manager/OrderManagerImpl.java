package hr.abysalto.hiring.api.junior.manager;

import hr.abysalto.hiring.api.junior.model.BuyerAddress;
import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.model.OrderItem;
import hr.abysalto.hiring.api.junior.model.OrderStatus;
import hr.abysalto.hiring.api.junior.repository.BuyerAddressRepository;
import hr.abysalto.hiring.api.junior.repository.BuyerRepository;
import hr.abysalto.hiring.api.junior.repository.OrderItemRepository;
import hr.abysalto.hiring.api.junior.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderManagerImpl implements OrderManager {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BuyerRepository buyerRepository;
    private final BuyerAddressRepository buyerAddressRepository;

    public OrderManagerImpl(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            BuyerRepository buyerRepository,
            BuyerAddressRepository buyerAddressRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.buyerRepository = buyerRepository;
        this.buyerAddressRepository = buyerAddressRepository;
    }

    @Override
    @Transactional
    public Order create(CreateOrderCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("CreateOrderCommand is required");
        }
        if (!this.buyerRepository.existsById(command.getBuyerId())) {
            throw new IllegalArgumentException("Buyer not found: " + command.getBuyerId());
        }
        if (command.getItems() == null || command.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        BuyerAddress address = new BuyerAddress();
        address.setCity(command.getCity());
        address.setStreet(command.getStreet());
        address.setHomeNumber(command.getHomeNumber());
        BuyerAddress savedAddress = this.buyerAddressRepository.save(address);

        Order order = new Order();
        order.setBuyerId(command.getBuyerId());
        order.setOrderStatus(OrderStatus.WAITING_FOR_CONFIRMATION);
        order.setOrderTime(LocalDateTime.now());
        order.setPaymentOption(command.getPaymentOption());
        order.setDeliveryAddressId(savedAddress.getBuyerAddressId());
        order.setContactNumber(command.getContactNumber());
        order.setNote(command.getNote());
        order.setCurrency(normalizeCurrency(command.getCurrency()));
        order.setTotalPrice(calculateTotal(command.getItems()));

        Order savedOrder = this.orderRepository.save(order);

        int itemNr = 1;
        for (CreateOrderItemCommand inputItem : command.getItems()) {
            OrderItem item = new OrderItem();
            item.setOrderId(savedOrder.getOrderId());
            item.setItemNr(itemNr++);
            item.setName(inputItem.getName());
            item.setQuantity(inputItem.getQuantity());
            item.setPrice(inputItem.getPrice());
            this.orderItemRepository.save(item);
        }

        return savedOrder;
    }

    @Override
    public List<Order> listSortedByTotal(String sort) {
        Iterable<Order> source = "asc".equalsIgnoreCase(sort)
                ? this.orderRepository.findAllOrderByTotalPriceAsc()
                : this.orderRepository.findAllOrderByTotalPriceDesc();

        List<Order> result = new ArrayList<>();
        source.forEach(result::add);
        return result;
    }

    @Override
    public List<OrderItem> getItemsByOrderId(Long orderId) {
        Iterable<OrderItem> source = this.orderItemRepository.findByOrderId(orderId);
        List<OrderItem> result = new ArrayList<>();
        source.forEach(result::add);
        return result;
    }

    @Override
    public Order getById(Long orderId) {
        return this.orderRepository.findById(orderId).orElse(null);
    }

    @Override
    @Transactional
    public void updateStatus(Long orderId, OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status is required");
        }
        if (!this.orderRepository.existsById(orderId)) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        this.orderRepository.updateOrderStatus(orderId, status.toString());
    }

    private BigDecimal calculateTotal(List<CreateOrderItemCommand> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (CreateOrderItemCommand item : items) {
            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return total;
    }

    private String normalizeCurrency(String currency) {
        return currency == null ? null : currency.trim().toUpperCase(Locale.ROOT);
    }
}