package com.wxapply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("com.wxapply.service")
public class WxapplyApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxapplyApplication.class, args);
    }
}
