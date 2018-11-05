package com.wxapply.handler;


import com.wxapply.builder.NewsBuilder;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Binary Wang
 */
@Component
public class SubscribeHandler extends AbstractHandler {
    private static final Logger logger = LoggerFactory.getLogger(SubscribeHandler.class);


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        // 获取微信用户基本信息
        WxMpUser userWxInfo = wxMpService.getUserService().userInfo(wxMessage.getFromUser(), "zh_CN");

        if (userWxInfo != null) {
//             TODO 可以添加关注用户到本地
        }

        WxMpXmlOutMessage responseResult = null;
        try {
            responseResult = handleSpecial(wxMessage);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        if (responseResult != null) {
            return responseResult;
        }

        try {
            return new NewsBuilder().build("醉美湖湾·无锡滨湖欢迎您来创新创业", wxMessage, wxMpService);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage) throws Exception {
        //TODO
        return null;
    }

}