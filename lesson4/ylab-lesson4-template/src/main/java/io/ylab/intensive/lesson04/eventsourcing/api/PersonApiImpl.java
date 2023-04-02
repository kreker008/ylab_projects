package io.ylab.intensive.lesson04.eventsourcing.api;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import io.ylab.intensive.lesson04.eventsourcing.Message;
import io.ylab.intensive.lesson04.eventsourcing.Operation;
import io.ylab.intensive.lesson04.eventsourcing.Person;

import javax.sql.DataSource;

/**
 * Тут пишем реализацию
 */
public class PersonApiImpl implements PersonApi {
    private String exchangeName;
    private Channel channel;
    private DataSource dataSource;
    private ObjectMapper objectMapper;

    private static final String selectPersonSQL = "SELECT * FROM person WHERE person_id=?;";
    private static final String selectAllPersonSQL = "SELECT * FROM person;";

    public PersonApiImpl(String exchangeName, Channel channel, DataSource dataSource) {
        this.exchangeName = exchangeName;
        this.channel = channel;
        this.dataSource = dataSource;
        this.objectMapper = new ObjectMapper();
    }

    @Override
  public void deletePerson(Long personId) {
        Person person = new Person();
        person.setId(personId);
        Message message = new Message(Operation.DELETE, person);
        try{
            this.channel.basicPublish(this.exchangeName, "*", null, objectMapper.writeValueAsBytes(message));
        } catch (IOException e){
            e.printStackTrace();
        }
  }

  @Override
  public void savePerson(Long personId, String firstName, String lastName, String middleName)  {
      Person person = new Person();
      person.setId(personId);
      person.setName(firstName);
      person.setMiddleName(middleName);
      person.setLastName(lastName);
      Message message = new Message(Operation.SAVE, person);
      try{
          this.channel.basicPublish(this.exchangeName, "*", null, objectMapper.writeValueAsBytes(message));
      } catch (IOException e){
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
      try (Connection connection = this.dataSource.getConnection();
           PreparedStatement preparedStatement = connection.prepareStatement(selectPersonSQL)){
          preparedStatement.setLong(1, personId);
          ResultSet resultSet = preparedStatement.executeQuery();
          if(!resultSet.next()){
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
        try (Connection connection = this.dataSource.getConnection();
           PreparedStatement preparedStatementValues = connection.prepareStatement(selectAllPersonSQL)) {
          ResultSet resultSet = preparedStatementValues.executeQuery();
          if(!resultSet.next()){
              return null;
          }
          List<Person> personList = new ArrayList<>();
          Person firstPerson = new Person();
          setPerson(firstPerson, resultSet);
          personList.add(firstPerson);
          while (resultSet.next()){
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
