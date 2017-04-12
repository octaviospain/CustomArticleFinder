package cuni.software;

import java.util.*;

/**
 * This class isolates the behaviour of a Search Engine that uses a vector model approach
 * in order to store and find {@link Article} instances given string query.
 *
 * @author Octavio Calleya
 */
public class SearchEngine {

    private Collection<Article> articles;
    private Map<String, TermStatistics> termStatistics;
    private Map<String, Double> tfs;

    public SearchEngine(Collection<Article> initialArticles) {
        articles = initialArticles;
        termStatistics = new HashMap<>();
        tfs = new HashMap<>();
        computeStatistics(articles);
    }

    public void addArticles(Collection<Article> newArticles) {
        articles.addAll(newArticles);
        computeStatistics(newArticles);
    }

    public List<Article> findRelatedArticles(String query) {
        throw new UnsupportedOperationException("Unimplemented yet");
    }

    private void computeStatistics(Collection<Article> articles) {
        articles.parallelStream().forEach(article -> article.getTerms().forEach(term -> {
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