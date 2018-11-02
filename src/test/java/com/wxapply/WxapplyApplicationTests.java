package com.wxapply;

import com.wxapply.service.WxService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WxapplyApplicationTests {
    @Autowired
    WxService wxService;
    @Test
    public void contextLoads() throws WxErrorException {

    }

}
