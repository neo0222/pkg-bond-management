import java.io.*;
import java.util.*;
import java.math.*;
import java.lang.NumberFormatException;

class InputBalance {
    public void execute(){
        do {
            try {
                System.out.println("<在庫データの入力>");
                System.out.println("在庫数を変更させたい銘柄のコードを以下から選択し、入力してください");
                System.out.println("また、終了する場合はqと入力してください");
                BondDao bonddao = new BondDao();

                //全ての銘柄一覧を表示する
                bonddao.showBondList();

                //変更したい銘柄コードを受け取る
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("銘柄コード > ");
                String inputCode = br.readLine();
                checkQuit(inputCode);

                //マスターデータに存在するかどうかをチェックする→存在しないならコメントを返す
                if(!bonddao.IsExist(inputCode)){
                    System.out.println("存在しない銘柄です");
                    break;
                }

                //マスターデータに存在する場合は、数量と簿価を入力させる
                System.out.print("数量 > ");
                String inputAmountStr = br.readLine();
                checkQuit(inputAmountStr);
                BigDecimal inputAmount = new BigDecimal(inputAmountStr);

                System.out.print("簿価 > ");
                String inputBookValueStr = br.readLine();
                checkQuit(inputBookValueStr);
                BigDecimal inputBookValue = new BigDecimal(inputBookValueStr);
                
                //入力した銘柄コードに対応する銘柄のBalanceインスタンスを取得する
                BalanceDao balancedao = new BalanceDao();
                Balance balance = balancedao.getBalance(inputCode);

                //現在保有していない銘柄の場合
                if(!balancedao.IsExist(inputCode)){
                    if(inputAmount.compareTo(BigDecimal.valueOf(0)) == -1){
                        System.out.println("その銘柄の保有数量が、入力した数量よりも少ないため、在庫量を減らすことは出来ません");
                        break;
                    }else{
                        Balance nb1 = new Balance(inputCode, inputAmount, inputBookValue, BigDecimal.valueOf(-1).setScale(3, RoundingMode.DOWN));
                        //balancedata.csvに銘柄データを追加する
                        balancedao.addBalance(nb1);
                        break;
                    }
                //保有している銘柄の場合
                }else{
                    //入力した数量によって保有数量がゼロ未満になってしまう場合
                    if(balance.getAmount().add(inputAmount).compareTo(BigDecimal.valueOf(0)) == -1){
                        System.out.println("保有量がマイナスになってしまいます");
                        System.out.println("その銘柄の保有量は" + balancedao.getBalance(inputCode).getAmount() + "です");
                        break;
                    //入力した数量が正常の場合
                    }else{
                        //balancedata.csvの対象銘柄コードの在庫数を変更して簿価を再計算
                        BigDecimal pastAmount = balance.getAmount();
                        BigDecimal pastBookValue = balance.getBookValue();
                        BigDecimal newAmount = pastAmount.add(inputAmount);

                        //移動平均法による算出
                        BigDecimal newBookValue = (pastAmount.multiply(pastBookValue)).add(inputAmount.multiply(inputBookValue)).divide(newAmount,3,RoundingMode.DOWN);

                        //更新するデータ
                        Balance nb2 = new Balance(inputCode, newAmount, newBookValue, BigDecimal.valueOf(-1).setScale(3, RoundingMode.DOWN));
                        balancedao.updateBalance(nb2);
                        break;
                    }
                }
            } catch (NumberFormatException ne) {
                System.out.println("正しい数値を入力してください");
                break;
            } catch (IOException ie) {
                System.out.println("ファイルが存在しません");
                break;
            } 
        } while (true);
    }

    //システムを終了させるアクション
    public void checkQuit(String input){
        if(input.equals("q")){
            System.exit(0);
        }
    }
}