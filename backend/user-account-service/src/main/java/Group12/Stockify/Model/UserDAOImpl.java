package Group12.Stockify;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class UserDAOImpl implements UserDAO {
    private SessionFactory factory;

    public UserDAOImpl(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void saveUser(User user) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }

    @Override
    public User getUser(String email) {
        Session session = factory.getCurrentSession();
        return session.get(User.class, email);
    }
}