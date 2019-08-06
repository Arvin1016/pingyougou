package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

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
     *
     * @return 品牌集合
     */
    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

    /**
     * 分页查询所有品牌
     *
     * @param pageNum  当前页
     * @param pageSize 每页记录数
     * @return 分页结果集
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 添加品牌
     *
     * @param brand
     */
    @Override
    public void add(TbBrand brand) {
        brandMapper.insert(brand);
    }

    /**
     * 根据id查询品牌
     *
     * @param id 品牌id
     * @return 品牌对象
     */
    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改品牌信息
     *
     * @param brand 品牌对象
     */
    @Override
    public void update(TbBrand brand) {
        brandMapper.updateByPrimaryKey(brand);
    }

    /**
     * 删除品牌
     * 由于根据id删除,所以要遍历获取每一个id
     *
     * @param ids 一个或多个品牌的数组
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            brandMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 品牌条件查询
     *
     * @param brand    品牌
     * @param pageNum  当前页
     * @param pageSize 每页记录数
     * @return 分页结果集
     */
    @Override
    public PageResult findPage(TbBrand brand, int pageNum, int pageSize) {
        //1.调用分页插件
        PageHelper.startPage(pageNum, pageSize);
        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        if (brand != null) {
            if (brand.getName() != null && brand.getName().length() > 0) {
                criteria.andNameLike("%" + brand.getName() + "%");
            }
            if (brand.getFirstChar() != null && brand.getFirstChar().length() > 0) {
                criteria.andFirstCharLike(brand.getFirstChar());
            }
        }

        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(example);

        return new PageResult(page.getTotal(),page.getResult());
}

    @Override
    public List<Map> selectOptionList() {
        return brandMapper.selectOptionList();
    }
}
