package hr.abysalto.hiring.api.junior.mapper;

import hr.abysalto.hiring.api.junior.dto.api.CreateOrderItemRequest;
import hr.abysalto.hiring.api.junior.dto.api.CreateOrderRequest;
import hr.abysalto.hiring.api.junior.dto.api.OrderItemResponse;
import hr.abysalto.hiring.api.junior.dto.api.OrderResponse;
import hr.abysalto.hiring.api.junior.manager.CreateOrderCommand;
import hr.abysalto.hiring.api.junior.manager.CreateOrderItemCommand;
import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.model.OrderItem;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderApiMapper {

    public CreateOrderCommand toCommand(CreateOrderRequest request) {
        CreateOrderCommand command = new CreateOrderCommand();
        command.setBuyerId(request.getBuyerId());
        command.setPaymentOption(request.getPaymentOption());
        command.setCity(request.getDeliveryAddress().getCity());
        command.setStreet(request.getDeliveryAddress().getStreet());
        command.setHomeNumber(request.getDeliveryAddress().getHomeNumber());
        command.setContactNumber(request.getContactNumber());
        command.setNote(request.getNote());
        command.setCurrency(request.getCurrency());

        List<CreateOrderItemCommand> items = request.getItems().stream()
                .map(this::toCommandItem)
                .toList();
        command.setItems(items);

        return command;
    }

    public OrderResponse toResponse(Order order, List<OrderItem> items) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setBuyerId(order.getBuyerId());
        response.setOrderStatus(order.getOrderStatus());
        response.setOrderTime(order.getOrderTime());
        response.setPaymentOption(order.getPaymentOption());
        response.setDeliveryAddressId(order.getDeliveryAddressId());
        response.setContactNumber(order.getContactNumber());
        response.setNote(order.getNote());
        response.setCurrency(order.getCurrency());
        response.setTotalPrice(order.getTotalPrice());
        response.setItems(items.stream().map(this::toResponseItem).toList());
        return response;
    }

    private CreateOrderItemCommand toCommandItem(CreateOrderItemRequest requestItem) {
        CreateOrderItemCommand commandItem = new CreateOrderItemCommand();
        commandItem.setName(requestItem.getName());
        commandItem.setQuantity(requestItem.getQuantity());
        commandItem.setPrice(requestItem.getPrice());
        return commandItem;
    }

    private OrderItemResponse toResponseItem(OrderItem item) {
        OrderItemResponse responseItem = new OrderItemResponse();
        responseItem.setItemNr(item.getItemNr());
        responseItem.setName(item.getName());
        responseItem.setQuantity(item.getQuantity());
        responseItem.setPrice(item.getPrice());
        return responseItem;
    }
}