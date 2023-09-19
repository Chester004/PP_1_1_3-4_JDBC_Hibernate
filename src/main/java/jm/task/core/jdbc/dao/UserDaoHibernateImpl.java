package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        String createTableSQL = "CREATE TABLE users("
                + "ID INT(5) NOT NULL AUTO_INCREMENT, "
                + "NAME VARCHAR(20) NOT NULL, "
                + "LASTNAME VARCHAR(20) NOT NULL, "
                + "AGE INT(3) NOT NULL, " + "PRIMARY KEY (ID) "
                + ")";
        try(Session session = Util.getHib()){
            session.beginTransaction();
            session.createSQLQuery(createTableSQL).executeUpdate();
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            System.err.println("Ошибка создания таблицы");
        }
    }

    @Override
    public void dropUsersTable() {
        String dropSQL = "DROP TABLE `users`";
        try(Session session = Util.getHib()){
        session.beginTransaction();
        session.createSQLQuery(dropSQL).executeUpdate();
        session.getTransaction().commit();
        }catch (PersistenceException e){
            System.err.println("Таблица уже удалена");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = Util.getHib();
        session.beginTransaction();
        session.save(new User(name,lastName,age));
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void removeUserById(long id) {
        try(Session session = Util.getHib();) {
            session.beginTransaction();
            User user = session.load(User.class, id );
            session.delete(user);
            session.flush();
            session.close();
        }catch (EntityNotFoundException e){
            System.err.printf("Id %d нет в таблице \n",id);
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = Util.getHib();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        criteria.from(User.class);
        List<User> data = session.createQuery(criteria).getResultList();
        return data;
    }
    @Override
    public void cleanUsersTable() {
        String dropSQL = "DELETE FROM `users`";
        Session session = Util.getHib();
        session.beginTransaction();
        session.createSQLQuery(dropSQL).executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
