import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LFU {
    public static Queue<Page> Page; //入力文字列
    public static List<Page> Memory; //メモリ領域
    public static int memSize;//メモリノサイズ
    public static int cntFault; //ページフォルトの回数を記録する変数
    public static int time;//時間

    LFU(Queue<Page> Q, int PGNUM) { //コンストラクタで初期化
        Page = new ArrayDeque<>(Q);
        Memory = new ArrayList<>(1);
        memSize = PGNUM;
        cntFault = 0;
        time = 1;
        paging();//処理をする
        System.out.println("\n ページフォルトが"+cntFault +"回起こりました");
    }

    public void paging(){
        boolean flag;//フラグ
        Page p;//オブジェクトを一時的に入れておくもの

        //Pageが空になるまで回す
        while (Page.peek() != null) {
            flag = false;
            System.out.print((time++)+"ms ->");//時間の経過

                //メモリ内に読み込みたいページがあるか
            for (int i=0; i<Memory.size(); i++)
                if (Memory.get(i).pageNum == Page.element().pageNum){//あったら
                    Page.remove();
                    Memory.get(i).refCnt++;//参照回数++
                    i = Memory.size();
                    flag = false; //ページフォルトなし
                }else flag = true;//ページフォルトあり

            if (time==2) flag = true; //一番最初は上の処理に入らないので強制的にページフォル メモリ空だから

            if (flag) { //ページフォルト
                cntFault++; //ページフォルトの回数++
                if (Memory.size() == memSize){ //LFUの処理
                    Memory = Memory.stream().sorted().collect(Collectors.toList());
                    Memory.remove(0); //メモリから消す
                }

                p = Page.remove();
                p.refCnt ++;//呼び出し回数++
                Memory.add(p);//メモリに追加
            }

            //表示
            Memory.stream()
                    .map(i -> " " + i.pageNum)
                    .forEach(System.out::print);

            System.out.println();
        }
    }
}