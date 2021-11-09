package ro.ubb.exam.Service;

import ro.ubb.exam.Domain.Exceptions.ValidatorException;
import ro.ubb.exam.Domain.Holiday;
import ro.ubb.exam.Repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class HolidayService {

    private Repository<Long, Holiday> holidayRepository;

    public HolidayService(Repository<Long, Holiday> repository) {
        this.holidayRepository = repository;
    }


    public void addHoliday(Holiday holiday) throws ValidatorException {
        holidayRepository.save(holiday);
    }

    public Iterable<Holiday> getAll() {
        return holidayRepository.findAll();
    }

    public Optional<Holiday> findOne(Long id) {
        Optional<Holiday> holiday= holidayRepository.findOne(id);
        return holiday;
    }

    public void deleteHoliday(Holiday holiday) throws ValidatorException {
        holidayRepository.delete(holiday.getId());
    }


    public Optional<Holiday> updateHoliday(Long id, String name, String destination, long price)  {
        Optional<Holiday> existing = holidayRepository.findOne(id);
        if (existing.isPresent() ) {
            Holiday holiday = existing.get();
            holiday.setName(name);
            holiday.setDestination(destination);
            holiday.setPrice(price);
            holidayRepository.update(holiday);
        }else {
            System.out.println("this holiday does not exist");
        }
        return existing;

    }

    public List<Holiday> sortHolidaysByPriceInDescendOrder() {
        Set<Holiday> holidaySet = (Set<Holiday>) holidayRepository.findAll();
        return holidaySet.stream().sorted((tour1, tour2) -> {
            return Math.round(tour2.getPrice() - tour1.getPrice());
        }).collect(Collectors.toList());
    }
}
