package controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import view.MarkToMarketView;
import model.data.Bond;
import model.logic.MarkToMarketLogic;

/**
  * 当日の値洗いクラス
  * 当日値洗い処理のcontroller
  */
public class MarkToMarket {
  /** 当日値洗い処理のview　*/
  private MarkToMarketView markToMarketView = new MarkToMarketView();
  /** 当日値洗い処理のlogicmodel　*/
  private MarkToMarketLogic markToMarketLogic = new MarkToMarketLogic();

  /**
    * 当日の値洗い処理をするメソッド
    */
  public void handle() {
    //残高ファイルからBondのリストを取得
    List<Bond> holdingBondList = this.markToMarketLogic.getHoldingBondList();

    //コードに対して入力された時価のマップを取得
    Map<String, BigDecimal> markedToMarketMap = this.markToMarketView.recieveCurrentPrice(holdingBondList);

    //値洗いする
    this.markToMarketLogic.updateCurrentPrice(markedToMarketMap);
  }
}
