package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : Arvin
 * @Project Name : pinyougou-parent
 * @Package Name : com.pinyougou.manager.controller
 * @Creation Date: 2019-08-03 14:34
 * @Description : 品牌web层
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    /**
     * 注入BrandService层
     */
    @Reference
    private BrandService brandService;

    /**
     * 查询所有品牌
     * @return
     */
    @RequestMapping("/findAll.do")
    public List<TbBrand> findAll(){
        return brandService.findAll();
    }
}
