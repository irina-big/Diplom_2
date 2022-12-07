import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.User;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {
    private final String PATH_REGISTER =  "/api/auth/register";
    private final String PATH_LOGIN = "/api/auth/login";
    private final String PATH_USER = "/api/auth/user";
    private final String PATH_LOGOUT = "/api/auth/logout";

    @Step("Создать пользователя")
    public ValidatableResponse createUser(User user){
        return  given()
                .spec(getSpecification ())
                .body(user)
                .when()
                .post(PATH_REGISTER)
                .then();
    }

    @Step ("Авторизоваться в системе")
    public ValidatableResponse loginUser (User user){
        return  given()
                .spec(getSpecification())
                .body(user)
                .when()
                .post(PATH_LOGIN)
                .then();
    }

    @Step ("Изменить данные о пользователе")
    public ValidatableResponse changeUserData(User newUser, String accessToken) {
        return  given()
                .spec(getSpecification())
                .auth()
                .oauth2(accessToken)
                .body(newUser)
                .when()
                .patch(PATH_USER)
                .then();
    }

    @Step ("Удалить пользователя")
    public ValidatableResponse deleteUser(String accessToken){
        return given()
                .spec(getSpecification())
                .auth()
                .oauth2(accessToken)
                .when()
                .delete(PATH_USER)
                .then();
    }

    @Step ("Выйти из системы")
    public ValidatableResponse logoutUser(String accessToken, String refreshToken){
        return  given()
                .spec(getSpecification())
                .auth()
                .oauth2(accessToken)
                .body("{\"token\":\"" + refreshToken + "\"}")
                .when()
                .post(PATH_LOGOUT)
                .then();
    }
}
