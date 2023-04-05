package io.ylab.intensive.lesson05.eventsourcing.api;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class ApiApp {
  public static void main(String[] args) {
    // Тут пишем создание PersonApi, запуск и демонстрацию работы
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    PersonApi personApi = applicationContext.getBean(PersonApi.class);
    // пишем взаимодействие с PersonApi
    personApi.savePerson(1L, "A", "1", "a");
    personApi.savePerson(2L, "B", "2", "b");
    personApi.savePerson(3L, "B", "3", "c");
    personApi.savePerson(4L, "C", "4", "d");
    personApi.savePerson(5L, "D", "5", "e");
    personApi.deletePerson(1L);
    List<Person> personList = personApi.findAll();
    applicationContext.close();
    if(personList == null){
      return;
    }
    for(Person person: personList){
      System.out.print(person.getId() + " ");
      System.out.print(person.getName() + " ");
      System.out.print(person.getMiddleName() + " ");
      System.out.print(person.getLastName() + " ");
      System.out.println();
    }
  }
}
