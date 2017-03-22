package cuni.software;

import java.util.*;

public class SearchEngine {

    private List<RssFeed> rssFeeds;

    public SearchEngine(List<RssFeed> rssFeeds) {
        this.rssFeeds = rssFeeds;
    }

    public void addRssFeeds(Collection<RssFeed> newRssFeeds) {
        rssFeeds.addAll(newRssFeeds);
    }

    public List<RssFeed> getRssFeeds() {
        return rssFeeds;
    }
}