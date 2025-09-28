package flik;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {
@Test
    public void TestEaqual(){
    int changed=0;
    assertTrue(changed == 0);
    int i = 0;
    for (int j = 0; i < 500; ++i, ++j) {
        boolean k=Flik.isSameNumber(i, j);
        assertTrue(k);
    }

}

}
