package model.logic;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ArrayList;

import dao.BondDao;
import dao.BalanceDao;
import model.data.Bond;
import model.data.Balance;

/**
  *保有銘柄残高一覧を表示するクラス
  */
public class ExportBondList {
  /** マスターデータのDAO　*/
  private BondDao bondDao = new BondDao();
  /** 残高データのDAO　*/
  private BalanceDao balanceDao = new BalanceDao();

  /**
    *保有銘柄残高一覧を表示するメソッド
    */
  public void execute() {
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

    System.out.printf("|%-7s|%-16s|%-5s|%-6s|%-5s|%-4s|%-13s|%-13s|%-8s|\n",
              "銘柄コード", "銘柄名", "償還年月日", "利率", "クーポン回数", "保有数量", "簿価", "時価", "評価損益");
    for(String bond : bondList) {
      System.out.println(bond);
    }
  }
}
