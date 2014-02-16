package core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
     * 入力ファイルからBomを除去したファイルを、指定の出力先に出力します。
     * @param file 入力ファイル
     * @param output 出力先
     * @return 処理結果
     * <ul>
     *   <li>true:出力成功</li>
     *   <li>false:出力失敗</li>
     * </ul>
     * @throws IllegalArgumentException 引数がNullの場合
     * @throws IOException 処理中に例外が発生した場合
     */
    public boolean erase(File input, File output) throws IOException {
        if(input == null) {
            throw new IllegalArgumentException("入力ファイルがnull");
        }
        if(output == null) {
            throw new IllegalArgumentException("出力先がnull");
        }
            
        if(!input.exists()) {
            return false;
        }
        if(output.exists()) {
            return false;
        }
        
        try (
            BufferedInputStream inputStream = this.createSkipBOMStream(input);
            FileOutputStream outputStream = new FileOutputStream(output);
        ) {
            int data;
            while((data = inputStream.read()) > 0) {
                outputStream.write(data);
            }
            return true;
        }
        catch (FileNotFoundException e) {
            throw new IOException(e);
        }
    }
    
    /**
     * BOMを読み飛ばすStreamを作成します。
     * @param file 読み取り対象のファイル
     * @return BOMを読み飛ばすStream
     * @throws IOException Streamの生成に失敗した場合
     */
    public BufferedInputStream createSkipBOMStream( File file ) throws IOException {
        if(file == null) {
            throw new IllegalArgumentException("引数がnull");
        }
        
        BufferedInputStream stream = null;
        try {
            stream = new BufferedInputStream(
                        new FileInputStream(file));
            
            if(stream.available() >= 3) {
                byte[] buffer = {0,0,0};
                stream.read(buffer, 0, 3);
                if(this.isBom(buffer)) {
                    // 読み込み位置をBOMの直後に修正
                    stream.mark(3);
                }
            }
            
            return stream;
        }
        catch(FileNotFoundException e) {
            if(stream != null) {
                stream.close();
            }
            throw new IOException(e);
        }
    }
    
    /**
     * 指定配列の先頭がBOMを判定します。
     * @param target 指定配列
     * @return 判定結果
     * <ul>
     *  <li>true:指定配列の先頭がBOMである</li>
     *  <li>false:指定配列の先頭がBOMではない</li>
     * </ul>
     * @throws IllegalAccessException 引数がnullの場合
     */
    public boolean isBom(byte[] target) {
        if(target == null) {
            throw new IllegalArgumentException("引数がnull");
        }
        
        if(target.length < 3) {
            return false;
        }
        
        if( target[0] != (byte)0xEF ) {
            return false;
        }
        
        if(target[1] != (byte)0xBB ) {
            return false;
        }
        
        if(target[2] != (byte)0xBF ) {
            return false;
        }
        
        return true;
    }
}

