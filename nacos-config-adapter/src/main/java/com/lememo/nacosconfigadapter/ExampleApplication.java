package com.lememo.nacosconfigadapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author houyi
 * 示例代码
 **/
@Controller
@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

    @Bean
    public NacosConfigService configService() {
        return new ExampleNacosConfigService("localhost", "test", "DEFAULT_GROUP");
    }

    @Autowired
    private NacosConfigService configService;
//
//    @RequestMapping("/")
//    public @ResponseBody
//    String index() {
//        return "index";
//    }

    @RequestMapping("/nacos/config/list")
    public @ResponseBody
    List<NacosConfig> getConfigList(@RequestParam(value = "grade") int grade) {
        return configService.getConfigList(grade);
    }

    @RequestMapping("/nacos/config/item")
    public @ResponseBody
    NacosConfig getConfig(@RequestParam(value = "key") String key) {
        return configService.getConfig(key);
    }

    @RequestMapping("/nacos/config/value")
    public @ResponseBody
    Object getConfigValue(@RequestParam(value = "key") String key) {
        return configService.getConfigValue(key);
    }

    @RequestMapping("/nacos/config/update")
    public @ResponseBody
    ResultDTO updateConfigValue(
            @RequestParam(value = "key") String key,
            @RequestParam(value = "value") Object value,
            @RequestParam(value = "grade") int grade) {
        boolean success;
        String msg = "success";
        try {
            success = configService.updateConfig(key, value, grade);
        } catch (Exception e) {
            success = false;
            msg = e.getMessage();
        }
        return new ResultDTO(success, msg);
    }


    /**
     * 返回前端的DTO
     */
    private class ResultDTO {
        private int code;
        private boolean success;
        private String errorMsg;

        ResultDTO(boolean success, String errorMsg) {
            this.code = 200;
            this.success = success;
            this.errorMsg = errorMsg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }


}
