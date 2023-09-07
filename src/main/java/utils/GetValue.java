package utils;

import java.util.Scanner;

public class GetValue {
    static Scanner input = new Scanner(System.in);
    public static String getString(String str) {
        System.out.println(str);
        return input.nextLine();
    }
    public static int getInt(String str) {
        try{
            int choice = Integer.parseInt(GetValue.getString(str));
            if (choice < 0) {
                throw new NumberFormatException();
            }
            return choice;
        } catch (NumberFormatException e) {
            e.getMessage();
            return getInt(str);
        }
    }
}
