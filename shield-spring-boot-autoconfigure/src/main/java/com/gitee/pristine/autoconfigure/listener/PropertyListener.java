package com.gitee.pristine.autoconfigure.listener;

public interface PropertyListener<E extends PropertyEvent> {

    void onEvent(E event);

    void ofEvent();

}
