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
            File file = this.getNotExistingFile();
            if(file.exists()) {
                // 出力されたファイルを削除
                file.delete();
            }
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void 入力ファイルがNullだと例外発生() throws IOException {
            this.target.erase(null,
                              this.getExistingFile());
        }

        @Test(expected = IllegalArgumentException.class)
        public void 出力ファイルがNullだと例外発生() throws IOException {
            this.target.erase(this.getExistingFile(),
                              null);
        }
        
        @Test
        public void 入力ファイルが存在しないファイルだと戻り値false() throws IOException {
            assertFalse(
                    this.target.erase(this.getNotExistingFile(),
                                      this.getNotExistingFile()));
        }
        
        @Test
        public void 出力ファイルが存在するファイルだと戻り値false() throws IOException {
            assertFalse(this.target.erase(this.getExistingFile(),
                                          this.getExistingFile()));
        }
        
        @Test
        public void 入力が存在するファイルで出力が存在しないファイルだとtrue() throws IOException {
            assertTrue(
                    this.target.erase(this.getExistingFile(),
                                      this.getNotExistingFile()));
        }
        
        
        /**
         * 存在するファイルを取得します。
         * @return 存在するファイル
         */
        private File getExistingFile() {
            String path = "src/test/resources/FileWithBom.txt";
            return new File(path);
        }
        
        /**
         * 存在しないファイルを取得します。
         * @return 存在しないファイル
         */
        private File getNotExistingFile() {
            String path = "src/test/resources/FileWithoutBom.txt";
            return new File(path);
        }
    }

    /**
     * BomEraserのisBmメソッド用テストクラス
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
        public void 引数がBOM以外から始まる配列だと戻り値false() {
            byte[] input = {(byte)0x33, (byte)0x34, (byte)0x35, (byte)0x36, (byte)0x37};
            assertFalse(this.target.isBom(input));
        }
        
        @Test
        public void 引数がBOMから始まる配列だと戻り値true() {
            byte[] input = {(byte)0xEF, (byte)0xBB, (byte)0xBF, (byte)0x33, (byte)0x34};
            assertTrue(this.target.isBom(input));
        }
    }
}
