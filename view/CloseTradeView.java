package view;

/**
  * 締め処理のview
  */
public class CloseTradeView {
  /**
    * 取引データがない場合はエラーを表示するメソッド
    */
  public void showProcessIsColsed() {
    System.out.println("前日の取引の締め処理は完了しています。\n");
  }

  /**
    * 締め処理完了の旨を表示するメソッド
    */
  public void showProcessIsColsing() {
    System.out.println("締め処理が完了しました。\n");
  }
}
