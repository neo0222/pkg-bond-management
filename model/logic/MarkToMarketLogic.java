package model.logic;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import model.data.Bond;
import model.data.Balance;
import dao.BondDao;
import dao.BalanceDao;

/**
  * 当日の値洗い処理をするlogicmodel
  */
public class MarkToMarketLogic {
  /** マスターデータのDAO　*/
  private final BondDao bondDao = new BondDao();
  /** 残高データのDAO　*/
  private final BalanceDao balanceDao = new BalanceDao();

  /**
    * 保有している銘柄のマスターデータを返すメソッド
    * @return 保有している銘柄のマスターデータ
    */
  public List<Bond> getHoldingBondList() {
    List<Bond> holdingBondList = new ArrayList<>();
    //銘柄残高情報のリストを取得
    List<Balance> balanceList = this.balanceDao.getBalanceList();
    for(Balance balance : balanceList) {
      //銘柄コードを取得
      String code = balance.getCode();
      //保有銘柄のマスターデータを取得
      Bond bond = this.bondDao.getMasterData(code);

      holdingBondList.add(bond);
    }
    return holdingBondList;
  }

  /**
    * 時価を更新するメソッド
    * @param markedToMarketMap 値洗いする銘柄のコードと時価
    */
  public void updateCurrentPrice(Map<String, BigDecimal> markedToMarketMap) {
    //銘柄残高情報のリストを取得
    List<Balance> balanceList = new ArrayList<>();
    for(String code : markedToMarketMap.keySet()) {
      Balance balance = this.balanceDao.getBalanceData(code);
      //時価を更新
      balance.setCurrentPrice(markedToMarketMap.get(code));
      balanceList.add(balance);
    }
    this.balanceDao.writeBalanceData(balanceList);
  }
}
