package sokoban;
 
import java.util.Stack;
 
/**
 * 説明：ゲームロジッククラス、ゲーム内の様々なロジックを処理する
 */
public class GameLogic {
 
	// 主人公の位置（行と列）
	private int manPositionRow, manPositionColumn;
	// マップの行と列
	private int mapRows, mapColumns;
	// マップの配列
	private byte[][] map;
	// 一つ前の操作を取り消すために使用されるスタック
	private Stack<Dump> stack = new Stack<>();
	private static volatile GameLogic instance;
	private static final byte WALL = 1, BOX = 2, BOXONEND = 3, END = 4, MANDOWN = 5, MANLEFT = 6, MANRIGHT = 7,
			MANUP = 8, GRASS = 9, MANDOWNONEND = 10, MANLEFTONEND = 11, MANRIGHTONEND = 12, MANUPONEND = 13;
 
	private GameLogic() {
	}
 
	// パラメータの初期化
	private void initParams() {
		// スナップショットスタックをクリア
		stack.clear();
		// マップの行を初期化する
		mapRows = map.length;
		// マップの列を初期化する
		mapColumns = map[0].length;
		// 主人公の位置を取得する
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] == MANDOWN || map[i][j] == MANUP || map[i][j] == MANLEFT || map[i][j] == MANRIGHT) {
					manPositionRow = i;
					manPositionColumn = j;
					break;
				}
			}
		}
 
	}
 
	// マップデータの初期化
	private void initMap(int grade) {
		map = GameMapSet.getMap(grade);
	}
 
	//レベルを設定し、マップデータ、行と列の数、プレイヤーの位置を初期化する
	public void setGrade(int grade) {
		initMap(grade);
		initParams();
	}
 
	//現在のマップデータを返し、インターフェースの描画に使用する
	public byte[][] getMapData() {
		return realCloneArray(map);
	}
 
	// テストのためのマップの詳細を印刷する
	private void printMapDetails(byte[][] map) {
		System.out.println("***************************************");
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("***************************************");
	}
 
	// プレイヤーがレベルをクリアしたかどうかを判断する
	public boolean isFinished() {
		for (int i = 0; i < mapRows; i++)
			for (int j = 0; j < mapColumns; j++) {
				if (map[i][j] == END || map[i][j] == MANDOWNONEND || map[i][j] == MANUPONEND
						|| map[i][j] == MANLEFTONEND || map[i][j] == MANRIGHTONEND) {
					return false;
				}
			}
		return true;
	}
 
	// 現在の主人公が通路上にいるか終点にいるかを返す
	public byte isGrassOrEnd(byte man) {
		byte result = GRASS;
		if (man == MANLEFTONEND || man == MANRIGHTONEND || man == MANUPONEND || man == MANDOWNONEND) {
			result = END;
		}
 
		return result;
	}
 
	//スナップショットクラス、操作を元に戻すために使用する
	private class Dump {
		private int manPositionRow = 0;
		private int manPositionColumn = 0;
		private byte dumpMap[][];
 
		public Dump(int manPositionRow, int manPositionColumn, byte[][] map) {
			this.manPositionRow = manPositionRow;
			this.manPositionColumn = manPositionColumn;
			this.dumpMap = map;
		}
 
		public int getManPositionRow() {
			return manPositionRow;
		}
 
		public int getManPositionColumn() {
			return manPositionColumn;
		}
 
		public byte[][] getMap() {
			return dumpMap;
		}
	}
 
	//主人公が上に動く
	public void moveUp() {
		// もし主人公が上に一つ動いて壁だったら、何もしない
		if (map[manPositionRow - 1][manPositionColumn] == WALL)
			return;
		byte tempBox;
		byte tempMan;
		// もし主人公が上に一つ動いて箱だったら
		if (map[manPositionRow - 1][manPositionColumn] == BOX
				|| map[manPositionRow - 1][manPositionColumn] == BOXONEND) {
			// もし主人公が上に二つ目が通路または終点だったら
			if (map[manPositionRow - 2][manPositionColumn] == GRASS
					|| map[manPositionRow - 2][manPositionColumn] == END) {
				Dump dump = new Dump(manPositionRow, manPositionColumn,realCloneArray(map));
				// 現在の状態を保存する
				stack.push(dump);
				// もし主人公の二つ上が終点なら、箱の状態をBOXONENDに変更する
				tempBox = map[manPositionRow - 2][manPositionColumn] == END ? BOXONEND : BOX;
				// もし主人公の一つ上の箱が終点にあるなら、主人公の状態をMANUPONENDに変更する
				tempMan = map[manPositionRow - 1][manPositionColumn] == BOXONEND ? MANUPONEND : MANUP;
				// もし主人公が現在終点にいるなら、現在のノードの状態をENDに戻す必要がある
				map[manPositionRow][manPositionColumn] = isGrassOrEnd(map[manPositionRow][manPositionColumn]);
				map[manPositionRow - 2][manPositionColumn] = tempBox;
				map[manPositionRow - 1][manPositionColumn] = tempMan;
				// 主人公を上に一つ動かす
				manPositionRow--;
			}
		} else {
			// もし主人公が上に一つ動いて通路または終点だったら
			Dump dump = new Dump(manPositionRow, manPositionColumn,realCloneArray(map));
			// 現在の状態を保存する
			stack.push(dump);
			// もし主人公が上に一つ動いて終点だったら、主人公の状態をMANUPONENDに変更する
			tempMan = map[manPositionRow-1][manPositionColumn] == END ? MANUPONEND : MANUP;
			// もし主人公が現在終点にいるなら、現在のノードの状態をENDに戻す必要がある
			map[manPositionRow][manPositionColumn] = isGrassOrEnd(map[manPositionRow][manPositionColumn]);
			map[manPositionRow - 1][manPositionColumn] = tempMan;	
			// 主人公を上に一つ動かす
			manPositionRow--;
		}
 
	}
 
	//　主人公が下に動く。詳細なロジックはmoveUp()を参照してください
	public void moveDown() {
		if (map[manPositionRow + 1][manPositionColumn] == WALL)
			return;
		byte tempBox;
		byte tempMan;
 
		if (map[manPositionRow + 1][manPositionColumn] == BOX
				|| map[manPositionRow + 1][manPositionColumn] == BOXONEND) {
			if (map[manPositionRow + 2][manPositionColumn] == END
					|| map[manPositionRow + 2][manPositionColumn] == GRASS) {
				Dump dump = new Dump(manPositionRow, manPositionColumn, realCloneArray(map));
				stack.push(dump);
				tempBox = map[manPositionRow + 2][manPositionColumn] == END ? BOXONEND : BOX;
				tempMan = map[manPositionRow + 1][manPositionColumn] == BOXONEND ? MANDOWNONEND : MANDOWN;
				map[manPositionRow][manPositionColumn] = isGrassOrEnd(map[manPositionRow][manPositionColumn]);
				map[manPositionRow + 2][manPositionColumn] = tempBox;
				map[manPositionRow + 1][manPositionColumn] = tempMan;
				manPositionRow++;
			}
		} else {
			Dump dump = new Dump(manPositionRow, manPositionColumn,  realCloneArray(map));
			stack.push(dump);
			tempMan = map[manPositionRow + 1][manPositionColumn] == GRASS ? MANDOWN : MANDOWNONEND;
			map[manPositionRow][manPositionColumn] = isGrassOrEnd(map[manPositionRow][manPositionColumn]);
			map[manPositionRow + 1][manPositionColumn] = tempMan;
			manPositionRow++;
		}
	}
 
	//　主人公が左に動く。詳細なロジックはmoveUp()を参照してください
	public void moveLeft() {
		if (map[manPositionRow][manPositionColumn - 1] == WALL)
			return;
		byte tempBox;
		byte tempMan;
 
		if (map[manPositionRow][manPositionColumn - 1] == BOX
				|| map[manPositionRow][manPositionColumn - 1] == BOXONEND) {
			if (map[manPositionRow][manPositionColumn - 2] == END
					|| map[manPositionRow][manPositionColumn - 2] == GRASS) {
				Dump dump = new Dump(manPositionRow, manPositionColumn, realCloneArray(map));
				stack.push(dump);
				tempBox = map[manPositionRow][manPositionColumn - 2] == END ? BOXONEND : BOX;
				tempMan = map[manPositionRow][manPositionColumn - 1] == BOXONEND ? MANLEFTONEND : MANLEFT;
				map[manPositionRow][manPositionColumn] = isGrassOrEnd(map[manPositionRow][manPositionColumn]);
				map[manPositionRow][manPositionColumn - 2] = tempBox;
				map[manPositionRow][manPositionColumn - 1] = tempMan;
				manPositionColumn--;
			}
		} else {
			Dump dump = new Dump(manPositionRow, manPositionColumn, realCloneArray(map));
			stack.push(dump);
			tempMan = map[manPositionRow][manPositionColumn - 1] == GRASS ? MANLEFT : MANLEFTONEND;
			map[manPositionRow][manPositionColumn] = isGrassOrEnd(map[manPositionRow][manPositionColumn]);
			map[manPositionRow][manPositionColumn - 1] = tempMan;
			manPositionColumn--;
		}
	}
 
	//　主人公が右に動く。詳細なロジックはmoveUp()を参照してください
	public void moveRight() {
		if (map[manPositionRow][manPositionColumn + 1] == WALL)
			return;
		byte tempBox;
		byte tempMan;
 
		if (map[manPositionRow][manPositionColumn + 1] == BOX
				|| map[manPositionRow][manPositionColumn + 1] == BOXONEND) {
			if (map[manPositionRow][manPositionColumn + 2] == END
					|| map[manPositionRow][manPositionColumn + 2] == GRASS) {
				Dump dump = new Dump(manPositionRow, manPositionColumn, realCloneArray(map));
				stack.push(dump);
				tempBox = map[manPositionRow][manPositionColumn + 2] == END ? BOXONEND : BOX;
				tempMan = map[manPositionRow][manPositionColumn + 1] == BOXONEND ? MANRIGHTONEND : MANRIGHT;
				map[manPositionRow][manPositionColumn] = isGrassOrEnd(map[manPositionRow][manPositionColumn]);
				map[manPositionRow][manPositionColumn + 2] = tempBox;
				map[manPositionRow][manPositionColumn + 1] = tempMan;
				manPositionColumn++;
			}
		} else {
			Dump dump = new Dump(manPositionRow, manPositionColumn,  realCloneArray(map));
			stack.push(dump);
			tempMan = map[manPositionRow][manPositionColumn + 1] == GRASS ? MANRIGHT : MANRIGHTONEND;
			map[manPositionRow][manPositionColumn] = isGrassOrEnd(map[manPositionRow][manPositionColumn]);
			map[manPositionRow][manPositionColumn + 1] = tempMan;
			manPositionColumn++;
		}
 
	}
	
	//　2次元配列をディープコピーする
	private byte[][] realCloneArray(byte[][] map){
		byte[][] cloneMap = new byte[map.length][map[0].length];
		for(int i=0;i<map.length;i++){
			cloneMap[i] = map[i].clone();
		}
		return cloneMap;
	}
	
	// 一つ前の操作を元に戻す、元に戻すことに成功した場合はtrueを返し、そうでない場合はfalseを返す
	public boolean undo() {
		if (stack.isEmpty()) {
			return false;
		}
		Dump dump = stack.pop();
		this.manPositionRow = dump.getManPositionRow();
		this.manPositionColumn = dump.getManPositionColumn();
		this.map = dump.getMap();
		return true;
	}
 
	//　シングルトンパターンでゲームロジック処理クラスを作成する
	public static GameLogic getInstance() {
		if (instance == null) {
			synchronized (GameLogic.class) {
				if (instance == null) {
					instance = new GameLogic();
				}
			}
		}
		return instance;
	}
 
}