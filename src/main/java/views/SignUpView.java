package views;
import services.IUserService;
import services.UserService;
import adminManagement.UserInformation;
import models.Role;
import models.User;
import utils.ValidateUtils;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class SignUpView {

    private final IUserService signUpService;

    public SignUpView() {
        signUpService = new UserService();
    }

    private final static String ADMIN_CODE = "admin";

    private static final Scanner input = new Scanner(System.in);

    public void createAccount() {
        System.out.println("\n------------------ \u001B[33mĐăng kí tài khoản\u001B[0m -----------------------\n");
        User newUser = new User();
        try {
            setID(newUser);
            enterFullName(newUser);
            enterBirthday(newUser);
            enterPhoneNumber(newUser);
            enterAddress(newUser);
            enterEmail(newUser);
            enterUserName(newUser);
            enterPassword(newUser);
            setUpRole(newUser);
            confirmSignUp(newUser);
        } catch (Exception ex) {
            ex.printStackTrace();
            Menu.alert();
        }
    }

    public void showConfirmForm() {
        System.out.println("+----------------------------------------------------------------+");
        System.out.println("|Vui lòng xác nhận bạn muốn đăng kí với những thông tin trên!    |");
        System.out.println("+----------------------------------------------------------------+");
        System.out.println("| 1. Xác nhận đăng ký.                                           |");
        System.out.println("+----------------------------------------------------------------+");
        System.out.println("| 2. Về trang chủ.                                               |");
        System.out.println("+----------------------------------------------------------------+");
        System.out.println("| 0. Thoát.                                                      |");
        System.out.println("+----------------------------------------------------------------+");
    }

    public void confirmSignUp(User newUser) {
        do {
            showConfirmForm();
            int number = Menu.chooseActionByNumber();
            if (number == 1) {
//                DateTimeFormatter fmt3 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                //newUser.setCreationTime(LocalDateTime.parse(LocalDateTime.now(), fmt3));
                LocalDateTime local = LocalDateTime.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth(),LocalDateTime.now().getHour(),LocalDateTime.now().getMinute());
                newUser.setCreationTime(local);
                signUpService.add(newUser);
                showSuccessfulMessage();
                break;
            }
            if (number == 2) {
                Menu.menu();
                break;
            }
            if (number == 0) {
                System.exit(0);
                break;
            }
            Menu.alert();
        } while (true);
    }

    public void showNextAction() {
        System.out.println("+-------------------------+");
        System.out.println("| 1. Đăng nhập.           |");
        System.out.println("+-------------------------+");
        System.out.println("| 2. Tạo tài khoản khác.  |");
        System.out.println("+-------------------------+");
        System.out.println("| 0. Thoát.               |");
        System.out.println("+-------------------------+");
    }

    public void showSuccessfulMessage() {
        System.out.println("\nĐăng kí thành công!\n");
        do {
            showNextAction();
            int number = Menu.chooseActionByNumber();
            if (number == 1) {
                LoginView.signIn();
                break;
            }
            if (number == 2) {
                createAccount();
                break;
            }
            if (number == 0) {
                System.exit(0);
                break;
            }
            Menu.alert();
        } while (true);
    }

    public void setID(User newUser) {
        int min = 100000;
        int max = 999999;
        long id;
        do {
            id = (long) Math.floor(Math.random() * (max - min + 1) + min);
        } while (signUpService.isIdExisted(id));
        newUser.setId(id);
        System.out.println("\n1. ID tài khoản của bạn sẽ là: " + id);
    }

    public boolean cancelEntering(String string) {

        switch (string.toLowerCase()) {
            case Exit.E1:
                System.out.println("\n-----> Đã dừng tiến trình đăng nhập!");
                Menu.menu();
                return true;
            case Exit.E2:
                System.out.println("\n-----> Đã dừng tiến trình chỉnh sửa!");
                CustomerView.chooseServicesForGuest();
                return true;
            case Exit.E3:
                System.out.println("\n-----> Đã dừng tiến trình chỉnh sửa!");
                UserInformation.chooseActionInUsersInfo();
                return true;
        }
        return false;
    }

    public boolean enterFullName(User newUser) {
        do {
            System.out.println("\n2. Nhập Họ và Tên ");
            System.out.println("(Chú ý: Tên không được chứa số và kí tự đặc biệt!!!!!)");
            System.out.print("==> ");
            String fullName = input.nextLine().trim();
            System.out.println();
            if (fullName.equals("")) {
                System.out.println("Họ và tên không được bỏ trống, vui lòng nhập họ và tên của bạn!!!");
                continue;
            }
            if (ValidateUtils.isNameValid(fullName)) {
                newUser.setFullName(fullName);
                return false;
            }
            if (cancelEntering(fullName)) return true;
            System.out.println("Họ và Tên không hợp lệ, vui lòng nhập lại!!!\n");
        } while (true);
    }

    public boolean enterBirthday(User newUser) {
        do {
            System.out.println("\n3. Nhập ngày sinh (Ví dụ: 10/05/1999) ");

            System.out.print("==> ");
            String birthday = input.nextLine().trim();
            System.out.println();
            if (cancelEntering(birthday)) return true;
            if (birthday.equals("")) {
                System.out.println("Bắt buộc phải nhập ngày sinh!");
                continue;
            }
            if (!ValidateUtils.isDateValid(birthday)) {
                System.out.println("Ngày sinh không hợp lệ, vui lòng nhập lại!!!\n");
                continue;
            }

            newUser.setBirthday(birthday);
            return false;
        } while (true);
    }

    public boolean enterPhoneNumber(User newUser) {
        do {
            System.out.println("\n4. Nhập số điện thoại (Ví dụ: 0905552415). ");
            System.out.println("(Note: Số đầu tiên phải là '0', số thứ hai từ '1' đến '9' và có độ dài từ 10 - 11 chữ số)");
            System.out.print("==> ");
            String phoneNumber = input.nextLine().trim();
            System.out.println();
            if (phoneNumber.equals("")) {
                System.out.println("Bắt buộc phải nhập SĐT");
                continue;
            }
            if (cancelEntering(phoneNumber)) return true;
            if (!ValidateUtils.isPhoneValid(phoneNumber)) {
                System.out.println("SĐT không hợp lệ, vui lòng nhập lại!!!\n");
                continue;
            }
            if (signUpService.checkExistedPhone(phoneNumber)) {
                System.out.println("SĐT đã tồn tại, vui lòng nhập số khác!!!\n");
                continue;
            }
            newUser.setPhoneNumber(phoneNumber);
            return false;
        } while (true);
    }

    public boolean enterAddress(User newUser) {
        do {
            System.out.println("\n5. Nhập địa chỉ (Ví dụ: 28 Nguyen Tri Phuong), không bao gồm các kí tự '~'.");
            System.out.print("==> ");
            String address = input.nextLine().trim();
            if (cancelEntering(address)) return true;
            if (!ValidateUtils.isAddressValid(address)) {
                System.out.println("Địa chỉ không hợp lệ, vui lòng nhập lại\n");
                continue;
            }
            newUser.setAddress(address);
            return false;
        } while (true);
    }

    public boolean enterEmail(User newUser) {
        do {
            System.out.println("\n6. Nhập địa chỉ email  (Ví dụ: nguyenthang123@gmail.com). ");
            System.out.print("==> ");
            String email = input.nextLine().trim().toLowerCase();
            if (email.equals("")) {
                System.out.println("Bắt buộc phải nhập email!");
                continue;
            }
            if (cancelEntering(email)) return true;
            if (!ValidateUtils.isEmailValid(email)) {
                System.out.println("Địa chỉ email không hợp lệ, vui lòng nhập lại!\n");
                continue;
            }
            if (signUpService.checkExistedEmail(email)) {
                System.out.println("Địa chỉ email đã tồn tại, vui lòng nhập email khác!\n");
                continue;
            }
            newUser.setEmail(email);
            return false;
        } while (true);
    }

    public boolean enterUserName(User newUser) {
        do {
            System.out.println("\n7. Nhập tên đăng nhập:");
            System.out.println("NOTE: ");
            System.out.println("  * Tên đăng nhập phải bắt đầu bằng chữ cái.");
            System.out.println("  * Tất cả kí tự có thể là chữ cái, số hoặc chứa dấu '_'.");
            System.out.println("  * Độ dài từ 8 - 20 kí tự.");
            System.out.print("==> ");
            String username = input.nextLine();
            System.out.println();
            if (username.equals("")) {
                System.out.println("Bắt buộc phải nhập tên người dùng!\n");
                continue;
            }
            if (cancelEntering(username)) return true;
            if (!ValidateUtils.isUsernameValid(username)) {
                System.out.println("Tên đăng nhập không hợp lệ, vui lòng nhập lại!!!\n");
                continue;
            }
            if (signUpService.checkExistedUserName(username)) {
                System.out.println("Tên đăng nhập đã tồn tại, vui lòng nhập tên khác!!!\n");
                continue;
            }
            newUser.setUserName(username);
            return false;
        } while (true);
    }

    public void enterPassword(User newUser) {
        do {
            System.out.println("\n8. Nhập mật khẩu (Ví dụ:thang!010)");
            System.out.println("(NOTE: Tối thiểu 8 kí tự, tối thiểu có 1 chữ cái, 1 số và 1 kí tự đặc biệt @$!%*#?&).");
            System.out.print("==> ");
            String password = input.nextLine().trim();
            System.out.println();
            if (password.equals("")) {
                System.out.println("Mật khẩu không được bỏ trống, nhập lại mật khẩu!!!");
                continue;
            }
            if (ValidateUtils.isPasswordValid(password)) {
                newUser.setPassword(password);
                break;
            }
            if (cancelEntering(password)) break;
            System.out.println("Mật khẩu không hợp lệ, vui lòng nhập lại!!!\n");
        } while (true);
    }

    private void setUpRole(User newUser) {
        System.out.println("\n9. Cài đặt quyền hạn:\n");
        System.out.println("---> Bạn có phải là quản lí không?");
        System.out.println("(Enter 'y' or 'n')\n");
        boolean check;
        do {
            String letter = Menu.chooseActionByLetter();
            if (letter.charAt(0) == 'y') {
                check = checkAdminCode();
                break;
            }
            if (letter.charAt(0) == 'n') {
                check = false;
                break;
            }
            Menu.alert();
        } while (true);
        if (check) newUser.setRole(Role.ADMIN);
        else newUser.setRole(Role.USER);
    }

    private boolean checkAdminCode() {
        System.out.print("Nhập mã của ADMIN: ");
        String code = input.nextLine().trim();
        if (code.equals(ADMIN_CODE)) {
            System.out.println("Vai trò của bạn là ADMIN!\n");
            return true;
        }
        do {
            System.out.println("Mã không hợp lệ, bạn có muốn thử lại?");
            System.out.println("(Nhập 'y' hoặc 'n')\n");
            String letter = Menu.chooseActionByLetter();
            switch (letter.charAt(0)) {
                case 'y':
                    return checkAdminCode();
                case 'n':
                    return false;
                default:
                    Menu.alert();
            }
        } while (true);
    }

}

