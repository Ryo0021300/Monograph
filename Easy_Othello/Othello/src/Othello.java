import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Othello extends JPanel implements ActionListener{
    public static int[][] boardInfo = new int[10][10];
    public static int[][] pre_boardInfo = new int[10][10];
    public static boolean[][] boardCanPut = new boolean[10][10];
    public static final int sentinel = -1; //番兵
    public static final int empty = 0;//空
    public static final int black = 1;//黒
    public static final int white = 2;//白
    public static boolean firstTurn = false;
    public static int turn;//ターン
    public static  final int black_t = 1;
    public static  final int white_t = 0;
    public static Image black_img = Toolkit.getDefaultToolkit().getImage("./Othello/Material/Black.png");
    public static Image white_img = Toolkit.getDefaultToolkit().getImage("./Othello/Material/White.png");
    public static Image turn_blackImage = Toolkit.getDefaultToolkit().getImage("./Othello/Material/turn_blackImage.png");
    public static Image turn_whiteImage = Toolkit.getDefaultToolkit().getImage("./Othello/Material/turn_whiteImage.png");
    public static Image winsBlack = Toolkit.getDefaultToolkit().getImage("./Othello/Material/winsBlack.png");
    public static Image winsWhite = Toolkit.getDefaultToolkit().getImage("./Othello/Material/winsWhite.png");
    public static Image winsDraw = Toolkit.getDefaultToolkit().getImage("./Othello/Material/winsDraw.png");
    public static Image canPut = Toolkit.getDefaultToolkit().getImage("./Othello/Material/can.png");
    public static Image num = Toolkit.getDefaultToolkit().getImage("./Othello/Material/num.png");
    public static Image[] b_Num = new Image[10];
    public static Image[] w_Num = new Image[10];
    public  AudioClip push = Applet.newAudioClip(getClass().getResource("push.wav"));
    public  AudioClip click = Applet.newAudioClip(getClass().getResource("click.wav"));
    public  AudioClip finish = Applet.newAudioClip(getClass().getResource("finish.wav"));
    public  AudioClip initialize = Applet.newAudioClip(getClass().getResource("initialize.wav"));
    public static boolean playCount = false;
    JButton undoBtn, restartBtn, exitBtn;

    //コンストラクタ
    Othello(){
        //初期化
        Initialize();

        //ボタンの配置
        JButton btn;
        this.setLayout(null);
        for (int i = 0; i < 8; i ++) {
            for (int j = 0; j < 8; j ++) {
                btn = new JButton();
                btn.setBounds(j*100, i*100, 100, 100);
                btn.addActionListener(this);
                btn.setActionCommand(String.valueOf(j+1) + "," + String.valueOf(i+1) +","+ "0");
				btn.setBorderPainted(false); //ボタンを透過
                this.add(btn);
            }
        }

        //戻るボタン
        undoBtn = new JButton("ひとつ戻る");
        undoBtn.setBounds(850, 400, 200, 60);
        undoBtn.setFont(new Font(Font.SERIF,Font.PLAIN,24));
        undoBtn.addActionListener(this);
        undoBtn.setActionCommand("0,0,1");
        undoBtn.setEnabled(false);
        this.add(undoBtn);
        //リスタートボタン
        restartBtn = new JButton("もう一回やる");
        restartBtn.setBounds(850, 480, 200, 60);
        restartBtn.setFont(new Font(Font.SERIF,Font.PLAIN,24));
        restartBtn.addActionListener(this);
        restartBtn.setActionCommand("0,0,2");
        restartBtn.setEnabled(false);
        this.add(restartBtn);
        //終了ボタン
        exitBtn = new JButton("ゲームを終了");
        exitBtn.setBounds(850, 560, 200, 60);
        exitBtn.setFont(new Font(Font.SERIF, Font.PLAIN, 24));
        exitBtn.addActionListener(this);
        exitBtn.setActionCommand("0,0,3");
        this.add(exitBtn);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image board_img = Toolkit.getDefaultToolkit().getImage("./Othello/Material/Board.png");
        Image menu_background = Toolkit.getDefaultToolkit().getImage("./Othello/Material/MenuBackGround.png");

        g.drawImage(board_img, 0, 0, this); //盤面の描画
        g.drawImage(menu_background,800,0,this);//メニュー背景の描画
        if (turn == black_t) g.drawImage(turn_blackImage,800,67,this);//黒の番のイメージ
        else g.drawImage(turn_whiteImage,800,67,this);//白の番のイメージ

        //置ける場所の表示
        canPutArea();
        for (int i=0; i<boardCanPut[0].length; i++)
            for (int j=0; j<boardCanPut.length; j++)
                if (boardCanPut[i][j]) g.drawImage(canPut, ((i-1)*100)+2, ((j-1)*100)+2, this);

        //石の描画と石の数をカウント
        int blackNum = 0;
        int whiteNum = 0;
        boolean emptyFlag = false;
        for (int i = 0; i < boardInfo.length; i++) {
            for (int j = 0; j < boardInfo[0].length; j++) {
                if (boardInfo[i][j] == black) {
                    g.drawImage(black_img, (i - 1) * 100, (j - 1) * 100, this);
                    blackNum++;
                }else if (boardInfo[i][j] == white) {
                    g.drawImage(white_img, (i - 1) * 100, (j - 1) * 100, this);
                    whiteNum++;
                }else if (boardInfo[i][j] == empty) emptyFlag = true;
            }
        }

        //勝敗判定 表示
        if (!emptyFlag || blackNum==0 || whiteNum==0) {
            if (!playCount){
                finish.play();
                playCount = true; }

            restartBtn.setEnabled(true);
            undoBtn.setEnabled(false);

            //画像
            if (blackNum > whiteNum) g.drawImage(winsBlack, 800, 67, this);
            else if (blackNum < whiteNum) g.drawImage(winsWhite, 800, 67, this);
            else g.drawImage(winsDraw, 800, 50, this);
        }

        //石の数を表示
        //数字の画像の読み込み
        for (int i=0; i<10; i++){
            b_Num[i] = Toolkit.getDefaultToolkit().getImage("./Othello/Material/b_" + i + ".png");
            w_Num[i] = Toolkit.getDefaultToolkit().getImage("./Othello/Material/w_" + i + ".png"); }

        g.drawImage(num,800,265,this); //B Wの画像

        if (blackNum < 10) {
            for (int i=0; i<10; i++)
                if (blackNum == i) g.drawImage(b_Num[i], 880, 280, this);
        }else {
            for (int i = 1; i < 10; i++)
                for (int k = 0; k < 10; k++)
                    if (blackNum == ((i * 10) + k)) {
                        for (int j = 0; j < 10; j++)
                            if (i == j)
                                g.drawImage(b_Num[j], 864, 280, this);
                        for (int j = 0; j < 10; j++)
                            if (k == j)
                                g.drawImage(b_Num[j], 893, 280, this);}
        }

        if (whiteNum < 10) {
            for (int i=0; i<10; i++)
                if (whiteNum == i) g.drawImage(w_Num[i], 1007, 280, this);
        }else {
            for (int i = 1; i < 10; i++)
                for (int k = 0; k < 10; k++)
                    if (whiteNum == ((i * 10) + k)) {
                        for (int j = 0; j < 10; j++)
                            if (i == j)
                                g.drawImage(w_Num[j], 991, 280, this);
                        for (int j = 0; j < 10; j++)
                            if (k == j)
                                g.drawImage(w_Num[j], 1020, 280, this);}
        }
    }

    //イベント処理
    public void actionPerformed(ActionEvent e){
        String st = e.getActionCommand(); //ボタンからコマンドを受け取る
        String[] index = st.split(",",0);
        int x = Integer.parseInt(index[0]);//x座標
        int y = Integer.parseInt(index[1]);//y座標
        int z = Integer.parseInt(index[2]);

        if (z == 0) {
            if (canPutDown(x, y)) { //石を置けるか
                push.play(); //石を置く音
                //戻るボタンの処理
                if (firstTurn)//最初のターン以外の場合,前状態を保持
                    for (int i=0 ; i<boardInfo.length; i++)
                        System.arraycopy(boardInfo[i], 0, pre_boardInfo[i], 0, boardInfo[0].length);
                undoBtn.setEnabled(true);
                putDownStone(x, y); //そのマスに石を置く
                reverse(x, y);//置いたことによる周りの反転
                if (turn == black_t) turn = white_t;//ターンを反転
                else turn = black_t;
            }

            //置ける場所がなくなった場合、ターンを維持
            boolean canPut = false;
            for (int i = 1; i < 9; i++)
                for (int j = 1; j < 9; j++)
                    if (canPutDown(i, j)) canPut = true;
            if (!canPut)
                if (turn == black_t) turn = white_t;
                else turn = black_t;

            firstTurn = true;
            repaint();

        }else switch (z) {
            case 1: //戻るボタンの動作
                if (undoBtn.isEnabled()) {
                    undoBtn.setEnabled(false);
                    for (int i = 0; i < boardInfo.length; i++)
                        System.arraycopy(pre_boardInfo[i], 0, boardInfo[i], 0, pre_boardInfo[0].length);
                    if (turn == black_t) turn = white_t;//ターンを反転
                    else turn = black_t;
                    repaint();
                    click.play();
                    break;
                }
            case 2 : //リスタートボタンの動作
                Initialize();
                restartBtn.setEnabled(false);
                repaint();
                initialize.play();
                break;
            case 3 : //終了ボタンの動作
                click.play();
                System.exit(-1);
                break;
            default :
                System.out.println("エラー");
                break;
        }
    }
    //初期化関数
    public void Initialize(){
        for (int i=0; i < boardInfo.length; i++) {
            for (int j = 0; j < boardInfo[0].length; j++)
                if (i == 0 || j == 0 || i == 9 || j == 9) boardInfo[i][j] = sentinel; //周りを番兵で囲む
                else boardInfo[i][j] = empty;//空
        }

        //初期石の設定
        boardInfo[4][4] = black;
        boardInfo[4][5] = white;
        boardInfo[5][4] = white;
        boardInfo[5][5] = black;

        //前状態を保持
        for (int i=0 ; i<boardInfo.length; i++)
            System.arraycopy(boardInfo[i], 0, pre_boardInfo[i], 0, boardInfo[0].length);
        //初期のターン
        turn = black_t;
        //finish音の一回限りのフラグ
        playCount = false;
    }
    //置ける場所を保持
    public void canPutArea(){
        for (int i=0; i<boardCanPut[0].length; i++)
            for (int j=0; j<boardCanPut.length; j++) {
                boardCanPut[i][j] = false;
                if (canPutDown(i, j)) boardCanPut[i][j] = true;
            }
    }
    //石の可打判定
    public boolean canPutDown(int x, int y){
        //すでに石が置かれていたら打てない
        if (boardInfo[x][y] != empty) return false;
        //8方向に対して打てるか
        for (int i=-1; i<2; i++)
            for (int j=-1; j<2; j++)
                if (canPutDown(x,y,i,j)) return true;
        //打てなければ偽
        return false;
    }
    //石の可打判定
    public boolean canPutDown(int x, int y, int vecX, int vecY){
        int putStone;
        if (turn == black_t) putStone = black;
        else putStone = white;

        x += vecX;
        y += vecY;
        if (boardInfo[x][y] == putStone) return false; //周りが自色なら打てない
        if (boardInfo[x][y] == empty) return false;//周りが空なら打てない
        if (boardInfo[x][y] == sentinel) return false;//番兵

        x += vecX;
        y += vecY;
        while (boardInfo[x][y] != sentinel){//番兵になるまでループ
            if(boardInfo[x][y] == empty) return false;
            if (boardInfo[x][y] == putStone) return true; //自分の色が来たら挟めるので、真を返す
            x += vecX;
            y += vecY;
        }
        return false;
    }
    //石を置くメソッド
    public void putDownStone(int x, int y){
        int stone;

        if (turn == black_t) stone = black;
        else stone = white;

        boardInfo[x][y] = stone;
        //ここで音を鳴らす
    }
    //反転判定
    public void reverse(int x, int y){
        for (int i=-1; i<2; i++)
            for (int j=-1; j<2; j++)
                if (canPutDown(x,y,i,j)) reverse(x,y,i,j);
        //ひっくり返せるか判定した後に、ひっくり返すメソッドを呼ぶ
    }
    //ひっくり返す
    public void reverse(int x, int y, int vecX, int vecY){
        int putStone;
        if (turn == black_t) putStone = black;
        else putStone = white;

        x += vecX;
        y += vecY;
        while (boardInfo[x][y] != putStone){//自色が来るまでループ
            boardInfo[x][y] = putStone;//反転
            //　音??
            x += vecX;
            y += vecY;
        }
    }
}