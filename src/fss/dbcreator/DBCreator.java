package fss.dbcreator;
import java.sql.*;
import java.util.Properties;

public class DBCreator {
    // Блок объявления констант
    public static final String DB_URL = "jdbc:postgresql://localhost/postgres";
    public static final String DB_Driver = "org.postgresql.Driver";

    public static void main(String[] args) {
        try {
            Class.forName(DB_Driver);
            Properties props = new Properties();
            //TODO: read username, pswd from args
            props.setProperty("user", "");
            props.setProperty("password","");
            Connection connection = DriverManager.getConnection(DB_URL, props);//соединениесБД
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("JDBC driver doesn't found");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error!");
        }
    }
}
