package com.gitee.pristine.sample.encrypt;

import com.gitee.pristine.autoconfigure.constant.ShieldAlgorithm;
import com.gitee.pristine.autoconfigure.desensitiser.ConfigParam;
import com.gitee.pristine.autoconfigure.desensitiser.PropertyDesensitiser;
import com.gitee.pristine.autoconfigure.desensitiser.impl.AesPropertyDesensitiser;
import com.gitee.pristine.autoconfigure.factory.DesensitiserFactory;

/**
 * 加密数据
 * @author Pristine Xu
 * @date 2022/3/27 9:27
 * @description:
 */
public class EncryptData {

    /**
     * 生成所需要的加密数据，放入配置文件中
     * @param args 参数
     */
    public static void main(String[] args) {
        // 创建脱敏器，可以根据不同的算法生成
        PropertyDesensitiser desensitiser = DesensitiserFactory.delegate(ShieldAlgorithm.AES);

        // 设置初始化参数（必须进行初始化）
        String secret = "ABCDEFGH12345678";
        String charset = "UTF-8";
        ConfigParam configParam = new ConfigParam(secret, charset);
        desensitiser.setConfigParam(configParam);

        // 检测密钥是否合法（可选）
        desensitiser.checkSecret(secret);

        // 加密数据
        System.out.println(desensitiser.encode("demo1 param value"));
        System.out.println(desensitiser.encode("demo2 param value"));
        System.out.println(desensitiser.encode("demo3 param value"));
    }

}
