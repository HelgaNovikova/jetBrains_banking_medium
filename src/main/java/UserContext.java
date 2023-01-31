public class UserContext {
    private String cardNumber = null;

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void cleanCardNumber() {
        cardNumber = null;
    }

    public boolean isLogged(){
        return cardNumber != null;
    }
}
