package utils;



import java.text.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidateUtils {
    public static final String NAME_REGEX = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
    public static final String PHONE_REGEX = "^[0][1-9][0-9]{8,9}$";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,3}$";
    public static final String USERNAME_REGEX = "^[A-Za-z][A-Za-z0-9_]{7,19}$";
    public static final String ADDRESS_REGEX = "^[0-9]{1,5}[ /][a-zA-Z]+(([',. -][a-zA-Z])?[a-zA-Z]*)*$";
    public static final String NUMBER_REGEX = "^[0-9]+$";
    public static final String NONGDOCOM_REGEX = "^(10|[1-9](/./d)?)$";

    public static final String NOTE_REGEX = "^[A-Za-zÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬĐÈẺẼÉẸÊỀỂỄẾỆÌỈĨÍỊÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢÙỦŨÚỤƯỪỬỮỨỰỲỶỸÝỴ][ ,A-Za-zàảãáạăằẳẵắặâầẩẫấậđèẻẽéẹêềểễếệiìỉĩíịòỏõóọôồổỗốộơờởỡớợùủũúụỤưừửữứựỳỷỹýỵ]*$";



    public static boolean isNongDoConVaild(String ndc){
        return Pattern.matches(NONGDOCOM_REGEX, ndc);
    }
    public static boolean isPasswordValid(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }
    public static boolean isNoteValid(String note){
        return Pattern.matches(NOTE_REGEX, note);
    }



    public static boolean isNumberValid(String number) {
        return Pattern.matches(NUMBER_REGEX,number);
    }

    public static boolean isNameValid(String name) {
        return Pattern.matches(NAME_REGEX, name);
    }

    public static boolean isPhoneValid(String phone) {
        return Pattern.matches(PHONE_REGEX, phone);
    }

    public static boolean isEmailValid(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isUsernameValid(String username) {
        return Pattern.matches(USERNAME_REGEX, username);
    }

    public static boolean isAddressValid(String address) {
        return Pattern.matches(ADDRESS_REGEX, address);
    }

    public static boolean isDateValid(String dateStr) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.BASIC_ISO_DATE;
        try {
            LocalDate.parse(convertDate(dateStr), dateFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public static String convertDate(String date) {
        //  23/04/2021 -> 20210423
        String[] array = date.split("/");
        String result = "";
        for (int i = array.length - 1; i >= 0; i--) {
            result += array[i];
        }
        return result;
    }

    public static long convertDateToMilli(String date) throws ParseException {
        // 23/04/2021 -> 1619110800000
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        return date1.getTime();
    }

    public static String convertMilliToDate(LocalDateTime millisecond) {
        // 1619110800000 -> 23/04/2021
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(millisecond);
    }

    public static String priceWithDecimal (Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(price);
    }

    public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter ISO_FORMATTER =DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return localDateTime.format(ISO_FORMATTER);
    }

    public static LocalDateTime parseTime(String strDate) {
        try {
            return LocalDateTime.parse(strDate);
        } catch (DateTimeParseException dateTimeParseException) {
            dateTimeParseException.printStackTrace();
        }
        return null;
    }
    public static String covertPriceToString(double price) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
        return currencyFormatter.format(price);
    }
    public static double parseDouble(String price) {
        String priceNew = price.replaceAll("\\D+", "");
        return Double.parseDouble(priceNew);
    }
    public static int parseInteger(double price) {
        String price1 = String.valueOf(price);
        String priceNew = price1.replaceAll("\\D+\\d", "");
        return Integer.parseInt(priceNew);
    }
}

