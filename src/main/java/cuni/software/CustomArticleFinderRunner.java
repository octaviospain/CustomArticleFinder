package cuni.software;

import org.slf4j.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class CustomArticleFinderRunner {

    private static final Logger LOG = LoggerFactory.getLogger(CustomArticleFinderRunner.class.getName());

    private final static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("****\tCustom Article Finder\t****");
        String query = "";

        try {
            List<RssFeed> loadedRssFeeds = RssFeedParse.fromFile(new File(args[0]));
            System.out.println("\n\t\tLoaded rss feeds:");
            loadedRssFeeds.forEach(feed -> {
                System.out.println("\n\tLoaded articles:");
                feed.getArticles().forEach(System.out::println);
            });

            List<Article> allArticles = loadedRssFeeds.stream()
                                                      .flatMap(feed -> feed.getArticles().stream())
                                                      .collect(Collectors.toList());

            SearchEngine searchEngine = new SearchEngine(allArticles);

            while (! query.equalsIgnoreCase("exit")) {
                System.out.println("\n\tEnter the query in order to find related articles. Type 'exit' to quit.");
                query = input.nextLine();

                searchEngine.findRelatedArticles(query).forEach(article -> System.out.println(article.getUri()));
            }
        }
        catch (Exception exception) {
            LOG.error("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}