package ro.ubb.exam.Repository.SqlRepository;

import ro.ubb.exam.Domain.Client;
import ro.ubb.exam.Domain.Exceptions.ValidatorException;
import ro.ubb.exam.Domain.Validators.Validator;
import ro.ubb.exam.Repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ClientDBRepository implements Repository<Long, Client> {
    private final String url;
    private final String user;
    private final String password;
    private final Validator<Client> clientValidator;

    public ClientDBRepository(String url, String user, String password, Validator<Client> clientValidator) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.clientValidator = clientValidator;
    }


    @Override
    public Optional<Client> findOne(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        String sql = "select * from clients where id = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            Client client = new Client(name, email);
            client.setId(id);
            return Optional.ofNullable(client);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return Optional.empty();
        }

    }

    @Override
    public Iterable<Client> findAll() {
        String sql = "select * from clients";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<Client> clientSet = new HashSet<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                Client client = new Client(name, email);
                client.setId(id);
                clientSet.add(client);
            }
            return clientSet;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException {
        clientValidator.validate(entity);
        String sql = "insert into clients(name,email) values(?,?)";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.executeUpdate();
            return Optional.ofNullable(entity);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return Optional.empty();
        }

    }



//    @Override
//    public Optional<Client> save(Client entity) throws ValidatorException {
//        clientValidator.validate(entity);
//
//        String sql = "insert into clients(name,email) values(?,?)";
//        try (Connection connection = getDBConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
//            preparedStatement.setString(1, entity.getName());
//            preparedStatement.setString(2, entity.getEmail());
//
//            preparedStatement.executeUpdate();
//            return Optional.ofNullable(entity);
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return Optional.empty();
//    }

    @Override
    public Optional<Client> delete(Long aLong) {
        Optional<Client> clientOptional = findOne(aLong);
        String sql = "delete from clients where id=?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return Optional.empty();
        }
        return clientOptional;
    }

    @Override
    public Optional<Client> update(Client entity) throws ValidatorException {
        clientValidator.validate(entity);
        String sql = "update clients set name=?, email=? where id=?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.executeUpdate();
            return Optional.ofNullable(entity);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return Optional.empty();
        }
    }

//    private Connection getDBConnection() throws SQLException {
//        return DriverManager.getConnection(url, user, password);
//    }
}
