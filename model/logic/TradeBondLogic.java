package model.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;

import model.data.Balance;
import model.data.Trade;
import model.data.TradeResult;
import model.data.TradeType;
import dao.BalanceDao;
import dao.TradeDao;

/**
  * 売買取引データの入力処理をするlogicmodel
  */
public class TradeBondLogic {
  /** 残高データのDAO　*/
  private BalanceDao balanceDao = new BalanceDao();
  /** 取引データのDAO　*/
  private TradeDao tradeDao = new TradeDao();

  /**
    * 取引データを入力する処理をするメソッド
    * @param trade 売買取引の情報
    * @return 売買取引の結果
    */
  public TradeResult execute(Trade trade) {
    String code = trade.getCode();
    TradeType tradeType = trade.getTradeType();
    BigDecimal tradePrice = trade.getPrice();
    BigDecimal tradeAmount = trade.getAmount();

    //取引金額
    BigDecimal resultPrice = tradePrice.multiply(tradeAmount);

    //銘柄残高ファイルへの書き込み・実現損益の計算
    //実現損益
    BigDecimal realizedProfit = null;
    //指定のコードの銘柄を保有しているか確認
    if(this.balanceDao.isExistBalance(code)) { //保有しているときの処理
      //銘柄の保有数量と簿価を取得
      Balance balance = this.balanceDao.getBalanceData(code);
      BigDecimal oldAmount = balance.getAmount();
      BigDecimal oldBookValue = balance.getBookValue();

      //計算のために取引が「売り」の場合は数量をマイナスにする
      if(tradeType == TradeType.SELL) {
        //実現損益の計算
        realizedProfit = tradePrice.subtract(oldBookValue).multiply(tradeAmount);
        tradeAmount = tradeAmount.negate();
      }

      //保有数量の更新
      BigDecimal newAmount = oldAmount.add(tradeAmount);

      //保有数量が0未満ならエラーを表示
      if(newAmount.compareTo(BigDecimal.ZERO) == -1) {
        return null;
      }

      //簿価の更新
      BigDecimal newBookValue = BigDecimal.ZERO;
      if(tradeAmount.compareTo(BigDecimal.ZERO) == -1) { //売りの場合は簿価は変わらない
        newBookValue = oldBookValue;
      } else if(!newAmount.equals(BigDecimal.ZERO)) {
        newBookValue = (oldAmount.multiply(oldBookValue).add(tradeAmount.multiply(tradePrice)))
                                .divide(newAmount, 3, RoundingMode.FLOOR);
      }

      //銘柄残高ファイル上の保有数量と簿価の更新
      balance.setAmount(newAmount);
      balance.setBookValue(newBookValue);
      this.balanceDao.updateBalanceData(balance);

    } else { //保有していないときの処理
      Balance balance = new Balance(code, tradeAmount, tradePrice.setScale(3, RoundingMode.FLOOR));
      this.balanceDao.putBalanceData(balance);
    }
    //取引リストの更新
    tradeDao.putTradeData(trade);

    return new TradeResult(tradeType, resultPrice, realizedProfit);
  }
}
