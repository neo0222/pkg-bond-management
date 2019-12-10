import controller.MainController;

/**
  * 処理を実行するクラス
  */
public class BondManagementSystem {
  public static void main(String args[]) {
    MainController mainController = new MainController();

    mainController.handle();
  }
}
