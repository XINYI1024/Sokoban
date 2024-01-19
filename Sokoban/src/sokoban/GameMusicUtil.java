package sokoban;
import java.io.File; 
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
 
/**
 * 説明：ゲーム内の音楽を管理し、再生するためのもので、MIDIシステムを使用して、異なるシーン（開始、ゲーム中、終了）のBGMをロード、再生、停止する。
 */

public class GameMusicUtil {
	
	// _musicFile は音楽ファイルのパスを保存する
	// _seq は、該当するシーンのMIDIシーケンスオブジェクトで、音楽データを格納する
	// _midi はMIDIシーケンサーオブジェクトで、対応する音楽シーケンスを再生する
	// 静的初期化ブロックを使用して、3つのシーンの音楽ファイルをそれぞれロードし、それをMIDIシーケンスに変換し、対応するシーケンサーインスタンスを取得する
	private static String game_musicFile;
	private static String menu_musicFile;
	private static String success_musicFile;
	private static Sequence game_seq;
	private static Sequencer game_midi;
	private static Sequence menu_seq;
	private static Sequencer menu_midi;
	private static Sequence success_seq;
	private static Sequencer success_midi;
	static {
		try {
			game_musicFile = new String("res/nor.mid");
			game_seq = MidiSystem.getSequence(new File(game_musicFile));
			game_midi = MidiSystem.getSequencer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static {
		try {
			success_musicFile = new String("res/success.mid");
			success_seq = MidiSystem.getSequence(new File(success_musicFile));
			success_midi = MidiSystem.getSequencer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static {
		try {
			menu_musicFile = new String("res/menu.mid");
			menu_seq = MidiSystem.getSequence(new File(menu_musicFile));
			menu_midi = MidiSystem.getSequencer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	// _play再生；_stop停止
	public static void game_play() {
		try {
			if(game_midi!=null){
				game_midi.open();
				game_midi.setSequence(game_seq);				
				game_midi.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
				game_midi.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	
	public static void game_stop() {
		if(game_midi!=null){
			game_midi.stop();
			game_midi.close();			
		}
	}
	
	public static void menu_play() {
		try {
			if(menu_midi!=null){
				menu_midi.open();
				menu_midi.setSequence(menu_seq);				
				menu_midi.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
				menu_midi.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	
	public static void menu_stop() {
		if(menu_midi!=null){
			menu_midi.stop();
			menu_midi.close();			
		}
	}
	
	public static void success_play() {
		try {
			if(success_midi!=null){
				success_midi.open();
				success_midi.setSequence(success_seq);				
				success_midi.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
				success_midi.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	
	public static void success_stop() {
		if(success_midi!=null){
			success_midi.stop();
			success_midi.close();			
		}
	}
}

