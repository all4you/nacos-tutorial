package com.lememo.nacosconfigadapter;

import java.util.Arrays;

/**
 * @author houyi
 * Nacos 配置项的类型
 **/
public enum NacosConfigType {

    /**
     * int
     */
    INT("int"),
    /**
     * double
     */
    DOUBLE("double"),
    /**
     * float
     */
    FLOAT("float"),
    /**
     * long
     */
    LONG("long"),
    /**
     * boolean
     */
    BOOLEAN("boolean"),
    /**
     * string
     */
    STRING("string"),
    ;

    private String type;

    NacosConfigType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * 获取枚举
     *
     * @param type 类型
     * @return 枚举
     */
    public static NacosConfigType getEnum(String type) {
        return Arrays.stream(values())
                .filter(t -> t.getType().equals(type))
                .findFirst()
                .orElse(null);
    }

}
