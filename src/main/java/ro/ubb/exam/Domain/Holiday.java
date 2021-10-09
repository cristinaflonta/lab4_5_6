package ro.ubb.exam.Domain;

public class Holiday extends Entity<Long> {

    private String name, destination;
    private float price;
    private int availableSeats;

    public Holiday(String name, String destination, float price) {
        this.name = name;
        this.destination = destination;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return "Holiday{" +
                "name='" + name + '\'' +
                ", destination='" + destination + '\'' +
                ", price=" + price +
                '}';
    }

}
