package com.gitee.pristine.autoconfigure.listener;

/**
 * 属性事件
 * @author Pristine Xu
 */
public class PropertyEvent {

    private Boolean isEncode = false;
    private final String propertyName;
    private final Object propertyValue;
    private String encodePropertyValue;

    public PropertyEvent(String propertyName, Object propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public PropertyEvent(String propertyName, Object propertyValue, String encodePropertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
        this.encodePropertyValue = encodePropertyValue;
    }

    public PropertyEvent(Boolean isEncode, String propertyName, Object propertyValue, String encodePropertyValue) {
        this.isEncode = isEncode;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
        this.encodePropertyValue = encodePropertyValue;
    }

    public Boolean getEncode() {
        return isEncode;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getPropertyValue() {
        return propertyValue;
    }

    public String getEncodePropertyValue() {
        return encodePropertyValue;
    }

    @Override
    public String toString() {
        return "PropertyEvent{" +
                "isEncode=" + isEncode +
                ", propertyName='" + propertyName + '\'' +
                ", propertyValue=" + propertyValue +
                ", encodePropertyValue='" + encodePropertyValue + '\'' +
                '}';
    }
}
