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
            //TODO: read username, pswd from args
            props.setProperty("user", "");
            props.setProperty("password","");
            Connection connection = DriverManager.getConnection(DB_URL, props);//соединениесБД

            ArrayList<TableField> fields = new ArrayList<>();
            fields.add(new TableField("id", FieldType.INTEGER));
            fields.add(new TableField("data", FieldType.STRING));

            createTable(connection, "Test", fields);

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

    private static void createTable(Connection dbConnection, String name, ArrayList<TableField> fields) {
        try {
            Statement stmt = dbConnection.createStatement();

            String sql = "CREATE TABLE " + name + "(";

            boolean first = true;
            for(var field : fields) {
                if(!first) {
                    sql += ", ";
                } else {
                    first = false;
                }

                sql += field.getName();
                sql += " ";
                sql += getStringByType(field.getType());
                sql += " not NULL";
            }

            sql += ", PRIMARY KEY ( id ))";
            //sql += ")";

            stmt.executeUpdate(sql);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
