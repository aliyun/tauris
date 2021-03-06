package com.aliyun.tauris.utils.converters;

import com.aliyun.tauris.utils.TConverter;
import org.apache.commons.beanutils.ConversionException;

import java.util.regex.Pattern;

/**
 * Created by ZhangLei on 2018/5/11.
 */
public class PatternConverter extends TConverter {

    @Override
    public Class<?> getType() {
        return Pattern.class;
    }

    @Override
    public <T> T convert(Class<T> type, Object value) {
        if (!(value instanceof String)) {
            throw new ConversionException(String.format("cannot convert %s to %s", value.getClass(), getType()));
        }
        try {
            return (T) Pattern.compile((String) value);
        } catch (Exception e) {
            throw new ConversionException(e.getMessage());
        }
    }

}
