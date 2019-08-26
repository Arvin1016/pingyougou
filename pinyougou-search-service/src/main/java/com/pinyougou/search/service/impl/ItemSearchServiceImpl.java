package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户搜索服务实现类
 *
 * @author appoleo
 */
@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 搜索，关键字搜索，关键字高亮，条件过滤，返回结果集，获取分组类目集合
     *
     * @param searchMap 搜索集合：{keywords: '', category: '', brand: '', spec: {}, pageNo: '', pageSize: '', sort: '', sortField: ''}
     * @return 搜索返回结果集：{rows: '', categoryList: '', brandList: '', specList: {}, totalPages: '', total: ''}
     * @see #searchList(Map) 高亮搜索，条件过滤，分页
     * @see #searchCategoryList(Map) 分组类目
     * @see #searchBrandListAndSpecList(String) 根据商品分类名称查询品牌列表和规格列表
     */
    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {
        // 处理关键字中的空格
        String keywords = (String) searchMap.get("keywords");
        searchMap.put("keywords", keywords.replace(" ", ""));
        // 1. 查询列表（关键字高亮，条件过滤）
        Map<String, Object> map = new HashMap<>(searchList(searchMap)); // replace putAll()
        // 2. 分组查询商品分类列表
        List<String> categoryList = searchCategoryList(searchMap);
        map.put("categoryList", categoryList);
        // 3. 查询品牌和规格列，默认分类为第一条，当点击选择了分类，按所选分类查询
        String category = (String) searchMap.get("category");
        if ("".equals(category)) {
            if (categoryList.size() > 0) {
                map.putAll(searchBrandListAndSpecList(categoryList.get(0)));
            }
        } else {
            map.putAll(searchBrandListAndSpecList(category));
        }
        return map;
    }

    /**
     * 搜索，关键字搜索，返回结果集，高亮显示关键词，按条件过滤
     *
     * @param searchMap 关键字集合
     * @return 搜索返回结果集
     */
    private Map<String, Object> searchList(Map<String, Object> searchMap) {
        // 构建高亮查询对象
        HighlightQuery query = new SimpleHighlightQuery();
        // 构建高亮选项对象
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title"); // 高亮域
        highlightOptions.setSimplePrefix("<em style='color:red'>"); // 前缀
        highlightOptions.setSimplePostfix("</em>"); // 后缀
        query.setHighlightOptions(highlightOptions); // 为查询对象设置高亮选项

        /* ------------------------------ 按条件过滤 ------------------------------- */

        // 1.1 关键字查询过滤
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords")); // like where
        query.addCriteria(criteria);
        // 1.2 商品分类过滤
        if (!"".equals(searchMap.get("category"))) { // 用户选择了分类条件再进行条件筛选
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        // 1.3 品牌分类过滤
        if (!"".equals(searchMap.get("brand"))) { // 用户选择了品牌条件再进行条件筛选
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        // 1.4 规格选项过滤
        if (searchMap.get("spec") != null) {
            @SuppressWarnings("unchecked")
            Map<String, String> specMap = (Map<String, String>) searchMap.get("spec");
            for (String key : specMap.keySet()) {
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_spec_" + key).is(specMap.get(key));
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
        // 1.5 价格过滤
        if (!"".equals(searchMap.get("price"))) {
            String[] price = ((String) searchMap.get("price")).split("-");
            if (!"0".equals(price[0])) { // 价格下限不等于0时，设置价格下限
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(price[0]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
            if (!"*".equals(price[1])) { // 价格上限不等于*时，设置价格上限
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_price").lessThanEqual(price[1]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
        // 1.6 分页
        Integer pageNo = (Integer) searchMap.get("pageNo");
        if (pageNo == null) {
            pageNo = 1;
        }
        Integer pageSize = (Integer) searchMap.get("pageSize");
        if (pageSize == null) {
            pageSize = 20;
        }
        query.setOffset((pageNo - 1) * pageSize);
        query.setRows(pageSize);
        // 1.7 排序
        String sortValue = (String) searchMap.get("sort");
        String sortField = (String) searchMap.get("sortField");
        if (sortValue != null && !"".equals(sortValue)) {
            if ("ASC".equalsIgnoreCase(sortValue)) {
                Sort sort = new Sort(Sort.Direction.ASC,"item_" + sortField);
                query.addSort(sort);
            }
            if ("DESC".equalsIgnoreCase(sortValue)) {
                Sort sort = new Sort(Sort.Direction.DESC, "item_" + sortField);
                query.addSort(sort);
            }
        }

        /* -------------------------------- 结果集 -------------------------------- */
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        // 高亮入口集合（每条记录的高亮入口）
        List<HighlightEntry<TbItem>> entryList = page.getHighlighted();
        for (HighlightEntry<TbItem> entry : entryList) {
            // 获取高亮列表（高亮域的个数）
            List<HighlightEntry.Highlight> highlightList = entry.getHighlights();
            /*for (HighlightEntry.Highlight highlight : highlightList) {
                List<String> snipplets = highlight.getSnipplets(); // 每个域中有个能存在多个值
            }*/
            TbItem item = entry.getEntity(); // 和page.getContent()中封装的实体是同一个
            if (highlightList.size() > 0 && highlightList.get(0).getSnipplets().size() > 0) {
                item.setTitle(highlightList.get(0).getSnipplets().get(0));
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("rows", page.getContent());
        map.put("totalPages", page.getTotalPages());
        map.put("total", page.getTotalElements());
        return map;
    }

    /**
     * 搜索，关键字搜索，返回分组结果集
     *
     * @param searchMap 关键字集合
     * @return 分组的category集合
     */
    private List<String> searchCategoryList(Map<String, Object> searchMap) {
        // 构建查询对象
        Query query = new SimpleQuery();
        // 根据关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria); // like where ...
        // 设置分组选项
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions); // like group by...
        // 获取分组页
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
        // 获取分组结果对象（指定域的分组结果对象）
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
        // 获取分组入口页
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
        // 获取分组入口集合
        List<GroupEntry<TbItem>> entryList = groupEntries.getContent();
        // 创建list集合，封装分组结果
        List<String> list = new ArrayList<>();
        for (GroupEntry<TbItem> entry : entryList) {
            list.add(entry.getGroupValue());
        }
        return list;
    }

    /**
     * 根据商品分类名称查询品牌列表和规格列表（spring data redis）
     *
     * @param category 分类名称，itemCat.name
     * @return 封装品牌列表和规格列表的Map集合
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> searchBrandListAndSpecList(String category) {
        Map<String, Object> map = new HashMap<>();
        Long templateId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        if (templateId != null) {
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(templateId);
            map.put("brandList", brandList);
            List specList = (List) redisTemplate.boundHashOps("specList").get(templateId);
            map.put("specList", specList);
        }
        return map;
    }

    /* ---------------------- 关键字搜索 ---------------------- */
    /*private Map<String, Object> justSearchList(Map<String, Object> searchMap){
     Query query = new SimpleQuery("*:*");
     Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
     query.addCriteria(criteria);
     ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
     Map<String, Object> map = new HashMap<>();
     map.put("rows", page.getContent());
     return map;
     }*/

    /**
     * 商品sku列表导入solr索引库
     *
     * @param itemList 商品sku列表
     */
    @Override
    public void importList(List<TbItem> itemList) {
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }

    /**
     * 删除索引库中对应商品id的sku数据
     *
     * @param goodsIds 商品id集合
     */
    @Override
    public void deleteByGoodsIds(List<Long> goodsIds) {
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_goodsid").in(goodsIds);
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
}
