package com.pinyougou.cart.service;

import com.pinyougou.pojogroup.Cart;

import java.util.HashMap;
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

    /**
     * 从redis中查询购物车
     * @param username 用户名
     * @return
     */
    public List<Cart> findListCartFromRedis(String username);

    /**
     * 将购物车存入redis
     * @param username 用户名
     * @param cartList 购物车集合
     */
    public void  saveCartListToRedis(String username,List<Cart> cartList);

    /**
     * 合并购物车
     * @param cartList1
     * @param cartList2
     * @return
     */
    public List<Cart> mergeCartList(List<Cart> cartList1,List<Cart> cartList2);
}
