package core;

import static org.junit.Assert.*;

import java.io.File;

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
        public void before()
        {
            this.target = new BomEraser();
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void Nullを渡すと例外発生() {
            this.target.erase(null);
        }
        
        @Test
        public void 存在しないファイルを渡すと戻り値null() {
            File file = new File("");
            assertNull(this.target.erase(file));
        }
        
        @Test
        public void 存在するファイルを渡すと戻り値は非null() {
            File file = new File("src/test/resources/FileWithBom.txt");
            assertNotNull(this.target.erase(file));
        }
    }
}
