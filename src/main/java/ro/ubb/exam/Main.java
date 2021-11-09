package ro.ubb.exam;


import ro.ubb.exam.Domain.Client;
import ro.ubb.exam.Domain.Holiday;
import ro.ubb.exam.Domain.Reservation;
import ro.ubb.exam.Domain.Validators.ClientValidator;
import ro.ubb.exam.Domain.Validators.HolidayValidator;
import ro.ubb.exam.Domain.Validators.ReservationValidator;
import ro.ubb.exam.Domain.Validators.Validator;
import ro.ubb.exam.Repository.Repository;
import ro.ubb.exam.Repository.XMLRepository.ClientXmlRepository;
import ro.ubb.exam.Repository.XMLRepository.HolidayXmlRepository;
import ro.ubb.exam.Repository.XMLRepository.ReservationXmlRepository;
import ro.ubb.exam.Service.ClientService;
import ro.ubb.exam.Service.HolidayService;
import ro.ubb.exam.Service.ReservationService;
import ro.ubb.exam.UI.Console;

public class Main {
    public static void main(String[] args, Repository<Long, Reservation> reservationXmlRepository) {

        Validator<Client> clientValidator = new ClientValidator();
        Validator<Holiday> holidayValidator = new HolidayValidator();
        Validator<Reservation> reservationValidator = new ReservationValidator();

        Repository<Long, Client> clientXMLRepository = new ClientXmlRepository(clientValidator, "data/Clients.xml");
        Repository<Long, Holiday> holidayXMLRepository = new HolidayXmlRepository(holidayValidator, "data/Holiday.xml");
        Repository<Long, Reservation> reservationXMLRepository = new ReservationXmlRepository(reservationValidator,"data/Reservation.xml");

        ClientService clientService = new ClientService(clientXMLRepository);
        HolidayService holidayService = new HolidayService(holidayXMLRepository);
        ReservationService reservationService = new ReservationService(reservationXmlRepository);

        Console console = new Console(clientService, holidayService, reservationService);
        console.run();
    }
}

