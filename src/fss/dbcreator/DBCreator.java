package fss.dbcreator;
import java.sql.*;
import java.util.Properties;

public class DBCreator {
    // Блок объявления констант
    public static final String DB_URL = "jdbc:postgresql://localhost/postgres";
    public static final String DB_Driver = "org.postgresql.Driver";

    public static void main(String[] args) {
        try {
            Class.forName(DB_Driver); //Проверяем наличие JDBC драйвера для работы с БД
            Properties props = new Properties();
            props.setProperty("user", "");
            props.setProperty("password","");
            Connection connection = DriverManager.getConnection(DB_URL, props);//соединениесБД
            System.out.println("Соединение с СУБД выполнено.");
            connection.close();       // отключение от БД
            System.out.println("Отключение от СУБД выполнено.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("Ошибка SQL !");
        }
    }
}
