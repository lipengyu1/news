package com.lpy.news.service;

import com.lpy.news.dto.NewsUserHistoryDto;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface HistoryService {
    void saveHistory(Long userId, LocalDateTime date, Long id);

    void delHistory(Long userId, Long newsId);

    ArrayList<NewsUserHistoryDto> queryHistory(Long userId, Long newsId);
}
