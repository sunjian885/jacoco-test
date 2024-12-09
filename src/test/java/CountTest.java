package com.xiaoju.framework;

import org.junit.Assert;
import org.junit.Test;

public class CountTest {
    @Test
    public void test(){
        Count count = new Count();
        System.out.println(".........="+count.add(2,3));
        Assert.assertEquals(count.add(2,3),5);
    }

}
