package com.gitee.pristine.autoconfigure.listener.impl;

import com.gitee.pristine.autoconfigure.listener.PropertyEvent;
import com.gitee.pristine.autoconfigure.listener.PropertyListener;
import com.gitee.pristine.autoconfigure.util.PrintUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 风险提示监听器
 * @author xzb
 */
public class RiskingPropertyListener implements PropertyListener<PropertyEvent> {

    private final String keywords;
    private final static Logger log = LoggerFactory.getLogger(RiskingPropertyListener.class);
    private List<String> riskMessages = new ArrayList<>();
    private int line = 0;

    public RiskingPropertyListener(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public void onEvent(PropertyEvent event) {
        String propertyName = event.getPropertyName();
        boolean isContain = contain(propertyName, keywords);
        boolean isEncode = event.getEncode();
        if (isContain && !isEncode) {
            line = line + 1 ;
            // 添加风险信息
            riskMessages.add(String.format("%d. 【风险!!!】属性名为[ %s ]的值未脱敏", line, propertyName));
        }
    }

    @Override
    public void ofEvent() {
        if (riskMessages.size() > 0) {
            log.warn("[shield] 风险提示，以下 {} 条配置可能需要进行脱敏配置: ", line);
            PrintUtil.printDetails(riskMessages);
        }
    }


    private boolean contain(String resource, String sequence) {
        if (sequence==null || "".equals(sequence)) {
            return false;
        }
        String[] split = sequence.split(",");
        boolean result = false;
        for (String s : split) {
            result = resource.contains(s);
            if (result) {
                break;
            }
        }
        return result;
    }
}
