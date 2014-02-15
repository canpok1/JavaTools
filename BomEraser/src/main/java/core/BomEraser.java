package core;

import java.io.File;

/**
 * 指定ファイルからBomを除去します。
 * @author tanabe
 *
 */
public class BomEraser {

    /**
     * プログラムのエントリポイント
     * @param args 起動時の引数
     * <ul>
     *  <li>第一引数は入力ファイルのパス。</li>
     *  <li>第二引数は出力先パス。</li>
     *  <li>第三引数以降は無視されます。</li>
     * </ul>
     */
    public static void main(String[] args) {
        try {
            if(args.length < 2) {
                dispHelp();
            }
        
            BomEraser eraser = new BomEraser();
        }
        catch(Exception e) {
            System.out.println("例外発生");
            System.out.println(e.toString());
        }
    }
    
    /**
     * 使い方を表示します。
     */
    public static void dispHelp() {
        System.out.println("指定ファイルからBomを除去したファイルを作成します。");
        System.out.println("第一引数 : 入力ファイル");
        System.out.println("第二引数 : 出力先");
    }
    
    /**
     * 指定ファイルからBomを除去します。
     * @param file 指定ファイル
     * @return Bomを除去したファイルの出力先。指定ファイルが存在しない場合はnull。
     * @throws IllegalArgumentException 引数がNullの場合
     */
    public File erase(File file) {
        if(file == null) {
            throw new IllegalArgumentException("引数がnull");
        }
        if(!file.exists()) {
            return null;
        }
        
        return null;
    }
}
