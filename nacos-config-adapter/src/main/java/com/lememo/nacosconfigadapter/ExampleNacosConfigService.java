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
 **/
public class ExampleNacosConfigService extends AbstractNacosConfigService {

    public ExampleNacosConfigService(String serverAddress, String dataId, String group) {
        super(serverAddress, dataId, group);
    }

    @Override
    public List<NacosConfigFactor> getConfigFactorList() {
        List<NacosConfigFactor> factorList = new ArrayList<>();
        // 你可以从数据库中将 factor 读取出来
        // 这里只供演示，所以写死几个值
        NacosConfigFactor factor1 = new NacosConfigFactor();
        // jdbc.url
        factor1.setKey("jdbc.url");
        factor1.setDesc("jdbc的链接");
        factor1.setType(NacosConfigType.STRING);
        // 不允许通过前端页面修改该配置
        factor1.setReadOnly(true);
        // 该配置的等级较高
        factor1.setGrade(5);

        NacosConfigFactor factor2 = new NacosConfigFactor();
        // jdbc.password
        factor2.setKey("jdbc.password");
        factor2.setDesc("jdbc的密码");
        factor2.setType(NacosConfigType.STRING);
        // 不允许通过前端页面修改该配置
        factor2.setReadOnly(true);
        // 该配置的等级较高
        factor2.setGrade(5);

        NacosConfigFactor factor3 = new NacosConfigFactor();
        // some.api.qps
        factor3.setKey("some.api.qps");
        factor3.setDesc("某个api的qps阈值");
        factor3.setType(NacosConfigType.INT);
        factor3.setGrade(3);

        NacosConfigFactor factor4 = new NacosConfigFactor();
        // some.method.switch
        factor4.setKey("some.method.switch");
        factor4.setDesc("某个方法的开关");
        factor4.setType(NacosConfigType.BOOLEAN);
        factor4.setGrade(2);

        factorList.add(factor1);
        factorList.add(factor2);
        factorList.add(factor3);
        factorList.add(factor4);

        return factorList;
    }

    @Override
    public boolean refreshFactorsOnReceiveConfig() {
        return false;
    }


}
