package com.gitee.pristine.autoconfigure.exception.extd;

import com.gitee.pristine.autoconfigure.exception.ShieldException;

/**
 * 配置解析异常
 * @author Pristine Xu
 * @date 2022/3/20 15:50
 * @description: 无法正常的解析配置的值，可能的原因是值无效或不存在
 */
public class ConfigValueParseException extends ShieldException {

    public ConfigValueParseException(String message) {
        super(message);
    }

    public ConfigValueParseException(String message, Object args) {
        super(message, args);
    }

}
