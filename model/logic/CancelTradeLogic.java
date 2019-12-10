package model.logic;

import java.math.BigDecimal;
import java.util.List;

import model.data.Balance;
import model.data.Trade;
import model.data.TradeType;
import dao.SettledBalanceDao;
import dao.TradeDao;

/**
  * 取引データを取り消す処理をするlogicmodel
  */
public class CancelTradeLogic {
  /** 確定残高データのDAO　*/
  private SettledBalanceDao settledBalanceDao = new SettledBalanceDao();
  /** 取引データのDAO　*/
  private TradeDao tradeDao = new TradeDao();

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
    if(amount.compareTo(BigDecimal.ZERO) == -1) {
      return false;
    }
    //取引リストから選択された取引を取り消す
    tradeList.remove(tradeNumToBeCanceled);
    //取引リストに書き込む
    this.tradeDao.writeTradeData(tradeList);

    return true;
  }
}
