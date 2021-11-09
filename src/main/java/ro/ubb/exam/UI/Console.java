package ro.ubb.exam.UI;

import ro.ubb.exam.Domain.Client;
import ro.ubb.exam.Domain.Holiday;
import ro.ubb.exam.Domain.Reservation;
import ro.ubb.exam.Service.ClientService;
import ro.ubb.exam.Service.HolidayService;
import ro.ubb.exam.Service.ReservationService;

import java.util.Scanner;

public class Console {
    private ClientService clientService;
    private HolidayService holidayService;
    private ReservationService reservationService;
    private Scanner scanner;

    public Console(ClientService clientService, HolidayService holidayService, ReservationService reservationService) {
        this.clientService = clientService;
        this.holidayService = holidayService;
        this.reservationService = reservationService;
        this.scanner = new Scanner(System.in);
    }

    private void showMenu() {
        System.out.println("1. Add Client");
        System.out.println("2. Update Client");
        System.out.println("3. Delete Client");
        System.out.println("4. View All Clients");
        System.out.println("5. Add Holiday");
        System.out.println("6. Update Holiday");
        System.out.println("7. Delete Holiday");
        System.out.println("8. View All Holidays");
        System.out.println("9. Add reservation");
        System.out.println("10. View all reservations");
        System.out.println("11. Delete reservation");
        System.out.println("12. Update reservation");
        System.out.println("13. Sort Clients Alphabeticaly");
        System.out.println("14. Sort Holiday in descending order by price ");
        System.out.println("15. Show reservation for clients ");
        System.out.println("16. Show number of clients per tour ");
        System.out.println("x. Exit");
    }
    //NECESARA O FILTRARE

    public void run() {
        while (true) {
            showMenu();

            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    handleAddClient();
                    break;
                case "2":
                    handleUpdateClient();
                    break;
                case "3":
                    handleRemoveClient();
                    break;
                case "4":
                    handleViewClients();
                    break;
                case "5":
                    handleAddHoliday();
                    break;
                case "6":
                    handleUpdateHoliday();
                    break;
                case "7":
                    handleRemoveHoliday();
                    break;
                case "8":
                    handleViewHolidays();
                    break;
                case "9":
                    handleAddReservation();
                    break;
                case "10":
                    handleViewReservations();
                    break;
                case "11":
                    handleRemoveReservation();
                    break;
                case "12":
                    handleUpdateReservation();
                    break;
                case "13":
                    handleSortClientsAlph();
                    break;
                case "14":
                    handleSortHolidaysinDescendingOrderByPrice();
                    break;
                case "15":
                    handleShowReservationsOfClients();
                    break;
                case "16":
                    handleSortHolidaysByNumberOfClients();
                    break;

                case "x":
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private void handleAddClient() {
        try {
            System.out.print("Enter id: ");
            Long id = scanner.nextLong();
            System.out.print("Enter client name: ");
            scanner.nextLine();
            String clientName = scanner.nextLine();
            System.out.print("Enter client email: ");
            String clientEmail = scanner.nextLine();
            /*System.out.print("Enter client phone: ");
            int phoneNumber = Integer.parseInt(scanner.nextLine());*/

            Client client = new Client(clientName, clientEmail);
            client.setId(id);
            clientService.addClient(client);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleUpdateClient() {
        try {
            System.out.print("Enter id: ");
            Long id = scanner.nextLong();
            scanner.nextLine();
            System.out.print("Enter client name: ");
            scanner.nextLine();
            String clientName = scanner.nextLine();
            System.out.print("Enter client email: ");
            String clientEmail = scanner.nextLine();

            clientService.updateClient(id, clientName, clientEmail);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleUpdateHoliday() {
        try {
            System.out.print("Enter id: ");
            Long id = scanner.nextLong();
            System.out.print("Enter holiday name: ");
            scanner.nextLine();
            String holidayName = scanner.nextLine();
            System.out.print("Enter holiday destination: ");
            String holidayDestination = scanner.nextLine();
            System.out.print("Enter holiday price: ");
            long holidayPrice = Long.parseLong(scanner.nextLine());

            holidayService.updateHoliday(id, holidayName, holidayDestination, holidayPrice);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void handleAddHoliday() {
        try {
            System.out.print("Enter id: ");
            Long id = scanner.nextLong();
            System.out.print("Enter holiday name: ");
            scanner.nextLine();
            String holidayName = scanner.nextLine();
            System.out.print("Enter holiday destination: ");
            String holidayDestination = scanner.nextLine();
            System.out.print("Enter holiday price: ");
            long holidayPrice = Long.parseLong(scanner.nextLine());


            Holiday holiday = new Holiday(holidayName, holidayDestination, holidayPrice);
            holiday.setId(id);
            holidayService.addHoliday(holiday);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void handleViewClients() {
        for (Client client : clientService.getAll()) {
            System.out.println("client id: " +client.getId() + " " + client);
        }
    }


    private void handleViewHolidays() {
        for (Holiday holiday : holidayService.getAll()) {
            System.out.println("holiday id: " + holiday.getId() + " " + holiday);
        }
    }

    private void handleRemoveClient() {
        try {
            System.out.print("Enter the client id to remove:");
            Long id = scanner.nextLong();
            clientService.deleteClient(clientService.findOne(id).get());

            System.out.println("Client removed!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleRemoveHoliday() {
        try {
            System.out.print("Enter the holiday id to remove:");
            Long id = scanner.nextLong();
            holidayService.deleteHoliday(holidayService.findOne(id).get());

            System.out.println("Holiday removed!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleSortHolidaysByNumberOfClients() {
        reservationService.getNumberOfClientsPerViewModels().forEach(numberOfClientsPerHolidayViewModel -> System.out.println(numberOfClientsPerHolidayViewModel));
    }

    private void handleAddReservation() {
        try {
            //System.out.print("Enter id: ");
            //Long id = scanner.nextLong();
            System.out.print("Enter client id: ");
            Long cllientId = scanner.nextLong();
            System.out.print("Enter holiday id: ");
            Long holidayId = scanner.nextLong();

            Client client = clientService.findOne(cllientId).get();
            Holiday holiday = holidayService.findOne(holidayId).get();

            Reservation reservation = new Reservation(client, holiday);
            //reservation.setId(id);
            reservationService.addReservation(reservation);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleViewReservations() {
        for (Reservation reservation : reservationService.getAll()) {
            System.out.println("reservation id: " + reservation.getId() + " " + reservation);
        }
    }

    private void handleRemoveReservation() {
        try {
            System.out.print("Enter the reservation id to remove:");
            Long id = scanner.nextLong();
            reservationService.deleteReservation(reservationService.findOne(id).get());

            System.out.println("reservation removed!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleUpdateReservation() {
        try {
            System.out.print("Enter id: ");
            Long id = scanner.nextLong();
            System.out.print("Enter client id: ");
            Long cllientId = scanner.nextLong();
            System.out.print("Enter holiday id: ");
            Long holidayId = scanner.nextLong();
            Client client = clientService.findOne(cllientId).get();
            Holiday holiday = holidayService.findOne(holidayId).get();
            reservationService.updateReservation(id, client, holiday);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleSortClientsAlph() {
        clientService.sortClientsAlph().forEach(client -> {
            System.out.println(client);
        });
    }

    private void handleShowReservationsOfClients() {
        System.out.println("enter the client id");
        Long id = scanner.nextLong();
        reservationService.getReservationsOfClients(id).forEach(reservation -> System.out.println(reservation));
    }

    private void handleSortHolidaysinDescendingOrderByPrice() {
        holidayService.sortHolidaysByPriceInDescendOrder().forEach(holiday -> System.out.println(holiday));
    }

}
