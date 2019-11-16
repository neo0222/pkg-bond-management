package dao;

import java.io.*;
import java.math.BigDecimal;

import model.data.Bond;

/**
  *銘柄マスターデータファイルに関する処理をするクラス。
  */
public class BondDao {
  /**
    *探索する銘柄コードと一致する銘柄の有無を確認するメソッド
    * @param code　探索する銘柄コード
    * @return 探索する銘柄コードに等しい銘柄が存在すればtrue
    */
  public boolean isExistBond(String code) {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader("csv/masterdata.csv"));

      String line = null;
      while((line = br.readLine()) != null) {
        String[] bond = line.split(",",-1);
        //一致する銘柄があったときの処理
        if(bond[0].equals(code)) {
          return true;
        }
      }
    } catch(IOException e) {
      System.out.println(e);
    } finally {
      if(br != null){
        try {
          br.close();
        } catch(IOException e2) {
          System.out.println(e2);
        }
      }
    }
    return false;
  }
  /**
    *銘柄のコードを探索し、情報を取り出すメソッド
    * @param code　探索する銘柄コード
    * @return 探索する銘柄コードに等しい銘柄のマスターデータ
    */
  public Bond getMasterData(String code) {
    Bond bond = null;
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader("csv/masterdata.csv"));

      String line = null;
      while((line = br.readLine()) != null) {
        String[] bondData = line.split(",",-1);
        //一致する銘柄があったときの処理
        if(bondData[0].equals(code)) {
          bond = new Bond(code, bondData[1], new BigDecimal(bondData[2]), Integer.parseInt(bondData[3]), Integer.parseInt(bondData[4]));
          return bond;
        }
      }
    } catch(IOException e) {
      System.out.println(e);
    } finally {
      if(br != null){
        try {
          br.close();
        } catch(IOException e2) {
          System.out.println(e2);
        }
      }
    }
    return null;
  }
}
