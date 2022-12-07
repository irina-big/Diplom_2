import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.Order;

import static io.restassured.RestAssured.given;

public class  OrderClient extends Client {
        private final String PATH_ORDERS =  "/api/orders";

        @Step("Создать заказ")
        public ValidatableResponse createOrder(Order order, String accessToken) {
            return given()
                    .spec(getSpecification())
                    .auth()
                    .oauth2(accessToken)
                    .body(order)
                    .when()
                    .post(PATH_ORDERS)
                    .then();
        }
        @Step ("Получить все заказы")
        public ValidatableResponse getListOrders() {
            return given()
                    .spec(getSpecification())
                    .when()
                    .get(PATH_ORDERS + "/all")
                    .then();
        }
        @Step ("Получить заказы конкретного пользователя")
        public ValidatableResponse getListUserOrders(String accessToken){
            return given()
                    .spec(getSpecification())
                    .auth()
                    .oauth2(accessToken)
                    .when()
                    .get(PATH_ORDERS)
                    .then();
        }
    }

