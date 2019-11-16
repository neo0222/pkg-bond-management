package model.logic;

import java.io.*;
import java.math.BigDecimal;

import dao.BondDao;
import dao.BalanceDao;
import model.data.Bond;
import model.data.Balance;

/**
  *在庫データの入力処理。
  *在庫データを入力する処理をするクラス。
  */
public class InputBond {
  /** マスターデータのDAO　*/
  private BondDao bondDao = new BondDao();
  /** 残高データのDAO　*/
  private BalanceDao balanceDao = new BalanceDao();

  /**
    *在庫データを入力する処理をするメソッド
    */
  public void execute() {
    while(true) {
      try {
        BufferedReader br = new BufferedReader
          (new InputStreamReader(System.in));

        System.out.print("銘柄コード>");
        String code = br.readLine();

        //入力が「end」なら終了
        if(code.matches("[eE][nN][dD]")) {
          break;
        }

        //コードがマスターファイルに存在するか確認する
        //存在しない場合はエラーを表示
        if(!this.bondDao.isExistBond(code)) {
          System.out.println("銘柄コード " + code + " の銘柄は存在しません。\n");
          continue;
        }

        //保有数量と新たな簿価の入力
        System.out.print("保有数量>");
        BigDecimal addAmount = new BigDecimal(br.readLine());
        System.out.print("簿価>");
        BigDecimal addBookValue = new BigDecimal(br.readLine());

        //指定のコードの銘柄を保有しているか確認
        boolean isExistBond = this.balanceDao.isExistBond(code);

        if(isExistBond) { //保有しているときの処理
          //銘柄の保有数量と簿価を取得
          Balance balance = this.balanceDao.getBalanceData(code);
          BigDecimal oldAmount = balance.getAmount();
          BigDecimal oldBookValue = balance.getBookValue();

          //保有数量の更新
          BigDecimal newAmount = oldAmount.add(addAmount);

          //保有数量が0未満ならエラーを表示
          if(newAmount.compareTo(BigDecimal.valueOf(0)) == -1) {
            System.out.println("保有数量がマイナスになってしまいます。");
            continue;
          }

          //簿価の更新
          BigDecimal newBookValue = null;
          if(newAmount.equals(BigDecimal.valueOf(0))) {
            newBookValue = BigDecimal.valueOf(0);
          } else {
            newBookValue = (oldAmount.multiply(oldBookValue).add(addAmount.multiply(addBookValue)))
                                    .divide(newAmount,3,BigDecimal.ROUND_DOWN);
          }

          //銘柄残高ファイル上の保有数量と簿価の更新
          balance.setAmount(newAmount);
          balance.setBookValue(newBookValue);
          this.balanceDao.updateBalanceData(code, balance, isExistBond);

        } else { //保有していないときの処理
          //保有数量が0未満ならエラーを表示
          if(addAmount.compareTo(BigDecimal.valueOf(0)) == -1) {
            System.out.println("保有数量がマイナスです。");
            continue;
          }

          Balance balance = new Balance(addAmount, addBookValue);
          this.balanceDao.updateBalanceData(code, balance, isExistBond);
        }
      } catch(IOException e) {
        System.out.println(e);
      } catch(NumberFormatException ee) {
        System.out.println("数字を入力してください。");
      }
    }

  }
}
