package jp.gr.java_conf.ktnet.bom;

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
                return;
            }
        
            File input = new File(args[0]);
            File output = new File(args[1]);
            if(new BomEraser().erase(input, output)) {
                System.out.println("出力成功" + "[" + input.getPath() + "]"
                                    + ">>[" + output.getPath() + "]");
            }
            else {
                System.out.println("出力失敗" + "[" + input.getPath() + "]"
                                    + ">>[" + output.getPath() + "]");
            }
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
     * @param input 入力ファイル
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
        
        BufferedInputStream inputStream = null;
        try {
            FileOutputStream outputStream = null;
            inputStream = this.createSkipBOMStream(input);
            try {
                outputStream = new FileOutputStream(output);
                int data;
                while((data = inputStream.read()) > 0) {
                    outputStream.write(data);
                }
                return true;
            }
            finally {
                if(outputStream != null) {
                    outputStream.close();
                }
            }
        }
        finally {
            if(inputStream != null) {
                inputStream.close();
            }
        }
    }
    
    /**
     * BOMを読み飛ばすStreamを作成します。
     * @param file 読み取り対象のファイル
     * @return BOMを読み飛ばすStream。ファイルが存在しない場合はnull。
     * @throws IOException Streamの生成に失敗した場合
     */
    public BufferedInputStream createSkipBOMStream( File file ) throws IOException {
        if(file == null) {
            throw new IllegalArgumentException("引数がnull");
        }
        if(!file.exists()) {
            return null;
        }
        
        if(isBomFile(file)) {
            BufferedInputStream stream = new BufferedInputStream(
                                            new FileInputStream(file));
            // 読み込み位置をBOMの直後に修正
            stream.mark(3);
            return stream;
        } else {
            return new BufferedInputStream(
                    new FileInputStream(file));
        }
    }

    /**
     * 指定ファイルがBOMから始まるか判定します。
     * @param file 指定ファイル
     * @return 判定結果
     * <ul>
     *   <li>true:指定ファイルがBOMから始まる</li>
     *   <li>false:指定ファイルがBOMから始まらない</li>
     * </ul>
     * @throws IOException ファイル読み込みに失敗した場合
     */
    public boolean isBomFile(File file) throws IOException {
        
        if(file == null) {
            throw new IllegalArgumentException("引数がnull");
        }
        
        BufferedInputStream stream = null;
        try {
            stream = new BufferedInputStream(
                        new FileInputStream(file));
            if(stream.available() < 3) {
                return false;
            }
            byte[] buffer = {0,0,0};
            stream.read(buffer, 0, 3);
            return this.isBom(buffer);
        }
        catch(FileNotFoundException e) {
            throw new IOException(e);
        }
        finally {
            if(stream != null) {
                stream.close();
            }
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

