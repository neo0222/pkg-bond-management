package model.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;

import model.data.Balance;
import dao.BondDao;
import dao.BalanceDao;

/**
  * 在庫データを入力する処理をするlogicmodel
  */
public class InputBalanceLogic {
  /** マスターデータのDAO　*/
  private BondDao bondDao = new BondDao();
  /** 残高データのDAO　*/
  private BalanceDao balanceDao = new BalanceDao();

  /**
    * 在庫データを入力する処理をするメソッド
    * @param newBalance 新たな残高情報
    * @return 保有数量が０以上ならtrue
    */
  public boolean execute(Balance newBalance) {
    String code = newBalance.getCode();
    BigDecimal newAmount = newBalance.getAmount();
    BigDecimal newBookValue = newBalance.getBookValue();

    //指定のコードの銘柄を保有しているか確認
    if(this.balanceDao.isExistBalance(code)) { //保有しているときの処理
      //銘柄の保有数量と簿価を取得
      Balance balance = this.balanceDao.getBalanceData(code);
      BigDecimal oldAmount = balance.getAmount();
      BigDecimal oldBookValue = balance.getBookValue();

      //保有数量の更新
      BigDecimal updatedAmount = oldAmount.add(newAmount);

      //保有数量が0未満ならエラーならcontrollerに処理を戻す
      if(updatedAmount.compareTo(BigDecimal.ZERO) == -1) {
        return false;
      }

      //簿価の更新
      BigDecimal updatedBookValue = BigDecimal.ZERO;
      if(!updatedAmount.equals(BigDecimal.ZERO)) {
        updatedBookValue = (oldAmount.multiply(oldBookValue).add(newAmount.multiply(newBookValue)))
                                .divide(updatedAmount,3,BigDecimal.ROUND_DOWN);
      }

      //銘柄残高ファイル上の保有数量と簿価の更新
      balance.setAmount(updatedAmount);
      balance.setBookValue(updatedBookValue);
      this.balanceDao.updateBalanceData(balance);

    } else { //保有していないときの処理
      //保有数量が0未満ならエラーを表示
      if(newAmount.compareTo(BigDecimal.ZERO) == -1) {
        return false;
      }

      Balance balance = new Balance(code, newAmount, newBookValue.setScale(3, RoundingMode.FLOOR));
      this.balanceDao.putBalanceData(balance);
    }
    return true;
  }
}
