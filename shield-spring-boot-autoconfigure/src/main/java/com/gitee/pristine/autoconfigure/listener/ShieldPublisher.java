package com.gitee.pristine.autoconfigure.listener;

import java.util.List;
import java.util.Vector;

public class ShieldPublisher {

    // 存储容器
    private final List<PropertyListener<PropertyEvent>> container = new Vector<>(2);

    // 注册监听器
    public void addListener(PropertyListener<PropertyEvent> listener) {
        container.add(listener);
    }

    // 移除监听器
    public void removeListener(PropertyListener<PropertyEvent> listener) {
        container.remove(listener);
    }


    // 通知监听器，触发事件
    public void notifyOnEvent(PropertyEvent event) {
        for (PropertyListener<PropertyEvent> listener : container) {
            listener.onEvent(event);
        }
    }

    // 通知监听器，停止触发
    public void notifyOfEvent() {
        for (PropertyListener<PropertyEvent> listener : container) {
            listener.ofEvent();
        }
    }

}
