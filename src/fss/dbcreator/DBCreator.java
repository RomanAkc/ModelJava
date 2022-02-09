package fss.dbcreator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
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
            Document xmlDoc = ReadDataBaseTemplate("DataBaseTemplate.xml");
            Connection connection = getDBConnection(args);

            //Читаем таблицы из файла xmlDoc и создаем таблицы в БД


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

    private static void CreateTablesFromXML(Document xml, Connection connection) {
        NodeList tables = xml.getChildNodes();
        for(int i = 0; i < tables.getLength(); ++i) {
            Node table = tables.item(i);

        }
    }

    private static Connection getDBConnection(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName(DB_Driver);
        Properties props = new Properties();
        props.setProperty("user", args[0]);
        props.setProperty("password", args[1]);
        Connection connection = DriverManager.getConnection(DB_URL, props); //соединениесБД
        return connection;
    }

    private static Document ReadDataBaseTemplate(String path) {
        try {
            // Создается построитель документа
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создается дерево DOM документа из файла
            return documentBuilder.parse(path);
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace(System.out);
        } catch (SAXException ex) {
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }

        return null;
    }

    private static void ReadXML() {
        try {
            // Создается построитель документа
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создается дерево DOM документа из файла
            Document document = documentBuilder.parse("DataBaseTemplate.xml");

            // Получаем корневой элемент
            Node root = document.getDocumentElement();

            /*System.out.println("List of books:");
            System.out.println();
            // Просматриваем все подэлементы корневого - т.е. книги
            NodeList books = root.getChildNodes();
            for (int i = 0; i < books.getLength(); i++) {
                Node book = books.item(i);
                // Если нода не текст, то это книга - заходим внутрь
                if (book.getNodeType() != Node.TEXT_NODE) {
                    NodeList bookProps = book.getChildNodes();
                    for(int j = 0; j < bookProps.getLength(); j++) {
                        Node bookProp = bookProps.item(j);
                        // Если нода не текст, то это один из параметров книги - печатаем
                        if (bookProp.getNodeType() != Node.TEXT_NODE) {
                            System.out.println(bookProp.getNodeName() + ":" + bookProp.getChildNodes().item(0).getTextContent());
                        }
                    }
                    System.out.println("===========>>>>");
                }
            }*/

        } catch (ParserConfigurationException ex) {
            ex.printStackTrace(System.out);
        } catch (SAXException ex) {
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
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
