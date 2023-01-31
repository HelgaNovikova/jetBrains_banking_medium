import java.util.Scanner;

public class Work {

    private final UserContext userContext;
    private DataBase db;
    private Menu menu;;

    public Work(DataBase db) {
        this.db = db;
        userContext = new UserContext();
        this.menu = new Menu(db, userContext);
    }

    public void start() {
        Scanner sc = new Scanner(System.in);
        db.createTableIfNotExists();
        menu.createInitialMenu();
        menu.showInitialMenu();
        int userChoice = sc.nextInt();
        while (userChoice != 0){
            if (! userContext.isLogged()){
                menu.getMenuItemBeforeLoginByChoice(userChoice).run(userContext, sc);
            }
            else {
                menu.getMenuItemAfterLoginByChoice(userChoice).run(userContext, sc);
            }
            userChoice = sc.nextInt();
        }
//        while (userChoice != 0 && !logged) {
//            switch (userChoice) {
//                case 1 -> {
//                    createAccount();
//                    menu.showInitialMenu();
//                    userChoice = sc.nextByte();
//                }
//                case 2 -> {
//                    System.out.print("Enter your card number:\n>");
//                    number = sc.next();
//                    System.out.print("Enter your PIN:\n>");
//                    String pin = sc.next();
//                    if (db.getPinByCardNumber(number).equals(pin)) {
//                        System.out.println("You have successfully logged in!");
//                        menu.showMenuAfterLogin();
//                        logged = true;
//                    } else {
//                        System.out.println("Wrong card number or PIN!");
//                        menu.showInitialMenu();
//                        userChoice = sc.nextByte();
//                    }
//                }
//            }
//        }
//        menu.exit();
    }
}