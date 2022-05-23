package food_delivery.restaurant.model;

import java.io.Serializable;

public class Price implements Serializable {
    private float price;

    public Price(){}

    public Price(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Price{" +
                "price=" + price +
                '}';
    }


} // class ends
