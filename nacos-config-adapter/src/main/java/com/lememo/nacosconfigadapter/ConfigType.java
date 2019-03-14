package com.lememo.nacosconfigadapter;

import java.util.Arrays;

/**
 * @author houyi
 * Nacos 配置项的值类型
 **/
public enum ConfigType {

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
    STRING("string"),;

    private String type;

    ConfigType(String type) {
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
    public static ConfigType getEnum(String type) {
        return Arrays.stream(values())
                .filter(t -> t.getType().equals(type))
                .findFirst()
                .orElse(null);
    }

}
