package com.aliyun.tauris.plugins.filter;

import com.aliyun.tauris.TEvent;
import com.aliyun.tauris.TFormatter;
import com.aliyun.tauris.annotations.Name;
import com.aliyun.tauris.annotations.Required;

/**
 * @author Ray Chaung<rockis@gmail.com>
 */
@Name("strformat")
public class StrFormatFilter extends BaseTFilter {

    @Required
    String target;

    @Required
    TFormatter format;

    protected boolean doFilter(TEvent e) {
        if (e == null) {
            return false;
        }
        e.set(target, format.format(e));
        return true;
    }
}
