package com.pinyougou.cart.service;

import com.pinyougou.pojogroup.Cart;

import java.util.List;

/**
 * @author : Arvin
 * @project Name : pinyougou-parent
 * @package Name : com.pinyougou.cart.service
 * @creation Date: 2019-08-26 14:50
 * @description :  购物车服务接口
 */
public interface CartService {

    /**
     * 添加商品到购物车
     * @param cartList 购物车集合
     * @param itemId SKU
     * @param num 每个商品的数量
     * @return
     */
    public List<Cart> addGoodsToCartList(List<Cart> cartList,Long itemId,Integer num);
}
