package converter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int inRadix = scanner.nextInt();
        scanner.nextLine();
        String num = scanner.nextLine();
        int outRadix = scanner.nextInt();
        System.out.println(BaseConverter.converter(num, inRadix, outRadix));
    }
}

class BaseConverter {
    public static String converter(String num, int radixIn, int radixOut) {
        String[] nums = num.split("\\.");
        return (nums.length == 1) ?
                intConverter(nums[0], radixIn, radixOut) :
                intConverter(nums[0], radixIn, radixOut) + "." + decimalConverter(nums[1], radixIn, radixOut);
    }

    public static String intConverter(String num, int radixIn, int radixOut) {
        int decimal = radixIn == 1 ? num.length() : Integer.parseInt(num, radixIn);
        return radixOut == 1 ? "1".repeat(decimal) : Integer.toString(decimal, radixOut);
    }

    public static String decimalConverter(String num, int radixIn, int radixOut) {
        //Convert num from radixIn to decimal
        double decimal = 0.0;
        for (int i = 0; i < num.length(); i++) {
            int val = Integer.parseInt(Character.toString(num.charAt(i)), radixIn);
            decimal += ((double) val) / Math.pow(radixIn, i + 1);
        }

        //Convert decimal to decimal in radixOut, max 5 digits
        StringBuilder sb = new StringBuilder();
        for (int digits = 0; digits < 5; digits++)
        {
            decimal = decimal * radixOut;
            int intPart = (int) decimal;
            decimal -= intPart;
            sb.append(Integer.toString(intPart, radixOut).charAt(0));
        }

        return sb.toString();
    }
}
