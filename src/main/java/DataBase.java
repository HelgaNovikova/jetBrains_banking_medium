import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class DataBase {

    private final SQLiteDataSource dataSource = new SQLiteDataSource();

    public DataBase(String tableName) {
        dataSource.setUrl("jdbc:sqlite:" + tableName);
    }

    public void createTableIfNotExists() {
        try (Connection con = dataSource.getConnection()) {
            Statement statement = con.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                    "id INTEGER," +
                    "number TEXT NOT NULL," +
                    "pin TEXT NOT NULL," +
                    "balance INTEGER DEFAULT 0)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPinByCardNumber(String number) {
        String getPin = "SELECT pin FROM card where number = ?";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement getPinCode = con.prepareStatement(getPin);
            getPinCode.setString(1, number);
            ResultSet pin = getPinCode.executeQuery();
            return pin.getString("pin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean checkIfCardExist(String number) {
        String findCard = "select * from card where number = ?";
        String num = "";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement find = con.prepareStatement(findCard);
            find.setString(1, number);
            ResultSet rs = find.executeQuery();
            num = rs.getString("number");
            return !num.equals("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteCard(String number) {
        String deleteCard = "delete from card where number = ?";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement delete = con.prepareStatement(deleteCard);
            delete.setString(1, number);
            delete.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getBalanceByCardNumber(String number) {
        String getBalance = "SELECT balance FROM card where number = ?";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement findBalance = con.prepareStatement(getBalance);
            findBalance.setString(1, number);
            ResultSet balance = findBalance.executeQuery();
            return balance.getInt("balance");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addAccount(BankCard bc) {
        String addAccount = "INSERT INTO CARD VALUES (?, ?, ?, ?)";
        try (Connection con = dataSource.getConnection()) {
            int index = getNextIndex();
            PreparedStatement addAcc = con.prepareStatement(addAccount);
            addAcc.setInt(1, index);
            addAcc.setString(2, bc.getNumber());
            addAcc.setString(3, bc.getPinCode());
            addAcc.setInt(4, 0);
            addAcc.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMoneyToAccount(String recipientCard, int amount) {
        String add = "Update card set balance = balance + ? where number = ?";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement addMoney = con.prepareStatement(add);
            addMoney.setInt(1, amount);
            addMoney.setString(2, recipientCard);
            addMoney.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void transferMoneyToAnotherCard(String toCard, String fromCard, int amount) {
        String subtract = "Update card set balance = balance - ? where number = ?";
        String add = "Update card set balance = balance + ? where number = ?";
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement subtractMoney = con.prepareStatement(subtract); PreparedStatement addMoney = con.prepareStatement(add)) {
                subtractMoney.setInt(1, amount);
                subtractMoney.setString(2, fromCard);
                subtractMoney.executeUpdate();

                addMoney.setInt(1, amount);
                addMoney.setString(2, toCard);
                addMoney.executeUpdate();
                con.commit();
            } //catch (SQLException e) {
//                if (con != null) {
//                    try {
//                        System.err.print("Transaction is being rolled back");
//                        con.rollback();
//                    } catch (SQLException excep) {
//                        excep.printStackTrace();
//                    }
//                }
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void subtractMoneyFromAccount(BankCard bc, int amount) {
//        String subtract = "Update card set balance = balance - ? where number = ?";
//        try (Connection con = dataSource.getConnection()) {
//            PreparedStatement subtractMoney = con.prepareStatement(subtract);
//            subtractMoney.setInt(1, amount);
//            subtractMoney.setString(2, bc.getNumber());
//            subtractMoney.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    private int getNextIndex() {
        try (Connection con = dataSource.getConnection()) {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select id from card order by id desc limit 1");
            return rs.getInt("id") + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
