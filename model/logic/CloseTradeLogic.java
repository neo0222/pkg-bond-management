package model.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Iterator;

import model.data.Balance;
import model.data.Trade;
import model.data.TradeType;
import dao.BalanceDao;
import dao.SettledBalanceDao;
import dao.TradeDao;

/**
  * 締め処理をするlogicmodel
  */
public class CloseTradeLogic {
  /** 暫定残高データのDAO　*/
  private BalanceDao balanceDao = new BalanceDao();
  /** 確定残高データのDAO　*/
  private SettledBalanceDao settledBalanceDao = new SettledBalanceDao();
  /** 取引データのDAO　*/
  private TradeDao tradeDao = new TradeDao();

  /**
    * 締め処理をするメソッド
    * @return 処理が完了したらtrue
    */
  public boolean execute() {
    //前日の取引一覧を取得
    List<Trade> tradeList = this.tradeDao.getTradeList();

    for(Trade trade : tradeList) {
      String code = trade.getCode();
      TradeType tradeType = trade.getTradeType();
      BigDecimal tradePrice = trade.getPrice();
      BigDecimal tradeAmount = trade.getAmount();

      if(tradeType == TradeType.SELL) { //取引が売りの場合は取引数量をマイナスにする
        tradeAmount = tradeAmount.negate();
      }

      if(this.settledBalanceDao.isExistBalance(code)) { //指定コードの銘柄をすでに保有している場合
        Balance balance = this.settledBalanceDao.getBalanceData(code);
        BigDecimal oldAmount = balance.getAmount();
        BigDecimal oldBookValue = balance.getBookValue();

        //保有量の更新
        BigDecimal newAmount = oldAmount.add(tradeAmount);

        //簿価の更新
        BigDecimal newBookValue = BigDecimal.ZERO;
        if(oldAmount.compareTo(BigDecimal.ZERO) < 0) { //残高がマイナスなら簿価は取引の価格にする
          newBookValue = tradePrice.setScale(3, RoundingMode.FLOOR);
        } else if(tradeAmount.compareTo(BigDecimal.ZERO) < 0) { //売りのときは簿価は変わらない
          newBookValue = oldBookValue;
        } else if(!newAmount.equals(BigDecimal.ZERO)){
          newBookValue = (oldAmount.multiply(oldBookValue).add(tradeAmount.multiply(tradePrice)))
                                  .divide(newAmount,3,RoundingMode.FLOOR);
        }

        //銘柄残高ファイル上の保有数量と簿価の更新
        balance.setAmount(newAmount);
        balance.setBookValue(newBookValue);
        this.settledBalanceDao.updateBalanceData(balance);

      } else { //指定コードの銘柄をまだ保有していない場合
        if(tradeAmount.compareTo(BigDecimal.ZERO) < 0) {
          tradePrice = BigDecimal.ZERO;
        }
        Balance balance = new Balance(code, tradeAmount, tradePrice.setScale(3, RoundingMode.FLOOR));
        this.settledBalanceDao.putBalanceData(balance);
      }
    }
    //保有量が０の銘柄の残高情報は削除する
    List<Balance> balanceList = this.settledBalanceDao.getBalanceList();
    Iterator<Balance> balanceIterator = balanceList.iterator();
    while(balanceIterator.hasNext()) {
      Balance balance = balanceIterator.next();
      if(balance.getAmount().equals(BigDecimal.ZERO)) {
        balanceIterator.remove();
      }
    }

    //確定残高リストを更新
    this.settledBalanceDao.writeBalanceData(balanceList);
    //暫定残高リストを更新
    this.balanceDao.writeBalanceData(balanceList);
    //取引リストの削除
    this.tradeDao.deleteTradeData();

    return true;
  }
}
