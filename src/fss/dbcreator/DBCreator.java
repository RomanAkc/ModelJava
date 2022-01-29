package fss.dbcreator;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class DBCreator {
    // Блок объявления констант
    public static final String DB_URL = "jdbc:postgresql://localhost/postgres";
    public static final String DB_Driver = "org.postgresql.Driver";

    public static void main(String[] args) {
        try {
            Class.forName(DB_Driver);
            Properties props = new Properties();
            props.setProperty("user", args[0]);
            props.setProperty("password", args[1]);
            Connection connection = DriverManager.getConnection(DB_URL, props);//соединениесБД

            Table test = new Table("test");
            test.addField(new Table.StringField("data"));

            createTable(connection, test);

            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("JDBC driver doesn't found");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error!");
        }
    }

    private static String getStringByType(FieldType type) {
        if(type == FieldType.INTEGER) {
            return "INT";
        } else if(type == FieldType.STRING) {
            return "VARCHAR(64)";
        }

        return "";
    }

    private static void createTable(Connection dbConnection, Table table) {
        try {
            Statement stmt = dbConnection.createStatement();

            String sql = "CREATE TABLE " + table.getName() + "(id INT not NULL";

            for(var field : table.getFields()) {
                sql += ", ";
                sql += field.getName();
                sql += " ";
                sql += getStringByType(field.getType());
                sql += " not NULL";
            }

            sql += ", PRIMARY KEY ( id ))";

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
