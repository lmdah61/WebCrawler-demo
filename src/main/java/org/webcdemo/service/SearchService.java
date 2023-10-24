

package org.webcdemo.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.webcdemo.model.SearchRequest;
import org.webcdemo.model.SearchResults;
import org.webcdemo.util.SearchIdGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SearchService {
    private Map<String, SearchResults> searches = new ConcurrentHashMap<>();

    public SearchResults startSearch(SearchRequest request, String baseUrl) {
        String id = SearchIdGenerator.generateId();
        searches.put(id, new SearchResults(id, "active", new CopyOnWriteArrayList<>()));

        // web crawling logic based on the baseUrl and request.keyword
        // Update the search results asynchronously
        startCrawl(id, baseUrl, request.getKeyword());

        return searches.get(id);
    }

    public SearchResults getSearchResults(String id) {
        return searches.get(id);
    }

    private void startCrawl(String id, String baseUrl, String keyword) {
        new Thread(() -> {
            try {
                List<String> visitedUrls = new ArrayList<>();
                List<String> toVisitUrls = new ArrayList<>();
                toVisitUrls.add(baseUrl);

                while (!toVisitUrls.isEmpty()) {
                    if (visitedUrls.size() >= 100) {
                        // Limit the number of results as per the project specification
                        break;
                    }

                    String url = toVisitUrls.remove(0);
                    visitedUrls.add(url);

                    try {
                        Document doc = Jsoup.connect(url).get();
                        Elements links = doc.select("a[href]");

                        for (Element link : links) {
                            String absoluteUrl = link.attr("abs:href");
                            if (absoluteUrl.startsWith(baseUrl) && !visitedUrls.contains(absoluteUrl) && !toVisitUrls.contains(absoluteUrl)) {
                                toVisitUrls.add(absoluteUrl);
                            }
                        }

                        String htmlContent = doc.html();
                        if (htmlContent.toLowerCase().contains(keyword.toLowerCase())) {
                            searches.get(id).getUrls().add(url);
                        }
                    } catch (IOException e) {
                        // Handle connection or parsing errors
                        e.printStackTrace();
                    }
                }

                searches.get(id).setStatus("done");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}

