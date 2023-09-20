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

    @Override
    public void createUsersTable() {
        String createTableSQL = "CREATE TABLE users("
                + "id BIGINT(5) NOT NULL AUTO_INCREMENT, "
                + "name VARCHAR(20) NOT NULL, "
                + "lastname VARCHAR(20) NOT NULL, "
                + "age TINYINT(3) NOT NULL, " + "PRIMARY KEY (ID) "
                + ")";
        try (PreparedStatement ps = connect.prepareStatement(createTableSQL)) {
            ps.executeUpdate();
        } catch (SQLSyntaxErrorException e) {
            System.err.println("Таблица уже создана");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
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

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = "Insert into users(name,lastname,age) VALUES (?,?,?)";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setByte(3, age);
            ps.executeUpdate();
            System.out.printf("User c именем  - %s добавлен в базу данных \n", name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (PreparedStatement ps = connect.prepareStatement("SELECT * FROM users")) {
            ResultSet res = ps.executeQuery();
            List<User> users = new ArrayList<>();
            while (res.next()) {
                User user = new User(res.getString(2), res.getString(3), res.getByte(4));
                user.setId(res.getLong(1));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUsersTable() {
        String sql = "DELETE FROM users";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
