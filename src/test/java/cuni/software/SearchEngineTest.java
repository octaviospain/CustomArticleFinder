package cuni.software;

import com.google.common.collect.*;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;


public class SearchEngineTest {

    SearchEngine searchEngine;
    Collection<Article> testArticles;

    private double getExpectedWeight(String term, Article article) {
        return getTf(term, article.f(term)) * getIdf(term);
    }

    private double getTf(String term, int freqInArticle) {
        return freqInArticle / getMaxFreq(term);
    }

    private int getMaxFreq(String term) {
        return testArticles.stream().map(a -> a.f(term)).max(Integer::compare).get();
    }

    private double getIdf(String term) {
        return Math.log(testArticles.size() / getDf(term)) / Math.log(2.0);
    }

    private long getDf(String term) {
        return testArticles.stream().filter(a -> a.f(term) > 0).count();
    }

    @Before
    public void beforeEach() {
        testArticles = new ArrayList<>();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void emptySearch() {
        searchEngine = new SearchEngine(Collections.emptyList());
        Article testArticle = new Article(0, "");
        searchEngine.getTermWeight("term", testArticle);
    }

    @Test
    public void singeArticleSingleTerm() {
        Article article = new Article(0, "");
        article.addTerms(Lists.newArrayList("cow"));
        testArticles.add(article);
        searchEngine = new SearchEngine(Collections.singleton(article));

        double expectedWeight = getExpectedWeight("cow", article);
        assertTrue(expectedWeight == searchEngine.getTermWeight("cow", article));
    }
}