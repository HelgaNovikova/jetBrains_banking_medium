import java.util.*;
import java.util.stream.Collectors;

public class Menu {
    private Map<Integer, MenuItem> menuItemsAfterLogin = new LinkedHashMap<>();


    public MenuItem getMenuItemAfterLoginByChoice(Integer choice){
        return menuItemsAfterLogin.get(choice);
    }

    public MenuItem getMenuItemBeforeLoginByChoice(Integer choice){
        return menuItemsBeforeLogin.get(choice);
    }

    private Map<Integer, MenuItem> menuItemsBeforeLogin = new LinkedHashMap<>();

    private DataBase db;
  //  Scanner sc = new Scanner(System.in);

    public Menu(DataBase db, UserContext userContext) {
        this.db = db;
    }

    public void createMenuAfterLogin() {

        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Balance", 1) {
            @Override
            public void run(UserContext userContext, Scanner sc) {
                System.out.println("Balance: " + db.getBalanceByCardNumber(userContext.getCardNumber()));
                showMenuAfterLogin();
            }
        });

        items.add(new MenuItem("Add income", 2) {
            @Override
            public void run(UserContext userContext, Scanner sc) {
                System.out.println("Enter income:");
                int amount = sc.nextInt();
                db.addMoneyToAccount(userContext.getCardNumber(), amount);
                System.out.println("Income was added!");
                showMenuAfterLogin();
            }
        });

        items.add(new MenuItem("Do transfer", 3) {
            @Override
            public void run(UserContext userContext, Scanner sc) {
                System.out.println("Transfer");
                System.out.println("Enter card number:");
                String enteredCardNumber = sc.nextLine();
                enteredCardNumber = sc.nextLine();
                if (!new BankCard(enteredCardNumber).checkCardNumberIsValid(enteredCardNumber)) {
                    System.out.println("Probably you made a mistake in the card number. Please try again!");
                    showMenuAfterLogin();
                } else if (!db.checkIfCardExist(enteredCardNumber)) {
                    System.out.println("Such a card does not exist.");
                    showMenuAfterLogin();
                } else {
                    System.out.println("Enter how much money you want to transfer:");
                    int amount = sc.nextInt();
                    if (amount > db.getBalanceByCardNumber(userContext.getCardNumber())) {
                        System.out.println("Not enough money!");
                        showMenuAfterLogin();
                    } else {
                        db.transferMoneyToAnotherCard(enteredCardNumber, userContext.getCardNumber(), amount);
                        System.out.println("Success!");
                        showMenuAfterLogin();
                    }

                }

            }
        });

        items.add(new MenuItem("Close account", 4) {
            @Override
            public void run(UserContext userContext, Scanner sc) {
                db.deleteCard(userContext.getCardNumber());
                System.out.println("The account has been closed!");
                showInitialMenu();
            }
        });

        items.add(new MenuItem("Log out", 5) {
            @Override
            public void run(UserContext userContext, Scanner sc) {
                System.out.println("You have successfully logged out!");
                userContext.cleanCardNumber();
                showInitialMenu();
            }
        });

        items.add(new MenuItem("Exit", 0) {
            @Override
            public void run(UserContext userContext, Scanner sc) {
                System.out.println("Bye!");
            }
        });
        menuItemsAfterLogin = buildMap(items);
    }

    public void createInitialMenu(){
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Create an account", 1) {
            @Override
            public void run(UserContext userContext, Scanner sc) {
                BankCard bc = new BankCard();
                db.addAccount(bc);
                System.out.println("Your card has been created\nYour card number:\n" + bc.getNumber());
                System.out.println("Your card PIN:\n" + bc.getPinCode());
                showInitialMenu();
            }
        });

        items.add(new MenuItem("Log into account", 2) {
            @Override
            public void run(UserContext userContext, Scanner sc) {
                System.out.print("Enter your card number:\n>");
                String number = sc.next();
                System.out.print("Enter your PIN:\n>");
                String pin = sc.next();
                if (db.getPinByCardNumber(number).equals(pin)) {
                    System.out.println("You have successfully logged in!");
                    userContext.setCardNumber(number);
                    createMenuAfterLogin();
                    showMenuAfterLogin();
                } else {
                    System.out.println("Wrong card number or PIN!");
                    showInitialMenu();
                }
            }
        });

        items.add(new MenuItem("Exit", 0) {
            @Override
            public void run(UserContext userContext, Scanner sc) {
                System.out.println("Bye!");
            }
        });

        menuItemsBeforeLogin = buildMap(items);
    }

    private LinkedHashMap<Integer, MenuItem> buildMap(List<MenuItem> items) {
        return items.stream()
                .collect(Collectors.toMap(i -> i.index, i -> i, (menuItem, menuItem2) -> menuItem, LinkedHashMap::new));
    }

    public void showInitialMenu() {
        for (MenuItem item : menuItemsBeforeLogin.values()) {
            System.out.println(item.toString());
        }
        System.out.print(">");
    }

    public void showMenuAfterLogin() {
        for (MenuItem item : menuItemsAfterLogin.values()) {
            System.out.println(item.toString());
        }
        System.out.print(">");
    }

}
