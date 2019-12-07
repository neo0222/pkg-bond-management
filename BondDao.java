import java.io.*;
import java.util.*;
import javax.print.event.PrintJobListener;
import java.math.*;
import java.lang.NullPointerException;

class BondDao {
    private static final String bond_file = "bond_data.csv";
    //対象のBondが存在するかどうかを返却するメソッド
    public boolean IsExist(String inputCode){
        try {
            FileReader fileName = new FileReader(bond_file);
            BufferedReader br = new BufferedReader(fileName);

            //読み込んだファイルを1行ずつ処理
            String line;
            while((line = br.readLine()) != null){
                //空白であっても文字列として取得する
                String[] bondDataStr = line.split(",", -1);
                if (bondDataStr[0].equals(inputCode)){
                    br.close();
                    return true;
                }
            }
            br.close();
        } catch(IOException ex) {
            System.out.println(ex);
        }
        return false;
    }

    public Bond getBond(String inputCode){
        try {
            FileReader fileName = new FileReader(bond_file);
            BufferedReader br = new BufferedReader(fileName);

            //読み込んだファイルを1行ずつ処理
            String line;

            while((line = br.readLine()) != null){
                String[] bondDataStr = line.split(",", -1);
                if(bondDataStr[0].equals(inputCode)){
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
            //ファイルが存在しなかった場合
            System.out.println(ie);
        } catch (NullPointerException ne){
            //コンマの数が合わなかった場合
            System.out.println(ne);
        }
        return null;
    }

    public List<Bond> getBondList(){
        List<Bond> bondList = new ArrayList<Bond>();
        try {
            FileReader fileName = new FileReader(bond_file);
            BufferedReader br = new BufferedReader(fileName);

            //読み込んだファイルを1行ずつ処理
            String line;

            while((line = br.readLine()) != null){
                String[] bondDataStr = line.split(",", -1);

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
            //ファイルが存在しなかった場合
            System.out.println(ie);
        }
        return bondList;
    }

    public void showBondList(){
        for(int i=0; i < this.getBondList().size(); i++){
            System.out.print("  ");
            this.getBondList().get(i).printBond();
        }
    }
}