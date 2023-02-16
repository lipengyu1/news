package com.lpy.news.news;

import com.lpy.news.dao.NewsLikeDao;
import com.lpy.news.entity.NewsLikeDB;
import com.lpy.news.service.SnowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;
import java.time.LocalDateTime;

@SpringBootTest
public class NewsAddLikeTest {
    @Autowired
    NewsLikeDao newsLikeDao;

    public Long addLikeUserId() throws SQLException, ClassNotFoundException {
        String sql1 = "select id user_id from user order by rand() limit 10";
        String user = "root";
        String password = "123456";
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/news?serverTimezone=GMT%2B8&characterEncoding=utf-8&useAffectedRows=true";
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql1);
        Long userId = null;
        while (resultSet.next()){//让光标后移，如果没有更多行，返回false
            userId = resultSet.getLong(1);//获取该行第一列
        }
        statement.close();
        connection.close();
        return userId;
    }
    public Long addLikeNewsId() throws SQLException, ClassNotFoundException {
        String sql2 = "select id news_id from news order by rand() limit 10;";
        String user = "root";
        String password = "123456";
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/news?serverTimezone=GMT%2B8&characterEncoding=utf-8&useAffectedRows=true";
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql2);
        Long newsId = null;
        while (resultSet.next()){//让光标后移，如果没有更多行，返回false
            newsId = resultSet.getLong(1);//获取该行第一列
        }
        statement.close();
        connection.close();
        return newsId;
    }

    /**
     * 测试数据添加
     * @throws SQLException
     * @throws ClassNotFoundException
     */

    @Test
    public void addLike() throws SQLException, ClassNotFoundException {
        SnowService snowService = new SnowService(1, 1);
        for (int i = 0; i < 300; i++) {
            Long userId = addLikeUserId();
            Long NewsId = addLikeNewsId();
            NewsLikeDB newsLikeDB = new NewsLikeDB();
            newsLikeDB.setId(snowService.getId());
            newsLikeDB.setUserId(userId);
            newsLikeDB.setNewsId(NewsId);
            newsLikeDB.setState(1);
            newsLikeDB.setUpdateTime(LocalDateTime.now());
            newsLikeDao.saveNewsLike(newsLikeDB);
        }
    }
}
