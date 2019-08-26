package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 监听类：activeMQ消息服务消费者，生成商品详情静态页面
 *
 * @author appoleo
 */
@Component
public class PageListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    /**
     * 生成商品详情静态页面
     *
     * @param message 商品id
     */
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String goodsId = textMessage.getText();
            System.out.println("-------- 监听到消息：" + goodsId + " --------");
            boolean flag = itemPageService.genItemHtml(Long.valueOf(goodsId));
            System.out.println("-------- 静态页面生成：" + flag + "--------");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}