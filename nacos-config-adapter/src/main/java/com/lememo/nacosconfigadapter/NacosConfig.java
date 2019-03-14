package com.lememo.nacosconfigadapter;

/**
 * @author houyi
 * Nacos 配置信息包装器
 **/
public class NacosConfig {

    /**
     * 配置项的key
     */
    private String key;

    /**
     * 配置项的value
     */
    private Object value;

    /**
     * 配置项的一些要素
     */
    private NacosConfigFactor factor;

    public NacosConfig(String key, Object value, NacosConfigFactor factor) {
        this.key = key;
        this.value = value;
        this.factor = factor;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public NacosConfigFactor getFactor() {
        return factor;
    }

    public void setFactor(NacosConfigFactor factor) {
        this.factor = factor;
    }

    /**
     * 该配置项是否合法
     *
     * @return true：合法 false：不合法
     */
    public boolean validFactor() {
        return factor != null;
    }

    public boolean validValueType(){
        return validFactor() && checkValueType();
    }

    /**
     * 检测值类型
     * @return true：合法的值类型 false：不合法的值类型
     */
    private boolean checkValueType(){
        boolean valid = true;
        String val = value.toString();
        try {
            switch (factor.getType()) {
                case INT: {
                    Integer.parseInt(val);
                }
                break;
                case DOUBLE: {
                    Double.parseDouble(val);
                }
                break;
                case FLOAT: {
                    Float.parseFloat(val);
                }
                break;
                case LONG: {
                    Long.parseLong(val);
                }
                break;
                case BOOLEAN: {
                    valid = Boolean.parseBoolean(val);
                }
                break;
                case STRING: {
                }
                break;
                default: {
                }
                break;
            }
        }catch (Exception e){
            valid = false;
        }
        return valid;
    }

    /**
     * 当前配置的等级是否比指定的等级更高
     *
     * @param grade 指定的等级
     * @return true：比指定等级高 false：比指定等级低
     */
    public boolean higherGrade(int grade) {
        return validFactor() && factor.getGrade() > grade;
    }

    /**
     * 当前配置项是否只读，只读则不允许修改
     *
     * @return true：只读 false：可写
     */
    public boolean readOnly() {
        return validFactor() && factor.isReadOnly();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
                .append("\"key\":")
                .append(key)
                .append(",\"value\":")
                .append(value)
                .append(",\"factor\":")
                .append(factor)
                .append("}");
        return sb.toString();
    }
}
