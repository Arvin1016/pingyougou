package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Arrays;

/**
 * 监听类，activeMQ消息服务消费者，topic模式，删除商品详情静态页面
 *
 * @author appoleo
 */
@Component
public class PageDeleteListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    /**
     * 删除商品详情静态页面
     *
     * @param message 商品id数组
     */
    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long[] goodsIds = (Long[]) objectMessage.getObject();
            System.out.println("-------- 监听到消息：" + Arrays.asList(goodsIds) + " --------");
            boolean flag = itemPageService.deleteItemHtml(goodsIds);
            System.out.println("-------- 静态页面删除：" + flag + " --------");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}