package adminManagement;

import customersServices.AccountManagement;
import models.Role;
import models.User;
import services.IUserService;
import services.UserService;
import utils.ValidateUtils;
import views.AdminView;
import views.Menu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;


public class UserInformation {

    private static IUserService userService = new UserService();

    private static List<User> users = userService.getUsers();

    private static void showActionForm() {
        System.out.println("\n ------------- Quản lí người dùng -------------");
        System.out.println("|                                               |");
        System.out.println("|        1. Hiển thị danh sách người dùng.      |");
        System.out.println("|        2. Sắp xếp theo Họ và Tên.             |");
        System.out.println("|        3. Sắp xếp với vai trò ADMIN.          |");
        System.out.println("|        4. Sắp xếp với vai trò User.           |");
        System.out.println("|        5. Tìm kiếm bằng Username.             |");
        System.out.println("|        6. Cập nhật thông tin Username.        |");
        System.out.println("|        0. Quay lại.                           |");
        System.out.println("|                                               |");
        System.out.println(" -----------------------------------------------");
    }

    public static void chooseActionInUsersInfo() {
        do {
            showActionForm();
            try {
                int number = Menu.chooseActionByNumber();
                if (number == 1) {
                    showUsersList(users, 1);
                    break;
                }
                if (number == 2) {
                    showUsersList(users, 2);
                    break;
                }
                if (number == 3) {
                    showUsersList(users, 3);
                    break;
                }
                if (number == 4) {
                    showUsersList(users, 4);
                    break;
                }
                if (number == 5) {
                    searchByFullName(users);
                    break;
                }
                if (number == 6) {
                    updateAdminAccount();
                    break;
                }
                if (number == 0) {
                    AdminView.chooseAdminAction();
                    break;
                }
                Menu.alert();
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
    }


    private static void searchByFullName(List<User> users) {
        boolean is = true;
        do {
            System.out.print("\nNhập tên bạn muốn tìm kiếm: ");
            Scanner input = new Scanner(System.in);
            String searchName = input.nextLine().toLowerCase().trim();
            int count = 0;
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getFullName().toLowerCase().contains(searchName)) {
                    count++;
                    if (count == 1) {
                        System.out.println("\n ------------------------------------------------------------------------------------USERS LIST----------------------------------------------------------------------------------------------------");
                        System.out.printf("%-12s %-28s %-20s %-20s %-25s %-25s %-22s %-10s %-10s\n", "ID", "Full Name", "Date of Birth", "Phone Number", "Email", "Address", "Username", "Role", "Creation Date");
                        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    }
                    showOneUser(users.get(i));
                }
                if (count > 0 && i == users.size() - 1) {
                    is = true;
                    System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                }
            }
            if (count == 0) {
                System.out.printf("\nKhông tìm thấy tài khoản với tên '%s'. Bạn có muốn thử lại?\n", searchName);
                do {
                    System.out.println("(Nhập 'y' để thử lại hoặc 'n' để thoát)");
                    try {
                        String letter = Menu.chooseActionByLetter();
                        if (letter.charAt(0) == 'y' && letter.length() == 1) {
                            is = false;
                            break;
                        }
                        if (letter.charAt(0) == 'n' && letter.length() == 1) {
                            is = true;
                            chooseActionInUsersInfo();
                            break;
                        }
                        Menu.alert();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Menu.alert();
                    }
                } while (true);
            }
        } while (!is);
        showReturningChoice();
    }

    private static void showReturningChoice() {
        do {
            System.out.print("Nhập '0' để quay lại : ");
            try {
                Scanner input = new Scanner(System.in);
                int number = input.nextInt();
                if (number == 0) {
                    chooseActionInUsersInfo();
                    break;
                }
                Menu.alert();
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
    }

    private static void showOneUser(User user) {
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss a");
        String creationDateFormat = ValidateUtils.convertLocalDateTimeToString(user.getCreationTime());
        System.out.printf("%-12d %-17s%-11s %-20s %-20s %-25s %-25s %-22s %-10s %-10s\n", user.getId(), user.getLastName(), user.getFirstName(), user.getBirthday(), user.getPhoneNumber(), user.getEmail(), user.getAddress(), user.getUserName(), user.getRole(), creationDateFormat);
    }

    private static void showUsersList(List<User> users, int choice) {
        System.out.println("\nUSERS LIST ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-12s %-28s %-20s %-20s %-25s %-25s %-22s %-10s %-10s\n", "ID", "Full Name", "Date of Birth", "Phone Number", "Email", "Address", "Username", "Role", "Creation Date");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        switch (choice) {
            case 1:
                showAllUsers(users);
                break;
            case 2:
                sortByNameASCE(users);
                break;
            case 3:
                filterByAdmin(users);
                break;
            case 4:
                filterByUser(users);
                break;
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        showReturningChoice();
    }

    private static void filterByAdmin(List<User> users) {
        int count = 0;
        for (User user : users) {
            if (user.getRole() == Role.ADMIN) {
                showOneUser(user);
                count++;
            }
        }
        System.out.printf("\n---> Tổng: %d ADMIN %s\n", count, count > 1 ? "s" : "");
    }

    private static void filterByUser(List<User> users) {
        int count = 0;
        for (User user : users) {
            if (user.getRole() == Role.USER) {
                showOneUser(user);
                count++;
            }
        }
        System.out.printf("\n---> Tổng: %d người dùng %s\n", count, count > 1 ? "s" : "");
    }

    private static void sortByNameASCE(List<User> users) {
        users.sort((e1, e2) -> Integer.compare(e1.getFirstName().compareTo(e2.getFirstName()), 0));
        showAllUsers(users);
    }

    private static void showAllUsers(List<User> users) {
        for (User user : users){
        showOneUser(user);}
    }


    public static void updateAdminAccount() {
        int number = AccountManagement.updateAccount();
        if (number == 0) {
            chooseActionInUsersInfo();
            return;
        }
        System.out.println("\n===> Tài khoản đã cập nhật thành công!\n");
        chooseActionInUsersInfo();
    }

}
