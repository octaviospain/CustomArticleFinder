package cuni.software;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.*;

import java.io.*;
import java.net.*;
import java.time.*;
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

    public URL getUrl() {
        return url;
    }

    public List<Article> getArticles() {
        return articles;
    }

    private void addArticlesFromFeed() throws IOException, FeedException {
        SyndFeed feed = new SyndFeedInput().build(new XmlReader(url));
        List<SyndEntry> feedEntries = feed.getEntries();
        articles.addAll(feedEntries.stream()
                                   .map(this::syndLinkToArticle)
                                   .collect(Collectors.toList()));
    }

    private Article syndLinkToArticle(SyndEntry syndEntry) {
        Article newArticle = new Article(syndEntry.getUri());
        addTermsToArticle(newArticle);
        int length = syndEntry.getPublishedDate().toString().length();
        int year = Integer.parseInt(syndEntry.getPublishedDate().toString().substring(length - 4, length));
        int month = syndEntry.getPublishedDate().getMonth() + 1;
        int day = Integer.parseInt(syndEntry.getPublishedDate().toString().substring(8, 10));
        newArticle.setPubDate(LocalDate.of(year, month, day));
        return newArticle;
    }

    /**
     * Traverses the HTML data of the article URI and adds the relevant
     * terms and tags to the instance.
     *
     * @param newArticle
     */
    private void addTermsToArticle(Article newArticle) {
        String uri = newArticle.getUri();
        Parser parser = new Parser();
        Multiset<String> articleTerms = parser.parseLink(uri);
        newArticle.addTerms(articleTerms);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("url", url).toString();
    }
}