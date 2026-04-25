package com.toptech.toptech.Service;

import com.toptech.toptech.model.NewsModel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NewsService {

    private static final String TOP_STORIES_URL =
            "https://hacker-news.firebaseio.com/v0/topstories.json";
    private static final String ITEM_URL =
            "https://hacker-news.firebaseio.com/v0/item/{id}.json";

    private final RestTemplate restTemplate = new RestTemplate();

    public String getTop10News() {
        int[] ids = restTemplate.getForObject(TOP_STORIES_URL, int[].class);

        if (ids == null || ids.length == 0) return "Could not fetch news right now.";

        StringBuilder sb = new StringBuilder("📰 *Top 10 Tech News — Hacker News*\n\n");

        int count = 0;
        for (int i = 0; i < ids.length && count < 10; i++) {
            NewsModel item = restTemplate.getForObject(ITEM_URL, NewsModel.class, ids[i]);

            if (item == null || item.title == null) continue;

            sb.append(count + 1).append(". *").append(item.title).append("*\n");

            if (item.url != null && !item.url.isEmpty()) {
                sb.append("🔗 ").append(item.url).append("\n");
            } else {
                sb.append("🔗 https://news.ycombinator.com/item?id=").append(ids[i]).append("\n");
            }

            sb.append("⬆️ Score: ").append(item.score).append("\n\n");
            count++;
        }

        return sb.toString();
    }
}