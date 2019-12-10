package view;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import model.data.Trade;
import model.data.TradeResult;
import model.data.TradeType;

/**
  * 売買取引データの入力処理のview
  */
public class TradeBondView {
  /**
    * コードを入力させ取得するメソッド
    * @return 入力された銘柄コード
    */
  public String recieveCode() {
    String code = null;
    try {
      BufferedReader br = new BufferedReader
        (new InputStreamReader(System.in));

      System.out.print("銘柄コード>");
      code = br.readLine();

    } catch(IOException e) {
      System.out.println(e);
    }
    return code;
  }

  /**
    * 売買取引内容を入力させその取引内容を返すメソッド
    * @param code キーとなるコード
    * @param existenceOfBalance コードの銘柄をすでに保有していればture
    * @return 取引内容の情報
    */
  public Trade recieveTradeData(String code, boolean existenceOfBalance) {
    Trade trade = null;
    try {
      BufferedReader br = new BufferedReader
        (new InputStreamReader(System.in));

      //売買入力
      System.out.print("0:売 1:買>");
      int typeId = Integer.parseInt(br.readLine());
      //入力が「売買」以外のときはエラーを表示
      if(typeId != 0 && typeId != 1) {
        System.out.println("0(売)か1(買)で指定してください。\n");
        return trade;
      }
      //売りの場合でその銘柄を持っていない場合はエラーを表示
      if(typeId == 0 && !existenceOfBalance) {
        System.out.println("この銘柄を持っていないため、売ることはできません。");
        return trade;
      }

      //価格・数量の入力
      TradeType tradeType = TradeType.valueOf(typeId);
      System.out.print("価格>");
      BigDecimal tradePrice = new BigDecimal(br.readLine());
      System.out.print("数量>");
      BigDecimal tradeAmount = new BigDecimal(br.readLine());

      //取引データの作成（価格・数量がマイナスの時は例外を返す）
      trade = new Trade(code, tradeType, tradePrice, tradeAmount);

    } catch(IOException e) {
      System.out.println(e);
    } catch(NumberFormatException ee) {
      System.out.println("数字を入力してください。");
    } catch(IllegalArgumentException eee) {
      System.out.println("価格と数量は０以上で指定してください。");
    }
    return trade;
  }

  /**
    * 取引結果を表示するメソッド
    * @param tradeResult 取引結果のデータ
    */
  public void showResult(TradeResult tradeResult) {
    //取引金額と実現損益の表示
    System.out.print("取引金額 : " + tradeResult.getPrice());
    //取引が「売り」の場合は実現損益を表示する
    if(tradeResult.getTradeType() == TradeType.SELL) {
      System.out.println(" 実現損益 : " + tradeResult.getRealizedProfit() + "\n");
    } else {
      System.out.println("\n");
    }
  }

  /**
    * マスターファイルに銘柄コードが存在しない時、エラーを表示するメソッド
    * @param code ユーザーが入力した銘柄コード
    */
  public void showError(String code) {
    System.out.println("銘柄コード " + code + " の銘柄は存在しません。\n");
  }

  /**
    * 保有数量がマイナスの時、エラーを表示するメソッド
    */
  public void showError() {
    System.out.println("保有数量がマイナスになってしまいます。");
  }
}
