import java.math.*;

class Balance {
    private String code; //銘柄コード
    private BigDecimal amount; //保有数量
    private BigDecimal bookValue; //簿価
    private BigDecimal currentValue; //時価

    public Balance(String code, BigDecimal amount, BigDecimal bookValue, BigDecimal currentValue){
        this.code = code;
        this.amount = amount;
        this.bookValue = bookValue;
        this.currentValue = currentValue;
    }

    //セッターを定義
    public void setCode(String code){
        this.code = code;
    }

    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }

    public void setBookValue(BigDecimal bookValue){
        this.bookValue = bookValue;
    }

    public void setCurrentValue(BigDecimal currentValue){
        this.currentValue = currentValue;
    }

    //ゲッターを定義
    public String getCode(){
        return this.code;
    }

    public BigDecimal getAmount(){
        return this.amount;
    }

    public BigDecimal getBookValue(){
        return this.bookValue;
    }

    public BigDecimal getCurrentValue(){
        return this.currentValue;
    }

    @Override
    public String toString(){
        String str = this.code + "," + this.amount + "," + this.bookValue + ", " + this.currentValue;
        return str;
    }

    public void printBalance(){
        String str = "銘柄コード: " + this.code + ", 保有数量: " + this.amount + ", 簿価: " + this.bookValue + ", 時価: " + this.currentValue;
        System.out.println(str);
    }
}