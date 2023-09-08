package customersServices;

import models.User;
import services.IUserService;
import services.UserService;
import utils.ValidateUtils;
import views.CustomerView;
import views.LoginView;
import views.Menu;
import views.SignUpView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class AccountManagement {
    private static IUserService userService = new UserService();

    public static void showCurrentAccount(User user) {
        LocalDateTime creationDate = (user.getCreationTime());
        System.out.println("\n--------------Tài khoản của bạn---------------\n");
        System.out.printf("%-17s %d\n", "1. ID:", user.getId());
        System.out.printf("%-17s %s\n", "2. Họ và Tên:", user.getFullName());
        System.out.printf("%-17s %s\n", "3. Ngày sinh:", user.getBirthday());
        System.out.printf("%-17s %s\n", "4. Số điện thoại:", user.getPhoneNumber());
        System.out.printf("%-17s %s\n", "5. Địa chỉ:", user.getAddress());
        System.out.printf("%-17s %s\n", "6. Email:", user.getEmail());
        System.out.printf("%-17s %s\n", "7. Username:", user.getUserName());
        System.out.printf("\n%s %s\n", "Ngày tạo:", creationDate);
        System.out.println("\n-----------------------------------------\n");
    }

    public static int updateAccount() {
        User currentUser = userService.getUserById(LoginView.getUserID());
        SignUpView newRegister = new SignUpView();
        boolean is;
        int number = -1;
        do {
            showCurrentAccount(currentUser);
            showChangeStatus(number);
            System.out.println("* Vui lòng chọn mục mà bạn muốn thay đổi thông tin *");
            System.out.println("---> Nhập '2' để cập nhật Họ và Tên.");
            System.out.println("---> Nhập '3' để cập nhật Ngày sinh.");
            System.out.println("---> Nhập '4' để cập nhật Số điện thoại.");
            System.out.println("---> Nhập '5' để cập nhật Địa chỉ.");
            System.out.println("---> Nhập '6' để cập nhật Email.");
            System.out.println("---> Nhập '7' để cập nhật mật khẩu.");
            System.out.println("---> Nhập '8' để xác nhận cập nhật.");
            System.out.println("---> Nhập '0' để kết thúc cập nhật.\n");

            try {
                number = Menu.chooseActionByNumber();
                switch (number) {
                    case 2:
                        is = newRegister.enterFullName(currentUser);
                        break;
                    case 3:
                        is = newRegister.enterBirthday(currentUser);
                        break;
                    case 4:
                        is = newRegister.enterPhoneNumber(currentUser);
                        break;
                    case 5:
                        is = newRegister.enterAddress(currentUser);
                        break;
                    case 6:
                        is = newRegister.enterEmail(currentUser);
                        break;
//                    case 7:
//                        is = newRegister.enterUserName(currentUser);
//                        break;
                    case 7:
                        is = true;
                        updatePassword(currentUser);
                        break;
                    case 8:
                        is = true;
                        userService.update(currentUser);
                        break;
                    case 0:
                        is = true;
                        break;
                    default:
                        Menu.alert();
                        is = false;
                }
            } catch (Exception ex) {
                is = false;
                Menu.alert();
            }
        } while (!is);
        return number;
    }

    private static void updatePassword(User currentUser) {
        Scanner input = new Scanner(System.in);
        System.out.println("\n----- Thay đổi mật khẩu!");
        int count = 0;
        do {
            System.out.print("\nNhập mật khẩu cũ: ");
            String oldPass = input.nextLine().trim();
            if (!currentUser.getPassword().equals(oldPass)) {
                count++;
                if (count < 3) {
                    System.out.println("\nSai mật khẩu! Vui lòng nhập lại.");
                    System.out.printf("You have %d trial%s left!\n", 3 - count, 3 - count > 1 ? "s" : "");
                }
                if (count == 3) {
                    System.out.println("\nSai mật khẩu!");
                    System.out.println("Bạn đã nhập sai quá nhiều lần!");
                    updateAccount();
                    break;
                }
                continue;
            }
            do {
                System.out.print("\nNhập mật khẩu mới: ");
                System.out.println("(NOTE: Tối thiểu 8 kí tự, tối thiểu có 1 chữ cái, 1 số và 1 kí tự đặc biệt @$!%*#?&).");
                String newPass = input.nextLine().trim();
                if (newPass.equals(oldPass)) {
                    System.out.println("Đây là mật khẩu cũ, vui lòng nhập mật khẩu khác!\n");
                    continue;
                }
                if (ValidateUtils.isPasswordValid(newPass)) {
                    currentUser.setPassword(newPass);
                    userService.update(currentUser);
                    System.out.println("\nĐã thay đổi mật khẩu thành công!");
                    updateAccount();
                    break;
                }
                System.out.println("Mật khẩu không hợp lệ, vui lòng thử lại!\n");
            } while (true);
            break;
        } while (true);
    }

    public static void showChangeStatus(int number) {
        switch (number) {
            case 2:
                System.out.println("--- Họ và Tên đã thay đổi thành công.\n");
                break;
            case 3:
                System.out.println("--- Ngày sinh đã thay đổi thành công.\n");
                break;
            case 4:
                System.out.println("--- Số điện thoại đã thay đổi thành công.\n");
                break;
            case 5:
                System.out.println("--- Địa chỉ đã thay đổi thành công.\n");
                break;
            case 6:
                System.out.println("--- Email đã thay đổi thành công.\n");
                break;
            case 7:
                System.out.println("--- Tên người dùng đã thay đổi thành công.\n");
                break;
        }
    }

    public static void confirmUpdating() {
        int number = updateAccount();
        if (number == 0) {
            CustomerView.chooseServicesForGuest();
            return;
        }
        if (number == 9) {
            System.out.println("\n===> Tài khoản của bạn đã thay đổi thành công!\n");
            CustomerView.chooseServicesForGuest();
        }
    }
}
