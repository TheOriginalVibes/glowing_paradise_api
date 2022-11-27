package at.vibes.libSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LibMySQL {
    private static LibMySQL instance;

    LibMySQL() {instance = this;}

    public Connection getConnection(String ip, String database, String username, String password) {
        String url = "jdbc:mysql://" + ip + ":3306/";
        String driver = "com.mysql.jdbc.Driver";

        try {
            Class.forName(driver).newInstance();
            return DriverManager.getConnection(url + database, username, password);
        } catch (IllegalAccessException | ClassNotFoundException | SQLException | InstantiationException var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }

    public void insert(String table, Object[] columns, Object[] values, Connection databaseConnection) {
        String cols = toString(columns);
        String value = toString(values);

        try {
            Statement st = databaseConnection.createStatement();
            st.executeQuery("INSERT INTO " + table + "(" + cols + ") VALUES (" + value + ")");
        } catch (SQLException var7) {
            var7.printStackTrace();
        }

    }

    public String toString(Object[] array) {
        String result = "";
        int count = 0;

        for (Object o : array) {
            if (count == 0) {
                result = (String) o;
            } else {
                result = result + ", " + o;
            }

            count = count + 1;
        }

        return result;
    }

    public Object select(Connection databaseConnection, String column, String table, String criteriaValue) {
        String[] criteria = criteriaValue.split(">");
        Object result = null;

        try {
            Statement st = databaseConnection.createStatement();

            for(ResultSet rs = st.executeQuery("SELECT " + column + " FROM " + table + "WHERE " + criteria[0] + "='" + criteria[1] + "'");
                rs.next();
                result = rs.getObject(column)) {
            }
        } catch (SQLException var8) {
            var8.printStackTrace();
        }

        return result;
    }

    public boolean update(Connection databaseConnection, String table, String criteria, String change) {
        try {
            Statement st = databaseConnection.createStatement();
            String[] criteriaV = criteria.split(">");
            String[] update = change.split(">");
            st.executeUpdate("UPDATE " + table + " WHERE " + criteriaV[0] + "=" + criteriaV[1] + " SET " + update[0] + "=" + update[1]);
            return true;
        } catch (SQLException var7) {
            var7.printStackTrace();
            return false;
        }
    }

    public boolean delete(Connection databaseConnection, String table, String criteria) {
        String[] caseValue = criteria.split(">");

        try {
            Statement st = databaseConnection.createStatement();
            st.executeQuery("DELETE FROM " + table + " WHERE " + caseValue[0] + "=" + caseValue[1]);
            return true;
        } catch (SQLException var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static LibMySQL getInstance() {return instance;}
}
