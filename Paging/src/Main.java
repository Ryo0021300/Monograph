//import
import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {//mainクラス
    static String crlf = System.getProperty("line.separator");//システムから改行コードを取得
    public static void main(String[] args) throws IOException {//メイン文

        while (true) {//問題の入力とアルゴリズムの入力ブロック 停止要求しない限り無限ループ
            String file;
            while (true) {
                System.out.println(crlf + "選択肢から数値を入力してください　ファイル参照 / 手入力 / システム終了");
                System.out.println("1 -- Pi.txt");
                System.out.println("2 -- Root.txt");
                System.out.println("3 -- Manual entry");
                System.out.println("4 -- Exit");
                file = new Scanner(System.in).nextLine();
                if (file.length() == 1 && (file.equals("1") || file.equals("2") || file.equals("3") || file.equals("4"))) break;
                else System.out.println("Input is failed");
            }

            FileReader fr;
            BufferedReader br;
            String line = "";
            switch (file) { //入力ソースの場合分け
                case "1" :
                    fr = new FileReader("Pi.txt");
                    br = new BufferedReader(fr);
                    line = br.readLine();
                    break;
                case "2":
                    fr = new FileReader("Root.txt");
                    br = new BufferedReader(fr);
                    line = br.readLine();
                    break;
                case "3" :
                    while (true) {
                        System.out.println("数値を偶数桁で入力してください");
                        line = new Scanner(System.in).nextLine();
                        if (line.length() % 2 == 0) break;
                        else System.out.println("入力エラー");
                    }
                    break;
                case "4":
                    System.out.println("システム終了");
                    System.exit(-1);
                    break;
                default:
                    System.out.println("Input is failed");
                    break;
            }

            NumSplit n = new NumSplit(1, line); //NumSplitクラスを使い、入力文字列を指定数で区切って配列にする
            String Algo;
            while (true) {
                System.out.println("選択肢から実行するアルゴリズムの数値を入力してください");
                System.out.println("1 -- FIFO");
                System.out.println("2 -- LFU");
                Algo = new Scanner(System.in).nextLine();
                if (Algo.length() == 1 && (Algo.equals("1") || Algo.equals("2"))) break;
                else System.out.println("Input is failed");
            }

            //ページ数の指定
            String pgNum="";
            System.out.println("ページ数を指定して下さい");
            pgNum = new Scanner(System.in).nextLine();

            //入力文字列の配列をキューに追加 FIFO
            Queue<Integer> PageFIFO = new ArrayDeque<>();
            for (int i=0 ; i<n.result.length; i++)
                PageFIFO.offer(Integer.parseInt(n.result[i]));

            //入力文字列の配列をキューに追加 LFU
            Queue<Page> PageLFU = new ArrayDeque<>();
            for (int i=0; i<n.result.length; i++)
                PageLFU.offer(new Page(Integer.parseInt(n.result[i])));

            switch (Algo) { //アルゴリズムの場合分け
                case "1":
                    new FIFO(PageFIFO, Integer.parseInt(pgNum)); //Queue,ページ数を引数にFIFOクラスをインスタンス化
                    break;
                case "2":
                    new LFU(PageLFU, Integer.parseInt(pgNum)); //Queue,ページ数を引数にLRUクラスをインスタンス化
                    break;
                default:
                    System.out.println("Input is failed");
                    break;
            }
        }
    }
}