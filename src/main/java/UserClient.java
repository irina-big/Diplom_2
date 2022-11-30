import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
import models.LogPassUser;
import models.User;


public class UserClient extends Client {
    private final String PATH_REGISTER =  "/api/auth/register";
    private final String PATH_LOGIN = "/api/auth/login";
    private final String PATH_DELETE = "/api/auth/user";

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
    public ValidatableResponse loginUser(LogPassUser logPassUser){
        return  given()
                .spec(getSpecification())
                .body(logPassUser)
                .when()
                .post(PATH_LOGIN)
                .then();
    }

    @Step ("Удалить пользователя")
    public ValidatableResponse deleteUser(User user){
        return given()
                .spec(getSpecification())
                .body(user)
                .when()
                .delete(PATH_DELETE)
                .then();
    }

}
