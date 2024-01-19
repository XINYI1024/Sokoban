package sokoban;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * 説明：ゲーム紹介ウィンドウの作成と初期化、背景画像、紹介内容、戻るボタンの設定。
 * ユーザーはゲームの紹介を読んで、戻るボタンをクリックしてゲームメニュー画面に戻ることができる。
 */
public class GameIntroduction extends JFrame {
	private JPanel jpanel = new JPanel();
	private JButton return_button;
	private JButton introduction;
	public GameIntroduction() {
		//ウィンドウタイトルの設定；サイズ変更不可；ウィンドウサイズ；座標；終了
		setTitle("sokoban");
		setResizable(false);
		setSize(600,600);
		setLocation(300,20);	
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       //背景を設定する
      	ImageIcon img = new ImageIcon("res/background.png"); //画像を加える
      	Image new_image = img.getImage().getScaledInstance(600, 600, Image.SCALE_DEFAULT);//画像サイズの設定
      	ImageIcon final_img = new ImageIcon(new_image);
		// 画像をJLabelコンポーネントbackgroundとして作成し、ウィンドウのレイヤーパネルに追加する。setOpaque(false)で内容パネルを透明にする。
      	JLabel background = new  JLabel(final_img);
      	this.getLayeredPane().add(background, String.valueOf(Integer.MIN_VALUE));  
      	background.setBounds(0, 0, final_img.getIconWidth(), final_img.getIconHeight());	
      	((JPanel)this.getContentPane()).setOpaque(false);
      	
      	jpanel.setLayout(null);
      	
      	// introductionオブジェクトを作成し、ゲームの紹介内容を含め、HTML形式でテキストのレイアウトを行う。ボタンのフォント、背景色、位置、枠線などの属性を設定し、jpanelに追加する。
      	introduction = new JButton("<html>&nbsp;&nbsp;The player needs to push the boxes to designated positions in order to pass each level. <br>"
      			+ "&nbsp;&nbsp;The player controls the character's movement on the game board using the arrow keys on the keyboard. <br>"
      			+ "&nbsp;&nbsp; You can press 'A' to return the previous level. <br>"
				+ "&nbsp;&nbsp; You can press 'S' to restart the present level. <br>"
				+ "&nbsp;&nbsp; You can press 'D' to pass the present level. <br>"
				+ "&nbsp;&nbsp; Additionally, you can press 'B' to return to the previous step. </html>");
      	introduction.setFont(new Font("Times Roman", Font.BOLD, 20));

      	introduction.setBackground(Color.white);
      	introduction.setBounds(50, 50, 500, 400);
      	introduction.setBorderPainted(false);
      	introduction.setContentAreaFilled(false);
        jpanel.add(introduction);
        //コンテナにJLabelコンポーネントを追加する


		ImageIcon button_img = new ImageIcon("res/back_button.png");
		Image new_button_image = button_img.getImage().getScaledInstance(200, 80, Image.SCALE_DEFAULT);
		ImageIcon final_button_img = new ImageIcon(new_button_image);
		return_button = new JButton(final_button_img);
		//return_buttonオブジェクトを作成し、ボタンのコンテンツエリアを透明に設定、枠線なし、ボタンの位置と枠線を設定する。
		return_button.setContentAreaFilled(false);
		return_button.setBorderPainted(false);
		return_button.setBounds(this.getWidth()/2-110, this.getHeight()/2+160, 200, 80);
		return_button.setBorder(BorderFactory.createLineBorder(Color.black));
		// ボタンにクリックイベントリスナーを追加し、ボタンをクリックするとGameMenuオブジェクトを作成し、現在のウィンドウを閉じる。
		return_button.addActionListener(e -> {
        	
			new GameMenu();
			
            dispose(); // スタート画面のウィンドウを閉じる
        });
		// jpanelをウィンドウの内容パネルに追加し、ウィンドウを表示状態に設定する。
		jpanel.add(return_button);
		this.getContentPane().add(jpanel);
		
      	this.setVisible(true);
	}
}
