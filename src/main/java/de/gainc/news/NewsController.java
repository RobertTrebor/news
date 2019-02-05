package de.gainc.news;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/news")
public class NewsController {

  private NewsService newsService;

  public NewsController(NewsService newsService){
    this.newsService = newsService;
  }

  @GetMapping
  public String showRecentNews(Model model,
      @RequestParam(name = "count", defaultValue = "5") int count){
    List<NewsItem> newsItems = newsService.findNews(count);
    model.addAttribute("news", newsItems);
    return "news";
  }

}
