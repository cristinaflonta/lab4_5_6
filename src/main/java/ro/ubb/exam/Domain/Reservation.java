package ro.ubb.exam.Domain;

public class Reservation extends Entity<Long> {

    private Client client;
    private Holiday holiday;


    public Reservation(Client client, Holiday holiday) {
        this.client = client;
        this.holiday = holiday;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Holiday getHoliday() {
        return holiday;
    }

    public void setHoliday(Holiday tour) {
        this.holiday = tour;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "client=" + client +
                ", holiday=" + holiday +
                '}';
    }
}

