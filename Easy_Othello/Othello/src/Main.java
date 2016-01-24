import javax.swing.*;

public class Main{
	public static void main(String[] args){
		JFrame frame = new JFrame("Easy Othello");
		frame.getContentPane().add(new Othello());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じるとプログラムも終了
		frame.setSize(1100, 822);
		frame.setLocationRelativeTo(null);//ウィンドウ中央に表示
		frame.setVisible(true);//可視化
	}
}