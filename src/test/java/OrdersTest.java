import generators.OrderGenerator;
import generators.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Order;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.concurrent.TimeUnit;

import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class OrdersTest {
    private final Order order;
    private final User user;
    private final int statusCodeExpected;
    private String accessToken;
    private OrderClient orderClient;

    public OrdersTest(User user, Order order, int statusCodeExpected){
        this.user = user;
        this.order = order;
        this.statusCodeExpected = statusCodeExpected;
    }

    @Parameterized.Parameters
    public static Object[][] getParameters() {
        return new Object[][]{
                {UserGenerator.getDefaultUser(), OrderGenerator.getOrderWithIngredients(), SC_OK},
                {UserGenerator.getDefaultUser(), OrderGenerator.getOrderWithoutIngredients(), SC_BAD_REQUEST},
                {UserGenerator.getUserWithoutPassword(), OrderGenerator.getOrderWithIngredients(), SC_OK},
                {UserGenerator.getUserWithoutPassword(), OrderGenerator.getOrderWithoutIngredients(), SC_BAD_REQUEST},
                {UserGenerator.getDefaultUser(), OrderGenerator.getOrderWithIncorrectIngredients(), SC_INTERNAL_SERVER_ERROR}
        };
    }

    @Before
    public void setUp(){
        orderClient = new OrderClient();
        UserClient userClient = new UserClient();
        ValidatableResponse responseLogin = userClient.loginUser(user);
        if (responseLogin.extract().statusCode() == SC_OK) {
            accessToken = responseLogin.extract().path("accessToken");
            accessToken = accessToken.split(" ")[1];
        }
        else {
            accessToken = "";
        }
    }

    @Test
    @DisplayName("Авторизоваунный пользователь может создать заказ, а неавторизованный - нет")
    public  void createOrderTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(10);
        ValidatableResponse response = orderClient.createOrder(order, accessToken);
        Assert.assertEquals(statusCodeExpected, response.extract().statusCode());
    }

}
