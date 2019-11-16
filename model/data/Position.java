package model.data;

import java.math.BigDecimal;

/**
  *保有債権銘柄のデータ
  *このクラスは保有債権銘柄のデータを表す。
  */
public class Position {
  /** 債権銘柄 */
  private Bond bond;
  /** 在庫情報 */
  private Balance balance;

  public Position(Bond bond,Balance balance) {
    this.bond = bond;
    this.balance = balance;
  }

  //ゲッター
  public Bond getBond() { return bond; }
  public Balance getBalance() { return balance; }


  @Override
  public String toString() {
    return bond.toString() + "," + balance.toString();
  }
}
