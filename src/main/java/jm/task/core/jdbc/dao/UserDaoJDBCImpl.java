package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static Connection connect = Util.getConnection();
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {

        String createTableSQL = "CREATE TABLE users("
                + "ID INT(5) NOT NULL AUTO_INCREMENT, "
                + "NAME VARCHAR(20) NOT NULL, "
                + "LASTNAME VARCHAR(20) NOT NULL, "
                + "AGE INT(3) NOT NULL, " + "PRIMARY KEY (ID) "
                + ")";
        try(PreparedStatement ps = connect.prepareStatement(createTableSQL)) {
            ps.executeUpdate();
        } catch (SQLSyntaxErrorException e) {
            System.err.println("Таблица уже создана");
        } catch (SQLException e ){
            throw new RuntimeException(e);
        }
    }

//    private void executeUpdate(String sql, String okMessage) {
//        try (Statement statement = Util.getConnection().createStatement()) {
//            int res = statement.executeUpdate(sql);
//            if (res > 0) {
//                System.out.println(okMessage);
//            }
//        } catch (SQLSyntaxErrorException e) {
//            System.out.println("Не получилось создать/удалить таблицу");
//        }catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void dropUsersTable() {
        String sql = "DROP TABLE `users`";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLSyntaxErrorException e) {
            System.err.println("Не удалить таблицу");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "Insert into users(name,lastname,age) VALUES (?,?,?)";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1,name);
            ps.setString(2,lastName);
            ps.setByte(3,age);
            ps.executeUpdate();
            System.out.printf("User c именем  - %s добавлен в базу данных \n",name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setLong(1,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        try(PreparedStatement ps = connect.prepareStatement("SELECT * FROM users")) {
            ResultSet res = ps.executeQuery();
            List<User> users = new ArrayList<>();
            while (res.next()){
                users.add(new User(res.getString(2),res.getString(3),res.getByte(4)));
            }
            return users;
        } catch (SQLException e ){
            throw new RuntimeException(e);
        }
    }

    public void cleanUsersTable() {
        String sql = "DELETE FROM users";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
