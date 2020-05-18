package com.aliyun.tauris.plugins.filter.mutate;

import com.aliyun.tauris.DefaultEvent;
import com.aliyun.tauris.TEvent;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * @author Ray Chaung<rockis@gmail.com>
 */
public class MutateFilterTest {


    @Test
    public void testCopy() throws Exception  {

        CopyField rf = new CopyField();
        rf.source = "f1";
        rf.target = "nf1";
        rf.fields = new String[]{"f2:nf2", "f3:nf3"};
        rf.init();;

        TEvent e = new DefaultEvent("");
        e.set("f1", "v1");
        e.set("f2", "v2");
        e.set("f3", "v3");

        rf.mutate(e);

        Assert.assertEquals("v1", e.get("nf1"));
        Assert.assertEquals("v2", e.get("nf2"));
        Assert.assertEquals("v3", e.get("nf3"));

        Assert.assertNotNull(e.get("f1"));
        Assert.assertNotNull(e.get("f2"));
        Assert.assertNotNull(e.get("f3"));
    }

    @Test
    public void testRename() throws Exception  {

        CopyField rf = new CopyField();
        rf.source = "f1";
        rf.target = "nf1";
        rf.fields = new String[]{"f2:nf2", "f3:nf3"};
        rf.rename = true;
        rf.init();;

        TEvent e = new DefaultEvent("");
        e.set("f1", "v1");
        e.set("f2", "v2");
        e.set("f3", "v3");

        rf.mutate(e);

        Assert.assertEquals("v1", e.get("nf1"));
        Assert.assertEquals("v2", e.get("nf2"));
        Assert.assertEquals("v3", e.get("nf3"));

        Assert.assertNull(e.get("f1"));
        Assert.assertNull(e.get("f2"));
        Assert.assertNull(e.get("f3"));
    }

    @Test
    public void testConvert() {

        LongConverter rf = new LongConverter();
        Object v = rf.convert("123").get();
        Assert.assertEquals(123l, v);

        Optional<Object> opt = rf.convert("notlong");
        Assert.assertFalse(opt.isPresent());
    }
}
