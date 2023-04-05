package io.ylab.intensive.lesson05.eventsourcing.api;

import javax.sql.DataSource;

import com.rabbitmq.client.ConnectionFactory;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan("io.ylab.intensive.lesson05.eventsourcing")
public class Config {
  
  @Bean
  public DataSource dataSource() {
    PGSimpleDataSource dataSource = new PGSimpleDataSource();
    dataSource.setServerName("localhost");
    dataSource.setUser("postgres");
    dataSource.setPassword("postgres");
    dataSource.setDatabaseName("postgres");
    dataSource.setPortNumber(5432);
    return dataSource;
  }

  @Bean
  public ConnectionFactory connectionFactory() {
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost("localhost");
    connectionFactory.setPort(5672);
    connectionFactory.setUsername("guest");
    connectionFactory.setPassword("guest");
    connectionFactory.setVirtualHost("/");
    return connectionFactory;
  }

  @Bean
  public Map<String, String> configure(){
    Map<String,String> map = new HashMap<>();
    map.put("queueName", "queue");
    map.put("exchangeName", "exc");
    map.put("ddl",""
            + "create table if not exists person(\n"
            + "person_id bigint primary key,\n"
            + "first_name varchar,\n"
            + "last_name varchar,\n"
            + "middle_name varchar\n"
            + ")");
    return map;

  }
}
