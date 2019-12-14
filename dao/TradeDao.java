package dao;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

import model.data.Trade;
import model.data.TradeType;
/**
  * 取引ファイルに関する処理をするクラス。
  */
public class TradeDao {
  /** 取引ファイルパス */
  private static final String filePath = "csv/tradedata.csv";
  /** 取引ファイルの存在自体を操作するためのPathインスタンス */
  private final Path path = Paths.get(filePath);

  /**
    * 取引ファイルが存在するか確認するメソッド
    * @return 取引ファイルが存在すればtrue
    */
  public boolean isExistTradeData() {
    return Files.exists(path);
  }
  /**
    * 取引ファイルを削除するメソッド
    */
  public void deleteTradeData() {
    try {
      Files.delete(path);
    } catch(IOException e) {
      System.out.println(e);
    }
  }
  /**
    * 探索する銘柄コードと一致する銘柄の取引の有無を確認するメソッド
    * @param code　探索する銘柄コード
    * @return 探索する銘柄コードの取引が存在すればtrue
    */
  public boolean isExistTrade(String code) {
    boolean result = false;
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(this.filePath));

      String line = null;
      while((line = br.readLine()) != null) {
        String[] tradeData = line.split(",", -1);
        //一致する銘柄があったときの処理
        if(tradeData[0].equals(code)) {
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
    * 取引ファイルに新たな銘柄を追加するメソッド
    * @param trade 銘柄コード・売買・取引価格・取引数量の情報
    */
  public void putTradeData(Trade trade) {
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new FileWriter(this.filePath,true));
      //銘柄残高ファイルの末尾に書き込み
      pw.println(trade.toString());

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
    * 取引ファイルから１行ずつリストに格納し返すメソッド
    * @return 取引ファイルにある取引リスト
    */
  public List<Trade> getTradeList() {
    List<Trade> tradeList = new ArrayList<>();

    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(this.filePath));

      String line = null;
      while((line = br.readLine()) != null) {
        String[] tradeData = line.split(",", -1);
        Trade trade = new Trade(tradeData[0], TradeType.valueOf(tradeData[1]),
                      new BigDecimal(tradeData[2]), new BigDecimal(tradeData[3]));
        tradeList.add(trade);
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
    return tradeList;
  }
  /**
    * リストから１行ずつ取引ファイルに書き込むメソッド
    * @param tradeList 取引のリスト
    */
  public void writeTradeData(List<Trade> tradeList) {
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new FileWriter(this.filePath,false));
      //銘柄残高ファイルに書き込み
      for(Trade trade : tradeList) {
        pw.println(trade.toString());
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
}
