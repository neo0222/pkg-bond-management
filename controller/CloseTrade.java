package controller;

import view.CloseTradeView;
import model.logic.CloseTradeLogic;
import dao.TradeDao;

/**
  * 締め処理クラス
  * 締め処理をするcontroller
  */
public class CloseTrade {
  /** 暫定残高データのDAO　*/
  private final CloseTradeView closeTradeView = new CloseTradeView();
  /** 確定残高データのDAO　*/
  private final CloseTradeLogic closeTradeLogic = new CloseTradeLogic();
  /** 取引データのDAO　*/
  private final TradeDao tradeDao = new TradeDao();

  /**
    * 締め処理をするメソッド
    */
  public void handle() {
    //締め処理が完了している場合はその旨を表示
    if(!this.tradeDao.isExistTradeData()) {
      this.closeTradeView.showProcessIsColsed();
      return;
    }
    //締め処理を実行して完了したらその旨を表示
    if(this.closeTradeLogic.execute()) {
      this.closeTradeView.showProcessIsColsing();
    }
  }
}
