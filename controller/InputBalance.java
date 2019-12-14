package controller;

import view.InputBalanceView;
import model.data.Bond;
import model.data.Balance;
import model.logic.InputBalanceLogic;
import dao.BondDao;

/**
  * 在庫データ入力クラス
  * 在庫データの入力処理をするcontroller
  */
public class InputBalance {
  /** 在庫データを入力する処理をするview　*/
  private final InputBalanceView inputBalanceView = new InputBalanceView();
  /** 在庫データを入力する処理をするlogicmodel */
  private final InputBalanceLogic inputBalanceLogic = new InputBalanceLogic();
  /** マスターデータのDAO　*/
  private final BondDao bondDao = new BondDao();

  /**
    * 在庫データを入力する処理をするメソッド
    */
  public void handle() {
    while(true) {
      //ユーザーが入力したcodeを取得
      String code = this.inputBalanceView.recieveCode();

      //入力が「end」なら終了
      if(code.matches("end|End|END")) {
        break;
      }
      //コードがマスターファイルに存在しない場合はエラーを表示
      if(!this.bondDao.isExistBond(code)) {
        this.inputBalanceView.showError(code);
        continue;
      }
      //ユーザーが入力した残高情報を取得
      Balance inputedBalance = this.inputBalanceView.recieveInputBalance(code);

      if(inputedBalance == null) {
        continue;
      }

      //残高ファイルに在庫データを書き込む。もし、保有数量が０未満の時はエラーを表示
      if(!this.inputBalanceLogic.execute(inputedBalance)) {
        this.inputBalanceView.showError();
      }
    }
  }
}
