package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

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

    /**
     * 品牌分页查询
     * @param pageNum 当前页
     * @param pageSize 每页记录数
     * @return
     */
    public PageResult findPage(int pageNum,int pageSize);

    /**
     * 新增品牌
     * @param brand 品牌对象
     */
    public void add(TbBrand brand);

    /**
     * 根据id查询单个品牌
     * @param id  品牌id
     * @return 品牌对象
     */
   public TbBrand findOne(Long id);

    /**
     * 修改品牌信息
     * @param brand 品牌对象
     */
   public void update(TbBrand brand);


    /**
     * 删除品牌
     * @param ids 一个或多个品牌的数组
     */
   public void delete(Long [] ids);

    /**
     * 品牌条件查询
     * @param brand 品牌
     * @param pageNum 当前页
     * @param pageSize 每页记录数
     * @return 分页结果集
     */
    public PageResult findPage(TbBrand brand,int pageNum,int pageSize);


    public List<Map> selectOptionList();
}
