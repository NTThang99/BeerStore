package services;

import models.User;
import utils.CSVUtils;

import java.util.ArrayList;
import java.util.List;

public class UserService implements IUserService {

    public static String path = "C:\\BeerStore\\src\\main\\java\\data\\users.csv";

    @Override
    public List<User> getUsers() {
        List<User> newUserList = new ArrayList<>();
        List<String> records = CSVUtils.readData(path);
        for (String record : records) {
            newUserList.add(new User(record));
        }
        return newUserList;
    }

    @Override
    public User login(String username, String password) {
        List<User> users = getUsers();
        for (User user : users) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password))
                return user;
        }
        return null;
    }

    @Override
    public void add(User newUser) {
        List<User> users = getUsers();
        users.add(newUser);
        CSVUtils.writeData(path, users);
    }

    @Override
    public void update(User newUser) {
        List<User> users = getUsers();
        for (User user : users) {
            if (user.getId() == newUser.getId()) {
                if (newUser.getFullName() != null && !newUser.getFullName().isEmpty())
                    user.setFullName(newUser.getFullName());
                if (newUser.getBirthday() != null && !newUser.getBirthday().isEmpty())
                    user.setBirthday(newUser.getBirthday());
                if (newUser.getPhoneNumber() != null && !newUser.getPhoneNumber().isEmpty())
                    user.setPhoneNumber(newUser.getPhoneNumber());
                if (newUser.getAddress() != null && !newUser.getAddress().isEmpty())
                    user.setAddress(newUser.getAddress());
                if (newUser.getEmail() != null && !newUser.getEmail().isEmpty())
                    user.setEmail(newUser.getEmail());
                if (newUser.getUserName() != null && !newUser.getUserName().isEmpty())
                    user.setUserName(newUser.getUserName());
                if (newUser.getPassword() != null && !newUser.getPassword().isEmpty())
                    user.setPassword(newUser.getPassword());
                CSVUtils.writeData(path, users);
                break;
            }
        }
    }


    @Override
    public boolean isIdExisted(long id) {
        return getUserById(id) != null;
    }

    @Override
    public boolean checkExistedEmail(String email) {
        List<User> users = getUsers();
        for (User user : users) {
            if (user.getEmail().equals(email))
                return true;
        }
        return false;
    }

    @Override
    public boolean checkExistedPhone(String phone) {
        List<User> users = getUsers();
        for (User user : users) {
            if (user.getPhoneNumber().equals(phone))
                return true;
        }
        return false;
    }

    @Override
    public boolean checkExistedUserName(String userName) {
        List<User> users = getUsers();
        for (User user : users) {
            if (user.getUserName().equals(userName))
                return true;
        }
        return false;
    }

    @Override
    public User getUserById(long id) {
        List<User> users = getUsers();
        for (User user : users) {
            if (user.getId() == id)
                return user;
        }
        return null;
    }
}
