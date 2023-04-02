package io.ylab.intensive.lesson04.persistentmap;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;

/**
 * Класс, методы которого надо реализовать 
 */
public class PersistentMapImpl implements PersistentMap {
  
  private DataSource dataSource;
  private String name;
  private static final String insertSQL = "INSERT INTO persistent_map(\n" +
          "\t map_name, KEY, value)\n" +
          "\t VALUES (?,?,?);";
  private static final String selectValueSQL = "SELECT value FROM persistent_map " +
          "\tWHERE map_name=? and key=?;";
  private static final String selectValuesSQL = "SELECT value FROM persistent_map " +
          "\tWHERE map_name=?;";
  private static final String deleteKeySQL = "DELETE FROM persistent_map" +
          "\tWHERE map_name=? and key=?";
  private static final String deleteAllSQL = "DELETE FROM persistent_map" +
          "\tWHERE map_name=?";

  private void checkInitStatus(){
    if(this.name == null){
      throw new ClassNotPreparedException("Экземпляр PersistentMapImpl не инициализирован, для инициализации необходимо вызвать метод init()");
    }
  }

  public PersistentMapImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void init(String name) {
    if(this.name == null){
      this.name = name;
    }else{
      System.err.println("Экземпляр уже проинициализирована");
    }
  }

    @Override
  public boolean containsKey(String key) throws SQLException {
      checkInitStatus();
      try (Connection connection = this.dataSource.getConnection();
           PreparedStatement preparedStatementSelect = connection.prepareStatement(selectValueSQL)){
        preparedStatementSelect.setString(1, this.name);
        preparedStatementSelect.setString(2, key);
        return preparedStatementSelect.executeQuery().next();
      }
    }

  @Override
  public List<String> getKeys() throws SQLException {
    checkInitStatus();
    try (Connection connection = this.dataSource.getConnection();
         PreparedStatement preparedStatementValues = connection.prepareStatement(selectValuesSQL)) {
      preparedStatementValues.setString(1, this.name);
      ResultSet resultSet = preparedStatementValues.executeQuery();
      List<String> stringList = null;
      if(resultSet.next()){
        stringList = new ArrayList<>();
        stringList.add(resultSet.getString(1));
        while (resultSet.next()){
          stringList.add(resultSet.getString(1));
        }
      }
      return stringList;
    }
  }

  @Override
  public String get(String key) throws SQLException {
    checkInitStatus();
    try (Connection connection = this.dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(selectValueSQL)){
      preparedStatement.setString(1, this.name);
      preparedStatement.setString(2, key);
      ResultSet resultSet = preparedStatement.executeQuery();
      return resultSet.next()? resultSet.getString(1): null;
    }
  }

  @Override
  public void remove(String key) throws SQLException {
    checkInitStatus();
    try (Connection connection = this.dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(deleteKeySQL)){
      preparedStatement.setString(1, this.name);
      preparedStatement.setString(2, key);
      preparedStatement.executeUpdate();
    }
  }

  @Override
  public void put(String key, String value) throws SQLException {
    checkInitStatus();
    try (Connection connection = this.dataSource.getConnection();
         PreparedStatement preparedStatementSelect = connection.prepareStatement(selectValueSQL);
         PreparedStatement preparedStatementInsert = connection.prepareStatement(insertSQL)){
      preparedStatementSelect.setString(1, this.name);
      preparedStatementSelect.setString(2, key);
      if(preparedStatementSelect.executeQuery().next()){
          System.err.println("Запись с таким ключом уже существует");
          return;
      }
      preparedStatementInsert.setString(1, this.name);
      preparedStatementInsert.setString(2, key);
      preparedStatementInsert.setString(3, value);
      preparedStatementInsert.executeUpdate();
    }
  }

  @Override
  public void clear() throws SQLException {
    checkInitStatus();
    try (Connection connection = this.dataSource.getConnection();
         PreparedStatement preparedStatementDelete = connection.prepareStatement(deleteAllSQL)){
      preparedStatementDelete.setString(1, this.name);
      preparedStatementDelete.executeUpdate();
    }
  }
}
