package blog.chen.blog.controllers;

import blog.chen.blog.Logic.Logic;
import blog.chen.blog.entities.ArticleListEntity;
import blog.chen.blog.repos.ArticleListRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RestfulController {
    static ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private ArticleListRepo articleListRepo;

    @RequestMapping(value = "/addArticle", method = RequestMethod.POST)
    public String ff(@RequestBody(required = false) Map<String, String> map) {
        String title = map.get("title");
        String tag = map.get("tag");
        String writer = map.get("writer");
        String imgUrl = map.get("imgUrl");
        String content = map.get("content");
        String description = map.get("description");
        System.out.println(title);
        ArticleListEntity articleListEntity = new ArticleListEntity(title,tag,writer,imgUrl,content,description);
        articleListRepo.save(articleListEntity);
        return mapper.createObjectNode().put("status", "success").toString();
    }

    @RequestMapping("/deleteArticle/{title}")
    public String deleteArticle(@PathVariable(value = "title") String title) {
        return articleListRepo.deleteByTitle(title)>0 ?
                mapper.createObjectNode().put("status", "success").toString() :
                mapper.createObjectNode().put("status", "fail").toString();
    }

    @RequestMapping("/viewArticle")
    public String viewArticle() {
        List<ArticleListEntity> listEntities = articleListRepo.findallSortByTime();
        ArrayNode arrayNode = mapper.createArrayNode();
        for (ArticleListEntity articleListEntity : listEntities) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("title", articleListEntity.getTitle());
            objectNode.put("tag", articleListEntity.getTag());
            objectNode.put("time", articleListEntity.getTime().toString().split(" ")[0]);
            objectNode.put("writer", articleListEntity.getWriter());
            arrayNode.add(objectNode);
        }
        ObjectNode root = mapper.createObjectNode();
        root.put("data", arrayNode);
        return root.toString();
    }

    @RequestMapping("/getArticle/{title}")
    public String getArticle(@PathVariable(value = "title") String title) {
        ArticleListEntity articleListEntity = articleListRepo.findByTitle(title);
        ObjectNode root = mapper.createObjectNode();
        root.put("title", articleListEntity.getTitle()).put("tag", articleListEntity.getTag())
                .put("writer", articleListEntity.getWriter()).put("imgUrl", articleListEntity.getImgUrl())
                .put("content", articleListEntity.getContent()).put("description", articleListEntity.getDescription());
        return root.toString();
    }

    @RequestMapping("/updateArticle/{title}")
    public String updateArticle(@PathVariable("title") String oldTitle,
                                @RequestBody HashMap<String,String> map) {
        int num = articleListRepo.updateWithOldTitle(map.get("title"), map.get("tag"), map.get("writer"),
                map.get("imgUrl"), map.get("content"), map.get("description"), oldTitle);
        return num > 0 ? mapper.createObjectNode().put("status", "success").toString() :
                mapper.createObjectNode().put("status", "fail").toString();
    }

    @RequestMapping("/article/{title}")
    public String articlePage(@PathVariable("title") String title) {
        List<ArticleListEntity> hotList = Logic.getHotArticles(articleListRepo);
        List<ArticleListEntity> recentList = Logic.getRecentArticles(articleListRepo);
        List<ArticleListEntity> randomList = Logic.getRandomArticles(articleListRepo);
        ArticleListEntity item = articleListRepo.findByTitle(title);
        ObjectNode root = mapper.createObjectNode();
        ObjectNode articleItem = mapper.convertValue(item, ObjectNode.class);
        articleItem.put("url", "/article/" + item.getTitle())
                .put("time", item.getTime().toString().split(" ")[0]);
        articleItem.remove("content");
        ArrayNode hot = Logic.returnSideBar(hotList);
        ArrayNode recent = Logic.returnSideBar(recentList);
        ArrayNode random = Logic.returnSideBar(randomList);
        root.put("articleListItem", articleItem);
        root.put("recentArticles", recent);
        root.put("randomArticles", random);
        root.put("hotArticles", hot);
        root.put("content", item.getContent());
        return root.toString();
    }

    @RequestMapping("/page/{page}")
    public String page(@PathVariable("page") Integer page) {
        System.out.println(page);
        Page<ArticleListEntity> articlePage = articleListRepo
                .findArticlePageByPage(PageRequest.of(page - 1, 10, Sort.by("time").descending()));
        int maxPage = articlePage.getTotalPages();
        ArrayNode arrayNode = mapper.createArrayNode();
        List<ArticleListEntity> list = articlePage.getContent();
        System.out.println(list.size());
        for (ArticleListEntity articleListEntity : list) {
            ObjectNode objectNode = mapper.convertValue(articleListEntity, ObjectNode.class);
            objectNode.remove("content");
            objectNode.put("url", "/article/" + articleListEntity.getTitle());
            objectNode.put("time", articleListEntity.getTime().toString().split(" ")[0]);
            arrayNode.add(objectNode);
        }
        ObjectNode root = mapper.createObjectNode();
        ArrayNode hot = Logic.returnSideBar(Logic.getHotArticles(articleListRepo));
        ArrayNode random = Logic.returnSideBar(Logic.getRandomArticles(articleListRepo));
        ArrayNode recent = Logic.returnSideBar(Logic.getRecentArticles(articleListRepo));
        root.set("articleList", arrayNode);
        root.set("recentArticles", recent);
        root.set("hotArticles", hot);
        root.set("randomArticles", random);
        root.put("maxPage", maxPage);
        return root.toString();
    }

    @RequestMapping(value = "/tag/{tag}")
    public String getTagArticles(@PathVariable(name = "tag") String tag,
                                 @RequestParam(name = "page") Integer page) {
        Page<ArticleListEntity> list = articleListRepo
                .findByArticleTagPageByPage(PageRequest.of(page - 1, 10, Sort.by("time").ascending()), tag);
        Integer maxPage = list.getTotalPages();
        List<ArticleListEntity> entityList = list.getContent();
        ArrayNode arrayNode = mapper.convertValue(entityList, ArrayNode.class);
        for(JsonNode node:arrayNode){
            ObjectNode objectNode = (ObjectNode) node;
            objectNode.remove("content");
            objectNode.put("url", "/article/" + objectNode.get("title").asText());
        }
        ArrayNode hot = Logic.getHotOrRecentOrRandom(articleListRepo, "hot");
        ArrayNode recent = Logic.getHotOrRecentOrRandom(articleListRepo, "recent");
        ArrayNode random = Logic.getHotOrRecentOrRandom(articleListRepo, "random");
        ObjectNode root = mapper.createObjectNode();
        root.set("articleList", arrayNode);
        root.put("maxPage", maxPage);
        root.set("hotArticles", hot);
        root.set("recentArticles", recent);
        root.set("randomArticles", random);
        return root.toString();
    }

    @RequestMapping(value = "/addReadTimes/{title}")
    public String addReadTimes(@PathVariable(name = "title") String title) {
        return articleListRepo.updateReadTimes(title) > 0
                ? mapper.createObjectNode().put("status", "success").toString() :
                mapper.createObjectNode().put("status", "fail").toString();
    }

    @RequestMapping("/addLikes/{title}")
    public String addLikes(@PathVariable("title") String title) {
        return articleListRepo.updateLikes(title) > 0
                ? mapper.createObjectNode().put("status", "success").toString() :
                mapper.createObjectNode().put("status", "fail").toString();
    }
    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public String ffff(@RequestBody Map<String,Object> map) {
        System.out.println(map.get("ff"));
        return "{\"dd\":\"dd\"}";
    }
}
