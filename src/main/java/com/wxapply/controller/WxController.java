package com.wxapply.controller;

import com.wxapply.handler.LogHandler;
import com.wxapply.handler.SubscribeHandler;
import com.wxapply.service.WxService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.bean.device.WxDeviceMsg;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@RequestMapping("/")
public class WxController {

    public static final Logger logger = LoggerFactory.getLogger(WxController.class);

    @Autowired
    WxService wxService;

    @Autowired
    LogHandler logHandler;

    @Autowired
    SubscribeHandler subscribeHandler;

    private String url = "http://r5v8ki.natappfree.cc";
    private String openId = "olPd81eGHuYSb-91T3f0KVQPg7Sc";
    private String templateId = "s8GK3oqzOL33VmhrLajkKOEdyMSCIDC-3Ji1r_z_Dzs";

    private WxMpMessageRouter wxMpMessageRouter = new WxMpMessageRouter(wxService);

    @RequestMapping(value = "/sendTemplateMessage", method = RequestMethod.POST)
    public String sendTemplateMessage() throws WxErrorException {
        WxMpTemplateMessage wxMpTemplateMessage = WxMpTemplateMessage.
                builder()
                // 手机端点击消息即将进入的网址
                .url(url)
                .toUser(openId)
                .templateId(templateId)
                .build();
        wxMpTemplateMessage.addData(new WxMpTemplateData("first", "one"));
        wxMpTemplateMessage.addData(new WxMpTemplateData("keyword1", "two", Color.red.toString()));
        wxMpTemplateMessage.addData(new WxMpTemplateData("remark", "感谢您的支持。", ""));
        return wxService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
    }

    @RequestMapping(value = "/sendMessageToDevice", method = RequestMethod.POST)
    public String test() throws WxErrorException {
        WxDeviceMsg wxDeviceMsg = new WxDeviceMsg();
        wxDeviceMsg.setContent("test");
        wxDeviceMsg.setOpenId(openId);
        wxService.getDeviceService().transMsg(wxDeviceMsg);

        return "success";
    }

    @PostMapping(value = "/wechat/portal", produces = "application/xml; charset=UTF-8")
    public String post(@RequestBody String requestBody, @RequestParam("signature") String signature,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature,
                       @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce) {
        logger.info("接收微信请求：[signature=[{}], encType=[{}], msgSignature=[{}]," + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                signature, encType, msgSignature, timestamp, nonce, requestBody);

        if (!this.wxService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toXml();
        } else if ("aes".equals(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody,
                    wxService.getWxMpConfigStorage(), timestamp, nonce, msgSignature);
            logger.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
        }

        logger.debug("\n组装回复信息：{}", out);

        return out;
    }

    /**
     * @return
     */
    public WxMpXmlOutMessage route(WxMpXmlMessage message) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxService);
        // 记录所有事件的日志
        newRouter.rule().handler(this.logHandler).next();
        // 关注事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.SUBSCRIBE).handler(subscribeHandler)
                .end();
        WxMpXmlOutMessage wxMpXmlOutMessage = newRouter.route(message);
        return wxMpXmlOutMessage;
    }

    @RequestMapping(value = "/getEncry", method = RequestMethod.GET)
    public WxJsapiSignature getEncry() throws WxErrorException {
        WxJsapiSignature wxJsapiSignature = wxService.createJsapiSignature("http://rw4dc8.natappfree.cc/send");
        return wxJsapiSignature;
    }
}
