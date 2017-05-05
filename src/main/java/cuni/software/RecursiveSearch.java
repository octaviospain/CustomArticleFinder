package cuni.software;

import com.google.common.collect.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author Octavio Calleya
 */
public class RecursiveSearch extends RecursiveTask<List<Article>> {

    private static final int MAX_COMPARING_ARTICLES = 10;
    private static final int NUMBER_OF_PARTITIONS = 6;
    private static final double SIMILARITY_THRESHOLD = 1.514;

    private Article queryArticle;
    private Set<String> terms;
    private List<Article> articles;
    private List<Article> foundArticles = new ArrayList<>();

    public RecursiveSearch(Article queryArticle, List<Article> articles, Set<String> terms) {
        this.queryArticle = queryArticle;
        this.terms = terms;
        this.articles = articles;
    }

    @Override
    protected List<Article> compute() {
        if (articles.size() > MAX_COMPARING_ARTICLES)
            forkIntoSubActions();
        else {
            articles.forEach(article -> {
                double cosineSimilarity = cosineSimilarity(queryArticle, article);
                if (cosineSimilarity < SIMILARITY_THRESHOLD)
                    foundArticles.add(article);
            });
        }
        return foundArticles;
    }

    private void forkIntoSubActions() {
        int subListsSize = articles.size() / NUMBER_OF_PARTITIONS;
        List<RecursiveSearch> subSearches = Lists.partition(articles, subListsSize)
                                                .stream()
                                                .map(articlesSubList -> new RecursiveSearch(queryArticle, articlesSubList, terms))
                                                .collect(ImmutableList.toImmutableList());
        subSearches.forEach(RecursiveTask::fork);
        subSearches.forEach(action -> foundArticles.addAll(action.join()));
    }

    private double cosineSimilarity(Article query, Article other) {
        double numeratorSum = 0.0;
        for (String term: terms)
            numeratorSum += other.f(term) * query.f(term);

        double sumQueryW = 0.0;
        for (String term: terms)
            sumQueryW += Math.pow(query.f(term), 2);

        double sumOtherW = 0.0;
        for (String term: terms)
            sumOtherW += Math.pow(other.f(term), 2);

        double denominator = Math.sqrt(sumQueryW * sumOtherW);

        return Math.acos(numeratorSum / denominator);
    }
}