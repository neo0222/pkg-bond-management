package model.data;

import java.math.BigDecimal;

/**
  *保有債権銘柄の在庫情報
  *このクラスは保有債権銘柄の在庫情報を表す。
  */
public class Balance {
  /** 保有数量 */
  private BigDecimal amount;
  /** 簿価 */
  private BigDecimal bookValue;
  /** 時価 */
  private BigDecimal currentPrice;

  public Balance(BigDecimal amount,BigDecimal bookValue) {
    this.amount = amount;
    this.bookValue = bookValue;
    this.currentPrice = BigDecimal.valueOf(0);
  }
  public Balance(BigDecimal amount,BigDecimal bookValue, BigDecimal currentPrice) {
    this.amount = amount;
    this.bookValue = bookValue;
    this.currentPrice = currentPrice;
  }

  //ゲッター
  public BigDecimal getAmount() { return amount; }
  public BigDecimal getBookValue() { return bookValue; }
  public BigDecimal getCurrentPrice() { return currentPrice; }

  //セッター
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public void setBookValue(BigDecimal bookValue) { this.bookValue = bookValue; }
  public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }

  @Override
  public String toString() {
    return getAmount() + "," + getBookValue() + "," + getCurrentPrice();
  }
}
