package adminManagement;

import models.Beer;
import models.Order;
import services.BeerService;
import services.IBeerService;
import services.IOrderItemService;
import services.OrderItemService;
import utils.ValidateUtils;
import views.AdminView;
import views.Exit;
import views.Menu;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static customersServices.AccountManagement.showChangeStatus;

public class BeerManagement {
    private static IBeerService beerService = new BeerService();
    private static IOrderItemService orderItemService = new OrderItemService();
    private static final Scanner input = new Scanner(System.in);
    private static final int YEARS = 5;

    private static void showActionForm() {
        System.out.println("\u001B[35m╔══════════════════════════════════════════════════════════╗\u001B[0m");
        System.out.println("\u001B[35m                       QUẢN LÍ HÀNG HÓA                          \u001B[35m");
        System.out.println("\u001B[35m                                                     \u001B[0m");
        System.out.println("\u001B[35m            1. Hiển thị danh sách mặt hàng.             \u001B[0m");
        System.out.println("\u001B[35m            2. Sắp xếp theo số lượng.                    \u001B[0m");
        System.out.println("\u001B[35m            3. Sắp xếp theo giá tiền.                    \u001B[0m");
        System.out.println("\u001B[35m            4. Hiển thị loại bia đã hết hạn sử dụng.     \u001B[0m");
        System.out.println("\u001B[35m            5. Kiểm tra thông tin bia bằng ID.           \u001B[0m");
        System.out.println("\u001B[35m            6. Tìm kiếm bia theo tên.                    \u001B[0m");
        System.out.println("\u001B[35m            7. Thêm bia mới.                             \u001B[0m");
        System.out.println("\u001B[35m            0. Thoát.                                    \u001B[0m");
        System.out.println("\u001B[35m                                                     \u001B[0m");
        System.out.println("\u001B[35m╚══════════════════════════════════════════════════════════╝\u001B[0m");
    }

    public static void chooseActionInMedicineManagement() {
        List<Beer> beers = beerService.getBeers();
        do {
            showActionForm();
            try {
                int number = Menu.chooseActionByNumber();
                if (number == 1) {
                    showBeersList(beers, 1);
                    break;
                }
                if (number == 2) {
                    showBeersList(beers, 2);
                    break;
                }
                if (number == 3) {
                    showBeersList(beers, 3);
                    break;
                }
                if (number == 4) {
                    showBeersList(beers, 4);
                    break;
                }
                if (number == 5) {
                    updateBeer();
                    break;
                }
                if (number == 6) {
                    searchBeersByName(beers);
                    break;
                }
                if (number == 7) {
                    addNewBeer();
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

    private static void addNewBeer() {
        System.out.println("\n\n----- Thêm mặt hàng mới -----\n");
        Beer newBeer = new Beer();
        try {
            setID(newBeer);
            do {
                enterBeerName(newBeer);
                enterAlcoholConcentration(newBeer);
                enterQuantity(newBeer);
                enterPricePerPill(newBeer);
                enterProductionDate(newBeer);
                enterExpirationDate(newBeer);
                enterNote(newBeer);
            } while (!checkDuplicateBeer(newBeer));
            confirmAddingNewBeer(newBeer);
        } catch (Exception ex) {
            Menu.alert();
        }
    }

    private static boolean enterAlcoholConcentration(Beer newBeer) {
        do {
            try {
                System.out.println("4. Nhập nồng độ cồn: "  + "% (Ví dụ: 4.7)");
                System.out.print("==> ");
                String alcoholConcentration  = input.nextLine();
                if (cancelEntering(alcoholConcentration)) return true;
                double alcoholConcentrationValue = Double.parseDouble(alcoholConcentration);
                System.out.println();
                if (alcoholConcentrationValue < 0 && ValidateUtils.isNongDoConVaild(alcoholConcentration)) {
                    System.out.println("Nồng độ cồn phải lớn hơn 0 và bé hơn 10. Vui lòng nhập lại!\n");
                    continue;
                }
                newBeer.setAlcoholConcentration(alcoholConcentrationValue);
                return false;
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
    }

    private static void confirmAddingNewBeer(Beer newBeer) {
        do {
            try {
                showConfirmForm();
                int number = Menu.chooseActionByNumber();
                if (number == 1) {
                    beerService.add(newBeer);
                    System.out.println("\nMặt hàng mới đã thêm thành công!");
                    chooseActionInMedicineManagement();
                    break;
                }
                if (number == 2) {
                    chooseActionInMedicineManagement();
                    break;
                }
                Menu.alert();
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
    }

    private static void showConfirmForm() {
        System.out.println("\nXác nhận bạn muốn thêm mặt hàng với những thông tin dưới đây!");
        System.out.println("1. Xác nhận.");
        System.out.println("2. Đóng.\n");
    }


    private static boolean enterNote(Beer newBeer) {
        do {
            System.out.println("12. Nhập ghi chú (Ví dụ: bảo quản lạnh).");
            System.out.print("==> ");
            String note = input.nextLine();
            if (!ValidateUtils.isNoteValid(note)) {
                System.out.println("Ghi chú không hợp lệ, nhập bao gồm chữ, khoảng cách và dấu ','");
                continue;
            }
            if (cancelEntering(note)) return true;
            newBeer.setNote(note);
            return false;
        } while (true);
    }

    private static boolean cancelEntering(String string) {
        if (string.equalsIgnoreCase(Exit.E4)) {
            System.out.println("\nQúa trình cập nhật đã dừng lại!");
            chooseActionInMedicineManagement();
            return true;
        }
        return false;
    }

    private static boolean enterExpirationDate(Beer newBeer) throws ParseException {
        do {
            System.out.println("11. Nhập ngày hết hạn (Ví dụ: 08/08/2023) ");
            System.out.print("==> ");
            String expirationDate = input.nextLine().trim();
            if (cancelEntering(expirationDate)) return true;
            System.out.println();
            if (!ValidateUtils.isDateValid(expirationDate)) {
                System.out.println("Ngày không hợp lệ, vui lòng thử lại!\n");
                continue;
            }
            long expirationDateToMilli = ValidateUtils.convertDateToMilli(expirationDate);
            long productionDateToMilli = ValidateUtils.convertDateToMilli(newBeer.getProductionDate());
            if (expirationDateToMilli <= productionDateToMilli) {
                System.out.println("Ngày hết hạn không thể bé hơn ngày nhập kho, vui lòng thử lại!\n");
                continue;
            }
            newBeer.setExpirationDate(expirationDate);
            return false;
        } while (true);
    }

    private static boolean enterProductionDate(Beer newBeer) throws ParseException {
        do {
            System.out.println("10. Nhập ngày nhập kho (Ví dụ: 08/08/2023).");
            System.out.print("==> ");
            String productionDate = input.nextLine().trim();
            if (cancelEntering(productionDate)) {
                return true;
            }
            System.out.println();
//            if (!checkProductionDate(productionDate)) continue;
            if (ValidateUtils.isDateValid(productionDate)) {
                newBeer.setProductionDate(productionDate);
                return false;
            }
            System.out.println("Ngày không hợp lệ. Vui lòng nhập lại!\n");
        } while (true);
    }


    private static boolean enterPricePerPill(Beer newBeer) {
        do {
            try {
                System.out.println("9. Nhập giá tiền:" +"Vnđ");
                System.out.print("==> ");
                String pricePerPill = input.nextLine();
                if (cancelEntering(pricePerPill)) return true;
                double pricePerPillValue = Double.parseDouble(pricePerPill);
                if (pricePerPillValue <= 0) {
                    System.out.println("Số tiền phải lớn hơn 0. Vui lòng nhập lại!\n");
                    continue;
                }
                newBeer.setPricePerPill(pricePerPillValue);
                return false;
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
    }

    private static boolean enterQuantity(Beer newBeer) {
        do {
            try {
                System.out.println("4. Nhập số lượng: " + "Thùng");
                System.out.print("==> ");
                String quantity = input.nextLine();
                if (cancelEntering(quantity)) return true;
                int quantityValue = Integer.parseInt(quantity);
                System.out.println();
                if (quantityValue < 0) {
                    System.out.println("Số lượng phải lớn hơn 0. Vui lòng nhập lại!\n");
                    continue;
                }
                newBeer.setQuantity(quantityValue);
                return false;
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
    }

    private static boolean checkDuplicateBeer(Beer newBeer) {
        List<Beer> beerList = beerService.getBeers();
        for (Beer beer : beerList) {
            if (beer.getBeerName().equals(newBeer.getBeerName())) {
                System.out.println("Mặt hàng đã tồn tại. Vui lòng nhập lại!\n");
                return false;
            }
        }
        return true;
    }

    private static boolean enterBeerName(Beer newBeer) {
        do {
            System.out.println("2. Nhập tên mặt hàng: (Ví dụ: Huda). ");
            System.out.print("==> ");
            String beerName = input.nextLine().trim();
            System.out.println();
            if (ValidateUtils.isNameValid(beerName)) {
                newBeer.setBeerName(beerName);
                return false;
            }
            if (cancelEntering(beerName)) return true;
            System.out.println("Tên không hợp lệ. Vui lòng nhập lại!\n");
        } while (true);
    }

    private static void setID(Beer newBeer) {
        int min = 100000;
        int max = 999999;
        int id;
        do {
            id = (int) Math.floor(Math.random() * (max - min + 1) + min);
        } while (beerService.isIdExisted(id));
        newBeer.setId(id);
        System.out.println("1. Beer ID: " + id);
    }

    private static void searchBeersByName(List<Beer> beers) {
        boolean is;
        do {
            System.out.print("\nNhập tên mặt hàng bạn muốn tìm kiếm: ");
            String searchName = input.nextLine().toLowerCase().trim();
            List<Beer> beersListSearch = beerService.getSearchBeerList(searchName, beers);
            if (beersListSearch.size() == 0) {
                System.out.printf("\nKhông tìm thấy với tên '%s'. Bạn có muốn thử lại?\n", searchName);
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
                            chooseActionInMedicineManagement();
                            break;
                        }
                        Menu.alert();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Menu.alert();
                    }
                } while (true);
                continue;
            }
            showAllBeers(beersListSearch);
            is = true;
        } while (!is);
        chooseNextOperation();
    }

    private static void updateBeer() {
        Beer oldBeer = null;
        do {
            try {
                System.out.println(" Nhập ID : ");
                String idStr = input.nextLine();
                if (!ValidateUtils.isNumberValid(idStr) ) {
                    System.out.println("ID không được có các ký tự chữ cái, vui lòng nhập lại hoặc kiểm tra lại thông tin nhập !");
                    continue;
                }
                int id = Integer.parseInt(idStr);
                if (id == 0) {
                    chooseActionInMedicineManagement();
                    break;
                }
                if (!beerService.isIdExisted(id)) {
                    System.out.println("ID không tồn tại, vui lòng thử lại hoặc nhập '0' để quay lại.");
                    continue;
                }
                oldBeer = beerService.getBeerById(id);
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } while (true);

        boolean is = true;
        int number = -1;
        do {
            showBeerDetail(oldBeer);
            showChangeStatus(number);
            System.out.println("Chọn thông tin bạn muốn cập nhật.");
            System.out.println("NOTE: Bạn không thể cập nhật ID, Vui lòng nhập giá trị trong phạm vi '2-8' \n");
            System.out.println("---> Nhập '9' để xác nhận bạn muốn cập nhật với những thông tin bên dưới.\n");
            System.out.println("---> Nhập '0' để dừng.");
            try {
                number = Menu.chooseActionByNumber();
                switch (number) {
                    case 2:
                        is = enterBeerName(oldBeer);
                        break;
                    case 3:
                        is = enterQuantity(oldBeer);
                    case 4:
                        is = enterAlcoholConcentration(oldBeer);
                    case 5:
                        is = enterPricePerPill(oldBeer);
                        break;
                    case 6:
                        is = enterProductionDate(oldBeer);
                        break;
                    case 7:
                        is = enterExpirationDate(oldBeer);
                        break;
                    case 8:
                        is = enterNote(oldBeer);
                        break;
                    case 9:
                        is = true;
                        beerService.update(oldBeer);
                        System.out.println("\n-----> Đã cập nhật thành công!\n");
                        chooseActionInMedicineManagement();
                        break;
                    case 0:
                        is = true;
                        chooseActionInMedicineManagement();
                        break;
                    default:
                        Menu.alert();
                        is = false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Menu.alert();
            }
        } while (!is);
    }

    private static void showBeerDetail(Beer beer) {
        System.out.println("\u001B[31m╔ -------------------BEER DETAIL-----------------╗\u001B[31m");
        System.out.printf("%-30s %-12d\n", "1. ID:", beer.getId());
        System.out.printf("%-30s %-12s\n", "2. Tên Bia:", beer.getBeerName());
        System.out.printf("%-30s %-12s\n", "3. Số lượng (thùng):", beer.getQuantity());
        System.out.printf("%-30s %-12s\n", "4. Nồng độ cồn (%):", beer.getAlcoholConcentration());
        System.out.printf("%-30s %-12s\n", "5. Giá :", ValidateUtils.covertPriceToString(beer.getPricePerPill()));
        System.out.printf("%-30s %-12s\n", "6. Ngày nhập kho:", beer.getProductionDate());
        System.out.printf("%-30s %-12s\n", "7. Ngày hết hạn:", beer.getExpirationDate());
        System.out.printf("%-30s %-12s\n", "8. Ghi chú:", beer.getNote());
        System.out.println("\u001B[31m╚-----------------------------------------------╝\u001B[31m");
    }

    private static void showBeersList(List<Beer> beers, int choice) throws ParseException {
        switch (choice) {
            case 1:
                showAllBeers(beers);
                break;
            case 2:
                sortByQuantityASCE(beers);
                break;
            case 3:
                sortByPricePerPillASCE(beers);
                break;
            case 4:
                showExpiredDrugs(beers);
                break;
        }
        chooseNextOperation();
    }

    private static void sortByQuantityASCE(List<Beer> beers) {
        beers.sort((e1, e2) -> Integer.compare(e1.getQuantity() - e2.getQuantity(), 0));
        showAllBeers(beers);
    }

    private static void sortByPricePerPillASCE(List<Beer> beers) {
        beers.sort((e1, e2) -> Double.compare(e1.getPricePerPill() - e2.getPricePerPill(), 0));
        showAllBeers(beers);
    }

    private static void showExpiredDrugs(List<Beer> beers) throws ParseException {
        List<Beer> expiredBeers = new ArrayList<>();
        for (Beer beer : beers) {
            long expirationTime = ValidateUtils.convertDateToMilli(beer.getExpirationDate());
            long expiredTime = (long) (System.currentTimeMillis() + 2592 * Math.pow(10, 6));
            if (expirationTime <= expiredTime) {
                expiredBeers.add(beer);
            }
        }
        System.out.println("\n----- Danh sách này bao gồm mặt hàng đã hết hạn hoặc sẽ hết hạn vào tháng tới -----");
        showAllBeers(expiredBeers);
    }

    private static void chooseNextOperation() {
        do {
            System.out.println("\n---> Nhập '1' để xem chi tiết thông tin (bằng ID).");
            System.out.println("---> Nhập '2' để xóa mặt hàng  (bằng ID).");
            System.out.println("---> Nhập '0' để thoát.");
            try {
                int number = Menu.chooseActionByNumber();
                if (number == 1) {
                    updateBeer();
                    break;
                }
                if (number == 2) {
                    removeBeer();
                    break;
                }
                if (number == 0) {
                    chooseActionInMedicineManagement();
                    break;
                }
                Menu.alert();
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
    }

    private static void removeBeer() {
        IBeerService beerService = new BeerService();
        Beer beer = getExistedBeer();
        do {
            System.out.printf("\nXác nhận bạn muốn xóa mặt hàng %s '%s'.\n", beer.getId(), beer.getBeerName());
            System.out.println("1. Xác nhận xóa.");
            System.out.println("2. Đóng.");
            try {
                int number = Menu.chooseActionByNumber();
                if (number == 1) {
                    beerService.remove(beer.getId());
                    System.out.printf("\nMặt hàng '%s' đã xóa thành công!\n", beer.getId());
                    chooseActionInMedicineManagement();
                    break;
                }
                if (number == 2) {
                    chooseActionInMedicineManagement();
                    break;
                }
                Menu.alert();
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
    }

    private static Beer getExistedBeer() {
        do {
            try {
                System.out.println(" Nhập ID : ");
                int id = Integer.parseInt(input.nextLine());
                if (id == 0) {
                    chooseActionInMedicineManagement();
                    break;
                }
                if (!beerService.isIdExisted(id)) {
                    System.out.println("ID không tồn tại, vui lòng thử lại hoặc nhập '0' để quay lại.");
                    continue;
                }
                if (orderItemService.isItemOrdered(id)) {
                    System.out.println("Bạn không thể xóa mặt hàng này vì nó đã được đặt hàng.");
                    System.out.println("Vui lòng nhập ID khác!");
                    continue;
                }
                return beerService.getBeerById(id);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } while (true);
        return null;
    }


    public static void showAllBeers(List<Beer> beers) {
        System.out.println("\u001B[35m╔--------------------------------------------------------Danh sách bia -------------------------------------------------------------------╗\u001B[35m");
        System.out.printf("|%-12s %-25s %-30s %-23s %-23s  %-19s| \n",
                 "ID", "Tên Bia", "Nồng độ cồn (%)", "Giá", "Số lượng (thùng)", "Ngày hết hạn");
        System.out.println("\u001B[35m|-----------------------------------------------------------------------------------------------------------------------------------------|\u001B[35m");
        for (Beer beer : beers) {
            System.out.printf("|%-12s %-25s %-30s %-23s %-23s  %-19s | \n", beer.getId(), beer.getBeerName(), beer.getAlcoholConcentration(),
                    ValidateUtils.covertPriceToString(beer.getPricePerPill()), beer.getQuantity(), beer.getExpirationDate());
        }
        System.out.println("\u001B[35m╚-----------------------------------------------------------------------------------------------------------------------------------------╝\u001B[35m");
    }
}

