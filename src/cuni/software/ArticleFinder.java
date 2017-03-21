package cuni.software;

import java.util.*;

public class ArticleFinder {

    private SearchEngine searchEngine;
    private String query;

    public ArticleFinder(String query, SearchEngine searchEngine) {
        this.query = query;
        this.searchEngine = searchEngine;
    }

    public Set<Article> findRelatedArticles() {
        return null;
    }
}