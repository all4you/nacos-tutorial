package com.lememo.nacosconfigadapter;

import java.util.List;

/**
 * @author houyi
 * Nacos 每个配置项的要素
 **/
public class NacosConfigFactor {

    /**
     * 默认等级：0
     * 比较低的一个等级
     */
    public static final int DEFAULT_GRADE = 0;

    /**
     * 配置项的key
     */
    private String key;

    /**
     * 配置项的描述
     */
    private String desc;

    /**
     * 配置项的值类型
     */
    private NacosConfigType type;

    /**
     * 是否只读
     * 为true的话不允许修改该配置项
     */
    private boolean readOnly;

    /**
     * 危险等级
     * 该字段主要用来标识每个配置项的危险等级
     * 只允许等级高于或等于该配置项等级的请求
     * 才能对该配置项进行查询或修改
     */
    private int grade = DEFAULT_GRADE;

    /**
     * 配置项的组件类型
     * 主要是给前端页面使用
     * 例如：下拉框，单选框等等
     */
    private String compType;

    /**
     * 可能的值范围
     */
    private List<Object> possibleValues;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public NacosConfigType getType() {
        return type;
    }

    public void setType(NacosConfigType type) {
        this.type = type;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getCompType() {
        return compType;
    }

    public void setCompType(String compType) {
        this.compType = compType;
    }

    public List<Object> getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(List<Object> possibleValues) {
        this.possibleValues = possibleValues;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
                .append("\"type\":")
                .append(type)
                .append(",\"readOnly\":")
                .append(readOnly)
                .append(",\"grade\":")
                .append(grade)
                .append("}");
        return sb.toString();
    }

}

