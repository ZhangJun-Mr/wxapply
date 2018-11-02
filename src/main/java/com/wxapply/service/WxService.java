package com.wxapply.service;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Component
public class WxService extends WxMpServiceImpl {

    @PostConstruct
    void config() {
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        // 设置微信公众号的appid
        config.setAppId("wx844428d917fb2bdf");
        // 设置微信公众号的app corpSecret
        config.setSecret("fee1d3dcfb63c45b6347fb5b02fd7632");
        // 设置微信公众号的token
        config.setToken("jy_wlrkgl");
        // 设置微信公众号的EncodingAESKey
//        config.setAesKey("...");
        super.setWxMpConfigStorage(config);
    }






}
