package com.toptech.toptech.Controller;


import com.toptech.toptech.Service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController()
public class NewsController {
    @Autowired
    NewsService newsService;

    @GetMapping("/")
    public String home(){
        return "bot is working";
    }
    @GetMapping("/news")
    public String getNews() {
        return newsService.getTop10News();
    }
}
