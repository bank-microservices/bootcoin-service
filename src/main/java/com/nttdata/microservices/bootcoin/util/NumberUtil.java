package com.nttdata.microservices.bootcoin.util;

import java.util.Random;

public class NumberUtil {

  public static String generateRandomNumber(int length) {
    Random random = new Random(System.currentTimeMillis());
    String bin = "3353";
    int randomNumberLength = length - (bin.length() + 1);
    var builder = new StringBuilder(bin);
    for (int i = 0; i < randomNumberLength; i++) {
      int digit = random.nextInt(10);
      builder.append(digit);
    }

    int checkDigit = getCheckDigit(builder.toString());
    builder.append(checkDigit);
    return builder.toString();
  }

  private static int getCheckDigit(String number) {
    int sum = 0;
    for (int i = 0; i < number.length(); i++) {

      // Get the digit at the current position.
      int digit = Integer.parseInt(number.substring(i, (i + 1)));

      if ((i % 2) == 0) {
        digit = digit * 2;
        if (digit > 9) {
          digit = (digit / 10) + (digit % 10);
        }
      }
      sum += digit;
    }

    // The check digit is the number required to make the sum a multiple of
    // 10.
    int mod = sum % 10;
    return ((mod == 0) ? 0 : 10 - mod);
  }
}
