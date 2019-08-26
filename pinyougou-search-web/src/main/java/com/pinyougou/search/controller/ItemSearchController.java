package com.pinyougou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author : Arvin
 * @project Name : pinyougou-parent
 * @package Name : com.pinyougou.search.controller
 * @creation Date: 2019-08-16 18:14
 * @description :
 */
@RestController
@RequestMapping("/itemsearch")
public class ItemSearchController {


    @Reference(timeout = 10000)
    private ItemSearchService itemSearchService;

    @RequestMapping("/search")
    public Map search(@RequestBody Map searchMap){
        Map search = itemSearchService.search(searchMap);
        System.out.println(search);
        return search;
    }
}
