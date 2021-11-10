package ro.ubb.exam.Repository.SqlRepository;

import ro.ubb.exam.Domain.Client;
import ro.ubb.exam.Domain.Exceptions.ValidatorException;
import ro.ubb.exam.Domain.Holiday;
import ro.ubb.exam.Domain.Reservation;
import ro.ubb.exam.Domain.Validators.Validator;
import ro.ubb.exam.Repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ReservationDBRepository implements Repository<Long, Reservation> {
    private final String url;
    private final String user;
    private final String password;
    private final Validator<Reservation> reservationValidator;

    public ReservationDBRepository (String url, String user, String password, Validator<Reservation> reservationValidator) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.reservationValidator = reservationValidator;
    }

    @Override
    public Optional<Reservation> findOne(Long aLong) {
        if(aLong == null){
            throw new IllegalArgumentException("Id must not be null");
        }
        String sql = "select * from reservations where id=?";
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Long clientId = resultSet.getLong("client_id");
            String clientName = resultSet.getString("client_name");
            String email = resultSet.getString("email");
            Long tourId = resultSet.getLong("tour_id");
            String tourName = resultSet.getString("tour_name");
            String destination = resultSet.getString("destination");
            Long price = resultSet.getLong("price");
            Long id = resultSet.getLong("id");
            Client client = new Client(clientName,email);
            client.setId(clientId);
            Holiday holiday = new Holiday(tourName, destination, price);
            holiday.setId(tourId);
            Reservation reservation = new Reservation(client,holiday);
            reservation.setId(id);
            return Optional.ofNullable(reservation);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
            return Optional.empty();
        }

    }

    @Override
    public Iterable<Reservation> findAll() {
        String sql = "select * from reservations";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<Reservation> reservationSet = new HashSet<>();
            while (resultSet.next()) {
                Long clientId = resultSet.getLong("client_id");
                String clientName = resultSet.getString("client_name");
                String email = resultSet.getString("email");
                Long tourId = resultSet.getLong("tour_id");
                String tourName = resultSet.getString("tour_name");
                String destination = resultSet.getString("destination");
                Long price = resultSet.getLong("price");
                Long id = resultSet.getLong("id");
                Client client = new Client(clientName,email);
                client.setId(clientId);
                Holiday holiday = new Holiday(tourName, destination, price);
                holiday.setId(tourId);
                Reservation reservation = new Reservation(client,holiday);
                reservation.setId(id);
                reservationSet.add(reservation);
            }
            return reservationSet;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }


    @Override
    public Optional<Reservation> save(Reservation entity) throws ValidatorException {
        reservationValidator.validate(entity);
        String sql = "insert into reservations(client_id,holidays_id) values(?,?)";
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setLong(1, entity.getClient().getId());
            preparedStatement.setLong(2, entity.getHoliday().getId());
            preparedStatement.executeUpdate();
            return Optional.ofNullable(entity);

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Reservation> delete(Long aLong) {
        Optional<Reservation> reservationOptional = findOne(aLong);
        String sql = "delete from reservations where id=?";
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
            return Optional.empty();
        }
        return reservationOptional;
    }

    @Override
    public Optional<Reservation> update(Reservation entity) throws ValidatorException {
        reservationValidator.validate(entity);
        String sql = "update tb_transaction set client_id=?, tour_id=? where id=?";
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setLong(1, entity.getClient().getId());
            preparedStatement.setLong(2, entity.getHoliday().getId());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.executeUpdate();
            return Optional.ofNullable(entity);

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
            return Optional.empty();
        }
    }
}
