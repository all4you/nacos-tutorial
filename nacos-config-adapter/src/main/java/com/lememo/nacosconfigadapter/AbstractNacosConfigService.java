package com.lememo.nacosconfigadapter;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * @author houyi
 **/
public abstract class AbstractNacosConfigService implements NacosConfigService {

    /**
     * key和unit的映射关系
     */
    private Map<String, ConfigUnit> unitMap;

    /**
     * key和配置对象的映射关系
     */
    private Map<String, NacosConfig> configMap;

    /**
     * Nacos 配置项的 dataId
     */
    private String dataId;

    /**
     * Nacos 配置项的 group
     */
    private String group;

    /**
     * Nacos 配置服务 api
     */
    private ConfigService configService;

    public AbstractNacosConfigService(String serverAddress, String dataId, String group) {
        this.unitMap = new ConcurrentHashMap<>();
        this.configMap = new ConcurrentHashMap<>();
        // 刷新 unit
        this.refreshUnit();
        this.dataId = dataId;
        this.group = group;
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddress);
        try {
            this.configService = NacosFactory.createConfigService(properties);
            // 增加监听器
            Listener listener = new ConfigListener();
            this.configService.addListener(dataId, group, listener);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 ConfigUnit 的对象列表
     *
     * @return 对象列表
     */
    public abstract List<ConfigUnit> getConfigUnitList();

    /**
     * 接收到最新的配置信息时，是否对 ConfigUnit 进行刷新
     *
     * @return true：刷新 false：不刷新
     */
    public abstract boolean refreshUnitOnReceiveConfig();

    @Override
    public NacosConfig getConfig(String key) {
        return configMap.get(key);
    }

    @Override
    public Object getConfigValue(String key) {
        NacosConfig config = getConfig(key);
        return config != null ? config.getValue() : null;
    }

    @Override
    public List<NacosConfig> getConfigList(int grade) {
        if (configMap.isEmpty()) {
            return new ArrayList<>();
        }
        List<NacosConfig> configList = new ArrayList<>(configMap.size());
        for (Map.Entry<String, NacosConfig> entry : configMap.entrySet()) {
            NacosConfig config = entry.getValue();
            // 如果当前config的等级小于或等于指定的等级
            if (!config.higherGrade(grade)) {
                configList.add(config);
            }
        }
        // 根据 grade 进行升序排序
        configList.sort(new Comparator<NacosConfig>() {
            @Override
            public int compare(NacosConfig o1, NacosConfig o2) {
                return o1.getUnit().getGrade() - o2.getUnit().getGrade();
            }
        });
        return configList;
    }

    @Override
    public boolean updateConfig(String key, Object value, int grade) {
        if (key == null || key.trim().length() == 0) {
            throw new IllegalArgumentException("key can not be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value can not be null");
        }
        // 获取配置项
        NacosConfig config = getConfig(key);
        if (config == null) {
            throw new IllegalArgumentException("config does not exists with key=" + key);
        }
        // 如果配置项的 unit 不合法
        if (!config.validUnit()) {
            throw new IllegalArgumentException("config unit invalid with key=" + key);
        }
        // 如果当前配置项的等级更高，则不允许修改
        if (config.higherGrade(grade)) {
            throw new IllegalArgumentException("you can't update config with key=" + key + ", cause this config has a higher grade");
        }
        // 如果当前配置项的等级更高，则不允许修改
        if (config.readOnly()) {
            throw new IllegalArgumentException("you can't update config with key=" + key + ", cause this config is read only");
        }

        // 更新配置项前的一些操作，例如：
        // 1.记录更新操作的日志，更新者，更新时间等等，方便回溯
        // 2.甚至可以把操作前的内容记录下来，方便一键回滚到上一次的值

        // 更新配置项的值
        config.setValue(value);
        // 检查当前配置项的值类型是否合法
        if (!config.validValueType()) {
            throw new IllegalArgumentException("value type invalid with key=" + key + ", should be:" + config.getUnit().getType());
        }
        configMap.put(key, config);

        // 将更新后的配置内容推送到 nacos 服务端
        return publishConfig();
    }

    /**
     * 刷新key 和 unit 的对应关系
     */
    private void refreshUnit() {
        List<ConfigUnit> factorList = getConfigUnitList();
        if (factorList == null || factorList.isEmpty()) {
            return;
        }
        unitMap.clear();
        for (ConfigUnit unit : factorList) {
            unitMap.put(unit.getKey(), unit);
        }
    }

    /**
     * 重新处理配置信息
     * 将配置信息加载到 keyConfig 中去
     *
     * @param content 配置信息
     */
    private void reloadConfigs(String content) {
        Properties properties = new Properties();
        try {
            properties.load(new ByteArrayInputStream(content.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 清空内存中原来的值
        configMap.clear();
        // 遍历 properties
        for (Object k : properties.keySet()) {
            String key = k.toString();
            // 获取该 key 对应的 unit
            ConfigUnit unit = unitMap.get(key);
            if (unit == null) {
                continue;
            }
            // 将配置项的值封装成 config 对象
            String val = properties.getProperty(key);
            Object value = parseValue(val, unit.getType());
            NacosConfig config = new NacosConfig(key, value, unit);
            configMap.put(key, config);
        }
    }

    private Object parseValue(String val, ConfigType type) {
        Object value;
        try {
            switch (type) {
                case INT: {
                    value = Integer.parseInt(val);
                }
                break;
                case DOUBLE: {
                    value = Double.parseDouble(val);
                }
                break;
                case FLOAT: {
                    value = Float.parseFloat(val);
                }
                break;
                case LONG: {
                    value = Long.parseLong(val);
                }
                break;
                case BOOLEAN: {
                    value = Boolean.parseBoolean(val);
                }
                break;
                case STRING: {
                    value = val;
                }
                break;
                default: {
                    value = val;
                }
                break;
            }
        } catch (Exception e) {
            value = val;
        }
        return value;
    }


    private boolean publishConfig() {
        StringBuilder sb = new StringBuilder();
        int size = configMap.size();
        int index = 1;
        for (Map.Entry<String, NacosConfig> entry : configMap.entrySet()) {
            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue().getValue());
            if (index++ < size) {
                sb.append("\n");
            }
        }
        String newContent = sb.toString();
        try {
            configService.publishConfig(dataId, group, newContent);
        } catch (NacosException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private class ConfigListener implements Listener {

        @Override
        public Executor getExecutor() {
            return null;
        }

        @Override
        public void receiveConfigInfo(String content) {
            // 如果需要每次对 unit 进行刷新
            if (refreshUnitOnReceiveConfig()) {
                refreshUnit();
            }
            reloadConfigs(content);
        }
    }


}
