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

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;

@RunWith(Parameterized.class)
public class UserCreateTest {
    private final User user;
    private final int createStatusCodeExpected;
    private int createStatusCodeActual;
    private final boolean successResultExpected;
    private  String accessToken;
    private UserClient userClient;

    public UserCreateTest(User user, int createStatusCodeExpected,  boolean successResultExpected) {
        this.user = user;
        this.createStatusCodeExpected = createStatusCodeExpected;
        this.successResultExpected = successResultExpected;
    }
    @Parameterized.Parameters
    public static Object[][] getParameters() {
        return new Object[][]{
                {UserGenerator.getUserWithoutLogin(), SC_FORBIDDEN, false},
                {UserGenerator.getUserWithoutPassword(), SC_FORBIDDEN, false},
                {UserGenerator.getUserWithoutName(),SC_FORBIDDEN, false},
                {UserGenerator.getUserWithRandomData(), SC_OK, true},
                {UserGenerator.getDefaultUser(), SC_FORBIDDEN, false},
        };
    }

    @Before
    public void setUp(){
        userClient = new UserClient();
    }
    @After
    public void cleanUp(){
        if (createStatusCodeActual == SC_OK) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Можно создать пользователя, заполнив все поля")
    public void userCanBeCreatedTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        ValidatableResponse responseCreate = userClient.createUser(user);
        createStatusCodeActual = responseCreate.extract().statusCode();
        if (createStatusCodeActual == SC_OK) {
            accessToken = responseCreate.extract().path("accessToken");
            accessToken = accessToken.split(" ")[1];
        }
        Assert.assertEquals(createStatusCodeExpected, createStatusCodeActual);
    }

    @Test
    @DisplayName("Успешное создание пользователя возвращает status:true")
    public  void userSuccessCreatedReturnsStatusTrueTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        ValidatableResponse responseCreate = userClient.createUser(user);
        boolean successResultActual = responseCreate.extract().path("success");
        if (successResultActual) {
            createStatusCodeActual = responseCreate.extract().statusCode();
            accessToken = responseCreate.extract().path("accessToken");
            accessToken = accessToken.split(" ")[1];
        }
        Assert.assertEquals(successResultExpected, successResultActual);
    }
}

