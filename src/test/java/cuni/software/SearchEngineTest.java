package cuni.software;

import com.google.common.collect.*;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class SearchEngineTest {

    private SearchEngine searchEngine;
    private Collection<Article> testArticles = new ArrayList<>();
    private Random random = new Random();

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

    private String getRandomString() {
        String str = "";
        for (int i = 0; i < 4; i++) {
            char c = (char) ('A' + random.nextInt('Z' - 'A'));
            str += c;
        }
        return str;
    }

    @Before
    public void beforeEach() {
        testArticles.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void emptySearch() {
        searchEngine = new SearchEngine();
        searchEngine.addArticles(Collections.emptyList());
        Article testArticle = new Article("");
        searchEngine.getTermWeight("term", testArticle);
    }

    @Test
    public void singleArticleSingleTerm() {
        Article article = new Article("");
        article.addTerms(Lists.newArrayList("cow"));
        testArticles.add(article);
        searchEngine = new SearchEngine();
        searchEngine.addArticles(testArticles);

        double expectedWeight = getExpectedWeight("cow", article);
        assertTrue(expectedWeight == searchEngine.getTermWeight("cow", article));
    }

    @Test
    public void singleArticleTwoTerms() {
        Article article = new Article("");
        article.addTerms(Lists.newArrayList("cow", "dog"));
        testArticles.add(article);
        searchEngine = new SearchEngine();
        searchEngine.addArticles(testArticles);

        double expectedWeight = getExpectedWeight("cow", article);
        assertTrue(expectedWeight == searchEngine.getTermWeight("cow", article));

        expectedWeight = getExpectedWeight("dog", article);
        assertTrue(expectedWeight == searchEngine.getTermWeight("dog", article));
    }

    @Test
    public void twoArticlesSingleTerm() {
        Article article = new Article("");
        article.addTerms(Lists.newArrayList("cow"));
        Article article2 = new Article("");
        article2.addTerms(Lists.newArrayList("cow"));

        testArticles.add(article);
        testArticles.add(article2);
        searchEngine = new SearchEngine();
        searchEngine.addArticles(testArticles);

        double expectedWeight = getExpectedWeight("cow", article);
        assertTrue(expectedWeight == searchEngine.getTermWeight("cow", article));
    }

    @Test
    public void twoArticlesTwoSameTerms() {
        Article article = new Article("");
        article.addTerms(Lists.newArrayList("cow", "dog"));
        Article article2 = new Article("");
        article2.addTerms(Lists.newArrayList("cow", "dog"));

        testArticles.add(article);
        testArticles.add(article2);
        searchEngine = new SearchEngine();
        searchEngine.addArticles(testArticles);

        double expectedWeight = getExpectedWeight("cow", article);
        assertTrue(expectedWeight == searchEngine.getTermWeight("cow", article));


        expectedWeight = getExpectedWeight("dog", article);
        assertTrue(expectedWeight == searchEngine.getTermWeight("dog", article));
    }

    @Test
    public void twoArticlesRandomTerms() {
        Article article = new Article("");
        List<String> terms = new ArrayList<>();
        for (int i = 0; i < 5000; i++)
            terms.add(getRandomString());
        article.addTerms(terms);

        Article article2 = new Article("");
        terms = new ArrayList<>();
        String lastTerm = "";
        for (int i = 0; i < 5000; i++) {
            lastTerm = getRandomString();
            terms.add(lastTerm);
        }
        article2.addTerms(terms);

        testArticles.add(article);
        testArticles.add(article2);
        searchEngine = new SearchEngine();
        searchEngine.addArticles(testArticles);

        double expectedWeight = getExpectedWeight(lastTerm, article2);
        assertTrue(expectedWeight == searchEngine.getTermWeight(lastTerm, article2));
    }
}