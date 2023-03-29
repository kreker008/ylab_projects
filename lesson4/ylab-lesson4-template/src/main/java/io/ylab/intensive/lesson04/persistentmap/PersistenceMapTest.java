package io.ylab.intensive.lesson04.persistentmap;

import java.sql.SQLException;
import javax.sql.DataSource;

import io.ylab.intensive.lesson04.DbUtil;

public class PersistenceMapTest {
  public static void main(String[] args) throws SQLException {
    DataSource dataSource = initDb();
    PersistentMap persistentMap = new PersistentMapImpl(dataSource);
    // Написать код демонстрации работы
    PersistentMap map2 = new PersistentMapImpl(dataSource);

    //Инициализация
    persistentMap.init("map1");

    persistentMap.init("some_name");
    System.out.println(persistentMap.containsKey("some_key"));
    System.out.println(persistentMap.get("some_key"));
    System.out.println(persistentMap.getKeys());
    persistentMap.remove("some_key");
    persistentMap.clear();
    System.out.println("----");

    //Положил внутрь две пары key value
    persistentMap.put("map1_key1", "map1_value1");
    persistentMap.put("map1_key2", "map1_value2");

    System.out.println(persistentMap.containsKey("map1_key1"));
    System.out.println(persistentMap.get("map1_key1"));
    System.out.println(persistentMap.getKeys());
    persistentMap.remove("map1_key1");
    System.out.println(persistentMap.get("map1_key1"));
    System.out.println(persistentMap.get("map1_key2"));
    persistentMap.clear();
    System.out.println(persistentMap.get("map1_key2"));

    //Запуск без инициализации
    try {
      map2.put("key","value");
    } catch (Exception e){
//      e.printStackTrace();
      System.out.println("Сообщение о том, что так нельзя");
    }


  }
  
  public static DataSource initDb() throws SQLException {
    String createMapTable = "" 
                                + "drop table if exists persistent_map; " 
                                + "CREATE TABLE if not exists persistent_map (\n"
                                + "   map_name varchar,\n"
                                + "   KEY varchar,\n"
                                + "   value varchar\n"
                                + ");";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(createMapTable, dataSource);
    return dataSource;
  }
}
