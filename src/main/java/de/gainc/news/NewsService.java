package de.gainc.news;

import java.util.List;

public interface NewsService {

  List<NewsItem> findNews(int count);
}
