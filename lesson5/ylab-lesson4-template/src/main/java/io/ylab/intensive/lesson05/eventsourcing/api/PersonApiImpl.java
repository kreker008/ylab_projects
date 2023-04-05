package io.ylab.intensive.lesson05.eventsourcing.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson05.eventsourcing.Operation;
import io.ylab.intensive.lesson05.eventsourcing.Person;
import io.ylab.intensive.lesson05.eventsourcing.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Тут пишем реализацию
 */
@Component
public class PersonApiImpl implements PersonApi {
    private final String exchangeName;
    private final String queueName;
    private final String ddl;
    private final com.rabbitmq.client.Connection connectionMQ;
    private final Connection connectionDB;
    private final ObjectMapper objectMapper;

    private static final String selectPersonSQL = "SELECT * FROM person WHERE person_id=?;";
    private static final String selectAllPersonSQL = "SELECT * FROM person;";

    @PostConstruct
    private void initDBaMQ() throws SQLException, IOException, TimeoutException {
        try (Statement statement = connectionDB.createStatement();
             Channel channel = connectionMQ.createChannel()) {
            statement.execute(ddl);
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, "*");
        }
    }

    @PreDestroy
    private void closeDBaMQ() throws SQLException, IOException {
        if (!connectionDB.isClosed()) {
            connectionDB.close();
        }
        if (connectionMQ.isOpen()) {
            connectionMQ.close();
        }
    }

    @Autowired
    public PersonApiImpl(ConnectionFactory connectionFactory, DataSource dataSource, @Qualifier("configure") Map<String, String> configure) throws IOException, TimeoutException, SQLException {
        this.exchangeName = configure.get("exchangeName");
        this.queueName = configure.get("queueName");
        this.ddl = configure.get("ddl");
        this.connectionMQ = connectionFactory.newConnection();
        this.connectionDB = dataSource.getConnection();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void deletePerson(Long personId) {
        Person person = new Person();
        person.setId(personId);
        Message message = new Message(Operation.DELETE, person);
        try (Channel channel = this.connectionMQ.createChannel()) {
            channel.basicPublish(this.exchangeName, "*", null, objectMapper.writeValueAsBytes(message));
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        Person person = new Person();
        person.setId(personId);
        person.setName(firstName);
        person.setMiddleName(middleName);
        person.setLastName(lastName);
        Message message = new Message(Operation.SAVE, person);
        try (Channel channel = this.connectionMQ.createChannel()) {
            channel.basicPublish(this.exchangeName, "*", null, objectMapper.writeValueAsBytes(message));
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void setPerson(Person person, ResultSet resultSet) throws SQLException {
        person.setId(resultSet.getLong(1));
        person.setName(resultSet.getString(2));
        person.setLastName(resultSet.getString(3));
        person.setMiddleName(resultSet.getString(4));
    }

    @Override
    public Person findPerson(Long personId) {
        try (PreparedStatement preparedStatement = connectionDB.prepareStatement(selectPersonSQL)) {
            preparedStatement.setLong(1, personId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            Person person = new Person();
            setPerson(person, resultSet);
            return person;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Person> findAll() {
        try (PreparedStatement preparedStatementValues = connectionDB.prepareStatement(selectAllPersonSQL)) {
            ResultSet resultSet = preparedStatementValues.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            List<Person> personList = new ArrayList<>();
            Person firstPerson = new Person();
            setPerson(firstPerson, resultSet);
            personList.add(firstPerson);
            while (resultSet.next()) {
                Person person = new Person();
                setPerson(person, resultSet);
                personList.add(person);
            }
            return personList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
