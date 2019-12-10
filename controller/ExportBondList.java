package controller;

import java.util.List;

import view.ExportBondListView;
import model.logic.ExportBondListLogic;

/**
  * 保有銘柄残高一覧の表示クラス
  * 保有銘柄残高一覧を表示するcontroller
  */
public class ExportBondList {
  /** 一覧表の出力形式のリストを作成するlogicmodel */
  private ExportBondListLogic exportBondListLogic = new ExportBondListLogic();
  /** 保有銘柄残高一覧を表示するview */
  private ExportBondListView exportBondListView = new ExportBondListView();

  /**
    * 保有銘柄残高一覧を表示するメソッド
    */
  public void handle() {
    //銘柄残高情報のリストを取得
    List<String> bondList = this.exportBondListLogic.execute();

    //一覧表を出力する
    this.exportBondListView.showBondList(bondList);
  }
}
