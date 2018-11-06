package com.wxapply.controller;

import com.wxapply.handler.LogHandler;
import com.wxapply.handler.SubscribeHandler;
import com.wxapply.service.WxService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.bean.device.WxDeviceMsg;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("${adminPath}")
public class WxController {

    public static final Logger logger = LoggerFactory.getLogger(WxController.class);

    @Autowired
    WxService wxService;

    @Autowired
    LogHandler logHandler;

    @Autowired
    SubscribeHandler subscribeHandler;

    private String url = "http://bx9fc5.natappfree.cc";
    private String openId = "olPd81eGHuYSb-91T3f0KVQPg7Sc";
    private String templateId = "s8GK3oqzOL33VmhrLajkKOEdyMSCIDC-3Ji1r_z_Dzs";


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
            logger.info("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }
            out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
        }
        logger.info("\n组装回复信息：{}", out);
        return out;
    }

    @GetMapping(value = "/wechat/portal")
    public String authGet(@RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {
        logger.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        if (wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }

        return "非法请求";
    }

    @PostMapping("/sendKefuMessage")
    public boolean sendKefuMessage() throws WxErrorException {
        WxMpKefuMessage.WxArticle wxArticle = new WxMpKefuMessage.WxArticle();
        wxArticle.setDescription("无锡市滨湖区地处长江三角洲腹地，江苏省东南部，无锡市西南部。南依太湖，北靠长江，东临上海，西接南京。全区区域面积628.15平方公里，其中陆地面积257.89平方公里，太湖湖岸线82.53公里，常住人口约69.5万，是长三角地区具有较大影响力和辐射力的生态新城、科创高地、文旅之都和商务中心。");
        wxArticle.setPicUrl("https://mmbiz.qpic.cn/mmbiz_png/yOTDWHboj5FfRH5ibFYtvFALFL42n0CW61kXPS19xf73W5rXTKRCX1crZJib6BicamD0eQF1AibljDSoDDBh1LAoEA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1");
        wxArticle.setTitle("醉美湖湾·无锡滨湖欢迎您来创新创业");
        wxArticle.setUrl("https://mp.weixin.qq.com/s?__biz=MzU0NjkyNzEyNg==&mid=100000426&idx=1&sn=a528698942e61cb82f68fc052d363d53&scene=19#wechat_redirect");

        WxMpKefuMessage wxMpKefuMessage = WxMpKefuMessage.NEWS()
                .toUser(openId)
                .addArticle(wxArticle)
                .build();

        boolean isTrue = wxService.getKefuService().sendKefuMessage(wxMpKefuMessage);
        return isTrue;
    }

    @PostMapping("/getQrCode")
    public String getQrCode() throws WxErrorException, IOException {
        int scene = 121;
        Integer expireSeconds = 7200;
        WxMpQrCodeTicket ticket = wxService.getQrcodeService().qrCodeCreateTmpTicket(scene, expireSeconds);
        // 获得在系统临时目录下的文件，需要自己保存使用，注意：临时文件夹下存放的文件不可靠，不要直接使用
        File file = wxService.getQrcodeService().qrCodePicture(ticket);

        String qrFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\qrCode\\";
        String fileName = file.getName();
        File file1 = new File(qrFilePath);
        File file2 = new File(qrFilePath + fileName);
        if (!file1.exists() && file1.mkdirs() && file2.createNewFile()) {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            fileOutputStream.write(FileUtils.readFileToByteArray(file));
            System.out.println("create success!");
        }else {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            fileOutputStream.write(FileUtils.readFileToByteArray(file));
            System.out.println("create success!");
        }
        return null;
    }

    @GetMapping("/getShortUrl")
    public String getShortUrl() throws WxErrorException {
        return wxService.shortUrl("www.baidu.com");
    }

    /**
     * @return
     */
    public WxMpXmlOutMessage route(WxMpXmlMessage message) {
        WxMpMessageRouter wxMpMessageRouter = new WxMpMessageRouter(wxService);
        // 记录所有事件的日志
        wxMpMessageRouter.rule().handler(logHandler).next();
        // 关注事件
        wxMpMessageRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.SUBSCRIBE).handler(subscribeHandler)
                .end();
        WxMpXmlOutMessage wxMpXmlOutMessage = wxMpMessageRouter.route(message);
        return wxMpXmlOutMessage;
    }

    @RequestMapping(value = "/getEncry", method = RequestMethod.GET)
    public WxJsapiSignature getEncry() throws WxErrorException {
        WxJsapiSignature wxJsapiSignature = wxService.createJsapiSignature("http://rw4dc8.natappfree.cc/send");
        return wxJsapiSignature;
    }

    @RequestMapping(value = "/getAuthToken", method = RequestMethod.GET)
    public String getAuthToken() throws WxErrorException {
        return wxService.getAccessToken();
    }

}
