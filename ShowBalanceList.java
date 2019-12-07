import java.util.*;
import java.math.*;

class ShowBalanceList {   
    public void execute(){
        System.out.println("<保有銘柄の残高一覧>");
        //Modelのインスタンスを作る
        BalanceDao balancedao = new BalanceDao();
        BondDao bonddao = new BondDao();
        System.out.println("+-------------------------------------------------------------------------------------------------------------------+");
        String str = String.format("|%-7s|%-12s|%-5s|%-6s|%-8s|%-5s|%-13s|%-13s|%-10s|", "銘柄コード", "銘柄名", "償還期限日", "利率", "クーポン", "保有量", "簿価", "時価", "損益");
        System.out.println(str);
        System.out.println("---------------------------------------------------------------------------------------------------------------------");
        for(int i=0; i < balancedao.getBalanceList().size(); i++){
            Balance balance = balancedao.getBalanceList().get(i);
            Bond bond = bonddao.getBond(balance.getCode());
            printData(bond, balance);
        }
        System.out.println("+-------------------------------------------------------------------------------------------------------------------+");
    }

    //受け取ったBondとBalanceデータから評価損益をプラスして出力する
    public void printData(Bond bond, Balance balance){
        //Bondのデータを変数に格納する
        String code = bond.getCode(); //銘柄コード
        String name = bond.getName(); //銘柄名
        BigDecimal interestRate = bond.getInterestRate(); //利率
        int redemptionDate = bond.getRedemptionDate(); //償還年月日(yyyymmdd)
        int coupon = bond.getCoupon(); //クーポン回数

        //Balanceのデータを変数に格納する
        BigDecimal amount = balance.getAmount(); //保有数量
        BigDecimal bookValue = balance.getBookValue(); //簿価

        //Bigdecimalのものを全て丸める
        String interestRateStr = interestRate.setScale(3, RoundingMode.DOWN).toString();
        String amountStr = amount.setScale(0, RoundingMode.DOWN).toString();
        String bookValueStr = bookValue.setScale(3, RoundingMode.DOWN).toString();

        BigDecimal currentValue; //時価
        BigDecimal valuationPL; //評価損益
        String currentValueStr = null;
        String valuationPLStr = null;

        //時価に-1が格納されているとき
        if(balance.getCurrentValue().compareTo(BigDecimal.valueOf(-1))==0){
            currentValue = null;
            valuationPL = null;
            currentValueStr = "N/A";
            valuationPLStr = "N/A";
        }else{
            currentValue = balance.getCurrentValue(); //時価
            valuationPL = calcuratePL(balance); //評価損益
            currentValueStr = currentValue.setScale(3, RoundingMode.DOWN).toString();
            valuationPLStr = valuationPL.setScale(3, RoundingMode.DOWN).toString();
        }

        //プリント
        String str = String.format("|%-12s|%-15s|%10d|%-8s|%12d|%-8s|%-15s|%-15s|%-12s|", code, name, redemptionDate, interestRateStr, coupon, amountStr, bookValueStr, currentValueStr, valuationPLStr);
        System.out.println(str);
    } 

    //評価損益の計算
    public BigDecimal calcuratePL(Balance balance){
        BigDecimal amount = balance.getAmount();
        BigDecimal bookValue = balance.getBookValue();
        BigDecimal currentValue = balance.getCurrentValue();
        BigDecimal valuationPL = (currentValue.subtract(bookValue)).divide(amount, 3, RoundingMode.DOWN);
        return valuationPL;
    }
}