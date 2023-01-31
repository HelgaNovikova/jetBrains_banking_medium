
public class RunProcess {

    public static void main(String[] args) {
        DataBase db = new DataBase(args[1]);
        Work w = new Work(db);
        w.start();
    }
}
