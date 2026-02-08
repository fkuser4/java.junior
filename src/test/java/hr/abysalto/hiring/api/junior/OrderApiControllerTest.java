package hr.abysalto.hiring.api.junior;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String basicAuthHeader() {
        String token = Base64.getEncoder()
                .encodeToString("user:password".getBytes(StandardCharsets.UTF_8));
        return "Basic " + token;
    }

    @Test
    void listOrders_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .param("sort", "desc")
                        .header("Authorization", basicAuthHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void createOrder_shouldReturn201() throws Exception {
        String payload = """
                {
                  "buyerId": 1,
                  "paymentOption": "CASH",
                  "deliveryAddress": {
                    "city": "Zagreb",
                    "street": "Savska",
                    "homeNumber": "12A"
                  },
                  "contactNumber": "+38591111222",
                  "note": "test order",
                  "currency": "EUR",
                  "items": [
                    {
                      "name": "Test item",
                      "quantity": 2,
                      "price": 5.50
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/api/orders")
                        .header("Authorization", basicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.orderStatus").value("WAITING_FOR_CONFIRMATION"))
                .andExpect(jsonPath("$.items[0].name").value("Test item"));
    }

    @Test
    void updateStatus_shouldReturn200() throws Exception {
        String payload = """
                {
                  "status": "PREPARING"
                }
                """;

        mockMvc.perform(patch("/api/orders/{orderId}/status", 1L)
                        .header("Authorization", basicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.orderStatus").value("PREPARING"));
    }
}