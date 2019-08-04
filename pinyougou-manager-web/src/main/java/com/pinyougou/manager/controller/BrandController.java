package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : Arvin
 * @project Name : pinyougou-parent
 * @package Name : com.pinyougou.manager.controller
 * @creation Date: 2019-08-03 14:34
 * @description : 品牌web层
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
     *
     * @return
     */
    @RequestMapping("/findAll.do")
    public List<TbBrand> findAll() {
        return brandService.findAll();
    }

    /**
     * 分页查询所有品牌
     *
     * @param page 当前页
     * @param size 总页数
     * @return
     */
    @RequestMapping("/findPage.do")
    public PageResult finPage(int page, int size) {
        return brandService.findPage(page, size);
    }

    /**
     * 添加品牌
     *
     * @param brand
     * @return
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody TbBrand brand) {
        try {
            brandService.add(brand);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 根据id查询品牌
     *
     * @param id 品牌id
     * @return 品牌对象
     */
    @RequestMapping("/findOne.do")
    public TbBrand findOne(Long id) {
        return brandService.findOne(id);
    }

    /**
     * 修改品牌信息
     * RequestBody的作用是前台返回的是json格式,加上注解可以直接获取数据
     *
     * @param brand 品牌对象
     * @return 结果集
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody TbBrand brand) {
        try {
            brandService.update(brand);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 根据id 删除品牌
     * @param ids 品牌的数组
     * @return 结果集
     */
    @RequestMapping("/delete.do")
    public Result delete(Long[] ids) {
        try {
            brandService.delete(ids);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 条件查询和分页
     * @param brand 品牌对象
     * @param page 当前页
     * @param size 每页记录数
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(@RequestBody TbBrand brand,int page,int size){
        return brandService.findPage(brand,page,size);
    }
}
