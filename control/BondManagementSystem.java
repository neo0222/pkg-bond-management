package control;

import java.io.*;
import model.logic.TradeBond;
import model.logic.UpdateBond;
import model.logic.ExportBondList;
import model.logic.CancelTrade;
import model.logic.CloseTrade;


public class BondManagementSystem {
  public static void main(String args[]) {

    TradeBond ib = new TradeBond();
    UpdateBond ub = new UpdateBond();
    ExportBondList ebl = new ExportBondList();
    CancelTrade cat = new CancelTrade();
    CloseTrade clt = new CloseTrade();

    System.out.println("------ 債権の在庫管理システム ------");

    //メニューの番号
    int menuNum = 0;

    do {
      //メニューの番号の初期化
      menuNum = 0;
      //メニューの選択
      System.out.println("\n<メニュー>");
      System.out.println("1:売買取引  2:当日の値洗い  3:保有銘柄残高一覧の表示  4:当日取引の取り消し 5:締め処理 6:終了\n");
      System.out.print("メニューを選択してください>");

      try {
        BufferedReader br = new BufferedReader
          (new InputStreamReader(System.in));

        menuNum = Integer.parseInt(br.readLine());
      } catch(NumberFormatException e1) {
        System.out.println("\n数字で指定してください。");
      } catch(IOException e2) {
        System.out.println("\n正しい値を入れてください。");
      }

      //メニューによる分岐
      switch(menuNum) {
        case 0:
         //入力がint型以外の場合は何も処理しない
         break;
        case 1:
         //売買取引
         ib.execute();
         break;
        case 2:
         //当日の値洗い
         ub.execute();
         break;
        case 3:
         //保有銘柄残高一覧の表示
         ebl.execute();
         break;
        case 4:
         //当日取引の取り消し
         cat.execute();
         break;
        case 5:
         //締め処理
         clt.execute();
         break;
        case 6:
         break;
        default:
         System.out.println("\n1-6の数字で指定してください。");
         break;
      }
    } while(menuNum != 6);
  }
}
