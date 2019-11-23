import java.util.*;

class ShowBalanceList {
    public void execute(){
        System.out.println("<保有銘柄の残高一覧>");
        BalanceDao bd = new BalanceDao();
        for(int i=0; i < bd.getBalanceList().size(); i++){
            bd.getBalanceList().get(i).printBalance();
        }
    }
}