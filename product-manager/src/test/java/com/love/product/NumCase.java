package com.love.product;

/**
 * @PackageName: com.love.product
 * @Description: NumCase
 * @Author: Administrator
 * @Date: 2023/6/10 16:48
 */

import java.util.Scanner;

public class NumCase {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int decimalNumber = sc.nextInt();

        System.out.print("Enter the base to convert to: ");
        int base = sc.nextInt();

        String convertedNumber = decimalToArbitraryBase(decimalNumber, base);
        System.out.println(decimalNumber + " in base " + base + " is " + convertedNumber);

        System.out.print("Enter the number to convert: ");
        String number = sc.next();

        System.out.print("Enter the base of the number: ");
        int sourceBase = sc.nextInt();

        System.out.print("Enter the target base: ");
        int targetBase = sc.nextInt();

        String result = convertNumberBase(number, sourceBase, targetBase);

        System.out.println(number + " (base " + sourceBase + ") converted to base " + targetBase + " is " + result);
    }

    public static String decimalToArbitraryBase(int decimalNumber, int base) {

        if (base < 2 || base > 36) {
            throw new IllegalArgumentException("Base must be between 2 and 36");
        }

        String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder convertedNumber = new StringBuilder();

        while (decimalNumber > 0) {
            int remainder = decimalNumber % base;
            convertedNumber.append(digits.charAt(remainder));
            decimalNumber /= base;
        }

        return convertedNumber.reverse().toString();
    }
    public static String convertNumberBase(String number, int sourceBase, int targetBase) {

        int decimalNumber = toDecimal(number, sourceBase);

        return fromDecimal(decimalNumber, targetBase);
    }

    public static int toDecimal(String number, int sourceBase) {//转10进制
        int decimalNumber = 0;
        int power = 0;

        for (int i = number.length() - 1; i >= 0; i--) {
            char c = number.charAt(i);
            int digit = Character.digit(c, sourceBase);
            decimalNumber += digit * Math.pow(sourceBase, power);
            power++;
        }

        return decimalNumber;
    }

    public static String fromDecimal(int decimalNumber, int targetBase) {
        StringBuilder resultBuilder = new StringBuilder();

        while (decimalNumber > 0) {
            int remainder = decimalNumber % targetBase;
            char c = Character.forDigit(remainder, targetBase);
            resultBuilder.append(c);
            decimalNumber /= targetBase;
        }

        return resultBuilder.reverse().toString();
    }
}
