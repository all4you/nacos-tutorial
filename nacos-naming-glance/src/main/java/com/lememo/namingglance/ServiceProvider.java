package com.lememo.namingglance;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * 模拟一个服务提供者
 * 向 Nacos 注册同一个服务的两个实例
 *
 * @author houyi
 **/
public class ServiceProvider {

    public static void main(String[] args) throws NacosException {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", Constants.NACOS_SERVER_ADDRESS);
        properties.setProperty("namespace", Constants.NAMESPACE);

        NamingService naming = NamingFactory.createNamingService(properties);

        naming.registerInstance(Constants.SERVICE_NAME, Constants.IP_1, Constants.PORT_1, Constants.CLUSTER_NAME_1);
        naming.registerInstance(Constants.SERVICE_NAME, Constants.IP_2, Constants.PORT_2, Constants.CLUSTER_NAME_2);
        List<Instance> instances = naming.getAllInstances(Constants.SERVICE_NAME);
        System.out.println("getAllInstances after registered\ninstance size="
                + instances.size() + "\ninstance list=" + instances);
        try {
            int in = System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
