package com.lpy.news.service;

import com.lpy.news.common.BasePageResponse;
import com.lpy.news.entity.Picture;

import java.util.ArrayList;

public interface PictureService {
    void savePicture(Picture picture);

    void removePicture(Long[] ids);

    void updateNews(Picture picture);

    BasePageResponse<Picture> queryPicturePage(int pageNo, int pageSize, String divideName);

    ArrayList<Picture> showPicturePage(String divideName);
}
