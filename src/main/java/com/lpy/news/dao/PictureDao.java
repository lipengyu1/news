package com.lpy.news.dao;

import com.lpy.news.entity.Picture;

import java.util.ArrayList;
import java.util.List;

public interface PictureDao {
    void savePicture(Picture picture);

    void removeNotice(Long id);

    void updateNews(Picture picture);

    List<Picture> queryPicturePage(int pageNo, int pageSize, String divideName);

    int queryPictureCount(int pageNo, int pageSize, String divideName);

    ArrayList<Picture> showPicturePageByDiv(String divideName);

    Picture selectPictureById(Long id);
}
