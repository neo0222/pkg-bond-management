package control;

import java.io.*;
import model.logic.InputBond;
import model.logic.UpdateBond;
import model.logic.ExportBondList;


public class BondManagementSystem {
  public static void main(String args[]) {
    System.out.println("------ 債権の在庫管理システム ------");

    //メーニューの番号
    int menueNum = 0;

    do {
      //メーニューの番号の初期化
      menueNum = 0;
      //メニューの選択
      System.out.println("\n<メニュー>");
      System.out.println("1:在庫データの入力  2:当日の値洗い  3:保有銘柄残高一覧の表示  4:終了\n");
      System.out.print("メニューを選択してください>");

      try {
        BufferedReader br = new BufferedReader
          (new InputStreamReader(System.in));

        menueNum = Integer.parseInt(br.readLine());
      } catch(NumberFormatException e1) {
        System.out.println("\n数字で指定してください。");
      } catch(IOException e2) {
        System.out.println("\n正しい値を入れてください。");
      }

      //メニューによる分岐
      switch(menueNum) {
        case 1:
         //在庫データの入力
         InputBond ib = new InputBond();
         ib.execute();
         break;
        case 2:
         //当日の値洗い
         UpdateBond ub = new UpdateBond();
         ub.execute();
         break;
        case 3:
         //保有銘柄残高一覧の表示
         ExportBondList ebl = new ExportBondList();
         ebl.execute();
         break;
        case 4:
         break;
        default:
         System.out.println("\n1-4の数字で指定してください。");
         break;
      }
    } while(menueNum != 4);
  }
}
