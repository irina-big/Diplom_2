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

    public static User getUserWithoutName(){
        return new User ("login764@yandex.ru","password555", "" );
    }
    public static User getUserWithRandomData() {
        Random random = new Random();
        return new User("login" + random.nextInt(10000) + "@yandex.ru", "password" + random.nextInt(10000), "Alexandr");
    }

    public static User getUserFromParams(String email, String password, String name){
        return new User(email, password, name);
    }
}
