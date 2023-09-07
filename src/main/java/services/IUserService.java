package services;

import models.User;

import java.util.List;

public interface IUserService {
    List<User> getUsers();

    User login(String username, String password);

    void add(User newUser);

    void update(User newUser);

    boolean isIdExisted(long id);

    boolean checkExistedEmail(String email);

    boolean checkExistedPhone(String phone);

    boolean checkExistedUserName(String userName);

    User getUserById(long id);
}
