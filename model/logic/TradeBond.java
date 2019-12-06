package model.logic;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import dao.BondDao;
import dao.BalanceDao;
import dao.TradeDao;
import model.data.Bond;
import model.data.Balance;
import model.data.Trade;
import model.data.TradeType;

/**
  *売買取引データの入力処理。
  *売買取引データを入力する処理をするクラス。
  */
public class TradeBond {
  /** マスターデータのDAO　*/
  private BondDao bondDao = new BondDao();
  /** 残高データのDAO　*/
  private BalanceDao balanceDao = new BalanceDao();
  /** 取引データのDAO　*/
  private TradeDao tradeDao = new TradeDao();

  /**
    *取引データを入力する処理をするメソッド
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

        //売買入力
        System.out.print("0:売 1:買>");
        int typeId = Integer.parseInt(br.readLine());
        //入力が「売買」以外のときはエラーを表示
        if(typeId != 0 && typeId != 1) {
          System.out.println("0(売)か1(買)で指定してください。\n");
          continue;
        }
        //売りの場合でその銘柄を持っていない場合はエラーを表示
        if(typeId == 0 && !this.balanceDao.isExistBalance(code)) {
          System.out.println("この銘柄を持っていないため、売ることはできません。");
          continue;
        }

        //価格・数量の入力
        TradeType tradeType = TradeType.valueOf(typeId);
        System.out.print("価格>");
        BigDecimal tradePrice = new BigDecimal(br.readLine());
        System.out.print("数量>");
        BigDecimal tradeAmount = new BigDecimal(br.readLine());

        //取引データの作成（価格・数量がマイナスの時は例外を返す）
        Trade trade = new Trade(code, tradeType, tradePrice, tradeAmount);

        //取引金額
        BigDecimal price = tradePrice.multiply(tradeAmount);

        //計算のために取引が「売り」の場合は数量をマイナスにする
        if(tradeType == TradeType.SELL) {
          tradeAmount = tradeAmount.negate();
        }

        //銘柄残高ファイルへの書き込み・実現損益の計算
        //実現損益
        BigDecimal realizedProfit = null;
        //指定のコードの銘柄を保有しているか確認
        if(this.balanceDao.isExistBalance(code)) { //保有しているときの処理
          //銘柄の保有数量と簿価を取得
          Balance balance = this.balanceDao.getBalanceData(code);
          BigDecimal oldAmount = balance.getAmount();
          BigDecimal oldBookValue = balance.getBookValue();

          //保有数量の更新
          BigDecimal newAmount = oldAmount.add(tradeAmount);

          //保有数量が0未満ならエラーを表示
          if(newAmount.compareTo(BigDecimal.ZERO) == -1) {
            System.out.println("保有数量がマイナスになってしまいます。");
            continue;
          }

          //簿価の更新
          BigDecimal newBookValue = null;
          if(newAmount.equals(BigDecimal.ZERO)) {
            newBookValue = BigDecimal.ZERO;
          } else if(tradeAmount.compareTo(BigDecimal.ZERO) == -1) { //売りの場合は簿価は変わらない
            newBookValue = oldBookValue;
          } else {
            newBookValue = (oldAmount.multiply(oldBookValue).add(tradeAmount.multiply(tradePrice)))
                                    .divide(newAmount, 3, RoundingMode.FLOOR);
          }

          //銘柄残高ファイル上の保有数量と簿価の更新
          balance.setAmount(newAmount);
          balance.setBookValue(newBookValue);
          this.balanceDao.updateBalanceData(balance);

          //実現損益の計算
          realizedProfit = tradePrice.subtract(oldBookValue).multiply(tradeAmount.negate());

        } else { //保有していないときの処理
          Balance balance = new Balance(code, tradeAmount, tradePrice.setScale(3, RoundingMode.FLOOR));
          this.balanceDao.putBalanceData(balance);
        }

        //取引金額と実現損益の表示
        System.out.print("取引金額 : " + price);
        //取引が「売り」の場合は実現損益を表示する
        if(tradeType == TradeType.SELL) {
          System.out.println(" 実現損益 : " + realizedProfit + "\n");
        } else {
          System.out.println("\n");
        }

        //取引リストの更新
        tradeDao.putTradeData(trade);


      } catch(IOException e) {
        System.out.println(e);
      } catch(NumberFormatException e2) {
        System.out.println("数字を入力してください。");
      } catch(IllegalArgumentException e3) {
        System.out.println(e3);
      }
    }
  }
}
