package hr.abysalto.hiring.api.junior.controller;

import hr.abysalto.hiring.api.junior.dto.web.OrderForm;
import hr.abysalto.hiring.api.junior.dto.web.OrderItemForm;
import hr.abysalto.hiring.api.junior.dto.web.OrderWebView;
import hr.abysalto.hiring.api.junior.dto.web.UpdateOrderStatusForm;
import hr.abysalto.hiring.api.junior.manager.BuyerManager;
import hr.abysalto.hiring.api.junior.manager.OrderManager;
import hr.abysalto.hiring.api.junior.mapper.OrderWebMapper;
import hr.abysalto.hiring.api.junior.model.Buyer;
import hr.abysalto.hiring.api.junior.model.BuyerAddress;
import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.model.OrderItem;
import hr.abysalto.hiring.api.junior.model.OrderStatus;
import hr.abysalto.hiring.api.junior.model.PaymentOption;
import hr.abysalto.hiring.api.junior.repository.BuyerAddressRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/order")
@Controller
public class OrderController {

    private final OrderManager orderManager;
    private final BuyerManager buyerManager;
    private final BuyerAddressRepository buyerAddressRepository;
    private final OrderWebMapper orderWebMapper;

    public OrderController(
            OrderManager orderManager,
            BuyerManager buyerManager,
            BuyerAddressRepository buyerAddressRepository,
            OrderWebMapper orderWebMapper
    ) {
        this.orderManager = orderManager;
        this.buyerManager = buyerManager;
        this.buyerAddressRepository = buyerAddressRepository;
        this.orderWebMapper = orderWebMapper;
    }

    @GetMapping("/")
    public String list(@RequestParam(defaultValue = "desc") String sort, Model model) {
        String normalizedSort = "asc".equalsIgnoreCase(sort) ? "asc" : "desc";
        List<OrderWebView> orderList = this.orderManager.listSortedByTotal(normalizedSort).stream()
                .map(this::toWebView)
                .toList();

        model.addAttribute("orderList", orderList);
        model.addAttribute("sort", normalizedSort);
        return "order/index";
    }

    @GetMapping("/addnew")
    public String addNew(Model model) {
        OrderForm form = new OrderForm();
        form.getItems().add(new OrderItemForm());
        model.addAttribute("orderForm", form);
        enrichFormModel(model);
        return "order/neworder";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("orderForm") OrderForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            enrichFormModel(model);
            return "order/neworder";
        }
        this.orderManager.create(this.orderWebMapper.toCommand(form));
        return "redirect:/order/";
    }

    @GetMapping("/showFormForStatus/{id}")
    public String showStatusForm(@PathVariable("id") Long id, Model model) {
        Order order = this.orderManager.getById(id);
        if (order == null) {
            return "redirect:/order/";
        }

        UpdateOrderStatusForm form = new UpdateOrderStatusForm();
        form.setOrderId(order.getOrderId());
        form.setStatus(order.getOrderStatus());

        model.addAttribute("statusForm", form);
        model.addAttribute("order", toWebView(order));
        model.addAttribute("orderStatuses", OrderStatus.values());
        return "order/updatestatus";
    }

    @PostMapping("/updateStatus")
    public String updateStatus(@Valid @ModelAttribute("statusForm") UpdateOrderStatusForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Order order = this.orderManager.getById(form.getOrderId());
            model.addAttribute("order", order == null ? null : toWebView(order));
            model.addAttribute("orderStatuses", OrderStatus.values());
            return "order/updatestatus";
        }

        this.orderManager.updateStatus(form.getOrderId(), form.getStatus());
        return "redirect:/order/";
    }

    private void enrichFormModel(Model model) {
        model.addAttribute("buyers", this.buyerManager.getAllBuyers());
        model.addAttribute("paymentOptions", PaymentOption.values());
    }

    private OrderWebView toWebView(Order order) {
        Buyer buyer = this.buyerManager.getById(order.getBuyerId());
        BuyerAddress address = order.getDeliveryAddressId() == null
                ? null
                : this.buyerAddressRepository.findById(order.getDeliveryAddressId()).orElse(null);
        List<OrderItem> items = this.orderManager.getItemsByOrderId(order.getOrderId());
        return this.orderWebMapper.toView(order, buyer, address, items);
    }
}