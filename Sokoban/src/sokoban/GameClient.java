package sokoban;
import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;
/**
 * 説明：ゲームクライアントウィンドウの作成と初期化、関連するゲーム音楽のコントロール
 */

 // GameClientクラスを定義し、JFrameクラスから継承する
public class GameClient extends JFrame{

	public GameClient() throws IOException{
		//ウィンドウのタイトルを設定する
		super("sokoban");
		// ゲームパネルオブジェクトを生成する
		GamePanel gamePanel = new GamePanel();
		// 背景色を設定する
		gamePanel.setBackground(Color.white);
		// ゲームパネルをウィンドウのコンテナに追加する
		getContentPane().add(gamePanel);
		// 右上隅のxをクリックするとゲームが終了する
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 可視性を設定する
		setVisible(true);
		// ウィンドウのサイズを変更できないように設定する
		setResizable(false);
		// ウィンドウのサイズを設定する
		setSize(600,600);
		// ウィンドウの左上隅の座標を設定する
		setLocation(300,20);	
		// 音楽を再生する	
		GameMusicUtil.menu_stop();
		GameMusicUtil.game_play();
	}
	
	public static void main(String[] args) throws IOException {
		//ゲームスタート
		new GameClient();
	}

}
