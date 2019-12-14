package controller;

import java.util.List;

import view.CancelTradeView;
import model.data.Trade;
import model.logic.CancelTradeLogic;
import dao.TradeDao;

/**
  * 取引キャンセルの処理クラス
  * 取引データを取り消す処理をするcontroller
  */
public class CancelTrade {
  /** 取引データを取り消す処理のview */
  private final CancelTradeView cancelTradeView = new CancelTradeView();
  /** 取引データを取り消す処理をするlogicmodel */
  private final CancelTradeLogic cancelTradeLogic = new CancelTradeLogic();
  /** 取引データのDAO　*/
  private final TradeDao tradeDao = new TradeDao();

  /**
    * 取引データを取り消す処理をするメソッド
    */
  public void handle() {
    //当日の取引データがない場合はその旨を表示
    if(!this.tradeDao.isExistTradeData()) {
      this.cancelTradeView.showNothingInTradeList();
      return;
    }
    //ユーザが入力したコードを取得
    String code = this.cancelTradeView.recieveCode();

    //探索するコードの銘柄の取引データがない場合はエラーを表示
    if(!this.tradeDao.isExistTrade(code)) {
      this.cancelTradeView.showError(code);
      return;
    }
    //取引データの一覧を取得
    List<Trade> tradeList = this.tradeDao.getTradeList();

    //キャンセルする取引番号を取得
    int tradeNumToBeCanceled = this.cancelTradeView.recieveCancelTradeNumber(code, tradeList);

    //キャンセル番号が不正の場合は終了
    if(tradeNumToBeCanceled == -1) {
      return;
    }
    //取引のキャンセル処理をして保有数量がマイナスになってしまう場合はエラーを表示
    if(!this.cancelTradeLogic.execute(code, tradeNumToBeCanceled, tradeList)) {
      this.cancelTradeView.showError();
    }
  }
}
