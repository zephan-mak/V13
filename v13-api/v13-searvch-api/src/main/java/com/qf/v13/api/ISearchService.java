package com.qf.v13.api;

import com.qf.v13.common.pojo.ResultBean;

/**
 * @author maizifeng
 * @Date 2019/6/17
 */
public interface ISearchService {
    /**
     * 全局同步
     * @return
     */
    public ResultBean synAllData();

    /**
     * 根据关键字搜索
     * @param keyWord
     * @return
     */
    ResultBean searchKeyWord(String keyWord);

    /**
     * 增量同步
     * @param id
     * @return
     */
    public ResultBean updateById(Long id);

    /**
     * 根据关键字搜索跟分页
     * @param keyWord
     * @param indexPage
     * @param pageSize
     * @return
     */
    ResultBean searchKeyWord(String keyWord, int indexPage, int pageSize);
}
