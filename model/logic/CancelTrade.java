package model.logic;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import dao.SettledBalanceDao;
import dao.TradeDao;
import model.data.Balance;
import model.data.Trade;
import model.data.TradeType;

/**
  *取引キャンセルの処理。
  *取引データを取り消す処理をするクラス。
  */
public class CancelTrade {
  /** 確定残高データのDAO　*/
  private SettledBalanceDao settledBalanceDao = new SettledBalanceDao();
  /** 取引データのDAO　*/
  private TradeDao tradeDao = new TradeDao();

  /**
    *取引データを取り消す処理をするメソッド
    */
  public void execute() {
    //取引データがない場合はエラーを表示
    if(!tradeDao.isExsistTradeData()) {
      System.out.println("当日の取引はありません。");
      return;
    }

    try {
      BufferedReader br = new BufferedReader
        (new InputStreamReader(System.in));

      //取引の銘柄コードを入力
      System.out.print("キャンセルする取引の銘柄コード>");
      String code = br.readLine();

      //探索するコードの銘柄の取引データがない場合はエラーを表示
      if(!tradeDao.isExistTrade(code)) {
        System.out.println("銘柄コード " + code + " の取引はありません。");
        return;
      }

      //取引データの一覧を取得
      List<Trade> tradeList = tradeDao.getTradeList();
      //取り消す銘柄コードの取引データリスト中の番号リスト
      List<Integer> tradeNums = new ArrayList<>();
      //取引によって保有数量がマイナスにならないか確認するための数量
      BigDecimal amount = null;
      if(settledBalanceDao.isExistBond(code)) {
        amount = settledBalanceDao.getBalanceData(code).getAmount();
      } else {
        amount = BigDecimal.valueOf(0);
      }

      //選択した銘柄コードの取引リストを表示、番号リストの取得、保有数量の計算
      System.out.println("\n<銘柄コード " + code + " の取引データ一覧>");
      System.out.printf("|%-4s|%-3s|%-6s|%-6s|\n", "取引番号", "売買", "取引価格", "取引数量");
      for(int i = 0; i < tradeList.size(); i++) {
        Trade trade = tradeList.get(i);
        if(trade.getCode().equals(code)) {
          tradeNums.add(i);
          System.out.printf("|%8d|%-5s|%10s|%10s|\n", i, trade.getTradeType().toString(),
                  trade.getPrice().setScale(3, BigDecimal.ROUND_DOWN).toString(),
                  trade.getAmount().setScale(3, BigDecimal.ROUND_DOWN).toString());
          //保有数量の計算（買いなら加算、売りなら減算）
          if(trade.getTradeType() == TradeType.BUY) {
            amount = amount.add(trade.getAmount());
          } else {
            amount = amount.subtract(trade.getAmount());
          }
        }
      }
      //取引番号を入力
      System.out.print("\nキャンセルする取引の取引番号を指定してください>");
      int cancelNum = Integer.parseInt(br.readLine());

      //入力された番号が取り消し候補にない場合はエラーを表示
      boolean tradeExistence = false;
      for(Integer tradeNum : tradeNums) {
        if(cancelNum == tradeNum) {
          tradeExistence = true;
          break;
        }
      }
      if(!tradeExistence) {
        System.out.println("取引番号 " + cancelNum + "、銘柄コード " + code + " の取引は存在しません。");
        return;
      }

      //数量の計算
      if(tradeList.get(cancelNum).getTradeType() == TradeType.BUY) {
        amount = amount.subtract(tradeList.get(cancelNum).getAmount());
      } else {
        amount = amount.add(tradeList.get(cancelNum).getAmount());
      }

      //保有数量がマイナスならエラーを表示
      if(amount.compareTo(BigDecimal.valueOf(0)) == -1) {
        System.out.println("保有数量がマイナスになってしまいます。");
      }
      //取引リストから選択された取引を取り消す
      tradeList.remove(cancelNum);
      //取引リストに書き込む
      tradeDao.writeTradeData(tradeList);

    } catch(IOException e) {
      System.out.println(e);
    } catch(NumberFormatException e2) {
      System.out.println("数字で指定してください。");
    }
  }
}
