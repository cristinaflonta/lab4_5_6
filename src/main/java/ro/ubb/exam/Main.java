package ro.ubb.exam;


import ro.ubb.exam.Domain.Client;
import ro.ubb.exam.Domain.Holiday;
import ro.ubb.exam.Domain.Validators.ClientValidator;
import ro.ubb.exam.Domain.Validators.HolidayValidator;
import ro.ubb.exam.Domain.Validators.Validator;
import ro.ubb.exam.Repository.Repository;
import ro.ubb.exam.Repository.XMLRepository.ClientXmlRepository;
import ro.ubb.exam.Repository.XMLRepository.HolidayXmlRepository;
import ro.ubb.exam.Service.ClientService;
import ro.ubb.exam.Service.HolidayService;
import ro.ubb.exam.UI.Console;

public class Main {
    public static void main(String[] args) {

        Validator<Client> clientValidator = new ClientValidator();
        Validator<Holiday> holidayValidator = new HolidayValidator();

        Repository<Long, Client> clientXMLRepository = new ClientXmlRepository(clientValidator, "data/Clients.xml");
        Repository<Long, Holiday> holidayXMLRepository = new HolidayXmlRepository(holidayValidator, "data/Holiday.xml");

        ClientService clientService = new ClientService(clientXMLRepository);
        HolidayService holidayService = new HolidayService(holidayXMLRepository);

        Console console = new Console(clientService, holidayService);
        console.run();
    }
}

