package com.wxapply.builder;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;

/**
 * 
 * @author Binary Wang
 *
 */
public class NewsBuilder extends AbstractBuilder {

  @Override
  public WxMpXmlOutMessage build(String title, WxMpXmlMessage wxMessage,
                                 WxMpService service)   {
    WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
    item.setDescription("无锡市滨湖区地处长江三角洲腹地，江苏省东南部，无锡市西南部。南依太湖，北靠长江，东临上海，西接南京。全区区域面积628.15平方公里，其中陆地面积257.89平方公里，太湖湖岸线82.53公里，常住人口约69.5万，是长三角地区具有较大影响力和辐射力的生态新城、科创高地、文旅之都和商务中心。");
    item.setPicUrl("https://mmbiz.qpic.cn/mmbiz_png/yOTDWHboj5FfRH5ibFYtvFALFL42n0CW61kXPS19xf73W5rXTKRCX1crZJib6BicamD0eQF1AibljDSoDDBh1LAoEA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1");
    item.setTitle(title);
    item.setUrl("https://mp.weixin.qq.com/s?__biz=MzU0NjkyNzEyNg==&mid=100000426&idx=1&sn=a528698942e61cb82f68fc052d363d53&scene=19#wechat_redirect");
    WxMpXmlOutNewsMessage.Item item1 = new WxMpXmlOutNewsMessage.Item();
    item.setDescription("无锡市滨湖区地处长江三角洲腹地，江苏省东南部，无锡市西南部。南依太湖，北靠长江，东临上海，西接南京。全区区域面积628.15平方公里，其中陆地面积257.89平方公里，太湖湖岸线82.53公里，常住人口约69.5万，是长三角地区具有较大影响力和辐射力的生态新城、科创高地、文旅之都和商务中心。");
    item.setPicUrl("https://mmbiz.qpic.cn/mmbiz_png/yOTDWHboj5FfRH5ibFYtvFALFL42n0CW61kXPS19xf73W5rXTKRCX1crZJib6BicamD0eQF1AibljDSoDDBh1LAoEA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1");
    item.setTitle(title);
    item.setUrl("https://mp.weixin.qq.com/s?__biz=MzU0NjkyNzEyNg==&mid=100000426&idx=1&sn=a528698942e61cb82f68fc052d363d53&scene=19#wechat_redirect");
    WxMpXmlOutMessage m = WxMpXmlOutMessage.NEWS()
            .addArticle(item)
            .addArticle(item1)
            .fromUser(wxMessage.getToUser())
            .toUser(wxMessage.getFromUser())
            .build();
    return m;
  }

}
