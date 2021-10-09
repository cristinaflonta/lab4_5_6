package ro.ubb.exam;


import ro.ubb.exam.Domain.Client;
import ro.ubb.exam.Domain.Holiday;
import ro.ubb.exam.Domain.Validators.ClientValidator;
import ro.ubb.exam.Domain.Validators.HolidayValidator;
import ro.ubb.exam.Repository.InMemoryRepository;
import ro.ubb.exam.Service.HolidayService;
import ro.ubb.exam.Service.ClientService;
import ro.ubb.exam.Repository.Repository;

import ro.ubb.exam.UI.Console;

import java.security.KeyException;

public class Main {
    public static void main(String[] args) throws KeyException {
        ClientValidator clientValidator = new ClientValidator();
        InMemoryRepository clientRepository = new InMemoryRepository(clientValidator);
        HolidayValidator holidayValidator = new HolidayValidator();
        InMemoryRepository holidayRepository = new InMemoryRepository(holidayValidator);
        ClientService clientService = new ClientService(clientRepository);
        HolidayService holidayService = new HolidayService(holidayRepository);

       Console console = new Console(clientService, holidayService);
       console.run();
    }
}

