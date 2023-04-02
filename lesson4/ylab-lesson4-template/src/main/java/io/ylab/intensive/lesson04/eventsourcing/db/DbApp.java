package io.ylab.intensive.lesson04.eventsourcing.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;
import io.ylab.intensive.lesson04.eventsourcing.Message;
import io.ylab.intensive.lesson04.eventsourcing.Operation;
import io.ylab.intensive.lesson04.eventsourcing.Person;

public class DbApp {
  private static final String selectIdSQL = "SELECT person_id FROM person WHERE person_id=?;";
  private static final String deletePersonSQL = "DELETE FROM person WHERE person_id=?";
  private static final String insertSQL = "INSERT INTO person(person_id, first_name, last_name, middle_name)" +
          " VALUES (?,?,?,?);";
  private static final String updateSQL = "UPDATE person SET first_name=?, last_name=?, middle_name=? " +
          " WHERE person_id = ?;";


  private static void delete(Person person, DataSource dataSource){
    try (java.sql.Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatementSelect = connection.prepareStatement(selectIdSQL);
         PreparedStatement preparedStatementDelete = connection.prepareStatement(deletePersonSQL)){
      preparedStatementSelect.setLong(1, person.getId());
      if(!preparedStatementSelect.executeQuery().next()){
        System.err.println("Пользователя с таким id не существует");
        return;
      }
      preparedStatementDelete.setLong(1, person.getId());
      preparedStatementDelete.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void save(Person person, DataSource dataSource){
    try (java.sql.Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatementSelect = connection.prepareStatement(selectIdSQL);
         PreparedStatement preparedStatementInsert = connection.prepareStatement(insertSQL);
         PreparedStatement preparedStatementUpdate = connection.prepareStatement(updateSQL)){
      preparedStatementSelect.setLong(1, person.getId());
      if(preparedStatementSelect.executeQuery().next()){
        preparedStatementUpdate.setString(1, person.getName());
        preparedStatementUpdate.setString(2, person.getLastName());
        preparedStatementUpdate.setString(3, person.getMiddleName());
        preparedStatementUpdate.setLong(4,person.getId());
        preparedStatementUpdate.executeUpdate();
      }else{
        preparedStatementInsert.setLong(1,person.getId());
        preparedStatementInsert.setString(2, person.getName());
        preparedStatementInsert.setString(3, person.getLastName());
        preparedStatementInsert.setString(4, person.getMiddleName());
        preparedStatementInsert.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    DataSource dataSource = initDb();
    ConnectionFactory connectionFactory = initMQ();

    // тут пишем создание и запуск приложения работы с БД
    String queueName = "queue";
    ObjectMapper objectMapper = new ObjectMapper();

    try(Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel()) {
      while (!Thread.currentThread().isInterrupted()){
        GetResponse message = channel.basicGet(queueName, true);
        if(message != null){
          Message rqMessage = objectMapper.readValue(message.getBody(), Message.class);
          if(rqMessage.getOperation() == Operation.DELETE){
            delete(rqMessage.getPerson(), dataSource);
          }else{
            save(rqMessage.getPerson(), dataSource);
          }
        }
      }
    }
  }
  
  private static ConnectionFactory initMQ() throws Exception {
    return RabbitMQUtil.buildConnectionFactory();
  }
  
  private static DataSource initDb() throws SQLException {
    String ddl = "" 
                     + "drop table if exists person;" 
                     + "create table if not exists person(\n"
                     + "person_id bigint primary key,\n"
                     + "first_name varchar,\n"
                     + "last_name varchar,\n"
                     + "middle_name varchar\n"
                     + ")";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(ddl, dataSource);
    return dataSource;
  }
}
