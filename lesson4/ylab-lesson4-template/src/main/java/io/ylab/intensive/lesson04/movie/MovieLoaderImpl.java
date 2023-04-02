package io.ylab.intensive.lesson04.movie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import javax.sql.DataSource;

public class MovieLoaderImpl implements MovieLoader {
  private DataSource dataSource;
  private String insertSQL;

  public MovieLoaderImpl(DataSource dataSource) {
    this.dataSource = dataSource;
    this.insertSQL = "INSERT INTO movie(\n" +
            "\tyear, length, title, subject, " +
            "\tactors, actress, director, popularity, awards)\n" +
            "\t VALUES (?,?,?,?,?,?,?,?,?);";
  }

  private Movie parseMovieFromLine(String line){
    Movie movie = new Movie();

    String elements[] = line.split(";");
    try {
      movie.setYear(Integer.valueOf(elements[0]));
    }catch (NumberFormatException e){
      movie.setYear(null);
    }
    try {
      movie.setLength(Integer.valueOf(elements[1]));
    }catch (NumberFormatException e){
      movie.setLength(null);
    }
    movie.setTitle(elements[2]);
    movie.setSubject(elements[3]);
    movie.setActors(elements[4]);
    movie.setActress(elements[5]);
    movie.setDirector(elements[6]);
    try {
      movie.setPopularity(Integer.valueOf(elements[7]));
    }catch (NumberFormatException e){
      movie.setPopularity(null);
    }
    if(elements[8].equals("Yes")){
      movie.setAwards(true);
    }else if (elements[8].equals("No")){
      movie.setAwards(false);
    }else{
      movie.setAwards(null);
    }

    return movie;
  }

  private void setValueInPreparedStatement(int index, Object value, PreparedStatement preparedStatement, int sqlType) throws SQLException {
    if (value == null || (value instanceof String && value.equals(""))){
      preparedStatement.setNull(index, sqlType);
      return;
    }

    if(sqlType == Types.INTEGER){
        preparedStatement.setInt(index, (Integer) value);
    }else if(sqlType == Types.VARCHAR){
        preparedStatement.setString(index, (String) value);
    }else{
        preparedStatement.setBoolean(index, (Boolean) value);
    }
  }

  private void insertMovie(Movie movie, PreparedStatement preparedStatement, Connection connection) throws SQLException {
      setValueInPreparedStatement(1, movie.getYear(), preparedStatement, Types.INTEGER);
      setValueInPreparedStatement(2, movie.getLength(), preparedStatement, Types.INTEGER);
      setValueInPreparedStatement(3, movie.getTitle(), preparedStatement, Types.VARCHAR);
      setValueInPreparedStatement(4, movie.getSubject(), preparedStatement, Types.VARCHAR);
      setValueInPreparedStatement(5, movie.getActors(), preparedStatement, Types.VARCHAR);
      setValueInPreparedStatement(6, movie.getActress(), preparedStatement, Types.VARCHAR);
      setValueInPreparedStatement(7, movie.getDirector(), preparedStatement, Types.VARCHAR);
      setValueInPreparedStatement(8, movie.getPopularity(), preparedStatement, Types.INTEGER);
      setValueInPreparedStatement(9, movie.getAwards(), preparedStatement, Types.BOOLEAN);
      preparedStatement.executeUpdate();
      connection.commit();
  }

  @Override
  public void loadData(File file) {
    // РЕАЛИЗАЦИЮ ПИШЕМ ТУТ
    try(BufferedReader bufferedReader  = new BufferedReader(new FileReader(file))) {
      String line;

      if(!(bufferedReader.readLine().equals("Year;Length;Title;Subject;Actor;Actress;Director;Popularity;Awards;*Image") &&
        bufferedReader.readLine().equals("INT;INT;STRING;CAT;CAT;CAT;CAT;INT;BOOL;STRING"))){
        System.err.println("Invalid file format");
        return;
      }

      try (Connection connection = this.dataSource.getConnection();
           PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
        connection.setAutoCommit(false);
        while ((line = bufferedReader.readLine()) != null) {
          insertMovie(parseMovieFromLine(line), preparedStatement, connection);
        }
      }
    } catch (IOException | SQLException e) {
      e.printStackTrace();
    }
  }
}
