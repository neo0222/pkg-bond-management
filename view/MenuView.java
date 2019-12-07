package view;

import java.io.*;
/**
  * メニューを表示するView
  */
public class MenuView {
  /**
    * メニューを表示して選択されたメニュー番号を返すメソッド
    * @return 選択されたメニュー番号
    */
    public int outPutMenu() {
      //メニューの番号
      int menuNum = 0;
      System.out.println("------ 債権の在庫管理システム ------");
      System.out.println("\n<メニュー>");
      System.out.println("1:売買取引  2:当日の値洗い  3:保有銘柄残高一覧の表示  4:当日取引の取り消し 5:締め処理 6:終了\n");
      System.out.print("メニューを選択してください>");

      try {
        BufferedReader br = new BufferedReader
          (new InputStreamReader(System.in));

        menuNum = Integer.parseInt(br.readLine());

        if(menuNum > 6 || menuNum < 1) {
          System.out.println("\n1-6の数字で指定してください。");
        }

      } catch(NumberFormatException e1) {
        System.out.println("\n数字で指定してください。");
      } catch(IOException e2) {
        System.out.println("\n正しい値を入れてください。");
    }
    return menuNum;
  }
}
