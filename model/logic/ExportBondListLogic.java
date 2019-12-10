package model.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ArrayList;

import model.data.Bond;
import model.data.Balance;
import dao.BondDao;
import dao.BalanceDao;

/**
  * 一覧表出力形式のリストを作成するlogicmodel
  */
public class ExportBondListLogic {
  /** マスターデータのDAO　*/
  private BondDao bondDao = new BondDao();
  /** 残高データのDAO　*/
  private BalanceDao balanceDao = new BalanceDao();

  /**
    * 一覧表の出力形式のリストを返すメソッド
    * @return フォーマットされた文字列のリスト
    */
  public List<String> execute() {
    //銘柄残高情報のリストを取得
    List<Balance> balanceList = this.balanceDao.getBalanceList();

    //在庫データ一覧を表示するためのリストを用意
    List<String> bondList = new ArrayList<>();

    //銘柄の情報を１つずつ取得
    for(Balance balance : balanceList) {
      //銘柄コード、保有数量、簿価、時価を取得
      String code = balance.getCode();
      BigDecimal amount = balance.getAmount();
      BigDecimal bookValue = balance.getBookValue();
      BigDecimal currentPrice = balance.getCurrentPrice();

      //銘柄名、償還年月日、利率、クーポン回数を取得
      Bond masterData = this.bondDao.getMasterData(code);
      String name = masterData.getName();
      BigDecimal rate = masterData.getRate();
      int maturity = masterData.getMaturity();
      int coupon = masterData.getCoupon();

      String bond = null;
      if(currentPrice.compareTo(BigDecimal.ONE.negate()) == 0) {
        bond = String.format("|%-12s|%-15s|%10d|%8s|%12d|%8s|%15s|%15s|%12s|",
          code, name, maturity, rate.toString(), coupon, amount.toString(), bookValue.toString(), "#N/A", "#N/A");
      } else {
        //評価損益の計算
        BigDecimal valuationPL = currentPrice.subtract(bookValue).multiply(amount).setScale(0, RoundingMode.FLOOR);

        bond = String.format("|%-12s|%-15s|%10d|%8s|%12d|%8s|%15s|%15s|%12s|",
                  code, name, maturity, rate.toString(), coupon, amount.toString(),
                  bookValue.toString(), currentPrice.toString(), valuationPL.toString());
      }
      bondList.add(bond);
    }
    return bondList;
  }
}
