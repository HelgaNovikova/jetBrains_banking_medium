import java.text.DecimalFormat;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BankCardTest {

    @org.junit.jupiter.api.Test
    void generatePinCode() {
        Random r = new Random();
        DecimalFormat df = new DecimalFormat("0000");
        System.out.println( df.format(123));
    }
}