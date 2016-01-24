public class Page implements Comparable<Page>{
    public int pageNum;//ページ番号
    public int refCnt;//ページ参照回数
    Page(int p){
        this.pageNum = p;
        this.refCnt = 0;
    }

    @Override
    public int compareTo(Page o) { //並び替えの設定
        if (this.refCnt > o.refCnt) return 1;
        else if (this.refCnt < o.refCnt) return -1;
        else return 0;
    }
}