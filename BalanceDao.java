import java.io.*;
import java.util.*;
import java.math.*;

class BalanceDao {
    private static final String balance_file = "balance_data.csv";

    //対象のBalanceが存在するかをtrue,falseで返す
    public boolean IsExist(String inputCode){
        try {
            FileReader fileName = new FileReader(balance_file);
            BufferedReader br = new BufferedReader(fileName);

            //読み込んだファイルを1行ずつ処理
            String line;
            
            while((line = br.readLine()) != null){
                String[] balanceDataStr = line.split(",", -1);
                if (balanceDataStr[0].equals(inputCode)){
                    br.close();
                    //一致した時点でtrueを返す
                    return true;
                }
            }
            br.close();
        //csvファイルが存在しない場合の処理
        } catch(IOException ie) {
            System.out.println("ファイルが存在しません");
        }
        return false;
    }

    //対象のBalanceデータをインスタンスとして返却する
    public Balance getBalance(String inputCode){
        try {
            FileReader fileName = new FileReader(balance_file);
            BufferedReader br = new BufferedReader(fileName);

            //読み込んだファイルを1行ずつ処理
            String line;
            
            while((line = br.readLine()) != null){
                String[] balanceDataStr = line.split(",", -1);
                if(balanceDataStr[0].equals(inputCode)){
                    //Balanceの引数を設定
                    String code = balanceDataStr[0];
                    BigDecimal amount = new BigDecimal(balanceDataStr[1]);
                    BigDecimal bookValue = new BigDecimal(balanceDataStr[2]);
                    BigDecimal currentValue = null;
                    
                    //currentValueが入力されている場合の処理
                    if(!balanceDataStr[3].equals("")){
                        currentValue = new BigDecimal(balanceDataStr[3]);
                    }
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

    //登録されているバランスをリストにして返す
    public List<Balance> getBalanceList(){
        List<Balance> balanceList = new ArrayList<Balance>();
        try {
            FileReader fileName = new FileReader(balance_file);
            BufferedReader br = new BufferedReader(fileName);

            //読み込んだファイルを1行ずつ処理
            String line;

            while((line = br.readLine()) != null){
                String[] balanceDataStr = line.split(",", -1);

                //Balanceの引数を設定
                String code = balanceDataStr[0];
                BigDecimal amount = new BigDecimal(balanceDataStr[1]);
                BigDecimal bookValue = new BigDecimal(balanceDataStr[2]);
                BigDecimal currentValue = null;

                //currentValueが入力されている場合の処理
                if(!balanceDataStr[3].equals("")){
                    currentValue = new BigDecimal(balanceDataStr[3]);
                }

                //Balanceを生成
                Balance balance = new Balance(code, amount, bookValue, currentValue);

                //balanceListに追加
                balanceList.add(balance);
            }
            br.close();
        } catch (IOException ie) {
            System.out.println("ファイルが存在しません");
        }
        return balanceList;
    }

    //Balanceをファイルに追加する
    public void addBalance(Balance balance){
        try {
            // FileWriterクラスのオブジェクトを生成する
            FileWriter file = new FileWriter(balance_file, true);
            // PrintWriterクラスのオブジェクトを生成する
            PrintWriter pw = new PrintWriter(new BufferedWriter(file));
            
            //ファイルに追記する
            if(balance.getAmount().compareTo(BigDecimal.valueOf(0)) == 1){
                pw.println(balance.toString());
            }else{
                System.out.println("処理が実行できません");
            }
            
            //ファイルを閉じる
            pw.close();
        } catch (IOException ie) {
            System.out.println("ファイルが存在しません");
        }
    }

    //対象のバランスデータに変更を加える
    public void updateBalance(Balance balance){
        //空のリストを作る
        List<Balance> updatedList = new ArrayList<Balance>();

        //今のBalanceListを確認する
        for(int i=0; i<this.getBalanceList().size(); i++){
            //アップデート対象と異なる場合は、そのままリストに追加する
            if(!this.getBalanceList().get(i).getCode().equals(balance.getCode())){
                updatedList.add(getBalanceList().get(i));
            //アップデート対象かつ数量が0より大きい場合は、受け取ったデータにアップデート
            }else if(balance.getAmount().compareTo(BigDecimal.valueOf(0)) != 0){
                updatedList.add(balance);
            //アップデート対象が0の場合は、追加しない　→つまり削除
            }else{
                //do nothing
            }
        }
        
        try {
            // FileWriterクラスのオブジェクトを生成する
            FileWriter file = new FileWriter(balance_file);
            // PrintWriterクラスのオブジェクトを生成する
            PrintWriter pw = new PrintWriter(new BufferedWriter(file));
            
            //ファイルに書き込む
            for(int i=0; i<updatedList.size(); i++){
                pw.println(updatedList.get(i).toString());
            }

            //ファイルを閉じる
            pw.close();
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

}