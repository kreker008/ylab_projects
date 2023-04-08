package io.ylab.intensive.lesson05.messagefilter.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component("DBUtil")
public class DBUtil {
    private final int CONNECTION_NUM;
    private final List<Connection> connectionPull;
    private int connectionGot;
    @Value("${DBUtil.ddl}")
    private String ddl;
    @Value("${DBUtil.insertSQL}")
    private String insertSQL;
    @Value("${DBUtil.wordFileName}")
    private String wordFileName;
    @Value("${DBUtil.tableName}")
    private String tableName;

    @Autowired
    public DBUtil(DataSource dataSource, @Value("${DBUtil.connectionsNum}") int connectionNum) throws SQLException {
        this.CONNECTION_NUM = connectionNum;
        this.connectionPull = new ArrayList<>();
        this.connectionGot = 0;
        for(int i = 0; i < CONNECTION_NUM; ++i){
            this.connectionPull.add(dataSource.getConnection());
        }
    }
    private boolean isCreatedTable(String tableName) throws SQLException {
        DatabaseMetaData metaData = connectionPull.get(0).getMetaData();
        ResultSet tablesRS = metaData.getTables(null, null, null, new String[]{"TABLE"});

        while (tablesRS.next()) {
            if (tablesRS.getString("table_name").equals(tableName)) {
                return true;
            }
        }
        return false;
    }

    @PostConstruct
    private void createTableAndInsertWord() throws Exception {
        if(connectionPull.size() == 0){
            throw new  Exception("connection pull пустой");
        }
        Connection connection = connectionPull.get(0);
        try (Statement statement = connection.createStatement();
             PreparedStatement preparedStatementInsert = connection.prepareStatement(insertSQL);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(wordFileName))))) {

            if (isCreatedTable(tableName)){
                return;
            }
            statement.execute(ddl);
            String line;
            while((line = reader.readLine()) != null){
                line = line.toLowerCase();
                preparedStatementInsert.setString(1, line);
                preparedStatementInsert.addBatch();
            }
            preparedStatementInsert.executeBatch();
        }
    }


    @PreDestroy
    private void closeConnections() throws SQLException {
        for(Connection connection: connectionPull){
            if(!connection.isClosed()){
                connection.close();
            }
        }
    }

    public Connection getConnection(){
        if(connectionGot >= CONNECTION_NUM){
            return null;
        }
        return connectionPull.get(connectionGot++);
    }
}
