package io.ylab.intensive.lesson04.eventsourcing.api;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;
import io.ylab.intensive.lesson04.eventsourcing.Person;

import javax.sql.DataSource;
import java.sql.SQLException;

public class ApiApp {
  public static void main(String[] args) throws Exception {
    ConnectionFactory connectionFactory = initMQ();
    // Тут пишем создание PersonApi, запуск и демонстрацию работы
    DataSource dataSource = initDb();
    String exchangeName = "exc";
    String queueName = "queue";

    try(Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel()) {
      channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
      channel.queueDeclare(queueName, true, false, false, null);
      channel.queueBind(queueName, exchangeName, "*");

      PersonApiImpl personApi = new PersonApiImpl(exchangeName,channel, dataSource);
      personApi.savePerson(1L, "A", "1", "a");
      personApi.savePerson(2L, "B", "2", "b");
      personApi.savePerson(3L, "B", "3", "c");
      personApi.savePerson(4L, "C", "4", "d");
      personApi.savePerson(5L, "D", "5", "e");
      personApi.deletePerson(1L);
      for(Person person: personApi.findAll()){
        System.out.println(person.getId());
        System.out.println(person.getName());
        System.out.println(person.getMiddleName());
        System.out.println(person.getLastName());
      }
    }
  }

  private static DataSource initDb() throws SQLException {
    String ddl = ""
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

  private static ConnectionFactory initMQ() throws Exception {
    return RabbitMQUtil.buildConnectionFactory();
  }
}
