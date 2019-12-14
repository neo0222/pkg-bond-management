package controller;

import view.TradeBondView;
import model.data.Trade;
import model.data.TradeResult;
import model.logic.TradeBondLogic;
import dao.BondDao;
import dao.BalanceDao;

/**
  * 売買取引クラス
  * 売買取引データの入力処理をするcontroller
  */
public class TradeBond {
  /** 売買取引データを入力する処理をするview　*/
  private final TradeBondView tradeBondView = new TradeBondView();
  /** 売買取引データを入力する処理をするlogicmodel */
  private final TradeBondLogic tradeBondLogic = new TradeBondLogic();
  /** マスターデータのDAO　*/
  private final BondDao bondDao = new BondDao();
  /** 残高データのDAO　*/
  private final BalanceDao balanceDao = new BalanceDao();

  /**
    * 取引データを入力する処理をするメソッド
    */
  public void handle() {
    while(true) {
      //ユーザーが入力したcodeを取得
      String code = this.tradeBondView.recieveCode();

      //入力が「end」なら終了
      if(code.matches("end|End|END")) {
        break;
      }
      //コードがマスターファイルに存在しない場合はエラーを表示
      if(!this.bondDao.isExistBond(code)) {
        this.tradeBondView.showError(code);
        continue;
      }
      //ユーザーが入力した取引情報を取得
      Trade trade = this.tradeBondView.recieveTradeData(code, this.balanceDao.isExistBalance(code));

      //viewでtradeを取得できない時はやり直し
      if(trade == null) {
        continue;
      }
      //売買取引の処理
      TradeResult tradeResult = this.tradeBondLogic.execute(trade);
      //ファイルへの書き込みが完了したら取引結果を表示。もし、保有数量が０未満の時はエラーを表示
      if(tradeResult != null) {
        this.tradeBondView.showResult(tradeResult);
      } else {
        this.tradeBondView.showError();
      }
    }
  }
}
