package ro.ubb.exam.UI;

import ro.ubb.exam.Domain.Client;
import ro.ubb.exam.Domain.Holiday;
import ro.ubb.exam.Service.ClientService;
import ro.ubb.exam.Service.HolidayService;

import java.util.Scanner;

public class Console {
    private ClientService clientService;
    private HolidayService holidayService;
    private Scanner scanner;

    public Console(ClientService clientService, HolidayService holidayService) {
        this.clientService = clientService;
        this.holidayService = holidayService;
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

        System.out.println("x. Exit");
    }

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
            //System.out.print("Enter id: ");
            //Long id = scanner.nextLong();
            System.out.print("Enter client name: ");
            String clientName = scanner.nextLine();
            System.out.print("Enter client email: ");
            String clientEmail = scanner.nextLine();
            System.out.print("Enter client phone: ");
            int phoneNumber = Integer.parseInt(scanner.nextLine());

            Client client = new Client(clientName, clientEmail, phoneNumber);
            //client.setId(id);
            clientService.addClient(client);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleUpdateClient() {
        try {
            System.out.print("Enter id: ");
            Long id = scanner.nextLong();
            System.out.print("Enter client name: ");
            scanner.nextLine();
            String clientName = scanner.nextLine();
            System.out.print("Enter client email: ");
            String clientEmail = scanner.nextLine();
            System.out.print("Enter client phone: ");
            int phoneNumber = Integer.parseInt(scanner.nextLine());

            clientService.updateClient(id, clientName, clientEmail, phoneNumber);

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
            float holidayPrice = Float.parseFloat(scanner.nextLine());

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
            float holidayPrice = Float.parseFloat(scanner.nextLine());


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

}
