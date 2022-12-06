import generators.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class ListOrdersTest {
        private OrderClient orderClient;

        @Before
        public void setUp() {
            orderClient = new OrderClient();
        }

        @Test
        @DisplayName("Получить все заказы")
        public void getListOrdersTest () {
            ValidatableResponse response = orderClient.getListOrders();
            List<String> listOrders = new ArrayList<>(response.extract().path("orders.number"));
            Assert.assertEquals(50, listOrders.size());
        }

        @Test
        @DisplayName("Получить заказы конкретного пользователя")
        public void getListOrdersAuthorizedUserTest(){
            UserClient userClient = new UserClient();
            String token = userClient.loginUser(UserGenerator.getDefaultUser()).extract().path("accessToken");
            token = token.split(" ")[1];
            ValidatableResponse response = orderClient.getListUserOrders(token);
            List<String> listUserOrders = new ArrayList<>(response.extract().path("orders.number"));
            Assert.assertTrue(listUserOrders.size() > 0);
            Assert.assertEquals(SC_OK, response.extract().statusCode());
        }

        @Test
        @DisplayName("Запрос списка заказов неавторизованного пользователя вернет ошибку" )
        public void getListOrdersUnauthorizedUserTest(){
            ValidatableResponse response = orderClient.getListUserOrders("");
            Assert.assertEquals(SC_UNAUTHORIZED, response.extract().statusCode());
            Assert.assertEquals(response.extract().path("success"), false);
        }
    }

