import java.io.*;
import java.util.*;
import javax.print.event.PrintJobListener;
import java.math.*;

class BondDao {
    public boolean IsExist(String inputCode){
        try {
            FileReader fileName = new FileReader("bond_data.csv");
            BufferedReader br = new BufferedReader(fileName);

            //読み込んだファイルを1行ずつ処理
            String line;
            while((line = br.readLine()) != null){
                String[] bondDataStr = line.split(",", 0);
                if (bondDataStr[0].equals(inputCode)){
                    br.close();
                    return true;
                }
            }
            br.close();
        } catch(IOException ex) {
            System.out.println("ファイルが存在しません");
        }
        return false;
    }

    public Bond getBond(String inputCode){
        try {
            FileReader fileName = new FileReader("bond_data.csv");
            BufferedReader br = new BufferedReader(fileName);

            //読み込んだファイルを1行ずつ処理
            String line;

            while((line = br.readLine()) != null){
                String[] bondDataStr = line.split(",", 0);
                if(bondDataStr[0] == inputCode){
                    String code = bondDataStr[0];
                    String name = bondDataStr[1];
                    BigDecimal interestRate = new BigDecimal(bondDataStr[2]);
                    int redemptionDate = Integer.parseInt(bondDataStr[3]);
                    int coupon = Integer.parseInt(bondDataStr[4]);
                    //Bondを生成
                    Bond bond = new Bond(code, name, interestRate, redemptionDate, coupon);
                    br.close();
                    return bond;
                }
            }
            br.close();
        } catch (IOException ie) {
            System.out.println("ファイルが存在しません");
        }
        return null;
    }

    public List<Bond> getBondList(){
        List<Bond> bondList = new ArrayList<Bond>();
        try {
            FileReader fileName = new FileReader("bond_data.csv");
            BufferedReader br = new BufferedReader(fileName);

            //読み込んだファイルを1行ずつ処理
            String line;

            while((line = br.readLine()) != null){
                String[] bondDataStr = line.split(",", 0);

                //Bondを生成するために型変換を行う
                String code = bondDataStr[0];
                String name = bondDataStr[1];
                BigDecimal interestRate = new BigDecimal(bondDataStr[2]);
                int redemptionDate = Integer.parseInt(bondDataStr[3]);
                int coupon = Integer.parseInt(bondDataStr[4]);
                
                //Bondを生成
                Bond bond = new Bond(code, name, interestRate, redemptionDate, coupon);

                //bondListに追加
                bondList.add(bond);
            }
            br.close();
        } catch (IOException ie) {
            System.out.println("ファイルが存在しません");
        }
        return bondList;
    }
}