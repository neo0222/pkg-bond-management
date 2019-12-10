package model.data;

import java.math.BigDecimal;

/**
  *売買取引結果のデータ
  *このクラスは１つの債権取引結果のデータを表す。
  */
public class TradeResult {
  /** 売買 */
  private TradeType tradeType;
  /** 取引価格 */
  private BigDecimal price;
  /** 実現損益 */
  private BigDecimal realizedProfit;

  public TradeResult(TradeType tradeType, BigDecimal price, BigDecimal realizedProfit) {
    this.tradeType = tradeType;
    this.price = price;
    if(tradeType == TradeType.SELL) {
      this.realizedProfit = realizedProfit;
    } else {
      this.realizedProfit = null;
    }
  }

  public TradeType getTradeType() { return this.tradeType; }
  public BigDecimal getPrice() { return this.price; }
  public BigDecimal getRealizedProfit() { return this.realizedProfit; }

}
