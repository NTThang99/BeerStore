package customersServices;

import adminManagement.BeerManagement;
import models.Beer;
import models.Order;
import models.OrderItem;
import models.User;
import services.*;
import utils.CSVUtils;
import utils.ValidateUtils;
import views.CustomerView;
import views.Exit;
import views.LoginView;
import views.Menu;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class BeerBuy {
    private static IBeerService beerService = new BeerService();
    private static IUserService userService = new UserService();
    private static IOrderService orderService = new OrderService();
    private static IOrderItemService orderItemService = new OrderItemService();
    private static final Scanner input = new Scanner(System.in);

    public static void setInformation() {
        do {
            Order newOrder = new Order();
            System.out.println("\n!Trước khi đặt hàng, chúng tôi cần bạn cung cấp thông tin cá nhân!\n");
            System.out.println("     * Nhập '1' để sử dụng thông tin hiện tại.");
            System.out.println("     * Nhập '2' để sửa đổi thông tin hiện tại.");
            System.out.println("     * Nhập '0' to exit.");
            try {
                int number = Menu.chooseActionByNumber();
                if (number == 1) {
                    setNewOrderWithOtherInfo(newOrder);
                }
                if (number == 2) {
                    setNewOrderWithDefaultInfo(newOrder);
                }
                if (number == 0) {
                    CustomerView.chooseServicesForGuest();
                    break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Menu.alert();
            }

            if (newOrder.getName() == null || newOrder.getPhoneNumber() == null || newOrder.getAddress() == null) {
                System.out.println("\nKhông có thông tin, vui lòng thử lại!\n");
                continue;
            }

            List<Beer> beers = beerService.getBeers();
            ArrayList<OrderItem> orderItemList = showHowToGetBeer(newOrder, beers);
            if (orderItemList.size() == 0) {
                System.out.println("\n----- Không có mặt hàng nào trong giỏ hàng của bạn. Vui lòng thử lại!");
                continue;
            }
            if (orderItemList.size() > 0) {
                System.out.println("\n--- Thêm toàn bộ mặt hàng vào giỏ hàng thành công ---");
                System.out.println("*** Nhập '1' để in ra  hóa đơn của bạn.");
                System.out.println("*** Nhập '0' để xóa giỏ hàng hiện tại và quay lại đặt hàng.");
            }
            int num = Integer.parseInt(input.nextLine());
            switch (num) {
                case 1:
                    showbill(newOrder, orderItemList);
                    break;
                case 0:
                    continue;
            }
            try {
                do {
                    System.out.println("\n---> Vui lòng xác nhận thanh toán cho đơn hàng!");
                    System.out.println("(Nhập '1' để xác nhận hoặc '0' để thoát)");
                    int letter = Integer.parseInt(input.nextLine());
                    if (letter == 1) {
                        System.out.println("\n----- Thanh toán thành công. Xin cảm ơn! -----");
                        orderService.add(newOrder);
                        orderItemService.addMoreOrderItems(orderItemList);
                        CSVUtils.writeData("C:\\BeerStore\\src\\main\\java\\data\\beers.csv", beers);
                        CustomerView.chooseServicesForGuest();
                        break;
                    }
                    if (letter == 2) {
                        CustomerView.chooseServicesForGuest();
                        break;
                    }
                } while (true);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Menu.alert();
            }
        } while (true);
    }

    private static void setNewOrderWithOtherInfo(Order newOrder) {
        System.out.println("\n----- Thông tin của bạn đã được thêm -----");
        User currentUser = userService.getUserById(LoginView.getUserID());
        newOrder.setId(System.currentTimeMillis() % 1000);
        newOrder.setUserId(currentUser.getId());
        newOrder.setName(currentUser.getFullName());
        newOrder.setPhoneNumber(currentUser.getPhoneNumber());
        newOrder.setAddress(currentUser.getAddress());

        LocalDateTime local = LocalDateTime.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth(),LocalDateTime.now().getHour(),LocalDateTime.now().getMinute());

        newOrder.setCreationTime(local);
    }

    private static void setNewOrderWithDefaultInfo(Order newOrder) {
        System.out.println("\n----- Vui lòng điền đầy đủ những thông tin bên dưới -----");
        System.out.println("\n(Nhập 'ex5' tại bất kì bước nào để kết thúc)");
        String name = enterFullName();
        if (name == null) return;
        String phone = enterPhoneNumber();
        if (phone == null) return;
        String address = enterAddress();
        if (address == null) return;

        newOrder.setId(System.currentTimeMillis());
        newOrder.setUserId(LoginView.getUserID());
        newOrder.setName(name);
        newOrder.setPhoneNumber(phone);
        newOrder.setAddress(address);
        LocalDateTime local = LocalDateTime.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth(),LocalDateTime.now().getHour(),LocalDateTime.now().getMinute());

        newOrder.setCreationTime(local);
    }

    private static String enterAddress() {
        System.out.println("3. Nhập địa chỉ (Ví dụ: 28 Nguyen Tri Phuong).");
        System.out.print("==> ");
        String address = input.nextLine().trim();
        while (address.equals("")) {
            if (cancelEntering(address)) return null;
            System.out.println("Bắt buộc nhập địa chỉ, vui lòng thử lại!\n");
            System.out.print("==> ");
        }
        return address;
    }

    private static String enterPhoneNumber() {
        String phone;
        System.out.println("2. Nhập SĐT.");
        System.out.println("(Note: Số đầu tiên là '0', số thứ hai từ '1' đến '9' và có độ dài từ 10 - 11 chữ số)");
        System.out.print("==> ");
        while (!ValidateUtils.isPhoneValid(phone = input.nextLine().trim())) {
            if (cancelEntering(phone)) return null;
            System.out.println("SĐT không hợp lệ, vui lòng nhập lại!\n");
            System.out.println("2. Nhập SĐT.");
            System.out.println("(Note: Số đầu tiên là '0', số thứ hai từ '1' đến '9' và có độ dài từ 10 - 11 chữ số)");
            System.out.print("==> ");
        }
        return phone;
    }

    private static String enterFullName() {
        String fullName;
        System.out.println("1. Nhập Họ và Tên (Ví dụ: Nguyen Van An).");
        System.out.print("==> ");
        while (!ValidateUtils.isNameValid(fullName = input.nextLine().trim())) {
            if (cancelEntering(fullName)) return null;
            System.out.println("Họ và Tên không hợp lệ, vui lòng nhập lại!\n");
            System.out.println("1. Nhập Họ và Tên (Ví dụ: Nguyen Van An).");
            System.out.print("==> ");
        }
        return fullName;
    }
    private static boolean cancelEntering(String string) {
        if (Exit.E5.equalsIgnoreCase(string)) {
            System.out.println("\n-----> Đã bỏ qua tiến trình!");
            return true;
        }
        return false;
    }

    public static void showbill(Order newOrder, List<OrderItem> orderItemList) {
        System.out.println("\nBEER BILL --------------------------------------------------------------------------");
        System.out.printf("\n%-20s %-15s %-13s %-15s\n\n", "Tên mặt hàng", "Gía tiền (VND)", "Số lượng", "Tổng tiền (VND)");
        for (OrderItem orderItem : orderItemList) {
            System.out.printf("%-20s %-15s %-13s %-15s\n", orderItem.getBeerName(), ValidateUtils.priceWithDecimal(orderItem.getPricePerPill()),
                    orderItem.getQuantity(), ValidateUtils.covertPriceToString(orderItem.getTotalPrice()));
        }
        System.out.printf("\n%-52s %s %s\n", "", "Tổng thanh toán (VND):", ValidateUtils.covertPriceToString(newOrder.getGrandTotal()));
        System.out.println("Thông tin khách hàng:");
        System.out.println("     * Họ và Tên: " + newOrder.getName());
        System.out.println("     * Số điện thoại: " + newOrder.getPhoneNumber());
        System.out.println("     * Địa chỉ: " + newOrder.getAddress());
        System.out.println("Ngày thanh toán: " +newOrder.getCreationTime());
        System.out.println("\n---------------------------------------------------------------------------------------\n");
    }

    public static ArrayList<OrderItem> showHowToGetBeer(Order newOrder, List<Beer> beers) {
        ArrayList<OrderItem> orderItemList = new ArrayList<>();
        do {
            System.out.println("\n** 1. Đặt hàng bằng cách tìm kiếm ID **");
            System.out.println("\n** 0. Kết thúc **");
            try {
                int number = Menu.chooseActionByNumber();
                if (number == 1) {
                    orderItemList = getBeersBoughtByID(newOrder, beers);
                    break;
                }
                if (number == 2) {
                    orderItemList = searchBeerByName(newOrder, beers);
                    break;
                }
                if (number == 0) {
                    break;
                }
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
        return orderItemList;
}

    private static ArrayList<OrderItem> searchBeerByName(Order newOrder, List<Beer> beers) {
        ArrayList<OrderItem> newOrderItemList = new ArrayList<>();
        do {
            System.out.println("\nNhập tên mặt hàng bạn muốn mua (Nhập '0' để kết thúc). ");
            System.out.print("---> ");
            String beerNameSearch = input.nextLine().trim().toLowerCase();
            if (beerNameSearch.equals("0")) return newOrderItemList;
            List<Beer> beerListSearch = new ArrayList<>();
            for (Beer beer : beers) {
                if (beer.getBeerName().toLowerCase().contains(beerNameSearch)) {
                    beerListSearch.add(beer);
                }
            }
            if (beerListSearch.size() > 0) {
                newOrderItemList.addAll(getBeersBoughtByID(newOrder, beerListSearch));
                changeQuantityAfterGetting(beers, beerListSearch);
                continue;
            }
            System.out.printf("\n'%s' không tìm thấy. Vui lòng thử lại!\n", beerListSearch);
        } while (true);
    }



    private static void changeQuantityAfterGetting(List<Beer> beers, List<Beer> beerListSearch) {
        for (Beer beerSearch : beerListSearch) {
            for (Beer beer : beers) {
                if (beerSearch.getId() == beer.getId()) {
                    beer.setQuantity(beerSearch.getQuantity());
                    break;
                }
            }
        }
    }

    public static boolean checkQuantity(int quantity, int beerId) {
        Beer beer = beerService.getBeerById(beerId);
        if (quantity <= beer.getQuantity()){
            return true;
        }
        return false;
    }


    private static ArrayList<OrderItem> getBeersBoughtByID(Order newOrder, List<Beer> beers) {
    ArrayList<OrderItem> beersBought = new ArrayList<>();
    boolean stopBuying;
    do {
        BeerManagement.showAllBeers(beers);
        Beer availableBeer = getAvailableBeer();

        if (availableBeer == null) return beersBought;

        int quantityBuy = getQuantityBuy(beers, availableBeer);

        beersBought.add(getNewOrderBeer(availableBeer, quantityBuy));
        System.out.printf("\n---> '%s' - %d đã được thêm vào giỏ hàng.\n", availableBeer.getBeerName(), quantityBuy);
        modifyOrderItemList(beersBought);

        showDrugsGot(newOrder, beersBought);
        stopBuying = confirmContinuing();
    } while (!stopBuying);
        return beersBought;
    }

    private static boolean confirmContinuing() {

        do {
            System.out.println("\nBạn có muốn đặt hàng thêm không?");
            System.out.println("(Nhập 'y' để tiếp tục hoặc 'n' để dừng lại)");
            String letter = Menu.chooseActionByLetter();
            if (letter.equals("y")) return false;
            if (letter.equals("n")) return true;

            Menu.alert();
        } while (true);
    }

    private static void showDrugsGot(Order newOrder, List<OrderItem> orderItemList) {
        System.out.print("\n---------------------------------------------------------------------------------------");
        showDrugFromGetting(newOrder, orderItemList);
        System.out.println("---------------------------------------------------------------------------------------\n");
    }

    private static void showDrugFromGetting(Order newOrder, List<OrderItem> orderItemList) {
        System.out.printf("\n%-20s %-15s %-13s %-15s\n\n", "Tên mặt hàng", "Gía tiền (VND)", "Số lượng", "Tổng tiền (VND)");
        double grandTotal = 0;
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderID(newOrder.getId());
            orderItem.setCreationTime(newOrder.getCreationTime());
            double total = orderItem.getTotalPrice();
            System.out.printf("%-20s %-15s %-13s %-15s\n", orderItem.getBeerName(),
                    ValidateUtils.priceWithDecimal(orderItem.getPricePerPill()), orderItem.getQuantity(), ValidateUtils.priceWithDecimal(total));
            grandTotal += total;
        }
        newOrder.setGrandTotal(grandTotal);

        System.out.printf("\n%-52s %s %s\n", "", "Tổng tiền (VND):", ValidateUtils.priceWithDecimal(grandTotal));
    }

    private static void modifyOrderItemList(ArrayList<OrderItem> orderItemList) {
        for (int i = 0; i < orderItemList.size() - 1; ) {
            OrderItem orderItem1 = orderItemList.get(i);
            OrderItem orderItem2 = orderItemList.get(i + 1);
            if (orderItem1.getBeerName().equals(orderItem2.getBeerName())) {
                orderItem1.setQuantity(orderItem1.getQuantity() + orderItem2.getQuantity());
                orderItemList.remove(i + 1);
                continue;
            }
            i++;
        }
    }

    private static int getQuantityBuy(List<Beer> beers, Beer availableBeer) {
        int quantityBuy;
        do {
            try {
                System.out.print("\nNhập số lượng mà bạn muốn mua: ");
                quantityBuy = Integer.parseInt(input.nextLine());
                if (quantityBuy <= 0) {
                    System.out.println("Bạn không thể nhập số bé hơn 0 hoặc bằng 0. Vui lòng nhập lại!\n");
                    continue;
                }
                int oldQuantity = availableBeer.getQuantity();
                if (quantityBuy > oldQuantity) {
                    System.out.println("\n---> Không đủ số lượng. Vui lòng nhập số nhỏ hơn '" + oldQuantity + "'.\n");
                    continue;
                }
                int quantityLeft = oldQuantity - quantityBuy;
                for (int i = 0; i < beers.size(); i++){
                    if (beers.get(i).getId() == availableBeer.getId()){
                        availableBeer.setQuantity(quantityLeft);
                        beers.get(i).setQuantity(quantityLeft);
                    }
                }
                break;
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
        return quantityBuy;
    }

    private static OrderItem getNewOrderBeer(Beer availableBeer, int quantityBuy) {
        OrderItem newOrderBeer = new OrderItem();
        newOrderBeer.setId(System.currentTimeMillis() % 1000);
        newOrderBeer.setBeerID(availableBeer.getId());
        newOrderBeer.setBeerName(availableBeer.getBeerName());
        newOrderBeer.setPricePerPill(availableBeer.getPricePerPill());
        newOrderBeer.setQuantity(quantityBuy);
//        newOrderBeer.setTotalPrice();
        return newOrderBeer;

    }

    private static Beer getAvailableBeer() {
        Beer availableBeer;
        do {
            try {
                System.out.println("\nNhập ID mặt hàng bạn muốn mua (Nhập '0' để kết thúc).");
                System.out.print("---> ");
                int beerBoughtID =  Integer.parseInt(input.nextLine());
                if (beerBoughtID == 0) return null;
                availableBeer = beerService.getBeerById(beerBoughtID);
                if (availableBeer == null) {
                    System.out.println("Mặt hàng này đã hết vui lòng mua mặt hàng khác. Vui lòng nhập lại!\n");
                    return getAvailableBeer();
                }
                System.out.printf("Beer Name: %s \n", availableBeer.getBeerName());
                break;
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
        return availableBeer;
    }
}

