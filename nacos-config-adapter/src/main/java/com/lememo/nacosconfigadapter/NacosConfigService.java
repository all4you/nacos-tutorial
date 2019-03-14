package com.lememo.nacosconfigadapter;

import java.util.List;

/**
 * @author houyi
 **/
public interface NacosConfigService {

    /**
     * 根据配置项的key获取配置项
     * @param key 配置项的key
     * @return 配置项
     */
    NacosConfig getConfig(String key);

    /**
     * 根据配置项的key获取配置项的值
     * @param key 配置项的key
     * @return 配置项的值
     */
    Object getConfigValue(String key);

    /**
     * 获取(小于等于)指定等级下的所有配置项
     * @param grade 配置项的等级
     * @return 配置项的列表
     */
    List<NacosConfig> getConfigList(int grade);

    /**
     * 更新(小于等于)指定等级的配置项的值
     * @param key 配置项的key
     * @param value 配置项的值
     * @param grade 配置项的危险等级
     * @return 更新是否成功
     */
    boolean updateConfig(String key, Object value, int grade);

}
