package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author : Arvin
 * @Project Name : pinyougou-parent
 * @Package Name : com.pinyougou.sellergoods.service.impl
 * @Creation Date: 2019-08-03 14:31
 * @Description :品牌实现类
 */
@Service
public class BrandServiceImpl implements BrandService {

    /**
     * 注入TbBrandMapper
     */
    @Autowired
    private TbBrandMapper brandMapper;

    /**
     * 查询所有品牌
     * @return
     */
    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }
}
