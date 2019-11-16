package dao;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

import model.data.Balance;
/**
  *銘柄残高ファイルに関する処理をするクラス。
  */
public class BalanceDataDao {
  /**
    *銘柄のコードの保有数量と簿価を取り出すメソッド
    * @param code　探索する銘柄コード
    * @return 探索コードと等しい銘柄情報の配列
    */
  public Balance getBalanceData(String code) {
    Balance balance = null;
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader("csv/balancedata.csv"));

      String line = null;
      while((line = br.readLine()) != null) {
        String[] bondData = line.split(",",-1);
        //一致する銘柄に対する処理
        if(bondData[0].equals(code)) {
          balance = new Balance(new BigDecimal(bondData[1]),new BigDecimal(bondData[2]));
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
      br = new BufferedReader(new FileReader("csv/balancedata.csv"));

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
    *銘柄の簿価と保有数量を更新するメソッド
    * @param code　     銘柄コード
    * @param balance    保有数量・簿価・時価の情報
    * @param mode       更新は0、追加は1
    */
  public void updateBalanceData(String code, Balance balance, boolean mode) {
    String updateLine = code + "," + balance.getAmount() + "," + balance.getBookValue() + "," + balance.getCurrentPrice();

    //銘柄残高の一覧を取得
    List<String> bondList = getBalanceList();

    if(mode) { //更新
      //更新する銘柄の銘柄残高ファイルのなかの行番
      int updateLineNum = getRowNumber(code);

      //保有数量と簿価を更新
      bondList.set(updateLineNum,updateLine);
    } else { //追加
      bondList.add(updateLine);
    }

    //ファイルに書き込み
    writeBalanceData(bondList);
  }
  /**
    *銘柄残高ファイルから１行ずつリストに格納し返すメソッド
    * @return 銘柄残高ファイルにある銘柄情報リスト
    */
  public List<String> getBalanceList() {
    List<String> bondList = new ArrayList<>();

    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader("csv/balancedata.csv"));

      String line = null;
      while((line = br.readLine()) != null) {
        bondList.add(line);
      }
    } catch(IOException e) {
      System.out.println(e);
    } finally {
      if(br != null){
        try {
          br.close();
        } catch(Exception e2) {
          System.out.println(e2);
        }
      }
    }
    return bondList;
  }
  /**
    *リストから１行ずつ銘柄残高ファイルに書き込むメソッド
    * @param bondlist 銘柄の残高情報
    * @return 処理が成功するとtrue失敗するとfalse
    */
  public boolean writeBalanceData(List<String> bondList) {
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new FileWriter("csv/balancedata.csv",false));
      //銘柄残高ファイルに書き込み
      for(String line : bondList) {
        pw.println(line);
      }

      return true;
    } catch(IOException e) {
      System.out.println(e);
    } finally {
      if(pw != null) {
        try {
          pw.close();
        } catch(Exception e2) {
          System.out.println(e2);
        }
      }
    }
    return false;
  }
  /**
    *指定した銘柄コードの銘柄情報が銘柄残高ファイルの何行目にあるか返すメソッド
    * @param code 探索する銘柄コード
    * @return 探索する銘柄コードと同じ銘柄情報が銘柄残高ファイルの何行目にあるか
    */
  private int getRowNumber(String code) {
    int rowNumber = 0;

    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader("csv/balancedata.csv"));

      String line = null;
      while((line = br.readLine()) != null) {
        String[] bondData = line.split(",",-1);
        //探索コードと一致したときの処理
        if(bondData[0].equals(code)) {
          break;
        }
        rowNumber++;
      }
    } catch(IOException e) {
      System.out.println(e);
    } finally {
      if(br != null){
        try {
          br.close();
        } catch(Exception e2) {
          System.out.println(e2);
        }
      }
    }
    return rowNumber;
  }
}
