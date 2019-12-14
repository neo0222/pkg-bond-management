package view;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ArrayList;

import model.data.Trade;
import model.data.TradeType;

/**
  * 取引データを取り消す処理のview
  */
public class CancelTradeView {
  /**
    * コードを入力させ取得するメソッド
    * @return 入力された銘柄コード
    */
  public String recieveCode() {
    String code = null;
    try {
      BufferedReader br = new BufferedReader
        (new InputStreamReader(System.in));

      System.out.print("キャンセルする取引の銘柄コード>");
      code = br.readLine();

    } catch(IOException e) {
      System.out.println(e);
    }
    return code;
  }

  /**
    * キャンセルする取引の番号を入力させ取得するメソッド
    * @param code キャンセルする取引の銘柄コード　
    * @param tradeList 当日の全ての取引のリスト
    * @return キャンセルする取引番号
    */
  public int recieveCancelTradeNumber(String code, List<Trade> tradeList) {
    int tradeNumToBeCanceled = -1;
    //取り消す銘柄コードの取引データリスト中の番号リスト
    List<Integer> tradeNums = new ArrayList<>();
    //選択した銘柄コードの取引リストを表示、番号リストの取得、保有数量の計算
    System.out.println("\n<銘柄コード " + code + " の取引データ一覧>");
    System.out.printf("|%-4s|%-3s|%-6s|%-6s|\n", "取引番号", "売買", "取引価格", "取引数量");
    for(int i = 0; i < tradeList.size(); i++) {
      Trade trade = tradeList.get(i);
      if(trade.getCode().equals(code)) {
        tradeNums.add(i);
        System.out.printf("|%8d|%-5s|%10s|%10s|\n", i, trade.getTradeType().toString(),
                trade.getPrice().setScale(3, RoundingMode.FLOOR).toString(),
                trade.getAmount().toString());
      }
    }
    try {
      BufferedReader br = new BufferedReader
        (new InputStreamReader(System.in));

      //取引番号を入力
      System.out.print("\nキャンセルする取引の取引番号を指定してください>");
      tradeNumToBeCanceled = Integer.parseInt(br.readLine());

      //入力された番号が取り消し候補にない場合はエラーを表示
      boolean tradeExistence = false;
      for(Integer tradeNum : tradeNums) {
        if(tradeNumToBeCanceled == tradeNum) {
          tradeExistence = true;
          break;
        }
      }
      if(!tradeExistence) {
        System.out.println("取引番号 " + tradeNumToBeCanceled + "、銘柄コード " + code + " の取引は存在しません。");
        tradeNumToBeCanceled = -1;
      }

    } catch(IOException e) {
      System.out.println(e);
    } catch(NumberFormatException e2) {
      System.out.println("数字で指定してください。");
    }
    return tradeNumToBeCanceled;
  }

  /**
    * 取引データがない旨を表示するメソッド
    */
  public void showNothingInTradeList() {
    System.out.println("当日の取引はありません。\n");
  }

  /**
    * 保有数量がマイナスの時、エラーを表示するメソッド
    */
  public void showError() {
    System.out.println("保有数量がマイナスになってしまいます。");
  }

  /**
    * 取引データに銘柄コードが存在しない時、エラーを表示するメソッド
    * @param code ユーザーが入力したコード
    */
  public void showError(String code) {
    System.out.println("銘柄コード " + code + " の取引はありません。");
  }
}
