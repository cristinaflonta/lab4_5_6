package ro.ubb.exam.Service;

import ro.ubb.exam.Domain.Client;
import ro.ubb.exam.Domain.Exceptions.ValidatorException;
import ro.ubb.exam.Domain.Holiday;
import ro.ubb.exam.Domain.Reservation;
import ro.ubb.exam.Domain.ViewModels.NumberOfClientsPerHolidayViewModel;
import ro.ubb.exam.Repository.Repository;

import java.util.*;
import java.util.stream.Collectors;

public class ReservationService {
    private Repository<Long, Reservation> reservationRepository;

    public ReservationService(Repository<Long, Reservation> repository) {
        this.reservationRepository = repository;
    }

    public void addReservation(Reservation reservation) throws ValidatorException {
        reservationRepository.save(reservation);
    }

    public Iterable<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> findOne(Long id) {
        Optional<Reservation> reservationOptional= reservationRepository.findOne(id);
        return reservationOptional;
    }

    public void deleteReservation(Reservation reservation) throws ValidatorException {
        reservationRepository.delete(reservation.getId());
    }
    public Optional<Reservation> updateReservation(Long id, Client client, Holiday holiday)  {
        Optional<Reservation> existing = reservationRepository.findOne(id);
        if (existing.isPresent() ) {
            Reservation reservation = existing.get();
            reservation.setClient(client);
            reservation.setHoliday(holiday);
            reservationRepository.update(reservation);
        }else {
            System.out.println("this Reservation does not exist");
        }
        return existing;

    }
    public List<Reservation>  getReservationsOfClients (Long clientId) {
        Set<Reservation> reservationsSet = (Set<Reservation>) reservationRepository.findAll();
        return reservationsSet.stream().filter(reservation -> {
            return reservation.getClient().getId().equals(clientId);
        }).collect(Collectors.toList());
    }

    public List<NumberOfClientsPerHolidayViewModel> getNumberOfClientsPerViewModels () {
        Map<Holiday,List<Client>> holidayListMap = new HashMap<>();
        List<NumberOfClientsPerHolidayViewModel> results = new ArrayList<>();
        reservationRepository.findAll().forEach(Reservation -> {
            if (!holidayListMap.containsKey(Reservation.getHoliday())){
                List<Client> clientList = new ArrayList<>();
                clientList.add(Reservation.getClient());
                holidayListMap.put(Reservation.getHoliday(), clientList);
            }else {
                holidayListMap.get(Reservation.getHoliday()).add(Reservation.getClient());
            }
        });

        holidayListMap.forEach((holiday,clientList)->{
            NumberOfClientsPerHolidayViewModel numberOfClientsPerholidayViewModel = new NumberOfClientsPerHolidayViewModel();
            numberOfClientsPerholidayViewModel.setId(holiday.getId());
            numberOfClientsPerholidayViewModel.setHolidayName(holiday.getName());
            numberOfClientsPerholidayViewModel.setClientsNumber(clientList.size());
            results.add(numberOfClientsPerholidayViewModel);
        });

        results.sort((o1, o2) -> {
            return o2.getClientsNumber() - o1.getClientsNumber();
        });

        return results;
    }

}