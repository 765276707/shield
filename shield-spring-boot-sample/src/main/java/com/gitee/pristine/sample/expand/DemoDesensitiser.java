package com.gitee.pristine.sample.expand;

import com.gitee.pristine.autoconfigure.desensitiser.AbstractPropertyDesensitiser;

/**
 * 自定义脱敏器
 * @author xzb
 */
public class DemoDesensitiser extends AbstractPropertyDesensitiser {

    @Override
    public String encode(String value) {
        // 此处进行属性加密......
        return null;
    }

    @Override
    public String decode(String value) {
        // 此处进行属性解密......
        return null;
    }

}
