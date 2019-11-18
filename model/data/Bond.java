package model.data;

import java.math.BigDecimal;

/**
  *債権のマスターデータ
  *このクラスは１つの債権のマスターデータを表す。
  */
public class Bond {
  /** 銘柄コード */
  private String code;
  /** 銘柄名 */
  private String name;
  /** 償還年月日 */
  private int maturity;
  /** クーポン回数 */
  private int coupon;
  /** 利率 */
  private BigDecimal rate;

  public Bond(String code, String name, BigDecimal rate, int maturity, int coupon) {
    this.code = code;
    this.name = name;

    if(maturity < 20000101 || maturity > 29991231) {
			throw new IllegalArgumentException("償還年月日が不正。");
		}
		if(coupon < 0) {
			throw new IllegalArgumentException("クーポン回数が不正。");
		}
		if(rate.compareTo(BigDecimal.valueOf(0)) == -1) {
			throw new IllegalArgumentException("利率が不正。");
		}
    this.rate = rate;
		this.maturity = maturity;
		this.coupon = coupon;
  }

  public String getCode() { return code; }
  public String getName() { return name; }
  public BigDecimal getRate() { return rate; }
  public int getMaturity() { return maturity; }
  public int getCoupon() { return coupon; }

  @Override
  public String toString() {
    return code + "," + name + "," + rate + "," + maturity + "," + coupon;
  }
}
