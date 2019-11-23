import java.io.*;
import java.util.*;
import java.math.*;

class BalanceDao {
    public boolean IsExist(String inputCode){
        try {
            FileReader fileName = new FileReader("balance_data.csv");
            BufferedReader br = new BufferedReader(fileName);

            //読み込んだファイルを1行ずつ処理
            String line;

            while((line = br.readLine()) != null){
                String[] balanceDataStr = line.split(",", 0);
                if (balanceDataStr[0].equals(inputCode)){
                    br.close();
                    return true;
                }
            }
            br.close();
        } catch(IOException ie) {
            System.out.println("ファイルが存在しません");
        }
        return false;
    }

    public Balance getBalance(String inputCode){
        try {
            FileReader fileName = new FileReader("balance_data.csv");
            BufferedReader br = new BufferedReader(fileName);

            //読み込んだファイルを1行ずつ処理
            String line;

            while((line = br.readLine()) != null){
                String[] balanceDataStr = line.split(",", 0);
                if(balanceDataStr[0] == inputCode){
                    //Balanceの引数を設定
                    String code = balanceDataStr[0];
                    BigDecimal amount = new BigDecimal(balanceDataStr[1]);
                    BigDecimal bookValue = new BigDecimal(balanceDataStr[2]);
                    BigDecimal currentValue = new BigDecimal(balanceDataStr[3]);
                    //Balanceを生成
                    Balance balance = new Balance(code, amount, bookValue, currentValue);
                    br.close();
                    return balance;
                }
            }
            br.close();
        } catch (IOException ie) {
            System.out.println("ファイルが存在しません");
        }
        return null;
    }

    public List<Balance> getBalanceList(){
        List<Balance> balanceList = new ArrayList<Balance>();
        try {
            FileReader fileName = new FileReader("balance_data.csv");
            BufferedReader br = new BufferedReader(fileName);

            //読み込んだファイルを1行ずつ処理
            String line;

            while((line = br.readLine()) != null){
                String[] balanceDataStr = line.split(",", 0);

                //Balanceの引数を設定
                String code = balanceDataStr[0];
                BigDecimal amount = new BigDecimal(balanceDataStr[1]);
                BigDecimal bookValue = new BigDecimal(balanceDataStr[2]);
                BigDecimal currentValue = new BigDecimal(balanceDataStr[3]);

                //Balanceを生成
                Balance balance = new Balance(code, amount, bookValue, currentValue);

                //balanceListに追加
                balanceList.add(balance);
            }
            br.close();
        } catch (IOException ie) {
            System.out.println("ファイルが存在しませんw");
        }
        return balanceList;
    }

    public void addBalance(Balance balance){
        try {
            // FileWriterクラスのオブジェクトを生成する
            FileWriter file = new FileWriter("balance_data.csv", true);
            // PrintWriterクラスのオブジェクトを生成する
            PrintWriter pw = new PrintWriter(new BufferedWriter(file));
            
            //ファイルに追記する
            pw.println(balance.toString());
            
            //ファイルを閉じる
            pw.close();
        } catch (IOException ie) {
            System.out.println("ファイルが存在しません");
        }
    }

    public void updateBalance(Balance balance){
        //空のリストを作る
        List<Balance> updatedList = new ArrayList<Balance>();

        //今のBalanceListを確認する
        for(int i=0; i<this.getBalanceList().size(); i++){
            if(this.getBalanceList().get(i).getCode() != balance.getCode()){
                updatedList.add(getBalanceList().get(i));
            }else{
                //移動平均法を使って処理する
                BigDecimal a = this.getBalanceList().get(i).getAmount().multiply(this.getBalanceList().get(i).getBookValue());
                BigDecimal b = balance.getAmount().multiply(balance.getBookValue());
                BigDecimal c = this.getBalanceList().get(i).getAmount().add(balance.getAmount());

                BigDecimal updatedAmount = c;
                BigDecimal updatedBookValue = (a.add(b)).divide(c,5,RoundingMode.HALF_UP);

                Balance bal = new Balance(this.getBalanceList().get(i).getCode(), updatedAmount, updatedBookValue);
                updatedList.add(bal);
            }
        }
        
        try {
            // FileWriterクラスのオブジェクトを生成する
            FileWriter file = new FileWriter("balance_data.csv");
            // PrintWriterクラスのオブジェクトを生成する
            PrintWriter pw = new PrintWriter(new BufferedWriter(file));
            
            //ファイルに書き込む
            for(int i=0; i<updatedList.size(); i++){
                pw.println(updatedList.get(i).toString());
            }

            //ファイルを閉じる
            pw.close();
        } catch (IOException ie) {
            System.out.println("ファイルが存在しません");
        }
    }

}