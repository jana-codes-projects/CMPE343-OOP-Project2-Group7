package dao;

import models.User;
import java.util.List;

public interface UserDao {
    User findByUsername(String username);
    List<User> findAllUsers();
    void updateUser(User user);
    void addUser(User user);
    void deleteUser(int userId);
}
