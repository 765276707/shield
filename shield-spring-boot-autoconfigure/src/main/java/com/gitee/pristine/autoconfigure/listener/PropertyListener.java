package com.gitee.pristine.autoconfigure.listener;

/**
 * 属性脱敏监听器
 * @author xzb
 * @param <E>
 */
public interface PropertyListener<E extends PropertyEvent> {

    void onEvent(E event);

    void ofEvent();

}
