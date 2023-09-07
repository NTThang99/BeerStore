package views;

import customersServices.AccountManagement;
import customersServices.BeerBuy;
import customersServices.PurchaseHistory;

public class CustomerView {

    public static void showServicesForm() {
        System.out.println("\n*---------------------------------------------------*");
        System.out.println("|                                                   |");
        System.out.println("|                 <<  USER  >>                      |");
        System.out.println("|                                                   |");
        System.out.println("|   1. Đặt hàng.                                    |");
        System.out.println("|   2. Tìm kiếm lịch sử đặt hàng.                   |");
        System.out.println("|   3. Cập nhật tài khoản.                          |");
        System.out.println("|   0. Về trang chủ.                                |");
        System.out.println("|                                                   |");
        System.out.println("*---------------------------------------------------*\n");
    }
    public static void chooseServicesForGuest() {
        do {
            showServicesForm();
            try {
                int number = Menu.chooseActionByNumber();


                if (number == 1) {
                    BeerBuy.setInformation();
                    break;
                }
                if (number == 2) {
                    PurchaseHistory.chooseAction();
                    break;
                }
                if (number == 3) {
                    AccountManagement.confirmUpdating();
                    break;
                }
                if (number == 0) {
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
