package ro.ubb.exam.Repository.SqlRepository;

import ro.ubb.exam.Domain.Client;
import ro.ubb.exam.Domain.Exceptions.ValidatorException;
import ro.ubb.exam.Domain.Holiday;
import ro.ubb.exam.Domain.Validators.Validator;
import ro.ubb.exam.Repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class HolidayDBRepository implements Repository<Long, Holiday> {
    private final String url;
    private final String user;
    private final String password;
    private final Validator<Holiday> holidayValidator;

    public HolidayDBRepository (String url, String user, String password, Validator<Holiday> holidayValidator) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.holidayValidator = holidayValidator;
    }

    @Override
    public Optional<Holiday> findOne(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        String sql = "select * from holydays where id = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String destination = resultSet.getString("destination");
            Long price = resultSet.getLong("price");
            Holiday holiday = new Holiday(name, destination, price);
            holiday.setId(id);
            return Optional.ofNullable(holiday);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return Optional.empty();
        }

    }

    @Override
    public Iterable<Holiday> findAll() {
        String sql = "select * from holydays";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<Holiday> holidaySet = new HashSet<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String destination = resultSet.getString("destination");
                Long price = resultSet.getLong("price");
                Holiday holiday = new Holiday(name, destination, price);
                holiday.setId(id);
                holidaySet.add(holiday);
            }
            return holidaySet;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<Holiday> save(Holiday entity) throws ValidatorException {
        holidayValidator.validate(entity);
        String sql = "insert into holydays(id,name,destination, price) values(?,?,?,?)";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDestination());
            preparedStatement.setLong(3, entity.getPrice());
            preparedStatement.executeUpdate();
            return Optional.ofNullable(entity);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return Optional.empty();
        }

    }



    @Override
    public Optional<Holiday> delete(Long aLong) {
        Optional<Holiday> holidayOptional = findOne(aLong);
        String sql = "delete from clients where id=?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return Optional.empty();
        }
        return holidayOptional;
    }

    @Override
    public Optional<Holiday> update(Holiday entity) throws ValidatorException {
        holidayValidator.validate(entity);
        String sql = "update holydays set name=?, destination=?, price=? where id=?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDestination());
            preparedStatement.setLong(3, entity.getPrice());
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.executeUpdate();
            return Optional.ofNullable(entity);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return Optional.empty();
        }
    }

}
