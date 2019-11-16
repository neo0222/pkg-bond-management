package model.logic;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import dao.MasterDataDao;
import dao.BalanceDataDao;
import model.data.Bond;

/**
  *保有銘柄残高一覧を表示するクラス
  */
public class ExportBondList {
  /**
    *保有銘柄残高一覧を表示するメソッド
    */
  public void execute() {
    BalanceDataDao balanceDataDao = new BalanceDataDao();
    List<String> bondList = balanceDataDao.getBalanceList();
    //銘柄の情報を１つずつ取得
    for(int i = 0; i < bondList.size(); i++) {
      //銘柄コード、保有数量、簿価、時価を取得
      String[] bondData = bondList.get(i).split(",");
      String code = bondData[0];
      BigDecimal amount = (new BigDecimal(bondData[1])).setScale(0, BigDecimal.ROUND_DOWN);
      BigDecimal bookValue = (new BigDecimal(bondData[2])).setScale(3, BigDecimal.ROUND_DOWN);
      BigDecimal currentPrice = (new BigDecimal(bondData[3])).setScale(3, BigDecimal.ROUND_DOWN);

      //銘柄名、償還年月日、利率、クーポン回数を取得
      MasterDataDao masterDataDao = new MasterDataDao();
      Bond masterData = masterDataDao.getMasterData(code);
      String name = masterData.getName();
      BigDecimal rate = masterData.getRate();
      int maturity = masterData.getMaturity();
      int coupon = masterData.getCoupon();

      String bond = null;
      if(currentPrice.compareTo(BigDecimal.valueOf(0)) == 0) {
        bond = String.format("|%-12s|%-15s|%10d|%8s|%12d|%8s|%15s|%15s|%12s|",
          code, name, maturity, rate.toString(), coupon, amount.toString(), bookValue.toString(), "#N/A", "#N/A");
      } else {
        //評価損益の計算
        BigDecimal valuationPL = currentPrice.subtract(bookValue).multiply(amount).setScale(0, BigDecimal.ROUND_DOWN);

        bond = String.format("|%-12s|%-15s|%10d|%8s|%12d|%8s|%15s|%15s|%12s|",
                  code, name, maturity, rate.toString(), coupon, amount.toString(),
                  bookValue.toString(), currentPrice.toString(), valuationPL.toString());
      }
      bondList.set(i, bond);
    }

    System.out.printf("|%-7s|%-16s|%-5s|%-6s|%-5s|%-4s|%-13s|%-13s|%-8s|\n",
              "銘柄コード", "銘柄名", "償還年月日", "利率", "クーポン回数", "保有数量", "簿価", "時価", "評価損益");
    for(String bond : bondList) {
      System.out.println(bond);
    }
  }
}
