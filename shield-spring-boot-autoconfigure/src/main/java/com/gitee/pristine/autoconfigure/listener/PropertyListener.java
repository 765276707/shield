package com.gitee.pristine.autoconfigure.listener;

/**
 * 属性脱敏监听器
 * @author Pristine Xu
 * @param <E>
 */
public interface PropertyListener<E extends PropertyEvent> {

    /**
     * 每次读取一个配置属性值时候触发
     * @param event 事件
     */
    void onEachPropertyReadEvent(E event);

    /**
     * 读取完全部配置属性值时触发，只触发一次
     */
    void afterAllPropertyReadEvent();

}
