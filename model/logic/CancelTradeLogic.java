package model.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import model.data.Balance;
import model.data.Trade;
import model.data.TradeType;
import dao.BalanceDao;
import dao.SettledBalanceDao;
import dao.TradeDao;

/**
  * 取引データを取り消す処理をするlogicmodel
  */
public class CancelTradeLogic {
  /** 表示用残高データのDAO　*/
  private final BalanceDao balanceDao = new BalanceDao();
  /** 確定残高データのDAO　*/
  private final SettledBalanceDao settledBalanceDao = new SettledBalanceDao();
  /** 取引データのDAO　*/
  private final TradeDao tradeDao = new TradeDao();

  /**
    * 取引データを取り消す処理をするメソッド
    * @param code キャンセルされる取引の銘柄コード
    * @param tradeNumToBeCanceled キャンセルされる取引番号
    * @param tradeList 取引ファイルにある全ての取引データ
    * @return 保有数量がマイナスになる場合はfalse
    */
  public boolean execute(String code, int tradeNumToBeCanceled, List<Trade> tradeList) {
    //取引によって保有数量がマイナスにならないか確認するための数量
    BigDecimal amountToBeChecked = BigDecimal.ZERO;
    if(this.settledBalanceDao.isExistBalance(code)) {
      amountToBeChecked = this.settledBalanceDao.getBalanceData(code).getAmount();
    }
    //取引リストから選択された取引を取り消す
    tradeList.remove(tradeNumToBeCanceled);
    
    //保有数量のマイナスチェック
    for(Trade trade: tradeList) {
      if(trade.getCode().equals(code)) {
        //保有数量の計算（買いなら加算、売りなら減算）
        if(trade.getTradeType() == TradeType.BUY) {
          amountToBeChecked = amountToBeChecked.add(trade.getAmount());
        } else {
          amountToBeChecked = amountToBeChecked.subtract(trade.getAmount());
          if(amountToBeChecked.compareTo(BigDecimal.ZERO) < 0) {
            return false;
          }
        }
      }
    }

    //表示用の残高情報を変更する
    BigDecimal amount = BigDecimal.ZERO;
    BigDecimal bookValue = BigDecimal.ZERO;
    BigDecimal currentPrice = BigDecimal.ONE.negate();

    //確定残高に在庫がある場合はデータを持ってくる
    if(this.settledBalanceDao.isExistBalance(code)) {
      amount = this.settledBalanceDao.getBalanceData(code).getAmount();
      bookValue = this.settledBalanceDao.getBalanceData(code).getBookValue();
    }

    //表示用残高の方の時価がある場合は取ってくる
    if(this.balanceDao.isExistBalance(code)) {
      currentPrice = this.balanceDao.getBalanceData(code).getCurrentPrice();
    }
    for(Trade trade : tradeList) {
      if(trade.getCode().equals(code)) {
        BigDecimal tradeAmount = trade.getAmount();
        BigDecimal tradePrice = trade.getPrice();
        BigDecimal oldAmount = amount;

        if(trade.getTradeType() == TradeType.SELL) {//売りの場合
          amount = amount.subtract(tradeAmount);
          //簿価の更新
          if(amount.equals(BigDecimal.ZERO)) {
            bookValue = BigDecimal.ZERO;
          }
        } else { //買いの場合
          amount = amount.add(tradeAmount);
          //簿価の更新
          if(amount.equals(BigDecimal.ZERO)) {
            bookValue = BigDecimal.ZERO;
          } else {
            bookValue = bookValue.multiply(oldAmount).add(tradePrice.multiply(tradeAmount))
            .divide(amount, 3, RoundingMode.FLOOR);
          }
        }
      }
    }
    Balance balance = new Balance(code, amount, bookValue, currentPrice);
    this.balanceDao.updateBalanceData(balance);

    //取引リストに書き込む
    this.tradeDao.writeTradeData(tradeList);

    return true;
  }
}
