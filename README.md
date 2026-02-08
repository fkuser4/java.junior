# Restaurant Order Management (Java)

Simple Spring Boot app for restaurant order management.

What is included:
- MVC pages
- REST API
- H2 database initialized from `schema.sql` and `data.sql`

## Run

Requirements:
- Java 17
- Maven

```bash
mvn clean spring-boot:run
```

App starts on: `http://localhost:8080`

## Login

Security is enabled (Basic + form login):
- username: `user`
- password: `password`

## MVC routes

- `GET /order/` - list orders (sorted by total)
- `GET /order/addnew` - create order form
- `GET /order/showFormForStatus/{id}` - status update form

## REST routes

- `GET /api/orders?sort=asc|desc`
- `GET /api/orders/{orderId}`
- `POST /api/orders`
- `PATCH /api/orders/{orderId}/status`

## REST request/response examples

### Create order

`POST /api/orders`

Request:
```json
{
  "buyerId": 1,
  "paymentOption": "CASH",
  "deliveryAddress": {
    "city": "Zagreb",
    "street": "Savska",
    "homeNumber": "12A"
  },
  "contactNumber": "+38591111222",
  "note": "No onion",
  "currency": "EUR",
  "items": [
    {
      "name": "Pizza Margherita",
      "quantity": 2,
      "price": 9.50
    }
  ]
}
```

Response (`201 Created`):
```json
{
  "orderId": 3,
  "buyerId": 1,
  "orderStatus": "WAITING_FOR_CONFIRMATION",
  "orderTime": "2026-02-08T20:15:00",
  "paymentOption": "CASH",
  "deliveryAddressId": 3,
  "contactNumber": "+38591111222",
  "note": "No onion",
  "currency": "EUR",
  "totalPrice": 19.00,
  "items": [
    {
      "itemNr": 1,
      "name": "Pizza Margherita",
      "quantity": 2,
      "price": 9.50
    }
  ]
}
```

### Update status

`PATCH /api/orders/{orderId}/status`

Request:
```json
{
  "status": "PREPARING"
}
```

Response (`200 OK`):
```json
{
  "orderId": 3,
  "buyerId": 1,
  "orderStatus": "PREPARING",
  "orderTime": "2026-02-08T20:15:00",
  "paymentOption": "CASH",
  "deliveryAddressId": 3,
  "contactNumber": "+38591111222",
  "note": "No onion",
  "currency": "EUR",
  "totalPrice": 19.00,
  "items": [
    {
      "itemNr": 1,
      "name": "Pizza Margherita",
      "quantity": 2,
      "price": 9.50
    }
  ]
}
```

Valid enum values:
- `paymentOption`: `CASH`, `CARD_UPFRONT`, `CARD_ON_DELIVERY`
- `status`: `WAITING_FOR_CONFIRMATION`, `PREPARING`, `DONE`
