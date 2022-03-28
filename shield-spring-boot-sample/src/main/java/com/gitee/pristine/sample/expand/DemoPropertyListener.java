package com.gitee.pristine.sample.expand;

import com.gitee.pristine.autoconfigure.listener.PropertyEvent;
import com.gitee.pristine.autoconfigure.listener.PropertyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义属性解析监听器
 * @author Pristine Xu
 */
public class DemoPropertyListener implements PropertyListener<PropertyEvent> {

    private final Logger log = LoggerFactory.getLogger(DemoPropertyListener.class);

    @Override
    public void onEachPropertyReadEvent(PropertyEvent event) {
        // 每次读取一个配置属性值时候触发
        log.info("配置读取：{}", event.toString());
    }

    @Override
    public void afterAllPropertyReadEvent() {
        // 读取完全部配置属性值时触发，只触发一次
        log.info("全部配置已读取完毕......");
    }

}
