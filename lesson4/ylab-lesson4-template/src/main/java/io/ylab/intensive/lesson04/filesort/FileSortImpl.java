package io.ylab.intensive.lesson04.filesort;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class FileSortImpl implements FileSorter {
  private DataSource dataSource;
  private static String insertSQL = "INSERT INTO numbers(val) VALUES(?)";
  private static String selectSortedValues = "SELECT val FROM numbers ORDER BY val DESC";

  public FileSortImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  private void writeIntoDB(File data, PreparedStatement preparedStatementInsert) throws IOException{
    try(BufferedReader reader = new BufferedReader(new FileReader(data))){
      String line;
      while((line = reader.readLine()) != null){
        preparedStatementInsert.setLong(1, Long.parseLong(line));
        preparedStatementInsert.addBatch();
      }
      preparedStatementInsert.executeBatch();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  private void writeIntoFile(File data, ResultSet resultSet) throws IOException,SQLException {
    try(BufferedWriter writer = new BufferedWriter(new FileWriter(data))){
      while (resultSet.next()) {
        writer.write(resultSet.getString(1));
        writer.newLine();
      }
    }
  }

  @Override
  public File sort(File data) {
    // ТУТ ПИШЕМ РЕАЛИЗАЦИЮ
    try (Connection connection = this.dataSource.getConnection();
         PreparedStatement preparedStatementInsert = connection.prepareStatement(insertSQL);
         PreparedStatement preparedStatementSelect = connection.prepareStatement(selectSortedValues)) {
      writeIntoDB(data, preparedStatementInsert);
      ResultSet resultSet = preparedStatementSelect.executeQuery();
      writeIntoFile(data, resultSet);
    } catch (IOException | SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
