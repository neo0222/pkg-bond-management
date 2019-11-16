package model.logic;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import dao.MasterDataDao;
import dao.BalanceDataDao;
import model.data.Bond;

/**
  *当日の値洗い処理をするクラス
  */
public class UpdateBond {
  /**
    *当日の値洗い処理をするメソッド
    */
  public void execute() {
    BalanceDataDao balanceDataDao = new BalanceDataDao();
    List<String> bondList = balanceDataDao.getBalanceList();

    //1つずつ銘柄の時価を入力する
    for(int i = 0; i < bondList.size(); i++) {
      //銘柄コード、保有数量、簿価を取得
      String[] bondData = bondList.get(i).split(",");
      String code = bondData[0];
      BigDecimal amount = new BigDecimal(bondData[1]);
      BigDecimal bookValue = new BigDecimal(bondData[2]);

      //銘柄名を取得
      MasterDataDao masterDataDao = new MasterDataDao();
      Bond bond = masterDataDao.getMasterData(code);
      String name = bond.getName();

      try {
        BufferedReader br = new BufferedReader
          (new InputStreamReader(System.in));

        //時価を入力
        System.out.println("\n銘柄コード: " + code + " | 銘柄名: " + name);
        System.out.print("時価>");
        BigDecimal currentPrice = new BigDecimal(br.readLine());

        String updateLine = code + "," + amount + "," + bookValue + "," + currentPrice;
        bondList.set(i,updateLine);

      } catch(IOException e) {
        System.out.println(e);
      }
    }
    balanceDataDao.writeBalanceData(bondList);
  }
}
