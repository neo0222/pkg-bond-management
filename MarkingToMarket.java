import java.io.*;
import java.util.*;
import java.math.*;
import java.lang.NumberFormatException;

class MarkingToMarket {
    public void execute(){
        System.out.println("<時価の入力>");
        BalanceDao balancedao = new BalanceDao();
        BondDao bonddao = new BondDao();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            for(int i=0; i < balancedao.getBalanceList().size(); i++){
                Balance balance = balancedao.getBalanceList().get(i);
                Bond bond = bonddao.getBond(balance.getCode());
                System.out.println("銘柄コード: " + bond.getCode() + ", 銘柄名称: " + bond.getName());
                System.out.print("時価 > ");
                BigDecimal inputCurrentValue = new BigDecimal(br.readLine());

                Balance updatedBalance = new Balance(balance.getCode(), balance.getAmount(), balance.getBookValue(), inputCurrentValue.setScale(3, RoundingMode.DOWN));
                balancedao.updateBalance(updatedBalance);
                System.out.println();
            }
        }catch(IOException ie){
            System.out.println(ie);
        }catch(NumberFormatException ne){
            System.out.println(ne);
        }
    }
}