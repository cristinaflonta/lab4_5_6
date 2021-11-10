package ro.ubb.exam.Repository.FileRepository;

;
import ro.ubb.exam.Domain.Exceptions.ValidatorException;
import ro.ubb.exam.Domain.Holiday;
import ro.ubb.exam.Domain.Validators.Validator;
import ro.ubb.exam.Repository.InMemoryRepository;


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

/**
 * @author
 */
public class HolidayFileRepository extends InMemoryRepository<Long, Holiday> {
    private String fileName;

    public HolidayFileRepository(Validator<Holiday> validator, String fileName) {
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
                String name = items.get(1);
                String destination = items.get(2);
                long price = Long.parseLong(items.get(3));

                Holiday holiday = new Holiday(name, destination, price);
                holiday.setId(id);

                try {
                    super.save(holiday);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<Holiday> save(Holiday entity) throws ValidatorException {
        Optional<Holiday> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveToFile(entity);
        return Optional.empty();
    }

    private void saveToFile(Holiday entity) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(
                    entity.getId() + "," + entity.getName() + "," + entity.getDestination() + "," + entity.getPrice()) ;
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Optional<Holiday> delete(Long aLong) {
        Optional<Holiday> holidayOptional =  super.delete(aLong);
        replaceContentInFile();

        return holidayOptional;
    }

    private void replaceContentInFile() {
        try (FileWriter fileWriter = new FileWriter(fileName,false)) {
            super.findAll().forEach(this::saveToFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Optional<Holiday> update(Holiday entity) throws ValidatorException {
        Optional<Holiday> holidayOptional =  super.update(entity);
        replaceContentInFile();

        return holidayOptional;
    }
}

