package com.pinyougou.search.service.impl;

import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Arrays;

/**
 * 监听类：activeMQ消息服务消费者，queue模式，删除索引库指定数据
 *
 * @author appoleo
 */
@Component
public class ItemDeleteListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    /**
     * 索引库删除商品sku列表
     *
     * @param message 商品id数组
     */
    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long[] ids = (Long[]) objectMessage.getObject();
            System.out.println("-------- 监听到消息：" + Arrays.asList(ids) + " --------");
            itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
            System.out.println("-------- 索引库中删除数据 --------");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}