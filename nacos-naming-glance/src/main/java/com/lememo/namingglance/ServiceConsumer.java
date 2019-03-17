package com.lememo.namingglance;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * 模拟一个服务消费者
 * 向 Nacos 监听服务
 *
 * @author houyi
 **/
public class ServiceConsumer {
    public static void main(String[] args) throws NacosException {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", Constants.NACOS_SERVER_ADDRESS);
        properties.setProperty("namespace", Constants.NAMESPACE);

        NamingService naming = NamingFactory.createNamingService(properties);
        naming.subscribe(Constants.SERVICE_NAME, new EventListener() {
            @Override
            public void onEvent(Event event) {
                NamingEvent namingEvent = (NamingEvent) event;
                printInstances(namingEvent);
                mockConsume(naming, Constants.SERVICE_NAME);
            }
        });
        try {
            int in = System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void mockConsume(NamingService naming, String serviceName) {
        int i = 0, loop = 5;
        try {
            while (i++ < loop) {
                Instance instance = naming.selectOneHealthyInstance(serviceName);
                if (instance != null) {
                    System.out.println("get one healthy instance of service:" + serviceName
                            + "\nip=" + instance.getIp() + ", port=" + instance.getPort()
                            + ", cluster=" + instance.getClusterName()
                            + "\n=========================================\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printInstances(NamingEvent namingEvent) {
        List<Instance> instanceList = namingEvent.getInstances();
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        int i = 0, s = instanceList.size();
        for (Instance instance : instanceList) {
            sb.append("\t").append(instance);
            if (i++ < s - 1) {
                sb.append(",").append("\n");
            }
        }
        sb.append("\n]");
        System.out.println("===========receive new service===========\nserviceName=" + namingEvent.getServiceName()
                + "\ninstance size=" + instanceList.size()
                + "\ninstance list=" + sb.toString()
                + "\n=========================================\n");
    }

}
