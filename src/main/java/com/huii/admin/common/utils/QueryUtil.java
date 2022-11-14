package com.huii.admin.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

public class QueryUtil<T> {

    public IPage<T> getPageInfo(Map<String, Object> params){
        int current =1;
        int pageSize =10;
        if(params.get("current")!=null){
            current = Integer.parseInt((String) params.get("current"));
        }
        if(params.get("pageSize")!=null){
            pageSize = Integer.parseInt((String) params.get("pageSize"));
        }

        return new Page<>(current,pageSize);
    }
}
