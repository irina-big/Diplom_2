package generators;

import models.User;

import java.util.Random;

public class UserGenerator {
    public static User getDefaultUser(){
        return  new User("anutka-a94@yandex.ru", "24Sept", "Irina");
    }

    public static User getUserWithoutLogin() {
        return new User("", "24Sept", "Irina");
    }
    public static User getUserWithoutPassword() {
        return new User("anutka-a94@yandex.ru", "", "Irina");
    }
    public static User getUserWithRandomData() {
        Random random = new Random();
        return new User("login" + random.nextInt(1000) + "@yandex.ru", "password" + random.nextInt(1000), "Alexandr");
    }
    public static User getUserFromParams(String login, String password, String firstName) {
        return new User(login, password, firstName);
    }
}
