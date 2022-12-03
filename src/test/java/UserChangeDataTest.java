import generators.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.concurrent.TimeUnit;

import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class UserChangeDataTest {
    private final User user;
    private final int statusCodeExpected;
    private int statusCodeActual;
    private final boolean successResultExpected;
    private  String accessToken;
    private String refreshToken;
    private UserClient userClient;
    private ValidatableResponse responseLogin;
    public UserChangeDataTest(User user, int statusCodeExpected,  boolean successResultExpected) {
        this.user = user;
        this.statusCodeExpected = statusCodeExpected;
        this.successResultExpected = successResultExpected;
    }
    @Parameterized.Parameters
    public static Object[][] getParameters() {
        return new Object[][]{
                {UserGenerator.getDefaultUser(), SC_OK, true},
                {UserGenerator.getUserFromParams("x9x9x9x@yandex.ru", "x9x9x9x", "Irina"), SC_OK, true}
                //{UserGenerator.getUserFromParams("x9x9x9x@yandex.ru", "incorrect", "Anna"), SC_UNAUTHORIZED, false},
                //{UserGenerator.getUserFromParams("incorrect", "x9x9x9x", "Anna"), SC_UNAUTHORIZED, false},
                //{UserGenerator.getUserFromParams("x9x9x9x@yandex.ru", "", "Anna"), SC_UNAUTHORIZED, false},
               // {UserGenerator.getUserFromParams("", "x9x9x9x", "Anna"), SC_UNAUTHORIZED, false}
        };
    }

    @Before
    public void setUp(){
        userClient = new UserClient();
        responseLogin = userClient.loginUser(user);
        if (responseLogin.extract().statusCode() == SC_OK) {
            accessToken = responseLogin.extract().path("accessToken");
            accessToken = accessToken.split(" ")[1];
            refreshToken = responseLogin.extract().path("refreshToken");
        }
    }
    @After
    public void cleanUp(){
        if (statusCodeActual == SC_OK) {
            userClient.changeUserData (user, accessToken);
            userClient.logoutUser(accessToken, refreshToken);
        }
    }

    @Test
    @DisplayName("Авторизованный пользователь может обновить имя")
    public void userCanUpdateNameTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        User newUser = new User(user);
        newUser.setName("Anna");
        ValidatableResponse responseChange = userClient.changeUserData(newUser, accessToken);
        statusCodeActual = responseChange.extract().statusCode();
        Assert.assertEquals(statusCodeExpected, statusCodeActual);
    }

    @Test
    @DisplayName("Авторизованный пользователь может обновить пароль")
    public  void userCanUpdatePasswordTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        User newUser = new User(user);
        newUser.setPassword("xxx");
        ValidatableResponse responseChange = userClient.changeUserData(newUser, accessToken);
        statusCodeActual = responseChange.extract().statusCode();
        Assert.assertEquals(statusCodeExpected, statusCodeActual);
    }

    @Test
    @DisplayName("Авторизованный пользователь может обновить email")
    public  void userCanUpdateEmailTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        User newUser = new User(user);
        newUser.setEmail("z9z9z9z@yandex.ru");
        ValidatableResponse responseChange = userClient.changeUserData(newUser, accessToken);
        statusCodeActual = responseChange.extract().statusCode();
        Assert.assertEquals(statusCodeExpected, statusCodeActual);
    }

    @Test
    @DisplayName("Авторизованный пользователь может обновить все поля")
    public void userCanUpdateAllFieldsTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        User newUser = new User(user);
        newUser.setEmail("z9z9z9z@yandex.ru");
        newUser.setPassword("z9z9z9z");
        newUser.setName("Zara");
        ValidatableResponse responseChange = userClient.changeUserData(newUser, accessToken);
        statusCodeActual = responseChange.extract().statusCode();
        Assert.assertEquals(statusCodeExpected, statusCodeActual);

    }
}
