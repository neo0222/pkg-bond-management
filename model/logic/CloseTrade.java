package model.logic;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import dao.BalanceDao;
import dao.SettledBalanceDao;
import dao.TradeDao;
import model.data.Balance;
import model.data.Trade;
import model.data.TradeType;

/**
  *締め処理
  *締め処理をするクラス。
  */
public class CloseTrade {
  /** 暫定残高データのDAO　*/
  private BalanceDao balanceDao = new BalanceDao();
  /** 確定残高データのDAO　*/
  private SettledBalanceDao settledBalanceDao = new SettledBalanceDao();
  /** 取引データのDAO　*/
  private TradeDao tradeDao = new TradeDao();

  /**
    *締め処理をするメソッド
    */
  public void execute() {
    //取引データがない場合はエラーを表示
    if(!this.tradeDao.isExsistTradeData()) {
      System.out.println("前日の取引の締め処理は完了しています。");
      return;
    }
    //前日の取引一覧を取得
    List<Trade> tradeList = this.tradeDao.getTradeList();

    for(Trade trade : tradeList) {
      String code = trade.getCode();
      TradeType tradeType = trade.getTradeType();
      BigDecimal tradePrice = trade.getPrice();
      BigDecimal tradeAmount = trade.getAmount();

      if(tradeType == TradeType.SELL) { //取引が売りの場合は取引数量をマイナスにする
        tradeAmount = BigDecimal.valueOf(0).subtract(tradeAmount);
      }

      if(this.settledBalanceDao.isExistBond(code)) { //指定コードの銘柄をすでに保有している場合
        Balance balance = this.settledBalanceDao.getBalanceData(code);
        BigDecimal oldAmount = balance.getAmount();
        BigDecimal oldBookValue = balance.getBookValue();

        //保有量の更新
        BigDecimal newAmount = oldAmount.add(tradeAmount);

        //簿価の更新
        BigDecimal newBookValue = null;
        if(newAmount.compareTo(BigDecimal.valueOf(1)) == -1) {
          newBookValue = BigDecimal.valueOf(0);
        } else {
          newBookValue = (oldAmount.multiply(oldBookValue).add(tradeAmount.multiply(tradePrice)))
                                  .divide(newAmount,3,BigDecimal.ROUND_DOWN);
        }

        //銘柄残高ファイル上の保有数量と簿価の更新
        balance.setAmount(newAmount);
        balance.setBookValue(newBookValue);
        this.settledBalanceDao.updateBalanceData(balance);

      } else { //指定コードの銘柄をまだ保有していない場合
        if(tradeAmount.compareTo(BigDecimal.valueOf(1)) == -1) {
          tradePrice = BigDecimal.valueOf(0);
        }
        Balance balance = new Balance(code, tradeAmount, tradePrice);
        this.settledBalanceDao.putBalanceData(balance);
      }
    }
    //保有量が０の銘柄の残高情報は削除する
    List<Balance> balanceList = this.settledBalanceDao.getBalanceList();
    while(true) {
      //保有量が０の銘柄がなければtrue
      boolean isNoZero = true;
      for(int i = 0; i < balanceList.size(); i++) {
        Balance balance = balanceList.get(i);
        if(balance.getAmount().equals(BigDecimal.valueOf(0))) {
          balanceList.remove(i);
          isNoZero = false;
          break;
        }
      }
      if(isNoZero) {
        break;
      }
    }
    //確定残高リストを更新
    this.settledBalanceDao.writeBalanceData(balanceList);
    //暫定残高リストを更新
    this.balanceDao.writeBalanceData(balanceList);
    //取引リストの削除
    this.tradeDao.deleteTradeData();
  }
}