package de.gainc.news;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class NewsItemContainer {

  public List<NewsItem> articles = new ArrayList<>();
}
