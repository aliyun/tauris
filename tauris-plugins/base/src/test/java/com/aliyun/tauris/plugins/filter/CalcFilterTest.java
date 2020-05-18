package com.aliyun.tauris.plugins.filter;

import com.aliyun.tauris.DefaultEvent;
import com.aliyun.tauris.TEvent;
import io.tauris.expression.TExpression;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Ray Chaung<rockis@gmail.com>
 */
public class CalcFilterTest {


    @Test
    public void test() {

        CalcFilter cf = new CalcFilter();
        cf.expression = TExpression.compile("123+456");
        cf.target = "tgt";
        cf.valueType = CalcFilter.NumberType.DOUBLE;
//        cf.init();

        TEvent e = new DefaultEvent("");
        cf.filter(e);
        Assert.assertEquals(579.0, e.get("tgt"));


        cf = new CalcFilter();
        cf.expression = TExpression.compile("$val+456");
        cf.target = "tgt";
        cf.valueType = CalcFilter.NumberType.DOUBLE;
//        cf.init();
        e = new DefaultEvent("");
        e.set("val", 999);
        cf.filter(e);
        Assert.assertEquals(456+999.0, e.get("tgt"));
    }
}
