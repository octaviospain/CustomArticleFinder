package cuni.software;

import java.util.*;

public class SearchEngine {

    private List<RssFeed> rssFeeds;
    private List<Article> articles;
    private Map<String, Double> idfs;
    private Map<String, Double> tfs;

    public SearchEngine(List<RssFeed> rssFeeds) {
        this.rssFeeds = rssFeeds;
        articles = new ArrayList<>();
        idfs = new HashMap<>();
        tfs = new HashMap<>();
    }

    public void addRssFeeds(Collection<RssFeed> newRssFeeds) {
        rssFeeds.addAll(newRssFeeds);
    }

    public List<RssFeed> getRssFeeds() {
        return rssFeeds;
    }

    private int numberOfArticlesContainingTerm(String term) {
        return 0;
    }

    private double getInverseArticleTermFrequency(String term) {
        return 0.0;
    }

    

    private double getTermWeight(String term, Article article) {
        return tfs.get(article.getId() + term) * idfs.get(term);
    }
}