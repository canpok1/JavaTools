package core;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

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
    }

    /**
     * BomEraserのisBomFileメソッド用テストクラス
     * @author tanabe
     *
     */
    public static class isBomFileTest {
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
            this.target.isBomFile(null);
        }
        
        @Test
        public void BOM付ファイルを渡すと戻り値true() throws IOException {
            assertTrue(
                    this.target.isBomFile(
                            BomEraserTest.getExistingFileWithBom()));
        }
        
        @Test
        public void BOMなしファイルを渡すと戻り値false() throws IOException {
            assertFalse(
                    this.target.isBomFile(
                            BomEraserTest.getExistingFileWithoutBom()));
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
