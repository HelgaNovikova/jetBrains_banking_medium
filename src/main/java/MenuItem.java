import java.util.Scanner;

public abstract class MenuItem {
    String title;
    int index;

    public abstract void run(UserContext userContext, Scanner sc);

    public MenuItem(String title, int index) {
        this.title = title;
        this.index = index;
    }

    @Override
    public String toString(){
        return this.index + ". " + this.title;
    }

}
