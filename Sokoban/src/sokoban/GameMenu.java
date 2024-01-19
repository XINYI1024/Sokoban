package sokoban;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * 説明：ゲームメニュー画面を作成し、背景画像と2つのボタン（ゲーム開始と紹介を見る）を含める
 */

public class GameMenu extends JFrame {
	private JPanel jpanel = new JPanel();
	private JButton play_button;
	private JButton introduction_button;
    public GameMenu() {
		// タイトル、サイズ、位置、デフォルトのクローズ操作
        setTitle("sokoban");
		setResizable(false);
		setSize(600,600);
		setLocation(300,20);	
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //　背景の設定
      	ImageIcon img = new ImageIcon("res/sand_menu.png"); //　画像を加える
      	Image new_image = img.getImage().getScaledInstance(600, 600, Image.SCALE_DEFAULT);//　画像サイズの設定
      	ImageIcon final_img = new ImageIcon(new_image);
		// 画像をJLabelコンポーネントbackgroundとして作成し、ウィンドウのレイヤーパネルに追加する。setOpaque(false)で内容パネルを透明にする。
      	JLabel background = new  JLabel(final_img);
      	this.getLayeredPane().add(background, String.valueOf(Integer.MIN_VALUE));  
      	background.setBounds(0, 0, final_img.getIconWidth(), final_img.getIconHeight());	
      	((JPanel)this.getContentPane()).setOpaque(false);

      	jpanel.setLayout(null);
		// ボタン用の画像リソースを読み込み、サイズ調整し、2つのJButtonオブジェクト（play_buttonとintroduction_button）を作成し、スタイルをカスタマイズする
		// (例：塗りつぶし色の取消し、枠線の描画無し、境界位置の設定など）。
		ImageIcon button_img = new ImageIcon("res/play_button.png");
		ImageIcon button_img2 = new ImageIcon("res/introduction_button.png");
		Image new_button_image = button_img.getImage().getScaledInstance(200, 80, Image.SCALE_DEFAULT);
		Image new_button_image2 = button_img2.getImage().getScaledInstance(200, 80, Image.SCALE_DEFAULT);
		ImageIcon final_button_img = new ImageIcon(new_button_image);
		ImageIcon final_button_img2 = new ImageIcon(new_button_image2);
		play_button = new JButton(final_button_img);
		play_button.setContentAreaFilled(false);
		play_button.setBorderPainted(false);
		play_button.setBounds(this.getWidth()/2-110, this.getHeight()/2, 200, 80);
		play_button.setBorder(BorderFactory.createLineBorder(Color.black));
		
		
		
		// 2つのボタンをjpanelに追加し、その後jpanelをウィンドウのコンテンツパネルに追加し、ウィンドウを表示状態にする。
		introduction_button = new JButton(final_button_img2);
		introduction_button.setContentAreaFilled(false);
		introduction_button.setBorderPainted(false);
		introduction_button.setBounds(this.getWidth()/2-110, this.getHeight()/2 + 100, 200, 80);
		introduction_button.setBorder(BorderFactory.createLineBorder(Color.black));
		

		jpanel.add(play_button);
		jpanel.add(introduction_button);
		this.getContentPane().add(jpanel);
		this.setVisible(true);

		// 2つのボタンにリスナーイベントを追加する
        play_button.addActionListener(e -> {
        	try {
				new GameClient();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            dispose(); // スタート画面のウィンドウを閉じる
        });
        
        introduction_button.addActionListener(e -> {
        	
			new GameIntroduction();
			
            dispose(); // スタート画面のウィンドウを閉じる
        });
        GameMusicUtil.menu_play(); // ゲームメニューのBGM
        
    }

    public static void main(String[] args) {
        GameMenu startMenu = new GameMenu();
    }
}
