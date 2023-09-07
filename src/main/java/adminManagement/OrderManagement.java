package adminManagement;

import customersServices.PurchaseHistory;
import models.Order;
import models.OrderItem;
import models.Role;
import models.User;
import services.IOrderService;
import services.OrderItemService;
import services.OrderService;
import utils.ValidateUtils;
import views.AdminView;
import views.Menu;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class OrderManagement {

    static Scanner scanner = new Scanner(System.in);

    private static OrderItemService orderItem = new OrderItemService();
    private static IOrderService orderService = new OrderService();

    private static void showActionForm() {
        System.out.println("1. Sắp xếp theo tổng thanh toán.");
        System.out.println("2. Tìm thông tin bằng ID đơn hàng.");
        System.out.println("3. Xem doanh thu ngày.");
        System.out.println("0. Thoát.");
    }

    public static void chooseAction() {
        List<Order> userOrderList = orderService.getOrders();
        do {
            PurchaseHistory.showOrderList(Role.ADMIN, userOrderList);
            showActionForm();
            try {
                int number = Menu.chooseActionByNumber();
                if (number == 1) {
                    sortByTotalPriceASCE(userOrderList);
                }
                if (number == 2) {
                    PurchaseHistory.showOrderDetails(userOrderList);
                    continue;
                }
                if (number == 3){
                    System.out.println( "Tổng doanh thu ngày là : " + ValidateUtils.covertPriceToString(showRevenueOfDay(userOrderList)));
                    continue;
                }
                if (number == 0) {
                    AdminView.chooseAdminAction();
                    break;
                }
            } catch (Exception e) {
                Menu.alert();
            }
        } while (true);
    }

    private static void sortByTotalPriceASCE(List<Order> userOrderList) {
        userOrderList.sort((e1, e2) -> Double.compare(e1.getGrandTotal() - e2.getGrandTotal(), 0));
        showResultAndOperation(userOrderList);
    }

    public static void showResultAndOperation (List<Order> userOrdersList){
        PurchaseHistory.showOrderList(Role.ADMIN, userOrdersList);
        PurchaseHistory.showOrderDetails(userOrdersList);
    }
    public static double showRevenueOfDay(List<Order> orders) {
        System.out.println("Nhập ngày bạn muốn xem doanh thu (yyyy-MM-dd): ");
        String s;
        do {
            String inputDay = scanner.nextLine();
            if (isValidLocalDate(inputDay)) {
                s = inputDay;
                break;
            }
            System.out.println("Vui lòng nhập một ngày hợp lệ theo định dạng yyyy-MM-dd.");
        } while (true);

        LocalDate inputDate = LocalDate.parse(s);
        double total = 0;

        for (Order order : orders) {
            LocalDateTime orderDateTime = order.getCreationTime();
            LocalDate orderDate = orderDateTime.toLocalDate();

            if (orderDate.equals(inputDate)) {
                total += order.getGrandTotal();
            }
        }

        return total;
    }

    public static boolean isValidLocalDate(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate.parse(dateString, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
