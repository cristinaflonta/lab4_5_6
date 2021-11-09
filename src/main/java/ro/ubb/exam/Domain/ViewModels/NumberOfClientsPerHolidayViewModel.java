package ro.ubb.exam.Domain.ViewModels;

public class NumberOfClientsPerHolidayViewModel {

    private Long id;
    private String holidayName;
    private int clientsNumber;


    public NumberOfClientsPerHolidayViewModel(Long id, String holidayName, int clientsNumber) {
        this.id = id;
        this.holidayName = holidayName;
        this.clientsNumber = clientsNumber;
    }



    public NumberOfClientsPerHolidayViewModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public int getClientsNumber() {
        return clientsNumber;
    }

    public void setClientsNumber(int clientsNumber) {
        this.clientsNumber = clientsNumber;
    }

    @Override
    public String toString() {
        return "NumberOfClientsPerHolidayViewModel{" +
                "id=" + id +
                ", holidayName='" + holidayName + '\'' +
                ", clientsNumber=" + clientsNumber +
                '}';
    }
}
