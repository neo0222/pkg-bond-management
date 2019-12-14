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
    * @return 保有数量が明茄子になる場合はfalse
    */
  public boolean execute(String code, int tradeNumToBeCanceled, List<Trade> tradeList) {
    //取引によって保有数量がマイナスにならないか確認するための数量
    BigDecimal amount = BigDecimal.ZERO;
    if(this.settledBalanceDao.isExistBalance(code)) {
      amount = this.settledBalanceDao.getBalanceData(code).getAmount();
    }

    for(Trade trade: tradeList) {
      if(trade.getCode().equals(code)) {
        //保有数量の計算（買いなら加算、売りなら減算）
        if(trade.getTradeType() == TradeType.BUY) {
          amount = amount.add(trade.getAmount());
        } else {
          amount = amount.subtract(trade.getAmount());
        }
      }
    }
    //キャンセルする取引
    Trade tradeToBeCanceled = tradeList.get(tradeNumToBeCanceled);

    //キャンセルする取引の数量計算
    if(tradeToBeCanceled.getTradeType() == TradeType.BUY) {
      amount = amount.subtract(tradeToBeCanceled.getAmount());
    } else {
      amount = amount.add(tradeToBeCanceled.getAmount());
    }

    //保有数量がマイナスならエラーを表示
    if(amount.compareTo(BigDecimal.ZERO) < 0) {
      return false;
    }

    //表示用の残高情報を変更する
    List<Balance> balanceList = this.balanceDao.getBalanceList();
    for(Balance balance : balanceList) {
      if(balance.getCode().equals(code)) {
        BigDecimal oldAmount = balance.getAmount();
        BigDecimal oldBookValue = balance.getBookValue();
        BigDecimal amountToBeCanceled = tradeToBeCanceled.getAmount();
        if(tradeToBeCanceled.getTradeType() == TradeType.BUY) {
          amountToBeCanceled = amountToBeCanceled.negate();
        }
        BigDecimal newAmount = oldAmount.add(amountToBeCanceled);
        BigDecimal newBookValue = BigDecimal.ZERO;
        if(tradeToBeCanceled.getTradeType() == TradeType.SELL) {
          newBookValue = oldBookValue;
        } else if(!newAmount.equals(BigDecimal.ZERO)) {
          newBookValue = oldBookValue.multiply(oldAmount).add(tradeToBeCanceled.getPrice().multiply(amountToBeCanceled))
          .divide(newAmount, 3, RoundingMode.FLOOR);
        }
        balance.setAmount(newAmount);
        balance.setBookValue(newBookValue);
        this.balanceDao.updateBalanceData(balance);
        break;
      }
    }

    //取引リストから選択された取引を取り消す
    tradeList.remove(tradeNumToBeCanceled);
    //取引リストに書き込む
    this.tradeDao.writeTradeData(tradeList);

    return true;
  }
}
