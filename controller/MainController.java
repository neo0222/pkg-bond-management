package controller;

import view.MainView;

/**
  * メインメニュークラス
  * メインメニューを表示し、各controllerを呼び出すController
  */
public class MainController {
  /** 在庫入力を制御するController */
  private final InputBalance inputBalance = new InputBalance();
  /** 値洗いを制御するController */
  private final MarkToMarket markToMarket = new MarkToMarket();
  /** 一覧表の表示を制御するController */
  private final ExportBondList exportBondList = new ExportBondList();
  /** 売買取引を制御するController */
  private final TradeBond tradeBond = new TradeBond();
  /** 取引の取り消しを制御するController */
  private final CancelTrade cancelTrade = new CancelTrade();
  /** 締め処理を制御するController */
  private final CloseTrade closeTrade = new CloseTrade();
  /** メニューを表示するView */
  private final MainView mainView = new MainView();

  /**
    * メインメニューを表示し、各controllerを呼び出すメソッド
    */
  public void handle() {
    //メニュー番号の初期化
    int menuNum = 0;

    while(menuNum != 7) {
      //メニューを表示して選択されたメニュー番号を取得
      menuNum = this.mainView.recieveMenuNum();

      //メニューによる分岐
      switch(menuNum) {
        case 1:
         //在庫の入力
         this.inputBalance.handle();
         break;
        case 2:
         //当日の値洗い
         this.markToMarket.handle();
         break;
        case 3:
         //保有銘柄残高一覧の表示
         this.exportBondList.handle();
         break;
        case 4:
         //売買取引
         this.tradeBond.handle();
         break;
        case 5:
         //当日取引の取り消し
         this.cancelTrade.handle();
         break;
        case 6:
         //締め処理
         this.closeTrade.handle();
         break;
        default:
         break;
      }
    }
  }
}
