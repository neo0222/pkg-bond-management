package dao;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

import model.data.Balance;
/**
  *銘柄残高ファイルに関する処理をするクラス。
  */
public class SettledBalanceDao {
  /** 銘柄残高ファイルパス */
  private final String filePath = "csv/settledbalancedata.csv";
  /**
    *銘柄のコードの保有数量と簿価を取り出すメソッド
    * @param code　探索する銘柄コード
    * @return 探索コードと等しい銘柄情報の配列
    */
  public Balance getBalanceData(String code) {
    Balance balance = null;
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(filePath));

      String line = null;
      while((line = br.readLine()) != null) {
        String[] bondData = line.split(",",-1);
        //一致する銘柄に対する処理
        if(bondData[0].equals(code)) {
          balance = new Balance(code, new BigDecimal(bondData[1]),new BigDecimal(bondData[2]));
          return balance;
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
  /**
    *探索する銘柄コードと一致する銘柄の有無を確認するメソッド
    * @param code　探索する銘柄コード
    * @return 探索する銘柄コードに等しい銘柄が存在すればtrue
    */
  public boolean isExistBond(String code) {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(filePath));

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
}
