package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;

import java.util.List;

/**
 * @author : Arvin
 * @Project Name : pinyougou-parent
 * @Package Name : com.pinyougou.sellergoods.service
 * @Creation Date: 2019-08-03 14:30
 * @Description : 品牌接口
 */
public interface BrandService {

    /**
     * 查询所有品牌
     * @return
     */
    public List<TbBrand> findAll();
}
