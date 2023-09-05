package Group12.Stockify;

public interface UserDAO {
    void saveUser(User user);
    User getUser(String email);
}