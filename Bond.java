import java.math.*;

class Bond {
    private String code; //銘柄コード
    private String name; //銘柄名
    private BigDecimal interestRate; //利率
    private int redemptionDate; //償還年月日(yyyymmdd)
    private int coupon; //クーポン回数

    //constructor
    public Bond(String code, String name, BigDecimal interestRate, int redemptionDate, int coupon){
        this.code = code;
        this.name = name;
        this.interestRate = interestRate;
        this.redemptionDate = redemptionDate;
        this.coupon = coupon;
    }

    //各変数のgetterを定義する
    public String getCode(){
        return this.code;
    }

    public String getName(){
        return this.name;
    }

    public BigDecimal getInterestRate(){
        return this.interestRate;
    }

    public int getRedemptionDate(){
        return this.redemptionDate;
    }
    
    public int getCoupon(){
        return this.coupon;
    }

    public void printBond(){
        String str = "銘柄コード: " + this.code + ", 銘柄名: " + this.name + ", 利率: " + this.interestRate + ", 償還年月日: " + this.redemptionDate + ", クーポン回数: " + this.coupon;
        System.out.println(str);
    }
}