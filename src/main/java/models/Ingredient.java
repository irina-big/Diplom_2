package models;

public class Ingredient {
    private String _id;
    private String name;
    private String type;

    private int proteins ;
    private int fat;
    private int carbohydrates;
    private int calories;
    private float price;
    private String image ;
    private String image_mobile;
    private String image_large;
    private int __v;

    public Ingredient() {
        _id = "61c0c5a71d1f82001bdaaa70";
        name = "Говяжий метеорит (отбивная)";
        type = "main";
        proteins = 800;
        fat = 800;
        carbohydrates = 300;
        calories = 2674;
        price = 3000f;
        image = "https://code.s3.yandex.net/react/code/meat-04.png";
        image_mobile = "https://code.s3.yandex.net/react/code/meat-04-mobile.png";
        image_large = "https://code.s3.yandex.net/react/code/meat-04-large.png";
        __v = 0;
    }
    public Ingredient getIngredient(){
        return new Ingredient();
    }
}
