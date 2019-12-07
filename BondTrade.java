import java.util.*;
import java.io.*;

class BondTrade {
    public void execute(){
        do{
            try{
                System.out.println("<債権の取引>");
                System.out.println("終了する場合はqと入力してください");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                BondDao bonddao = new BondDao();
                BalanceDao balancedao = new BalanceDao();

                System.out.print("銘柄コード > ");
                String inputCode = br.readLine();
                checkQuit(inputCode);
                if(!bonddao.IsExist(inputCode)){
                    System.out.println("存在しない銘柄です");
                    continue;
                }

                System.out.println("売買（1:売る, 2:買う）> ");
                String inputAction = br.readLine();
                checkQuit(inputAction);
                if(!inpuAction.equals("1") && !inputAction.equals("2")){
                    System.out.println("半角数字の1または2を入力してください");
                    continue;
                }

                balancedao.getBalance(inputCode);

                System.out.println("取引価格 > ");
                String inputPriceStr = br.readLine();
                checkQuit(inputPriceStr);
                BigDecimal inputPrice = new BigDecimal(inputPriceStr);
                if(checkNegativeNumber(inputPrice)){
                    System.out.println("正の数を入力してください");
                    continue;
                }

                System.out.println("取引数量 > ");
                String inputAmountStr = br.readLine();
                checkQuit(inputAmountStr);
                BigDecimal inputAmount = new BigDecimal(inputAmountStr);
                if(checkPositiveNumber(inputAmount)){
                    System.out.println("正の数を入力してください");
                    continue;
                }

                //売れる量があるかどうかをチェックする
                if(!balancedao.IsExist(inputCode) || !balancedao.getBalance(inputCode).getAmount().compareTo(inputAmount) != 1){
                    System.out.println("売る数量がありません");
                }
                
                //売買処理を行う
                


            }catch(ParseException pe){
                System.out.println("不正な値です");
                continue;
            }catch(IOException ie){
                System.out.println(ie);
                continue;
            }
        }while(true);
    }

    //システムを終了させるアクション
    public void checkQuit(String input){
        if(input.equals("q")){
            System.exit(0);
        }
    }

    public Boolean checkNegativeNumber(BigDecimal num){
        if(num.compareTo(BigDecimal.valueOf(0)) == -1){
           return true;
        }
        return false;
    }
}