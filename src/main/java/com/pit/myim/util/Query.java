package com.pit.myim.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;

/**
 * 查询参数
 *
 * @author Mark sunlightcs@gmail.com
 */
public class Query<T> {
    /**
     * 当前页码
     */
    public static final String PAGE = "page";

    public IPage<T> getPage(Map<String, Object> params) {
        return this.getPage(params, null, false);
    }

    public IPage<T> getPage(Map<String, Object> params, List<String> defaultOrderFields, boolean isAsc) {
        //分页参数
        Integer pageNum = 0;
        Integer pageSize = 10;
        Integer pageNum1 = (Integer) params.get("pageNum");
        Integer pageSize1 = (Integer) params.get("pageSize");

        if (pageNum1 != null && pageNum1 >= 0) {
            pageNum = pageNum1;
        }
        if (pageSize1 != null && pageSize1 > 0) {
            pageSize = pageSize1;
        }


        //分页对象
        Page<T> page = new Page<>(pageNum, pageSize);

        //分页参数
        params.put(PAGE, page);

        return page;
    }
}