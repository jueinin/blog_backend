package blog.chen.blog.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "article_list", schema = "blog", catalog = "")
public class ArticleListEntity {
    private String title;
    private Integer comments=0;
    private String description;
    private String imgUrl;
    private Integer likes=0;
    private Integer readTimes=0;
    private String tag;
    private Timestamp time=new Timestamp(System.currentTimeMillis());
    private String writer;
    private String content;

    public ArticleListEntity(String title, String tag, String writer, String imgUrl, String content,String description) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.tag = tag;
        this.writer = writer;
        this.content = content;
        this.description = description;
    }

    public ArticleListEntity() {
    }

    @Id
    @Column(name = "title", nullable = false, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "comments", nullable = true)
    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 400)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "img_url", nullable = true, length = 255)
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Basic
    @Column(name = "likes", nullable = true)
    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    @Basic
    @Column(name = "read_times", nullable = true)
    public Integer getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(Integer readTimes) {
        this.readTimes = readTimes;
    }

    @Basic
    @Column(name = "tag", nullable = true, length = 50)
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Basic
    @Column(name = "time", nullable = true)
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "writer", nullable = true, length = 30)
    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    @Basic
    @Column(name = "content", nullable = true, length = 10000)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleListEntity that = (ArticleListEntity) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(comments, that.comments) &&
                Objects.equals(description, that.description) &&
                Objects.equals(imgUrl, that.imgUrl) &&
                Objects.equals(likes, that.likes) &&
                Objects.equals(readTimes, that.readTimes) &&
                Objects.equals(tag, that.tag) &&
                Objects.equals(time, that.time) &&
                Objects.equals(writer, that.writer) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, comments, description, imgUrl, likes, readTimes, tag, time, writer, content);
    }
}
