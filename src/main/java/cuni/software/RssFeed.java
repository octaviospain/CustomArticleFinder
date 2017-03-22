package cuni.software;

import com.google.common.base.*;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.*;

public class RssFeed {

    private URL url;
    private List<Article> articles;

    public RssFeed(URL url) throws IOException, FeedException {
        this.url = url;
        articles = new ArrayList<>();
        addArticlesFromFeed();
    }

    private void addArticlesFromFeed() throws IOException, FeedException {
        SyndFeed feed = new SyndFeedInput().build(new XmlReader(url));
        List<SyndEntry> feedEntries = feed.getEntries();
        articles.addAll(feedEntries.stream()
                                   .map(this::syndLinkToArticle)
                                   .collect(Collectors.toList()));
    }

    private Article syndLinkToArticle(SyndEntry syndEntry) {
        return new Article(syndEntry.getUri());
    }

    public List<Article> getArticles() {
        return articles;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("url", url).toString();
    }
}