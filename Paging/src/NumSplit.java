public class NumSplit { //文字列1行と数値を引数に指定された文字数で文字列を区切るクラス
    String [] result;

    NumSplit(int N, String L){
        int num_split = N;
        String line = L;
        //文字列の文字数と区切りたい文字数が割り切れないと警告する
        if (line.length() %num_split != 0) System.out.println("入力文字列に不備のある可能性があります");
        this.result = new String[line.length()];

        int i=0;
        //Stringクラスのsubstringメソッドを使い分けていく
        for (int j=0; j<line.length(); j+=num_split){
            this.result[i] = line.substring(j,j+num_split);
            i += 1;
        }
    }
}