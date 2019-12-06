package model.data;

public enum TradeType {
  SELL(0),
  BUY(1);

  private final int id;

  private TradeType (int id) {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }

  /**
    *idから定数オブジェクトを逆引きするメソッド
    */
  public static TradeType valueOf(int id) {
    //values()で列挙した定数オブジェクトを全て持つ配列を得られる
    for(TradeType tradeType : values()) {
      if(tradeType.getId() == id) {
        return tradeType;
      }
    }
    return null;
  }
}
