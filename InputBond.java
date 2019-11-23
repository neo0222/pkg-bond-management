import java.io.*;
import java.util.*;
import java.math.*;
import java.lang.NumberFormatException;

class InputBond {
    public void execute(){
        do {
            try {
                System.out.println("<在庫データの入力>");
                System.out.println("在庫数を変更させたい銘柄のコードを以下から選択し、入力してください");
                System.out.println("また、終了する場合はqと入力してください");
                BondDao bonddao = new BondDao();
                for(int i=0; i < bonddao.getBondList().size(); i++){
                    System.out.print("  ");
                    bonddao.getBondList().get(i).printBond();
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("銘柄コード > ");
                String inputCode = br.readLine();
                if(inputCode.equals("q")){
                    System.exit(0);
                }

                //マスターデータに存在するかどうかをチェックする
                if(!bonddao.IsExist(inputCode)){
                    System.out.println("存在しない銘柄です");
                    break;
                }

                System.out.print("保有数量 > ");
                BigDecimal inputAmount = new BigDecimal(br.readLine());

                System.out.print("簿価 > ");
                BigDecimal inputBookValue = new BigDecimal(br.readLine());

                System.out.print("時価 > ");
                BigDecimal inputCurrentValue = new BigDecimal(br.readLine());
                
                //保有データに存在するかどうかを確認する
                BalanceDao balancedao = new BalanceDao();
                Balance balance = balancedao.getBalance(inputCode);

                if(!balancedao.IsExist(inputCode)){
                    if(inputAmount.compareTo(BigDecimal.valueOf(0)) == -1){
                        System.out.println("その銘柄は現在保有していません");
                        break;
                    }else{
                        Balance nb1 = new Balance(inputCode, inputAmount, inputBookValue, inputCurrentValue);
                        //balancedata.csvに銘柄コード自体を足す
                        balancedao.addBalance(nb1);
                        break;
                    }
                }else{
                    if(balance.getAmount().add(inputAmount).compareTo(BigDecimal.valueOf(0)) == -1){
                        System.out.println("保有量がマイナスになってしまいます");
                        System.out.println("その銘柄の保有量は" + balancedao.getBalance(inputCode).getAmount() + "です");
                        break;
                    }else{
                        //balancedata.csvの対象銘柄コードの在庫数を変更して簿価を再計算  
                        Balance nb2 = new Balance(inputCode, inputAmount, inputBookValue, inputCurrentValue);
                        balancedao.addBalance(nb2);
                        break;
                    }
                }
            } catch (NumberFormatException ne) {
                System.out.println("保有数量と簿価には半角数字を入力してください");
                break;
            } catch (IOException ie) {
                System.out.println("error");
                break;
            } 
        } while (true);
    }
}