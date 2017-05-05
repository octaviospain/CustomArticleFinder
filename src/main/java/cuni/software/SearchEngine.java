package cuni.software;

import java.util.*;
import java.util.concurrent.*;

/**
 * This class isolates the behaviour of a Search Engine that uses a vector model approach
 * in order to store and find {@link Article} instances given string query.
 *
 * @author Octavio Calleya
 */
public class SearchEngine {

    private List<Article> articles;
    private Map<String, TermStatistics> termStatistics;
    private Map<String, Double> tfs;

    public SearchEngine() {
        articles = new ArrayList<>();
        termStatistics = new HashMap<>();
        tfs = new HashMap<>();
    }

    public void addArticles(Collection<Article> newArticles) {
        articles.addAll(newArticles);
        computeStatistics(newArticles);
    }

    public List<Article> findRelatedArticles(String query) {
        Article queryArticle = new Article("");
        Set<String> queryTerms = new HashSet<>();
        StringTokenizer stk = new StringTokenizer(query, " ");
        while (stk.hasMoreElements())
            queryTerms.add(stk.nextToken().toLowerCase());
        queryArticle.addTerms(queryTerms);

        ForkJoinPool forkJoinPool = new ForkJoinPool(6);
        RecursiveSearch recursiveSearch = new RecursiveSearch(queryArticle, articles, termStatistics.keySet());
        return forkJoinPool.invoke(recursiveSearch);
    }

    private void computeStatistics(Collection<Article> articles) {
        articles.forEach(article -> article.getTerms().forEach(term -> {
            updateTermStatistics(term);
            tfs.put(article.getId() + term, computeNormalizedTermFrequency(term, article));
        }));
    }

    private void updateTermStatistics(String term) {
        TermStatistics stats;
        if (! termStatistics.containsKey(term)) {
            stats = new TermStatistics(term);
            termStatistics.put(term, stats);
        }
        else
            stats = termStatistics.get(term);
        stats.incrementDocumentFrequency();
        stats.setInverseDocumentFrequency(computeIdf(stats));
        stats.incrementHighestFrequency();
    }

    private double computeNormalizedTermFrequency(String term, Article article) {
        int frequencyInArticle = article.f(term);
        long highestFreq = termStatistics.get(term).getHighestFrequency();
        return frequencyInArticle / highestFreq;
    }

    /**
     * Inverse document frequency of term (IDF)
     *
     * @param stats An instance of {@link TermStatistics} containing data of the given term
     *
     * @return The value of the IDF
     *
     * @see <a href="https://en.wikipedia.org/wiki/Tf%E2%80%93idf">TF-IDF</a>
     */
    private double computeIdf(TermStatistics stats) {
        int numberOfArticles = articles.size();
        return Math.log(numberOfArticles / stats.getDocumentFrequency()) / Math.log(2.0);
    }

    /**
     * The weight of a term for a given {@link Article}
     *
     * @param term    The given term from wich to get the weight
     * @param article The given {@code Article}
     *
     * @return The term weight
     *
     * @see <a href="https://en.wikipedia.org/wiki/Tf%E2%80%93idf">TF-IDF</a>
     */
    protected double getTermWeight(String term, Article article) {
        if (! articles.contains(article))
            throw new UnsupportedOperationException("Search engine doesn't contain that article");
        double termFrequency = tfs.get(article.getId() + term);
        double inverseDocumentFrequency = termStatistics.get(term).getInverseDocumentFrequency();
        return termFrequency * inverseDocumentFrequency;
    }
}