package hr.abysalto.hiring.api.junior.controller;

import hr.abysalto.hiring.api.junior.dto.api.CreateOrderRequest;
import hr.abysalto.hiring.api.junior.dto.api.OrderResponse;
import hr.abysalto.hiring.api.junior.dto.api.UpdateOrderStatusRequest;
import hr.abysalto.hiring.api.junior.manager.OrderManager;
import hr.abysalto.hiring.api.junior.mapper.OrderApiMapper;
import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.model.OrderItem;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/orders")
@RestController
public class OrderApiController {

    private final OrderManager orderManager;
    private final OrderApiMapper orderApiMapper;

    public OrderApiController(OrderManager orderManager, OrderApiMapper orderApiMapper) {
        this.orderManager = orderManager;
        this.orderApiMapper = orderApiMapper;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        Order createdOrder = this.orderManager.create(this.orderApiMapper.toCommand(request));
        List<OrderItem> items = this.orderManager.getItemsByOrderId(createdOrder.getOrderId());
        return ResponseEntity.status(HttpStatus.CREATED).body(this.orderApiMapper.toResponse(createdOrder, items));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> list(@RequestParam(defaultValue = "desc") String sort) {
        if (!"asc".equalsIgnoreCase(sort) && !"desc".equalsIgnoreCase(sort)) {
            throw new IllegalArgumentException("sort must be 'asc' or 'desc'");
        }

        List<OrderResponse> response = this.orderManager.listSortedByTotal(sort).stream()
                .map(order -> this.orderApiMapper.toResponse(order, this.orderManager.getItemsByOrderId(order.getOrderId())))
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long orderId) {
        Order order = this.orderManager.getById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        List<OrderItem> items = this.orderManager.getItemsByOrderId(orderId);
        return ResponseEntity.ok(this.orderApiMapper.toResponse(order, items));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        this.orderManager.updateStatus(orderId, request.getStatus());
        Order updatedOrder = this.orderManager.getById(orderId);

        if (updatedOrder == null) {
            return ResponseEntity.notFound().build();
        }

        List<OrderItem> items = this.orderManager.getItemsByOrderId(orderId);
        return ResponseEntity.ok(this.orderApiMapper.toResponse(updatedOrder, items));
    }
}