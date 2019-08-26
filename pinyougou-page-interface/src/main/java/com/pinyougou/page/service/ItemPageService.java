package com.pinyougou.page.service;


/**
 * @author : Arvin
 * @project Name : pinyougou-parent
 * @package Name : com.pinyougou.page.service
 * @creation Date: 2019-08-21 17:56
 * @description : 商品详细页接口
 * @blame Android Team
 */
public interface ItemPageService {

    public boolean genItemHtml(Long goodsId);

    /**
     * 删除商品详情页
     * @param goodsId
     * @return
     */
    public boolean deleteItemHtml(Long [] goodsIds);
}
