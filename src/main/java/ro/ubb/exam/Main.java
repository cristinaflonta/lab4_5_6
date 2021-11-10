package ro.ubb.exam;


import ro.ubb.exam.Domain.Client;
import ro.ubb.exam.Domain.Holiday;
import ro.ubb.exam.Domain.Reservation;
import ro.ubb.exam.Domain.Validators.ClientValidator;
import ro.ubb.exam.Domain.Validators.HolidayValidator;
import ro.ubb.exam.Domain.Validators.ReservationValidator;
import ro.ubb.exam.Domain.Validators.Validator;
import ro.ubb.exam.Repository.Repository;
import ro.ubb.exam.Repository.SqlRepository.ClientDBRepository;
import ro.ubb.exam.Repository.SqlRepository.HolidayDBRepository;
import ro.ubb.exam.Repository.SqlRepository.ReservationDBRepository;
import ro.ubb.exam.Service.ClientService;
import ro.ubb.exam.Service.HolidayService;
import ro.ubb.exam.Service.ReservationService;
import ro.ubb.exam.UI.Console;

import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {

//        String url = System.getProperty("url");
//        String password = System.getProperty("password");
//        String user = System.getProperty("user");

        String url = "jdbc:postgresql://localhost:5432/agency";
        String password = "1234";
        String user = "postgres";

        Validator<Client> clientValidator = new ClientValidator();
        Validator<Holiday> holidayValidator = new HolidayValidator();
        Validator<Reservation> reservationValidator = new ReservationValidator();

//        Repository<Long, Client> clientRepository = new ClientXmlRepository(clientValidator, "data/Clients.xml");
//        Repository<Long, Holiday> holidayXMLRepository = new HolidayXmlRepository(holidayValidator, "data/Holiday.xml");
//        Repository<Long, Reservation> reservationXMLRepository = new ReservationXmlRepository(reservationValidator,"data/Reservation.xml");

//        Repository<Long, Client> clientRepository = new ClientFileRepository(clientValidator, "data/Clients.csv");
//        Repository<Long, Holiday> holidayRepository = new HolidayFileRepository(holidayValidator, "data/Holiday.csv");
//        Repository<Long, Reservation> reservationRepository = new ReservationFileRepository(reservationValidator,"data/Reservation.csv");

        Repository<Long, Client> clientRepository = new ClientDBRepository(url, user, password, clientValidator);
        Repository<Long, Holiday> holidayRepository = new HolidayDBRepository(holidayValidator, "data/Holiday.csv");
        Repository<Long, Reservation> reservationRepository = new ReservationDBRepository(reservationValidator, "data/Reservation.csv");

        ClientService clientService = new ClientService(clientRepository);
        HolidayService holidayService = new HolidayService(holidayRepository);
        ReservationService reservationService = new ReservationService(reservationRepository);

        Console console = new Console(clientService, holidayService, reservationService);
        console.run();
    }
}

