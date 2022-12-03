import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
        private final String PATH_ORDERS =  "/api/orders";

        @Step("Создать заказ")
        public ValidatableResponse createOrder(Order order) {
            return given()
                    .spec(getSpecification())
                    .body(order)
                    .when()
                    .post(PATH_ORDERS)
                    .then();
        }
        @Step ("Получить список всех заказов")
        public ValidatableResponse getListOrders() {
            return given()
                    .spec(getSpecification())
                    .when()
                    .get(PATH_ORDERS)
                    .then();
        }
    }

