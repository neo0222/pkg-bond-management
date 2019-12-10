package view;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import model.data.Bond;

/**
  * 当日の値洗い処理のview
  */
public class MarkToMarketView {
  /**
    * ユーザーが入力した時価をコードとのmapで返すメソッド
    * @param holdingBondList 保有している銘柄のマスターデータのリスト
    * @return 銘柄コードと時価のmap
    */
  public Map<String, BigDecimal> recieveCurrentPrice(List<Bond> holdingBondList) {
    Map<String, BigDecimal> markedToMarketMap = new LinkedHashMap<>();
    for(Bond bond : holdingBondList) {
      String code = bond.getCode();
      String name = bond.getName();

      //正常な値が入るまでループ
      while(true) {
        try {
          BufferedReader br = new BufferedReader
            (new InputStreamReader(System.in));

          //時価を入力
          System.out.println("\n銘柄コード: " + code + " | 銘柄名: " + name);
          System.out.print("時価>");
          BigDecimal currentPrice = new BigDecimal(br.readLine()).setScale(3, RoundingMode.FLOOR);

          if(currentPrice.compareTo(BigDecimal.ZERO) == -1) {
            System.out.println("時価は0以上で指定してください。");
          } else {
            markedToMarketMap.put(code, currentPrice);
            break;
          }
        } catch(IOException e) {
          System.out.println(e);
        } catch(NumberFormatException ee) {
          System.out.println("数字で指定してください。");
        }
      }
    }
    return markedToMarketMap;
  }
}
