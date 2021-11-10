package ro.ubb.exam.Repository.FileRepository;

import ro.ubb.exam.Domain.Client;
import ro.ubb.exam.Domain.Exceptions.ValidatorException;
import ro.ubb.exam.Domain.Holiday;
import ro.ubb.exam.Domain.Reservation;
import ro.ubb.exam.Domain.Validators.Validator;
import ro.ubb.exam.Repository.InMemoryRepository;

import javax.xml.transform.TransformerFactory;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ReservationFileRepository extends InMemoryRepository<Long, Reservation> {
    private String fileName;


    public ReservationFileRepository(Validator<Reservation> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    private void loadData() {
        Path path = Paths.get(fileName);
        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(","));

                Long id = Long.valueOf(items.get(0));
                Long clientId = Long.valueOf(items.get(1));
                String clientName = items.get(2);
                String clientEmail = items.get(3);
                Long holidayId = Long.valueOf(items.get(4));
                String holidayName = items.get(5);
                String holidayDestionation = items.get(6);
                Long holidayPrice = Long.valueOf(items.get(7));
                Client client = new Client(clientName, clientEmail);
                client.setId(clientId);
                Holiday holiday = new Holiday(holidayName, holidayDestionation, holidayPrice);
                holiday.setId(holidayId);
                Reservation reservation = new Reservation(client, holiday);
                reservation.setId(id);

                try {
                    super.save(reservation);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<Reservation> save(Reservation entity) throws ValidatorException {
        Optional<Reservation> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveToFile(entity);
        return Optional.empty();
    }

    private void saveToFile(Reservation entity) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(
                    entity.getId() + "," + entity.getClient().getId() + "," + entity.getClient().getName() + "," + entity.getClient().getEmail()
                            + "," + "," + entity.getHoliday().getId()+ "," + entity.getHoliday().getName()+ "," + entity.getHoliday().getDestination()+ "," + entity.getHoliday().getPrice());
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Reservation> delete(Long aLong) {
        Optional<Reservation> reservationOptional = super.delete(aLong);
        replaceContentInFile();

        return reservationOptional;
    }

    private void replaceContentInFile() {
        try (FileWriter fileWriter = new FileWriter(fileName, false)) {
            super.findAll().forEach(this::saveToFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Optional<Reservation> update(Reservation entity) throws ValidatorException {
        Optional<Reservation> reservationOptional = super.update(entity);
        replaceContentInFile();
        return reservationOptional;
    }
}
