package sokoban;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;

/**
 * 説明：ゲームパネル、ビューの表示のみを担当
 */
public class GamePanel extends JPanel implements KeyListener {
 
	private GameLogic mLogic;
	private boolean isAcceptKey = true;
	private int width, height;// スクリーンのサイズ
	private byte[][] map;
	private int marginLeft, marginTop;
	private int grade = 0;
	// 画像リソース
	private Timer timer;
	private int gifIndex;

	private Image[] images;
	private Image[] box_images;
	private Image[] success_images;
	private Image backgroundImage;
	private static final int DIRECTION_UP = 1, DIRECTION_DOWN = 2, DIRECTION_LEFT = 3, DIRECTION_RIGHT = 4;
 
	public GamePanel() throws IOException {
		setSize(600, 600);
	
		// スクリーンの幅
		this.width = getWidth();
		// スクリーンの高さ
		this.height = getHeight();
		// 画像リソースのロード
		initImageResource();
		// ゲームロジッククラスのインスタンス化
		mLogic = GameLogic.getInstance();
		// レベルの初期化
		initGame(grade);
		setFocusable(true);
		addKeyListener(this);
		
		timer = new Timer(100, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        gifIndex++;
		        if (gifIndex >= images.length) {
		            gifIndex = 0;
		        }
		        repaint();
		    }
		});
		timer.start();

	}
 
	// 画像リソースの初期化
	private void initImageResource(){
		images = new Image[14];
		box_images = new Image[2];
		success_images = new Image[8];
		backgroundImage = Toolkit.getDefaultToolkit().getImage("res/background.png");
		for (int i = 0; i <= 13; i++) {
			if(i!=3)
				images[i] = Toolkit.getDefaultToolkit().getImage("res/pic" + i + ".png");
			else {
				box_images[0] = Toolkit.getDefaultToolkit().getImage("res/pic2.png");
				box_images[1] = Toolkit.getDefaultToolkit().getImage("res/BoxReach.png");
			}
			
		}
		for(int i=0;i<8;i++) {
			success_images[i] = Toolkit.getDefaultToolkit().getImage("res/success-" + i + ".png");
		}
	}
 
	private void initGame(int grade) {		
		// レベルを設定
		mLogic.setGrade(grade);
		map = mLogic.getMapData();
		// 左上角の左側のマージン
		marginLeft = (width - map[0].length * 30) / 2;
		// 左上角の上側のマージン
		marginTop = (height - map.length * 30) / 2 +70;		 
		repaint();
	}
 
	public void paint(Graphics g) {
		//　キャンバスをクリア
		

		g.drawImage(backgroundImage, 0, 0, width, height, this);
//		g.setColor(Color.white); 
		//g.fillRect(0, 0, width, height);
		// ゲーム要素の描画
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] != 0) {
					if(map[i][j]==3) {
						Image frameImage = box_images[(gifIndex%2+2)%2];
		                g.drawImage(frameImage, marginLeft + j * 30, marginTop + i * 30, 30, 30,this);
						
					}
					if(mLogic.isFinished() & (map[i][j] == 5 || map[i][j] == 6 || map[i][j] == 7 || map[i][j] == 8)) {
						Image frameImage = success_images[(gifIndex%8+8)%8];
		                g.drawImage(frameImage, marginLeft + j * 30, marginTop + i * 30, 30, 30,this);
					}else {
						if(map[i][j]!=3){
							g.drawImage(images[map[i][j]], marginLeft + j * 30, marginTop + i * 30, 30, 30, this);
			                
						}
					}
					
					
					
				}
			}
		g.setColor(Color.BLACK);
		g.setFont(new Font("楷体_2312", Font.BOLD, 30));
		FontMetrics fm = g.getFontMetrics();
		int x = (width - fm.stringWidth("Level  " +String.valueOf(grade + 1))) / 2;//中央揃えテキスト
		g.drawString("Level  " +String.valueOf(grade + 1), x-10, 150);
	}
 
	//主人公の移動を処理し、主人公が移動するたびに最新のマップデータを取得してインターフェースを描画する。そして、現在のレベルがクリアされたかどうかを判断する。
	private void move(int directionType) {
		switch (directionType) {
		case DIRECTION_UP:
			mLogic.moveUp();
			break;
		case DIRECTION_DOWN:
			mLogic.moveDown();
			break;
		case DIRECTION_LEFT:
			mLogic.moveLeft();
			break;
		case DIRECTION_RIGHT:
			mLogic.moveRight();
			break;
		}
		// map配列を取得する
		map = mLogic.getMapData();
		// インターフェースを再描画する
		repaint();
 
		// 移動のたびに、現在のレベルが完了したかどうかを判断する
		if (mLogic.isFinished()) {
			GameMusicUtil.game_stop();
			GameMusicUtil.success_play();
			isAcceptKey = false;
			System.out.print(grade);
			if (grade + 1 == GameMapSet.getGradeCount()) {
				DisplayOkToast("Congratulations on passing the final level!");
				
				
			} else {
				UIManager.put("OptionPane.yesButtonText", "Yes");
				UIManager.put("OptionPane.noButtonText", "No");
				String msg = "Congratulations on passing level " + (grade + 1) + "！！！\nDo you want to proceed to the next level？";
				int type = JOptionPane.YES_NO_OPTION;
				String title = "Success";
				int choice = 0;
				
				choice = JOptionPane.showConfirmDialog(this, msg, title, type);
				// 次のレベルに進むかどうかを尋ねる
				if (choice == 1) {
					System.exit(0);
				} else {
					isAcceptKey = true;
					GameMusicUtil.game_play();
					GameMusicUtil.success_stop();
					initGame(++grade);
				}
			}
		}
 
	}
 
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			move(DIRECTION_UP);
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			move(DIRECTION_DOWN);
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			move(DIRECTION_LEFT);
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			move(DIRECTION_RIGHT);
		}
 
		if (e.getKeyCode() == KeyEvent.VK_A) {// 前のレベル
			isAcceptKey = true;
			initGame(grade > 0 ? --grade : 0);
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {// このレベルを再開
			isAcceptKey = true;
			initGame(grade); 
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {// 次のレベル
			isAcceptKey = true;
		    initGame(grade < GameMapSet.getGradeCount() - 1 ? ++grade : GameMapSet.getGradeCount() - 1);
			return;
		}
		
		
		if (e.getKeyCode() == KeyEvent.VK_B) {// 取り消し
 
			if (isAcceptKey) {
				boolean undoResult = mLogic.undo();
				// 取り消しに成功したら、mapを再取得し、再描画する
				if (undoResult) {
					map = mLogic.getMapData();
					repaint();
				} else {
					DisplayErrorToast("Cannot return.");
				}
			}
 
		}
	}
 
	public void DisplayOkToast(String str) {
		UIManager.put("OptionPane.okButtonText", "OK");
		JOptionPane.showMessageDialog(this, str, "Success", JOptionPane.INFORMATION_MESSAGE);
	}
 
	public void DisplayErrorToast(String str) {
		JOptionPane.showMessageDialog(this, str, "Error", JOptionPane.ERROR_MESSAGE);
	}
 
	@Override
	public void keyReleased(KeyEvent e) {
 
	}
 
	@Override
	public void keyTyped(KeyEvent e) {
 
	}
 
}