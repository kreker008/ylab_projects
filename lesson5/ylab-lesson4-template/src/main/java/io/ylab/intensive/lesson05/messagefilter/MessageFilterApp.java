package io.ylab.intensive.lesson05.messagefilter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class MessageFilterApp {
  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    Parser parser = applicationContext.getBean(Parser.class);
    try {
      parser.listen();
    }catch (SQLException | IOException | TimeoutException e){
      e.printStackTrace();
    }
    applicationContext.close();
  }
}
