import generators.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.LogPassUser;
import models.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_CREATED;

@RunWith(Parameterized.class)
public class UserTest {
    private final User user;
    private final int createStatusCode;
    private final boolean okResult;
    private final String message;
    private UserClient userClient;

    public UserTest(User user, int createStatusCode,  boolean okResult, String message) {
        this.user = user;
        this.createStatusCode = createStatusCode;
        this.okResult = okResult;
        this.message = message;
    }
    @Parameterized.Parameters
    public static Object[][] getParameters() {
        return new Object[][]{
                {UserGenerator.getDefaultUser(), SC_CREATED, true, null},
                {UserGenerator.getUserWithRandomData(), SC_CREATED, true, null},
                {UserGenerator.getUserWithoutLogin(), SC_BAD_REQUEST, false, "Недостаточно данных для создания учетной записи"},
                {UserGenerator.getUserWithoutPassword(), SC_BAD_REQUEST, false, "Недостаточно данных для создания учетной записи"},
                {UserGenerator .getUserFromParams("login199", "0987654321", "Александр"), SC_CONFLICT, false, "Этот логин уже используется. Попробуйте другой."},
                {UserGenerator .getUserFromParams("randLog729", "0987654321", "Александр"), SC_CONFLICT, false, "Этот логин уже используется. Попробуйте другой."}
        };
    }

    @Before
    public void setUp(){
        userClient = new UserClient();
    }

    @After
    public void cleanUp(){
        ValidatableResponse responseLogin = userClient.loginUser(LogPassUser.fromUser(user));
        if (responseLogin.extract().statusCode() == SC_OK) {
           //Str id = responseLogin.extract().path("id");
            userClient.deleteUser(user);
        }
    }

    @Test
    @DisplayName("Можно создать пользователя, заполнив все поля")
    public void courierCanBeCreatedTest() {
        ValidatableResponse responseCreate = userClient.createUser(user);
        int createStatusCodeActual = responseCreate.extract().statusCode();
        String messageActual = responseCreate.extract().path("message");
        Assert.assertEquals(createStatusCode, createStatusCodeActual);
        Assert.assertEquals(messageActual, message);
    }

    @Test
    @DisplayName("Успешное создание курьера возвращает ok:true")
    public  void courierSuccessCreatedReturnsOkTest(){
        ValidatableResponse responseCreate = userClient.createUser(user);
        int createStatusCodeActual = responseCreate.extract().statusCode();
        boolean okResultActual = false;
        if (createStatusCodeActual == SC_CREATED) {
            okResultActual = responseCreate.extract().path("ok");
        }
        Assert.assertEquals(okResultActual, okResult);
    }
}

