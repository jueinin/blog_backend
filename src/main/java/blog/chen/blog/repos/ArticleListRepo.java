package blog.chen.blog.repos;

import blog.chen.blog.entities.ArticleListEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ArticleListRepo extends CrudRepository<ArticleListEntity,String> {
    @Modifying
    @Query(value = "insert into blog.article_list(title, tag, writer, img_url, content) " +
            "values(:title,:tag,:writer,:img_url,:content)", nativeQuery = true)
    @Transactional
    void insertArticle(@Param(value = "title") String title,@Param(value = "tag") String tag ,
    @Param(value="writer") String writer, @Param("img_url") String imgUrl ,
                                 @Param(value = "content") String content);

    //@Modifying
    @Transactional
    Integer deleteByTitle(String title);

    ArticleListEntity findByTitle(String title);
    @Modifying
    @Query(value="update ArticleListEntity set title=:title,tag=:tag,writer=:writer,imgUrl=:imgUrl,content=:content" +
            " ,description=:description where title=:oldTitle")
    @Transactional
    int updateWithOldTitle(@Param("title") String title, @Param("tag") String tag, @Param("writer") String writer,
                           @Param("imgUrl") String imgUrl, @Param("content") String content,
                           @Param("description") String description,@Param("oldTitle") String oldTitle);

    @Query("select a from ArticleListEntity a order by a.readTimes desc ")
    List<ArticleListEntity> getHotArticles(Pageable pageable);

    @Query("select a from ArticleListEntity a order by a.time desc ")
    List<ArticleListEntity> getRecentArticles(Pageable pageable);

    @Query("select a from ArticleListEntity a order by function('rand') ")
    List<ArticleListEntity> getRandomArticles(Pageable pageable);

    @Query("select a from ArticleListEntity a")
    Page<ArticleListEntity> findArticlePageByPage(Pageable pageable);

    @Query("select a from ArticleListEntity a where a.tag=:tag")
    Page<ArticleListEntity> findByArticleTagPageByPage(Pageable pageable, @Param(value = "tag") String tag);

    @Modifying
    @Query("update ArticleListEntity a set a.readTimes=a.readTimes+1 where a.title=:title")
    @Transactional
    int updateReadTimes(@Param(value = "title") String title);

    @Modifying
    @Query("update ArticleListEntity a set a.likes=a.likes+1 where a.title=:title")
    @Transactional
    int updateLikes(@Param(value = "title") String title);

    @Query("select a from ArticleListEntity a order by a.time desc ")
    List<ArticleListEntity> findallSortByTime();
}
