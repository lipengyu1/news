package com.lpy.news.service.impl;

import com.lpy.news.dao.NewsDao;
import com.lpy.news.dao.NewsLikeDao;
import com.lpy.news.dao.UserDao;
import com.lpy.news.dto.NewsUserRecommendDto;
import com.lpy.news.service.UserCFNewsRecommendSerevice;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserCFNewsRecommendServiceImpl implements UserCFNewsRecommendSerevice {
    @Autowired
    private RedisServiceImpl redisService;
    @Autowired
    UserDao userDao;
    @Autowired
    NewsDao newsDao;
    @Autowired
    NewsLikeDao newsLikeDao;

    /**
     * 个性化新闻推荐
     * @param userId
     * @return
     */
    @Override
    public ArrayList<NewsUserRecommendDto> RecommendTopic(Long userId){
        List<Long> userList = userDao.getAllUserIdList();
        Integer N=userList.size();
        Long[][] sparseMatrix=new Long[N][N];//建立用户稀疏矩阵，用于用户相似度计算【相似度矩阵】
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++)
                sparseMatrix[i][j]=(long)0;
        }
        Map<Long, Integer> userItemLength = new HashMap<>();//存储每一个用户对应的不同物品总数  eg: A 3
        Map<Long, Set<Long>> itemUserCollection = new HashMap<>();//建立物品到用户的倒排表 eg: a A B
        Set<Long> items = new HashSet<>();//辅助存储物品集合
        Map<Long, Integer> userID = new HashMap<>();//辅助存储每一个用户的用户ID映射:user->id
        Map<Integer, Long> idUser = new HashMap<>();//辅助存储每一个ID对应的用户映射:id->user
        for(int i=0;i<N;i++){//依次处理N个用户
            Long user=userList.get((int)i);
//            从redis中获取用户历史浏览记录
            List<Long> itemlist=redisService.getNewsIdlistByUserId(user);
//            System.out.println("UserID:"+user+" 阅读过："+itemlist);
            userItemLength.put(user,itemlist.size());
            //用户ID与稀疏矩阵建立对应关系
            userID.put(user,i);
            idUser.put(i,user);

            //建立物品--用户倒排表
            for(int j=0;j<itemlist.size();j++){
               Long topic=itemlist.get(j);
                if(items.contains(topic)){//如果已经包含对应的物品--用户映射，直接添加对应的用户
                    itemUserCollection.get(topic).add(user);
                }else{//否则创建对应物品--用户集合映射
                    items.add(topic);
                    //创建物品--用户倒排关系
                    itemUserCollection.put(topic,new HashSet<Long>());
                    itemUserCollection.get(topic).add(user);
                }
            }
        }

        //输出倒排关系表
//        System.out.println("输出倒排关系表：\n"+itemUserCollection.toString());
        //计算相似度矩阵【稀疏】
        Set<Map.Entry<Long, Set<Long>>> entrySet = itemUserCollection.entrySet();
        Iterator<Map.Entry<Long, Set<Long>>> iterator = entrySet.iterator();
        while (iterator.hasNext()){
            Set<Long> commonUsers=iterator.next().getValue();
            for(Long user_u:commonUsers){
                for(Long user_v:commonUsers){
                    if(user_u==user_v){
                        continue;
                    }
                    //计算用户u与用户v都有正反馈的物品总数
                    sparseMatrix[userID.get(user_u)][userID.get(user_v)]+=1;
                }
            }
        }
        //计算用户之间的相似度【余弦相似性】
        Integer recommendUserId = userID.get(userId);
        for(int j=0;j<sparseMatrix.length;j++){
            if(j!=recommendUserId){
                System.out.println(idUser.get(recommendUserId)+"--"+idUser.get(j)+"相似度:"
                        +sparseMatrix[recommendUserId][j]/Math.sqrt(userItemLength.get(idUser.get(recommendUserId))*userItemLength.get(idUser.get(j))));
            }
        }
        //计算指定用户recommendUser的物品推荐度
        Map<Long,Double> itemRecommendDegree=new HashMap<>();//topic->推荐度
        for(Long item:items){//遍历每一件物品
            //得到购买当前物品的所有用户集合
            Set<Long> users=itemUserCollection.get(item);
            //如果被推荐用户没有购买当前物品，则进行推荐度计算
            if(!users.contains(userId)){
                double RecommendDegree = 0.0;
                for(Long user:users){
                    //推荐度计算
                    RecommendDegree+=sparseMatrix[userID.get(userId)][userID.get(user)]/Math.sqrt(userItemLength.get(userId)*userItemLength.get(user));
                }
                itemRecommendDegree.put(item,RecommendDegree);
            }
        }
//        System.out.println("newsId,推荐度\n"+itemRecommendDegree);
        //取推荐度最大的5个
        if(itemRecommendDegree.size()<5){
            List<Long> list = itemRecommendDegree.entrySet().stream()
                    .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                    .map(entry -> entry.getKey()).collect(Collectors.toList())
                    .subList(0, itemRecommendDegree.size());
            List<Long> list1=newsDao.getAllNewsIdList();//获取所有新闻的id
            for(int i=0;i<list1.size();i++){
                if(list.size()==5)
                    break;
                if(list.contains(list1.get(i)))
                    continue;
                list.add(list1.get(i));
            }
//            System.out.println(list);
            ArrayList<NewsUserRecommendDto> recommendDtosList = new ArrayList<>();
            for (Long aLong : list) {
                NewsUserRecommendDto newsUserRecommendDto = newsDao.findByTopicIdIn(aLong);
                Integer num = newsLikeDao.selectNewsCountLike(aLong);
                newsUserRecommendDto.setLikeCount(num);
                recommendDtosList.add(newsUserRecommendDto);
            }
            return recommendDtosList;
        }else {
            List<Long> list = itemRecommendDegree.entrySet().stream()
                    .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                    .map(entry -> entry.getKey()).collect(Collectors.toList())
                    .subList(0, 5);
//            System.out.println(list);
            ArrayList<NewsUserRecommendDto> recommendDtosList = new ArrayList<>();
            for (Long aLong : list) {
                NewsUserRecommendDto newsUserRecommendDto = newsDao.findByTopicIdIn(aLong);
                Integer num = newsLikeDao.selectNewsCountLike(aLong);
                newsUserRecommendDto.setLikeCount(num);
                recommendDtosList.add(newsUserRecommendDto);
            }
            return recommendDtosList;
        }
    }
}
