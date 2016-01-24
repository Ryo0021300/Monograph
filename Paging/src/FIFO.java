import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class FIFO {
    public static Queue<Integer> Page; //入力文字列
    public static ArrayList<Integer> Memory; //メモリ領域
    public static int memSize;
    public static int cntFault; //ページフォルトの回数を記録する変数
    public static int time;

    FIFO(Queue<Integer> Q, int PGNUM) { //コンストラクタで初期化
        Page = new ArrayDeque<>(Q);
        Memory = new ArrayList<>(1);
        memSize = PGNUM;
        cntFault = 0;
        time = 1;
        paging();//処理をする
        System.out.println("\nページフォルトが "+cntFault +"回 起こりました");
    }

    public void paging(){
        boolean flag;

        //Pageが空になるまで回す
        while (Page.peek() != null) {
            flag = false;//flagの初期化
            System.out.print((time++)+"ms ->");//時間の経過

            for (int i=0; i<Memory.size(); i++)
                if (Memory.get(i) == Page.element()){ //メモリ内に読み込みたいページがあれば
                    Page.remove();
                    i = Memory.size();
                    flag = false; //ページフォルトなし
                }else flag = true; //ページフォルトあり

            if (time==2) flag=true;  //一番最初は上の処理に入らないので強制的にページフォル メモリ空だから

            if (flag) { //ページフォルト
                cntFault++;
                if (Memory.size() == memSize) Memory.remove(0);
                Memory.add(Page.remove());
            }

            //表示
            Memory.stream()
                    .map(i -> " " + i)
                    .forEach(System.out::print);

            System.out.println();
        }
    }
}