import generators.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.concurrent.TimeUnit;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

@RunWith(Parameterized.class)
public class UserLoginTest {
    private final User user;
    private final int StatusCodeExpected;
    private final boolean successResultExpected;
    private UserClient userClient;

    public UserLoginTest(User user, int StatusCodeExpected, boolean successResultExpected) {
        this.user = user;
        this.StatusCodeExpected = StatusCodeExpected;
        this.successResultExpected = successResultExpected;
    }

    @Parameterized.Parameters
    public static Object[][] getParameters() {

        return new Object[][]{
                {UserGenerator.getDefaultUser(), SC_OK, true},
                {UserGenerator.getUserFromParams("x9x9x9x@yandex.ru", "x9x9x9x", "Anna"), SC_OK, true},
                {UserGenerator.getUserFromParams("x9x9x9x@yandex.ru", "incorrect", "Anna"), SC_UNAUTHORIZED, false},
                {UserGenerator.getUserFromParams("incorrect", "x9x9x9x", "Anna"), SC_UNAUTHORIZED, false},
                {UserGenerator.getUserFromParams("x9x9x9x@yandex.ru", "", "Anna"), SC_UNAUTHORIZED, false},
                {UserGenerator.getUserFromParams("", "x9x9x9x", "Anna"), SC_UNAUTHORIZED, false}
        };
    }

    @Before
    public void setUp() throws InterruptedException {
        userClient = new UserClient();
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    @DisplayName("Пользователь может авторизоваться")
    public void userCanLoginTest() {
        ValidatableResponse responseLogin = userClient.loginUser(user);
        int statusCodeActual = responseLogin.extract().statusCode();
        Assert.assertEquals(StatusCodeExpected, statusCodeActual);
    }

    @Test
    @DisplayName("Успешное создание пользователя возвращает status:true")
    public void userSuccessLoginReturnsStatusTrueTest() {
        ValidatableResponse responseLogin = userClient.loginUser(user);
        boolean successResultActual = responseLogin.extract().path("success");
        Assert.assertEquals(successResultExpected, successResultActual);
    }
}

