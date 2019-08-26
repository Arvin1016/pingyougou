package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.List;

/**
 * controller 商品spu控制层
 *
 * @author appoleo
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;
    // @Reference(timeout = 100000)
    // private ItemSearchService itemSearchService;
    // @Reference(timeout = 40000)
    // private ItemPageService itemPageService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination queueSolrDestination; // 用于导入solr索引库的消息目标（点对点）
    @Autowired
    private Destination queueSolrDeleteDestination; // 用于删除索引库数据的消息目标（点对点）
    @Autowired
    private Destination topicPageDestination; // 用于生成商品详情页的消息目标（发布订阅）
    @Autowired
    private Destination topicPageDeleteDestination; // 用于删除商品详情页的消息目标（发布订阅）

    /**
     * 返回全部列表
     *
     * @return 商品实体列表
     */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }

    /**
     * 返回全部列表
     *
     * @return 分页结果集
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return goodsService.findPage(page, rows);
    }

    /**
     * 修改
     *
     * @param goods 修改后的商品实体
     * @return 操作结果
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Goods goods) {
        try {
            goodsService.update(goods);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id 商品id
     * @return 商品实体
     */
    @RequestMapping("/findOne")
    public Goods findOne(Long id) {
        return goodsService.findOne(id);
    }

    /**
     * 批量删除，同时使用activeMQ消息服务删除索引库数据，删除静态页面
     *
     * @param ids 商品id数组
     * @return 操作结果
     */
    @RequestMapping("/delete")
    public Result delete(final Long[] ids) {
        try {
            goodsService.delete(ids);

            // 从索引库中删除数据
            // itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
            jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createObjectMessage(ids);
                }
            });

            // 删除每个服务器上面的商品详情页
            jmsTemplate.send(topicPageDeleteDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createObjectMessage(ids);
                }
            });

            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 条件查询+分页
     *
     * @param goods 封装了查询条件的实体
     * @param page  当前页码
     * @param rows  每页记录数
     * @return 结果集
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
        return goodsService.findPage(goods, page, rows);
    }

    /**
     * 商品批量审核，使用activeMQ消息服务更新solr索引库，生成、删除商品详情静态页面
     * status->1：添加商品sku列表到索引库
     *
     * @param ids    商品id数组
     * @param status 审核的状态
     * @return 操作结果
     * @see GoodsService#findByItemListAndStatus(Long[], String)
     *      根据商品spu的id数组查询状态为status的sku列表
    // * @see ItemSearchService#importList(List) 商品sku列表导入solr索引库
     */
    @RequestMapping("/updateStatus")
    public Result updateStatus(final Long[] ids, String status) {
        try {
            goodsService.updateStatus(ids, status);
            if ("1".equals(status)) { // 审核通过，导入索引库，生成商品详情静态页面
                List<TbItem> itemList = goodsService.findByItemListAndStatus(ids, status);
                final String itemListJson = JSON.toJSONString(itemList); // 转换为json传输
                jmsTemplate.send(queueSolrDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createTextMessage(itemListJson);
                    }
                });
                // itemSearchService.importList(itemList);

                for (final Long goodsId : ids) { // 生成商品详情静态页面
                    // itemPageService.genItemHtml(goodsId);
                    jmsTemplate.send(topicPageDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            return session.createTextMessage(goodsId.toString());
                        }
                    });
                }
            }
            if ("2".equals(status)) { // 审核驳回，从索引库中删除数据，删除商品详情静态页面
                // itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
                jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createObjectMessage(ids);
                    }
                });

                // 删除每个服务器上面的商品详情页
                jmsTemplate.send(topicPageDeleteDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createObjectMessage(ids);
                    }
                });
            }
            return new Result(true, "审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "审核失败");
        }
    }
}