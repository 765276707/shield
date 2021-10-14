package com.gitee.pristine.autoconfigure.listener.impl;

import com.gitee.pristine.autoconfigure.listener.PropertyEvent;
import com.gitee.pristine.autoconfigure.listener.PropertyListener;
import com.gitee.pristine.autoconfigure.util.PrintUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 脱敏详情监听器
 * @author xzb
 */
public class DetailsPropertyListener implements PropertyListener<PropertyEvent> {

    private final static Logger log = LoggerFactory.getLogger(DetailsPropertyListener.class);
    private List<String> details = new ArrayList<>();
    private int line = 1;

    @Override
    public void onEvent(PropertyEvent event) {
        if (event.getEncode()) {
            String message = String.format("%d. 【属性名】 %s，【解密前】 %s, 【解密后】 %s",
                    line, event.getPropertyName(), event.getPropertyValue(), event.getEncodePropertyValue());
            details.add(message);
            line = line + 1;
        }
    }

    @Override
    public void ofEvent() {
        if (details.size() > 0) {
            log.info("[shield] 共有 {} 条配置进行脱敏脱敏，详细信息如下: ", line);
            PrintUtil.printDetails(details);
        }
    }

}
