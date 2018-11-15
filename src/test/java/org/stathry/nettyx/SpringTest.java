package org.stathry.nettyx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * TODO
 * Created by dongdaiming on 2018-06-05 14:20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class SpringTest {

    @Test
    public void testStart() {
        System.out.println("spring started.");
    }

}
