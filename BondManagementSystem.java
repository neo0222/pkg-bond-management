import java.io.*;
import java.lang.NumberFormatException;

public class BondManagementSystem {
    public static void main(String[] args) {
        System.out.println();
        System.out.println("---------------債権管理システム---------------");
        do {
            try {
                System.out.println("実行したい処理番号を以下から選択し、入力してください(半角数字)");
                System.out.println("  1: 在庫データの入力・変更");
                System.out.println("  2: 当日の値洗い");
                System.out.println("  3: 保有銘柄残高一覧の表示");
                System.out.println("  4: 債権取引の実行");
                System.out.println("  5: 債権取引の取り消し");
                System.out.println("  99: システムを終了する");
                System.out.print("処理番号 > ");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                int jobNumber = Integer.parseInt(br.readLine());

                //締め処理（取引データ→在庫データ）
                // ClosingProcedure cp = new ClosingProcedure();
                // cp.execute();

                System.out.println();

                switch (jobNumber) {
                    case 1:
                        //在庫量の調整
                        InputBalance ib = new InputBalance();
                        ib.execute();
                        continue;
                    case 2:
                        //値洗い（時価）
                        MarkingToMarket mm = new MarkingToMarket();
                        mm.execute();
                        continue;
                    // case 3:
                        //在庫データと損益の確認
                        // BondTrade td = new BondTrade();
                        // td.execute();
                        // continue;
                    case 4:
                        //債権取引の実行
                        ShowBalanceList sb = new ShowBalanceList();
                        sb.execute();
                        continue;
                    // case 5:
                        //債権取引の取り消し
                        // CancelTransaction ct = new CancelTransaction();
                        // ct.execute();
                        // continue;
                    case 99:
                        //システムの終了
                        System.exit(0);
                    default:
                        //上位以外の数値が入力された場合
                        System.out.println("リストにある処理番号から選んで、再度入力してください。");
                        continue;
                }
            } catch (NumberFormatException ne) {
                //数字として変換出来なかった入力の場合
                System.out.println("不正な値です。入力をやり直してください");
                continue;
            } catch (IOException ie) {
                System.out.println("エラーが発生しました");
                continue;
            } finally {
                //処理後に空白行を入れる
                System.out.println();
            }
        } while(true);
    }
}