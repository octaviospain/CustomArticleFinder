package cuni.software;

import com.google.common.base.*;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.*;

import cuni.software.Article;
import cuni.software.Parser;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
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
        newArticle.setPubDate(LocalDate.parse(syndEntry.getPublishedDate().toString()));
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
        Set<String> articleTerms = parser.parseLink(uri);    
        newArticle.addTerms(articleTerms);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("url", url).toString();
    }
}