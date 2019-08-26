package com.pinyougou.search.service;

import com.pinyougou.pojo.TbItem;

import java.util.List;
import java.util.Map;

/**
 * 用户搜索服务接口
 *
 * @author appoleo
 */
public interface ItemSearchService {

    /**
     * 搜索，关键字搜索，返回结果集
     *
     * @param searchMap 关键字集合
     * @return 搜索返回结果集
     */
    Map<String, Object> search(Map<String, Object> searchMap);

    /**
     * 商品sku列表导入solr索引库
     *
     * @param itemList 商品sku列表
     */
    void importList(List<TbItem> itemList);

    /**
     * 删除索引库中对应商品id的sku数据
     *
     * @param goodsIds 商品id集合
     */
    void deleteByGoodsIds(List<Long> goodsIds);
}