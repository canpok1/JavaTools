package jp.gr.java_conf.ktnet.bom;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import mockit.Expectations;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * BomEraserクラスのテスト
 * @author tanabe
 *
 */
@RunWith(Enclosed.class)
public class BomEraserTest {

    /**
     * 存在するファイル(BOM付)を取得します。
     * @return 存在するファイル(BOM付)
     */
    public static File getExistingFileWithBom() {
        String path = "build/resources/test/FileWithBom.txt";
        return new File(path);
    }

    /**
     * 存在するファイル(BOMなし)を取得します。
     * @return 存在するファイル(BOMなし)
     */
    public static File getExistingFileWithoutBom() {
        String path = "build/resources/test/FileWithoutBom.txt";
        return new File(path);
    }
    
    /**
     * 存在しないファイルを取得します。
     * @return 存在しないファイル
     */
    public static File getNotExistingFile() {
        String path = "build/resources/test/NonExisting.txt";
        return new File(path);
    }
    
    /**
     * BomEraserのmainメソッド用テストクラス
     * @author tanabe
     *
     */
    public static class MainTest {
        @Test
        public void 引数が0個の時はdispHelpが呼ばれる()
        {
            new Expectations(BomEraser.class) {{
                BomEraser.dispHelp();
            }};
            
            String[] args = {};
            BomEraser.main(args);
        }
        
        @Test
        public void 引数が1個の時はdispHelpが呼ばれる()
        {
            new Expectations(BomEraser.class) {{
                BomEraser.dispHelp();
            }};
            
            String[] args = {"abc"};
            BomEraser.main(args);
        }
        
        @Test
        public void 引数2個だとeraseが呼ばれ戻り値trueだと例外が発生しない() throws IOException
        {
            new Expectations(BomEraser.class) {{
                new BomEraser().erase((File)any, (File)any); result = true;
            }};
            
            String[] args = {"abc", "def"};
            BomEraser.main(args);
        }
        
        @Test
        public void 引数2個だとeraseが呼ばれ戻り値falseだと例外が発生しない() throws IOException
        {
            new Expectations(BomEraser.class) {{
                new BomEraser().erase((File)any, (File)any); result = false;
            }};
            
            String[] args = {"abc", "def"};
            BomEraser.main(args);
        }
        
        @Test
        public void eraseが例外を返すときは例外が発生しない() throws IOException
        {
            new Expectations(BomEraser.class) {{
                new BomEraser().erase((File)any, (File)any);
                result = new IllegalArgumentException();
            }};
            
            String[] args = {"abc", "def"};
            BomEraser.main(args);
        }
    }
    
    /**
     * BomEraserのdispHelpメソッド用テストクラス
     * @author tanabe
     *
     */
    public static class DispHelpTest {
        @Test
        public void 例外が発生しない() {
            BomEraser.dispHelp();
        }
    }
    
    /**
     * BomEraserのEraseメソッド用テストクラス
     * @author tanabe
     *
     */
    public static class EraseTest {
        
        /**
         * テスト対象
         */
        private BomEraser target;
        
        @Before
        public void before() {
            target = new BomEraser();
        }
        
        @After
        public void after() {
            File file = BomEraserTest.getNotExistingFile();
            if(file.exists()) {
                // 出力されたファイルを削除
                file.delete();
            }
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void 入力ファイルがNullだと例外発生() throws IOException {
            this.target.erase(null,
                              BomEraserTest.getExistingFileWithBom());
        }

        @Test(expected = IllegalArgumentException.class)
        public void 出力ファイルがNullだと例外発生() throws IOException {
            this.target.erase(BomEraserTest.getExistingFileWithBom(),
                              null);
        }
        
        @Test
        public void 入力ファイルが存在しないファイルだと戻り値false() throws IOException {
            assertFalse(
                    this.target.erase(BomEraserTest.getNotExistingFile(),
                                      BomEraserTest.getNotExistingFile()));
        }
        
        @Test
        public void 出力ファイルが存在するファイルだと戻り値false() throws IOException {
            assertFalse(this.target.erase(BomEraserTest.getExistingFileWithBom(),
                                          BomEraserTest.getExistingFileWithBom()));
        }
        
        @Test
        public void 入力が存在するファイルで出力が存在しないファイルだとtrue() throws IOException {
            assertTrue(
                    this.target.erase(BomEraserTest.getExistingFileWithBom(),
                                      BomEraserTest.getNotExistingFile()));
        }
        
    }

    /**
     * BomEraserのcreateSkipBOMStreamメソッド用テストクラス
     * @author tanabe
     *
     */
    public static class createSkipBOMStreamTest {
        /**
         * テスト対象
         */
        private BomEraser target;

        @Before
        public void before() {
            this.target = new BomEraser();
        }

        @Test(expected = IllegalArgumentException.class)
        public void 引数がNullだと例外発生() throws IOException {
            this.target.createSkipBOMStream(null);
        }

        @Test
        public void 存在しないファイルを渡すと戻り値null() throws IOException {
            assertNull(
                    this.target.createSkipBOMStream(
                            BomEraserTest.getNotExistingFile()));
        }
        
        @Test
        public void BOMなしファイルを渡すと戻り値非null() throws IOException {
            assertNotNull(
                    this.target.createSkipBOMStream(
                            BomEraserTest.getExistingFileWithoutBom()));
        }
    }

    /**
     * BomEraserのisBomFileメソッド用テストクラス
     * @author tanabe
     *
     */
    public static class isBomFileTest {
        /** テスト対象 */
        private BomEraser target;

        @Before
        public void before() {
            this.target = new BomEraser();
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void 引数がNullだと例外発生() throws IOException {
            this.target.isBomFile(null);
        }
        
        @Test
        public void isBomメソッドがtrueを返す時戻り値true() throws IOException {
            new Expectations(BomEraser.class) {{
                new BomEraser().isBom((byte[])any); result = true;
            }};
            BomEraser mock = new BomEraser();
            
            assertTrue(
                    mock.isBomFile(
                            BomEraserTest.getExistingFileWithBom()));
        }
        
        @Test
        public void isBomメソッドがfalseを返す時戻り値false() throws IOException {
            new Expectations(BomEraser.class) {{
                new BomEraser().isBom((byte[])any); result = false;
            }};
            BomEraser mock = new BomEraser();
            
            assertFalse(
                    mock.isBomFile(
                            BomEraserTest.getExistingFileWithBom()));
        }
        
        @Test(expected = IOException.class)
        public void 存在しないファイルを渡すと例外発生() throws IOException {
            this.target.isBomFile(
                    BomEraserTest.getNotExistingFile());
        }
    }
    
    /**
     * BomEraserのisBomメソッド用テストクラス
     * @author tanabe
     *
     */
    public static class IsBomTest {
        
        /**
         * テスト対象
         */
        private BomEraser target;
        
        @Before
        public void before() {
            this.target = new BomEraser();
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void 引数がNullだと例外発生() {
            this.target.isBom(null);
        }
        
        @Test
        public void 引数の要素数が0だと戻り値false() {
            byte[] input = {};
            assertFalse(this.target.isBom(input));
        }
        
        @Test
        public void 引数がBOM以外_0xEF_0xBB_0xBB_から始まる配列だと戻り値false() {
            byte[] input = {(byte)0xEF, (byte)0xBB, (byte)0xBB, (byte)0x36, (byte)0x37};
            assertFalse(this.target.isBom(input));
        }

        @Test
        public void 引数がBOM以外_0xEF_0xBF_0xBF_から始まる配列だと戻り値false() {
            byte[] input = {(byte)0xEF, (byte)0xBF, (byte)0xBF, (byte)0x36, (byte)0x37};
            assertFalse(this.target.isBom(input));
        }
        
        @Test
        public void 引数がBOM以外_0xBF_0xBB_0xBF_から始まる配列だと戻り値false() {
            byte[] input = {(byte)0xBF, (byte)0xBB, (byte)0xBF, (byte)0x36, (byte)0x37};
            assertFalse(this.target.isBom(input));
        }
        
        @Test
        public void 引数がBOMから始まる配列だと戻り値true() {
            byte[] input = {(byte)0xEF, (byte)0xBB, (byte)0xBF, (byte)0x33, (byte)0x34};
            assertTrue(this.target.isBom(input));
        }
    }
}
