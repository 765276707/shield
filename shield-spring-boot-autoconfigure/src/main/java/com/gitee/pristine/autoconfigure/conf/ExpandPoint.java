package com.gitee.pristine.autoconfigure.conf;

import com.gitee.pristine.autoconfigure.converter.PropertyConverter;
import com.gitee.pristine.autoconfigure.desensitiser.PropertyDesensitiser;
import com.gitee.pristine.autoconfigure.listener.PropertyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 拓展端点
 * @author Pristine Xu
 */
public class ExpandPoint {

    /**
     * 自定义的脱敏器
     */
    private PropertyDesensitiser customizeDesensitiser = null;
    /**
     * 自定义的属性监听器
     */
    private final List<PropertyListener> additionalListeners = new ArrayList<>(3);
    /**
     * 自定义的属性转换器
     */
    private final List<PropertyConverter> additionalConverters = new ArrayList<>(3);
    /**
     * 风险提示关键词，默认为空
     */
    private String riskingKeywords = "";

    public ExpandPoint() {}

    public static ExpandPoint createEmpty() {
        return new ExpandPoint();
    }

    public ExpandPoint registerDesensitiser(PropertyDesensitiser customizeDesensitiser) {
        this.customizeDesensitiser = customizeDesensitiser;
        return this;
    }

    public ExpandPoint setRiskingKeywords(String riskingKeywords) {
        this.riskingKeywords = riskingKeywords;
        return this;
    }

    public ExpandPoint addListener(PropertyListener listener) {
        this.additionalListeners.add(listener);
        return this;
    }

    public ExpandPoint addConverter(PropertyConverter converter) {
        this.additionalConverters.add(converter);
        return this;
    }

    public PropertyDesensitiser getCustomizeDesensitiser() {
        return customizeDesensitiser;
    }

    public List<PropertyListener> getAdditionalListeners() {
        return additionalListeners;
    }

    public List<PropertyConverter> getAdditionalConverters() {
        return additionalConverters;
    }

    public String getRiskingKeywords() {
        return riskingKeywords;
    }
}
