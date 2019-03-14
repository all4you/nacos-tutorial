package com.lememo.nacosconfigadapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author houyi
 * 假设有以下几个配置项
 * jdbc.url=some-url
 * jdbc.password=fake-password
 * some.api.qps=1000
 * some.method.switch=true
 * time.diff=3600000
 * 如果直接在 nacos 中管理这些配置，用户会非常头疼：
 * <ul>
 * <li>1.对于运营来说，诸如 jdbc.url 这种系统相关的配置项，他们不需要关心，也不应该由他们来管理这些配置项，直接暴露给他们的话，会是一个很危险的事情</li>
 * <li>2.有些配置项无法仅从 key 的命名就判断出该配置项的意思，因为他们并不能做到那么好的自解释，例如上述配置中的 time.diff 就很具有疑惑性</li>
 * <li>3.配置的修改应该做到最小化，如果将所有的配置项全部暴露，用户在修改时手误将其中某一个配置项删除了，将会带来灾难性的后果</li>
 * <li>4.不同的人根据自己的权限，只需要负责自己所能修改的配置项即可，不应该将所有配置项全部暴露</li>
 * </ul>
 **/
public class ExampleNacosConfigService extends AbstractNacosConfigService {

    public ExampleNacosConfigService(String serverAddress, String dataId, String group) {
        super(serverAddress, dataId, group);
    }

    @Override
    public List<ConfigUnit> getConfigUnitList() {
        List<ConfigUnit> factorList = new ArrayList<>();
        // 你可以从数据库中将 unit 读取出来
        // 这里只供演示，所以写死几个值
        ConfigUnit factor1 = new ConfigUnit();
        // jdbc.url
        factor1.setKey("jdbc.url");
        factor1.setDesc("jdbc的链接");
        factor1.setType(ConfigType.STRING);
        // 不允许通过前端页面修改该配置
        factor1.setReadOnly(true);
        // 该配置的等级较高
        factor1.setGrade(5);

        ConfigUnit factor2 = new ConfigUnit();
        // jdbc.password
        factor2.setKey("jdbc.password");
        factor2.setDesc("jdbc的密码");
        factor2.setType(ConfigType.STRING);
        // 不允许通过前端页面修改该配置
        factor2.setReadOnly(true);
        // 该配置的等级较高
        factor2.setGrade(5);

        ConfigUnit factor3 = new ConfigUnit();
        // some.api.qps
        factor3.setKey("some.api.qps");
        factor3.setDesc("某个api的qps阈值");
        factor3.setType(ConfigType.INT);
        factor3.setGrade(3);

        ConfigUnit factor4 = new ConfigUnit();
        // some.method.switch
        factor4.setKey("some.method.switch");
        factor4.setDesc("某个方法的开关");
        factor4.setType(ConfigType.BOOLEAN);
        factor4.setGrade(2);

        ConfigUnit factor5 = new ConfigUnit();
        // time.diff
        factor5.setKey("time.diff");
        factor5.setDesc("时间差，单位：毫秒");
        factor5.setType(ConfigType.INT);
        factor5.setGrade(2);

        factorList.add(factor1);
        factorList.add(factor2);
        factorList.add(factor3);
        factorList.add(factor4);
        factorList.add(factor5);

        return factorList;
    }

    @Override
    public boolean refreshUnitOnReceiveConfig() {
        return false;
    }


}
