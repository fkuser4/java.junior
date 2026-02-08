package hr.abysalto.hiring.api.junior.mapper;

import hr.abysalto.hiring.api.junior.dto.web.OrderForm;
import hr.abysalto.hiring.api.junior.dto.web.OrderItemForm;
import hr.abysalto.hiring.api.junior.dto.web.OrderItemWebView;
import hr.abysalto.hiring.api.junior.dto.web.OrderWebView;
import hr.abysalto.hiring.api.junior.manager.CreateOrderCommand;
import hr.abysalto.hiring.api.junior.manager.CreateOrderItemCommand;
import hr.abysalto.hiring.api.junior.model.Buyer;
import hr.abysalto.hiring.api.junior.model.BuyerAddress;
import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.model.OrderItem;
import hr.abysalto.hiring.api.junior.model.OrderStatus;
import hr.abysalto.hiring.api.junior.model.PaymentOption;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderWebMapper {

    public CreateOrderCommand toCommand(OrderForm form) {
        CreateOrderCommand command = new CreateOrderCommand();
        command.setBuyerId(form.getBuyerId());
        command.setPaymentOption(form.getPaymentOption());
        command.setCity(form.getCity());
        command.setStreet(form.getStreet());
        command.setHomeNumber(form.getHomeNumber());
        command.setContactNumber(form.getContactNumber());
        command.setNote(form.getNote());
        command.setCurrency(form.getCurrency());
        command.setItems(form.getItems().stream().map(this::toCommandItem).toList());
        return command;
    }

    public OrderWebView toView(Order order, Buyer buyer, BuyerAddress address, List<OrderItem> items) {
        OrderWebView view = new OrderWebView();
        view.setOrderId(order.getOrderId());
        view.setBuyerName(formatBuyerName(buyer));
        view.setOrderStatus(order.getOrderStatus());
        view.setOrderStatusLabel(toOrderStatusLabel(order.getOrderStatus()));
        view.setOrderTime(order.getOrderTime());
        view.setPaymentOption(order.getPaymentOption());
        view.setPaymentOptionLabel(toPaymentOptionLabel(order.getPaymentOption()));
        view.setDeliveryAddress(formatAddress(address));
        view.setContactNumber(order.getContactNumber());
        view.setNote(order.getNote());
        view.setCurrency(order.getCurrency());
        view.setTotalPrice(order.getTotalPrice());
        view.setItems(items.stream().map(this::toItemView).toList());
        return view;
    }

    private CreateOrderItemCommand toCommandItem(OrderItemForm itemForm) {
        CreateOrderItemCommand commandItem = new CreateOrderItemCommand();
        commandItem.setName(itemForm.getName());
        commandItem.setQuantity(itemForm.getQuantity());
        commandItem.setPrice(itemForm.getPrice());
        return commandItem;
    }

    private OrderItemWebView toItemView(OrderItem item) {
        OrderItemWebView view = new OrderItemWebView();
        view.setItemNr(item.getItemNr());
        view.setName(item.getName());
        view.setQuantity(item.getQuantity());
        view.setPrice(item.getPrice());
        return view;
    }

    private String formatBuyerName(Buyer buyer) {
        if (buyer == null) {
            return "Unknown buyer";
        }
        String title = buyer.getTitle() == null ? "" : buyer.getTitle().trim();
        if (!title.isEmpty()) {
            return buyer.getFirstName() + " " + buyer.getLastName() + " (" + title + ")";
        }
        return buyer.getFirstName() + " " + buyer.getLastName();
    }

    private String formatAddress(BuyerAddress address) {
        if (address == null) {
            return "N/A";
        }
        String homeNumber = address.getHomeNumber() == null ? "" : " " + address.getHomeNumber();
        return address.getStreet() + homeNumber + ", " + address.getCity();
    }

    private String toOrderStatusLabel(OrderStatus status) {
        if (status == null) {
            return "N/A";
        }
        return switch (status) {
            case WAITING_FOR_CONFIRMATION -> "Waiting for confirmation";
            case PREPARING -> "Preparing";
            case DONE -> "Done";
        };
    }

    private String toPaymentOptionLabel(PaymentOption paymentOption) {
        if (paymentOption == null) {
            return "N/A";
        }
        return switch (paymentOption) {
            case CASH -> "Cash";
            case CARD_UPFRONT -> "Card upfront";
            case CARD_ON_DELIVERY -> "Card on delivery";
        };
    }
}