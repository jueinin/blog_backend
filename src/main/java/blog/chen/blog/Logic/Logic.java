package blog.chen.blog.Logic;

import blog.chen.blog.entities.ArticleListEntity;
import blog.chen.blog.repos.ArticleListRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class Logic {
    static ObjectMapper mapper = new ObjectMapper();
    public static List<ArticleListEntity> getHotArticles(ArticleListRepo articleListRepo) {
        return articleListRepo.getHotArticles(new PageRequest(0, 6));
    }
    public static List<ArticleListEntity> getRecentArticles(ArticleListRepo articleListRepo) {
        return articleListRepo.getRecentArticles(new PageRequest(0, 6));
    }
    public static List<ArticleListEntity> getRandomArticles(ArticleListRepo articleListRepo) {
        return articleListRepo.getRandomArticles(new PageRequest(0, 6));
    }

    public static ArrayNode returnSideBar(List<ArticleListEntity>list) {
        ArrayNode arrayNode = mapper.createArrayNode();
        for (ArticleListEntity articleListEntity : list) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("articleUrl", "/article/" + articleListEntity.getTitle())
                    .put("articleTitle", articleListEntity.getTitle());
            arrayNode.add(objectNode);
        }
        return arrayNode;
    }

    public static ArrayNode getHotOrRecentOrRandom(ArticleListRepo articleListRepo,String category) {
        if (category == "hot") {
            return returnSideBar(getHotArticles(articleListRepo));
        } else if (category == "recent") {
            return returnSideBar(getRecentArticles(articleListRepo));
        } else {
            return returnSideBar(getRandomArticles(articleListRepo));
        }
    }

    public static void main(String[] args) {

    }
}
