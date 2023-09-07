package customersServices;

import models.Order;
import models.Role;
import services.IOrderItemService;
import services.IOrderService;
import services.OrderItemService;
import services.OrderService;
import utils.ValidateUtils;
import views.CustomerView;
import views.LoginView;
import views.Menu;

import java.util.List;
import java.util.Scanner;

public class PurchaseHistory {
    private static IOrderService orderService = new OrderService();
    private static IOrderItemService orderItemService = new OrderItemService();
    private static final Scanner input = new Scanner(System.in);

    public static void showActionForm() {
        System.out.println("1. Xem thông tin bằng ID đơn hàng.");
        System.out.println("0. Thoát.");
    }

    public static void showOrderList(Role role, List<Order> userOrderList) {
        System.out.println("\n╔----------------------------------------------------------Danh sách đơn hàng---------------------------------------------------------------------╗");
        System.out.printf("|%-17s %-17s %15s    %-25s %-23s %-20s %-19s|\n", "ID Đơn hàng", "Ngày thanh toán", role == Role.ADMIN ? "ID tài khoản" : "",
                "Họ và Tên", "Số điện thoại", "Địa chỉ", "Tổng đơn hàng (VND)");
        System.out.println("|-------------------------------------------------------------------------------------------------------------------------------------------------|");
        for (Order order : userOrderList) {
            String   creationDate  = ValidateUtils.convertLocalDateTimeToString(order.getCreationTime());
            System.out.printf("|%-17s %-17s %15s    %-25s %-23s %-20s %-19s|\n", order.getId(), creationDate, role == Role.ADMIN ? order.getUserId() + "    " : "",
                    order.getName(), order.getPhoneNumber(), order.getAddress(), ValidateUtils.covertPriceToString(order.getGrandTotal()));
        }
        System.out.println("╚-------------------------------------------------------------------------------------------------------------------------------------------------╝");    }

    public static void showOrderDetails(List<Order> userOrderList) {
        do {
            try {
                System.out.print("\nNhập ID đơn hàng bạn muốn kiểm tra (Nhập '0' để thoát).\n");
                System.out.print("---> ");
                long orderID = Long.parseLong(input.nextLine());
                if (orderID == 0) return;
                Order order = orderService.getOrderById(orderID, userOrderList);
                if (order == null) {
                    System.out.println("Sai ID đơn hàng. Vui lòng nhập lại!");
                    continue;
                }
                BeerBuy.showbill(order, orderItemService.getUserOrderItemList(orderID));
            } catch (Exception ex) {
                System.out.print("ID không hợp lệ, vui lòng nhập lại!");
            }
        } while (true);
    }

    public static void chooseAction() {
        List<Order> userOrdersList = orderService.getUserOrdersList(LoginView.getUserID());
        do {
            if (userOrdersList.size() == 0) {
                System.out.println("\n----- Bạn không có đơn đặt hàng nào!");
                CustomerView.chooseServicesForGuest();
                break;
            }
            showOrderList(Role.USER, userOrdersList);
            showActionForm();
            try {
                int number = Menu.chooseActionByNumber();
//                if (number == 1) {
//                    searchOrder(Role.USER, userOrdersList);
//                    continue;
//                }
                if (number == 1) {
                    showOrderDetails(userOrdersList);
                    continue;
                }
                if (number == 0) {
                    CustomerView.chooseServicesForGuest();
                    break;
                }
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
    }

    private static void searchOrder(Role role, List<Order> userOrdersList) {
        do {
            System.out.print("\nNhập thông tin mà bạn muốn tìm kiếm (Nhập '0' để thoát).\n");
            System.out.print("---> ");
            String searchContent = input.nextLine().trim().toLowerCase();
            if (searchContent.equals("0")) return;
            List<Order> searchList = orderService.getSearchOrderList(searchContent, userOrdersList);
            if (searchList.size() == 0) {
                System.out.println("Không tìm thấy đơn hàng với thông tin bạn cung cấp!");
                continue;
            }
            showOrderList(role, searchList);
            showOrderDetails(searchList);
        } while (true);
    }
}

