package model.logic;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import dao.BondDao;
import dao.BalanceDao;
import model.data.Bond;
import model.data.Balance;

/**
  *当日の値洗い処理をするクラス
  */
public class UpdateBond {
  /** マスターデータのDAO　*/
  private BondDao bondDao = new BondDao();
  /** 残高データのDAO　*/
  private BalanceDao balanceDao = new BalanceDao();

  /**
    *当日の値洗い処理をするメソッド
    */
  public void execute() {
    //銘柄残高情報のリストを取得
    List<Balance> balanceList = this.balanceDao.getBalanceList();

    //1つずつ銘柄の時価を入力する
    for(int i = 0; i < balanceList.size(); i++) {
      Balance balance = balanceList.get(i);
      //銘柄コードを取得
      String code = balance.getCode();
      //銘柄名を取得
      Bond bond = this.bondDao.getMasterData(code);
      String name = bond.getName();

      try {
        BufferedReader br = new BufferedReader
          (new InputStreamReader(System.in));

        //時価を入力
        System.out.println("\n銘柄コード: " + code + " | 銘柄名: " + name);
        System.out.print("時価>");
        BigDecimal currentPrice = new BigDecimal(br.readLine());
        //時価を銘柄情報に設定する
        balance.setCurrentPrice(currentPrice);

        balanceList.set(i, balance);

      } catch(IOException e) {
        System.out.println(e);
      }
    }
    this.balanceDao.writeBalanceData(balanceList);
  }
}
