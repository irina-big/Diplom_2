package generators;

import models.Order;

import java.util.List;

public class OrderGenerator {
    public static Order getOrderWithIngredients(){
        return new Order(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa79"));
    }

    public static Order getOrderWithoutIngredients(){
        return new Order();
    }

    public static Order getOrderWithIncorrectIngredients(){
        return new Order(List.of("999xxx999", "61c0cjhi82001b000aa72", "61c0c6ggh000001bdaaa79"));
    }
}
