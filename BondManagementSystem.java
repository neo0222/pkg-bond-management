import java.io.*;
import java.lang.NumberFormatException;

public class BondManagementSystem {
    public static void main(String[] args) {
        System.out.println();
        System.out.println("----------<債権管理システム>----------");
        do {
            try {
                System.out.println("実行したい処理番号を入力してください(半角数字)");
                System.out.println("  1: 在庫データの入力");
                System.out.println("  2: 当日の値洗い");
                System.out.println("  3: 保有銘柄残高一覧の表示");
                System.out.println("  99: システムを終了する");
                System.out.print("処理番号 > ");

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                int jobNumber = Integer.parseInt(br.readLine());
                System.out.println();

                switch (jobNumber) {
                    case 1:
                        InputBond ib = new InputBond();
                        ib.execute();
                        break;
                    case 2:
                        // MarkToMarket mm = new MarkToMarket();
                        // mm.execute();
                        break;
                    case 3:
                        ShowBalanceList sb = new ShowBalanceList();
                        sb.execute();
                        continue;
                    case 99:
                        System.exit(0);
                    default:
                        System.out.println("不正な値です。入力をやり直してください");
                        continue;
                }
                break;
            } catch (NumberFormatException ne) {
                System.out.println("数字を入力してください");
                break;
            } catch (IOException ie) {
                System.out.println("不正な値です。入力をやり直してください");
                break;
            } finally {
                System.out.println();
            }
        } while(true);
    }
}