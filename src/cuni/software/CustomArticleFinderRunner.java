package cuni.software;

import java.util.*;

public class CustomArticleFinderRunner {

    private final static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("****\tCustom Article Finder\t****");
        String query = "";

        // Here the program loads the rss feeds from a given file
        List<RssFeed> loadedRssFeeds = RssFeedParse.fromFile(null);
        SearchEngine searchEngine = new SearchEngine(loadedRssFeeds);

        while (! query.equalsIgnoreCase("exit")) {
            System.out.println("Enter the query in order to find related articles. Type 'exit' to quit.");
            query = input.nextLine();

            ArticleFinder articleFinder = new ArticleFinder(query, searchEngine);
            articleFinder.findRelatedArticles().forEach(article -> System.out.println(article.getUrl()));
        }
    }
}