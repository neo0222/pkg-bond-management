package control;

import view.MenuView;

public class BondManagementSystem {
  public static void main(String args[]) {
    //売買取引を制御するControl
    TradeBond tradeBond = new TradeBond();
    //値洗いを制御するControl
    UpdateBond updateBond = new UpdateBond();
    //一覧表の表示を制御するControl
    ExportBondList exportBondList = new ExportBondList();
    //取引の取り消しを制御するControl
    CancelTrade cancelTrade = new CancelTrade();
    //締め処理を制御するControl
    CloseTrade closeTrade = new CloseTrade();
    //メニューを表示するView
    MenuView menueView = new MenuView();

    //メニュー番号の初期化
    int menuNum = 0;

    do {
      //メニューを表示して選択されたメニュー番号を取得
      menuNum = menueView.outPutMenu();

      //メニューによる分岐
      switch(menuNum) {
        case 1:
         //売買取引
         tradeBond.execute();
         break;
        case 2:
         //当日の値洗い
         updateBond.execute();
         break;
        case 3:
         //保有銘柄残高一覧の表示
         exportBondList.execute();
         break;
        case 4:
         //当日取引の取り消し
         cancelTrade.execute();
         break;
        case 5:
         //締め処理
         closeTrade.execute();
         break;
        case 6:
         break;
        default:
         break;
      }
    } while(menuNum != 6);
  }
}
