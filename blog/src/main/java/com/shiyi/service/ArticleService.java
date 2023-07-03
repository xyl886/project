package com.shiyi.service;

import com.shiyi.common.Result;
import com.shiyi.entity.BlogArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.dto.ArticleDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客文章表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
public interface ArticleService extends IService<BlogArticle> {

    /**
     * 后台分页获取文章
     * @param map 参数map
     * @return
     */
    Result listArticle(Map<String,Object> map);

    /**
     * 后台根据主键获取文章详情
     * @param id 主键id
     * @return
     */
    Result getArticleById(Long id);

    /**
     * 添加文章
     * @param article 文章对象
     * @return
     */
    Result insertArticle(ArticleDTO article);

    /**
     * 修改文章
     * @param article 文章对象
     * @return
     */
    Result updateArticle(ArticleDTO article);

    /**
     * 后台根据文章id删除文章
     * @param id 文章id
     * @return
     */
    Result deleteArticle(Long id);

    /**
     * 后台批量删除文章
     * @param ids 文章id集合
     * @return
     */
    Result deleteBatchArticle(List<Long> ids);

    /**
     * 置顶文章
     * @param article 文章对象
     * @return
     */
    Result putTopArticle(ArticleDTO article);

    /**
     * 发布或下架文章
     * @param article 文章对象
     * @return
     */
    Result publishAndShelf(ArticleDTO article);

    /**
     * 百度seo
     * @param ids 文章id集合
     * @return
     */
    Result articleSeo(List<Long> ids);

    /**
     * 爬取文章
     * @param url 文章地址
     * @return
     */
    Result reptile(String url);

    /**
     * 随机获取图片
     * @return
     */
    Result randomImg();




    //    ----------web端开始------

    /**
     * 首页分页获取文章
     * @return
     */
    Result listWebArticle();

    /**
     * 首页获取文章详情
     * @param id 文章id
     * @return
     */
    Result webArticleInfo(Integer id);

    /**
     * 根据分类id或标签id获取文章
     * @param categoryId 分类id
     * @param tagId 标签id
     * @param pageSize 每页数量
     * @return
     */
    Result condition(Long categoryId, Long tagId, Integer pageSize);

    /**
     * 校验秘钥
     * @param code 验证码
     * @return
     */
    Result checkSecret(String code);

    /**
     * 文章归档
     * @return
     */
    Result archive();

    /**
     * 搜索文章
     * @param keywords 搜索关键词
     * @return
     */
    Result searchArticle(String keywords);

    /**
     * 文章点赞
     * @param articleId 文章id
     * @return
     */
    Result articleLike(Integer articleId);
}
