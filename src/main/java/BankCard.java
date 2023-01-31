import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class BankCard {

    private String number;
    private String pinCode;
    private int checksum = 0;

    public int getBalance() {
        return balance;
    }

    public BankCard(String number, String pinCode) {
        this.number = number;
        this.pinCode = pinCode;
    }

    public BankCard(String number) {
        this.number = number;
    }

    public BankCard() {
        generateNumber();
        generatePinCode();
    }

    private int balance = 0;

    public String getNumber() {
        return number;
    }

    public void generateNumber() {
        String bin = "400000";
        StringBuilder accIdentifier = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 9; i++) {
            accIdentifier.append(r.nextInt(10));
        }
        this.number = bin + accIdentifier + "" + calculateCheckSum(bin + accIdentifier);
    }

    public String getPinCode() {
        return pinCode;
    }

    public void generatePinCode() {
        Random r = new Random();
        DecimalFormat df = new DecimalFormat("0000");
        this.pinCode = df.format(r.nextInt(9999));
    }

    private int calculateCheckSum(String number) {
        int[] digits = Arrays.stream(number.split("")).mapToInt(Integer::parseInt).toArray();
        int sum = 0;
        int checkSum;
        for (int i = 0; i < digits.length; i++) {
            if (i % 2 == 0) {
                digits[i] *= 2;
                if (digits[i] > 9) {
                    digits[i] -= 9;
                }
            }
            sum = sum + digits[i];
        }
        if (sum % 10 == 0) {
            checkSum = 0;
        } else {
            checkSum = (sum / 10 + 1) * 10 - sum;
        }
        return checkSum;
    }

    public boolean checkCardNumberIsValid(String number) { 
        int numberOfDigits = number.length();
        int checkSum = Character.getNumericValue(number.charAt(numberOfDigits - 1));
        return (calculateCheckSum(number.substring(0, numberOfDigits - 1)) == checkSum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankCard bankCard = (BankCard) o;
        return Objects.equals(number, bankCard.number) && Objects.equals(pinCode, bankCard.pinCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, pinCode);
    }
}
