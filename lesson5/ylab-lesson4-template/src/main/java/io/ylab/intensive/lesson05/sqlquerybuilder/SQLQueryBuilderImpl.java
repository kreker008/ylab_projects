package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class SQLQueryBuilderImpl implements SQLQueryBuilder {
    private final Connection connectionDB;

    @Autowired
    public SQLQueryBuilderImpl(DataSource dataSource) throws SQLException {
        this.connectionDB = dataSource.getConnection();
    }

    @PreDestroy
    public void preDestroy() throws SQLException {
        if (!connectionDB.isClosed()) {
            connectionDB.close();
        }
    }

    private String getColumsNameOfTable(String tableName, DatabaseMetaData metaData) throws SQLException {
        StringBuilder columnsName = new StringBuilder();
        ResultSet columnsRS = metaData.getColumns(null, null, tableName, null);

        while (columnsRS.next()) {
            columnsName.append(columnsRS.getString("column_name")).append(", ");
        }
        columnsName.delete(columnsName.length() - 2, columnsName.length());
        return columnsName.toString();
    }

    @Override
    public String queryForTable(String tableName) throws SQLException {
        DatabaseMetaData metaData = connectionDB.getMetaData();
        ResultSet tablesRS = metaData.getTables(null, null, null, new String[]{"TABLE"});

        while (tablesRS.next()) {
            if (tablesRS.getString("table_name").equals(tableName)) {
                String columnsName = getColumsNameOfTable(tableName, metaData);
                return String.format("SELECT %s FROM %s", columnsName, tableName);
            }
        }
        return null;
    }

    @Override
    public List<String> getTables() throws SQLException {
        DatabaseMetaData metaData = connectionDB.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"});
        List<String> listTableName = new ArrayList<>();

        while (resultSet.next()) {
            listTableName.add(resultSet.getString("table_name"));
        }
        // Не сохраняю listTableName в поле, тк в теории между вызовыми может произойти изменения в бд
        return listTableName;
    }
}
