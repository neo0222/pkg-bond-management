package model.data;

import java.math.BigDecimal;

/**
  *債権取引のデータ
  *このクラスは１つの債権取引データを表す。
  */
public class Trade {
  /** 銘柄コード */
  private String code;
  /** 売買 */
  private TradeType tradeType;
  /** 価格 */
  private BigDecimal price;
  /** 数量 */
  private BigDecimal amount;

  public Trade(String code, TradeType tradeType, BigDecimal price, BigDecimal amount) {
    this.code = code;
    this.tradeType = tradeType;
    if(price.compareTo(BigDecimal.ZERO) == -1) {
			throw new IllegalArgumentException("価格は０以上で指定してください。");
		}
    if(amount.compareTo(BigDecimal.ZERO) == -1) {
			throw new IllegalArgumentException("数量は０以上で指定してください。");
		}
    this.price = price;
    this.amount = amount;
  }

  public String getCode() { return this.code; }
  public TradeType getTradeType() { return this.tradeType; }
  public BigDecimal getPrice() { return this.price; }
  public BigDecimal getAmount() { return this.amount; }

  @Override
  public String toString() {
    return this.code + "," + this.tradeType + "," + this.price + "," + this.amount;
  }
}
