package views;

import models.Role;
import models.User;
import services.IUserService;
import services.UserService;

import java.util.Scanner;

public class LoginView {

    private static long userID;

    public static long getUserID() {
        return userID;
    }

    private static IUserService userService = (IUserService) new UserService();

    public static void signIn() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n\n----- ĐĂNG NHẬP -----\n");
        System.out.print("1. Username: ");
        String username = input.nextLine().trim();
        System.out.print("2. Password: ");
        String password = input.nextLine().trim();
        User user = userService.login(username, password);

        if (user == null) {
            showChoicesWhenWrong();
            return;
        }

        if (user.getRole() == Role.ADMIN) {
            System.out.println("\nĐăng nhập thành công với ADMIN!");
            userID = user.getId();
            AdminView.chooseAdminAction();
            return;
        }

        System.out.println("\nĐăng nhập thành công!");
        userID = user.getId();
        CustomerView.chooseServicesForGuest();

    }

    public static void showChoicesWhenWrong() {
        System.out.println("\nSai tên người dùng hoặc mật khẩu!");
        System.out.println("Bạn có muốn đăng nhập lại?");
        do {
            System.out.println("(Nhập 'y' để đăng nhập lại hoặc nhập 'n' để quay lại)");
            try {
                String letter = Menu.chooseActionByLetter();
                if (letter.charAt(0) == 'y' && letter.length() == 1) {
                    signIn();
                    break;
                }
                if (letter.charAt(0) == 'n' && letter.length() == 1) {
                    Menu.menu();
                    break;
                }
                Menu.alert();
            } catch (Exception ex) {
                ex.printStackTrace();
                Menu.alert();
            }
        } while (true);
    }
}
