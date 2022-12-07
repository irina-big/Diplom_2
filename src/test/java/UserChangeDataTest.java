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
    public UserChangeDataTest(User user, int statusCodeExpected,  boolean successResultExpected) {
        this.user = user;
        this.statusCodeExpected = statusCodeExpected;
        this.successResultExpected = successResultExpected;
    }
    @Parameterized.Parameters
    public static Object[][] getParameters() {
        return new Object[][]{
                {UserGenerator.getDefaultUser(), SC_OK, true},
                {UserGenerator.getUserFromParams("z9z9z9z@yandex.ru", "z9z9z9z", "Zara"), SC_OK, true},
                {UserGenerator.getUserFromParams("z9z9z9z@yandex.ru", "qwerty", "Zara"), SC_UNAUTHORIZED, false}
        };
    }
    @Before
    public void setUp() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        userClient = new UserClient();
        ValidatableResponse responseLogin = userClient.loginUser(user);
        if (responseLogin.extract().statusCode() == SC_OK) {
            accessToken = responseLogin.extract().path("accessToken");
            accessToken = accessToken.split(" ")[1];
            refreshToken = responseLogin.extract().path("refreshToken");
        }
        else {
            accessToken = "";
            refreshToken = "";
        }
    }
    @After
    public void cleanUp() {
        if (statusCodeActual == SC_OK) {
            userClient.changeUserData (user, accessToken);
            userClient.logoutUser(accessToken, refreshToken);
        }
    }
    @Test
    @DisplayName("Авторизованный пользователь может обновить имя")
    public void userCanUpdateNameTest() {
        User newUser = new User(user);
        newUser.setName("Anna");
        ValidatableResponse responseChange = userClient.changeUserData(newUser, accessToken);
        statusCodeActual = responseChange.extract().statusCode();
        Assert.assertEquals(statusCodeExpected, statusCodeActual);
        Assert.assertEquals(successResultExpected, responseChange.extract().path("success"));
    }
    @Test
    @DisplayName("Авторизованный пользователь может обновить пароль")
    public  void userCanUpdatePasswordTest() {
        User newUser = new User(user);
        newUser.setPassword("xxx");
        ValidatableResponse responseChange = userClient.changeUserData(newUser, accessToken);
        statusCodeActual = responseChange.extract().statusCode();
        Assert.assertEquals(statusCodeExpected, statusCodeActual);
        Assert.assertEquals(successResultExpected, responseChange.extract().path("success"));
    }
    @Test
    @DisplayName("Авторизованный пользователь может обновить email")
    public  void userCanUpdateEmailTest() {
        User newUser = new User(user);
        newUser.setEmail("x0x0x0x0x@yandex.ru");
        ValidatableResponse responseChange = userClient.changeUserData(newUser, accessToken);
        statusCodeActual = responseChange.extract().statusCode();
        Assert.assertEquals(statusCodeExpected, statusCodeActual);
        Assert.assertEquals(successResultExpected, responseChange.extract().path("success"));
    }
    @Test
    @DisplayName("Если изменить email на уже используемый, вернется ошибка")
    public  void userCannotUpdateEmailIfExistsTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        User locUser = UserGenerator.getDefaultUser();
        ValidatableResponse responseLogin = userClient.loginUser(locUser);
        String token = responseLogin.extract().path("accessToken");
        token = token.split(" ")[1];
        String refresh = responseLogin.extract().path("refreshToken");
        User newUser = new User(locUser);
        newUser.setEmail("login426@yandex.ru");
        ValidatableResponse responseChange = userClient.changeUserData(newUser, token);
        Assert.assertEquals(SC_FORBIDDEN, responseChange.extract().statusCode());
        userClient.logoutUser(token, refresh);
    }
    @Test
    @DisplayName("Авторизованный пользователь может обновить все поля")
    public void userCanUpdateAllFieldsTest() {
        User newUser = new User(user);
        newUser.setEmail("z0z0z0z@yandex.ru");
        newUser.setPassword("z0z0z0z");
        newUser.setName("Alla");
        ValidatableResponse responseChange = userClient.changeUserData(newUser, accessToken);
        statusCodeActual = responseChange.extract().statusCode();
        Assert.assertEquals(statusCodeExpected, statusCodeActual);
        Assert.assertEquals(successResultExpected, responseChange.extract().path("success"));

    }
}
