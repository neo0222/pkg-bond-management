package view;

import java.io.*;
import java.math.BigDecimal;

import model.data.Balance;

/**
  * 在庫データを入力する処理のview
  */
public class InputBalanceView {
  /**
    * コードを入力させ取得するメソッド
    * @return 入力された銘柄コード
    */
  public String recieveCode() {
    String code = null;
    try {
      BufferedReader br = new BufferedReader
        (new InputStreamReader(System.in));

      System.out.print("銘柄コード [end:終了]>");
      code = br.readLine();

    } catch(IOException e) {
      System.out.println(e);
    }
    return code;
  }

  /**
    * 保有数量と簿価を入力させてBlanceを返すメソッド
    * @param code キーとなるコード
    * @return 入力された保有数量と簿価がフィールドのBalance
    */
  public Balance recieveInputBalance(String code) {
    Balance inputBalance = null;
    try {
      BufferedReader br = new BufferedReader
        (new InputStreamReader(System.in));

      //保有数量と新たな簿価の入力
      System.out.print("保有数量>");
      BigDecimal amount = new BigDecimal(br.readLine());
      System.out.print("簿価>");
      BigDecimal bookValue = new BigDecimal(br.readLine());

      if(bookValue.compareTo(BigDecimal.ZERO) == -1) {
        System.out.println("簿価は０以上で指定してください。");
      } else {
        inputBalance = new Balance(code, amount, bookValue);
      }

    } catch(IOException e) {
      System.out.println(e);
    } catch(NumberFormatException ee) {
      System.out.println("数字を入力してください。");
    }
    return inputBalance;
  }

  /**
    * マスターファイルに銘柄コードが存在しない時、エラーを表示するメソッド
    * @param code 入力されたコード
    */
  public void showError(String code) {
    System.out.println("銘柄コード " + code + " の銘柄は存在しません。\n");
  }

  /**
    * 保有数量がマイナスの時、エラーを表示するメソッド
    */
  public void showError() {
    System.out.println("保有数量がマイナスになってしまいます。");
  }
}
