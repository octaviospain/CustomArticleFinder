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
        addTermsAndTagsToArticle(newArticle);
        return newArticle;
    }

    /**
     * Traverses the HTML data of the article URI and adds the relevant
     * terms and tags to the instance.
     *
     * @param newArticle
     */
    private void addTermsAndTagsToArticle(Article newArticle) {
        String uri = newArticle.getUri();
        Set<String> articleTerms = new HashSet<>();
        Set<String> articleTags = new HashSet<>();

        /**
         * // TODO
         * Find a way to get the html data of the article and traverse it
         * until find the relevant content and add each term to the article instance,
         * as well as relevant tags that categorize the article itself
         */

        newArticle.addTerms(articleTerms);
        newArticle.addTags(articleTags);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("url", url).toString();
    }
}