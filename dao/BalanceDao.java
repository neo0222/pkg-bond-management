package dao;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

import model.data.Balance;
/**
  *表示用の銘柄暫定残高ファイルに関する処理をするクラス。
  */
public class BalanceDao {
  /** 銘柄暫定残高ファイルパス */
  private static final String filePath = "csv/balancedata.csv";
  /**
    *銘柄のコードの保有数量と簿価を取り出すメソッド
    * @param code　探索する銘柄コード
    * @return 探索コードと等しい銘柄情報
    */
  public Balance getBalanceData(String code) {
    Balance balance = null;
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(this.filePath));

      String line = null;
      while((line = br.readLine()) != null) {
        String[] balanceData = line.split(",", -1);
        //一致する銘柄に対する処理
        if(balanceData[0].equals(code)) {
          balance = new Balance(code, new BigDecimal(balanceData[1]),new BigDecimal(balanceData[2]));
          break;
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
    return balance;
  }
  /**
    *探索する銘柄コードと一致する銘柄の有無を確認するメソッド
    * @param code　探索する銘柄コード
    * @return 探索する銘柄コードに等しい銘柄が存在すればtrue
    */
  public boolean isExistBalance(String code) {
    boolean result = false;
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(this.filePath));

      String line = null;
      while((line = br.readLine()) != null) {
        String[] balanceData = line.split(",", -1);
        //一致する銘柄があったときの処理
        if(balanceData[0].equals(code)) {
          result = true;
          break;
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
    return result;
  }
  /**
    *銘柄の簿価と保有数量を更新するメソッド
    * @param balance 銘柄コード・保有数量・簿価・時価の情報
    */
  public void updateBalanceData(Balance balance) {
    //銘柄残高の一覧を取得
    List<Balance> balanceList = this.getBalanceList();

    //更新する銘柄の銘柄残高ファイル中の行番
    int updateLineNum = this.getRowNumber(balance.getCode());
    //保有数量と簿価を更新
    balanceList.set(updateLineNum, balance);

    //ファイルに書き込み
    this.writeBalanceData(balanceList);
  }
  /**
    *銘柄残高ファイルに新たな銘柄を追加するメソッド
    * @param balance 銘柄コード・保有数量・簿価・時価の情報
    */
  public void putBalanceData(Balance balance) {
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new FileWriter(this.filePath,true));
      //銘柄残高ファイルの末尾に書き込み
      pw.println(balance.toString());

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
  }
  /**
    *銘柄残高ファイルから１行ずつリストに格納し返すメソッド
    * @return 銘柄残高ファイルにある銘柄情報リスト
    */
  public List<Balance> getBalanceList() {
    List<Balance> balanceList = new ArrayList<>();

    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(this.filePath));

      String line = null;
      while((line = br.readLine()) != null) {
        String[] balanceData = line.split(",", -1);
        Balance balance = new Balance(balanceData[0], new BigDecimal(balanceData[1]),
                      new BigDecimal(balanceData[2]), new BigDecimal(balanceData[3]));
        balanceList.add(balance);
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
    return balanceList;
  }
  /**
    *リストから１行ずつ銘柄残高ファイルに書き込むメソッド
    * @param balanceList 銘柄の残高情報
    */
  public void writeBalanceData(List<Balance> balanceList) {
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new FileWriter(this.filePath,false));
      //銘柄残高ファイルに書き込み
      for(Balance balance : balanceList) {
        pw.println(balance.toString());
      }
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
      br = new BufferedReader(new FileReader(this.filePath));

      String line = null;
      while((line = br.readLine()) != null) {
        String[] balanceData = line.split(",", -1);
        //探索コードと一致したときの処理
        if(balanceData[0].equals(code)) {
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
