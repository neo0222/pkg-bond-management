package view;

import java.io.*;
/**
  * メニューを表示するView
  */
public class MainView {
  /**
    * メニューを表示して選択されたメニュー番号を返すメソッド
    * @return 選択されたメニュー番号
    */
    public int recieveMenuNum() {
      //メニューの番号
      int menuNum = 0;
      System.out.println("\n------ 債権の在庫管理システム ------");
      System.out.println("<メニュー>");
      System.out.println("1:在庫の入力  2:当日の値洗い  3:保有銘柄残高一覧の表示  4:売買取引 5:当日取引の取り消し 6:締め処理 7:終了\n");
      System.out.print("メニューを選択してください>");

      try {
        BufferedReader br = new BufferedReader
          (new InputStreamReader(System.in));

        menuNum = Integer.parseInt(br.readLine());

        if(menuNum > 7 || menuNum < 1) {
          System.out.println("\n1-7の数字で指定してください。");
        }

      } catch(NumberFormatException e1) {
        System.out.println("\n数字で指定してください。");
      } catch(IOException e2) {
        System.out.println("\n正しい値を入れてください。");
    }
    return menuNum;
  }
}
