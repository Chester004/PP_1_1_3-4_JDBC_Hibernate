package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import java.util.Properties;
import java.util.logging.Level;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/mydbtest";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static Connection connection;
    public static Connection getConnection(){
        try{
           connection = DriverManager.getConnection(URL,USER,PASSWORD);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void jdbcClose(){
        try {
            connection.close();
            System.out.println("Соединение успешно закрыто");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static Session getHib(){
        Properties prop = new Properties();
        prop.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/mydbtest");
        prop.setProperty("dialect", "org.hibernate.dialect.MySQLDialect");
        prop.setProperty("hibernate.connection.username", "root");
        prop.setProperty("hibernate.connection.password", "root");
        prop.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        prop.setProperty("show_sql", "true");
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        SessionFactory factory = new Configuration()
                .addProperties(prop)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();

        Session session = factory.openSession();
        return session;
    }
}
