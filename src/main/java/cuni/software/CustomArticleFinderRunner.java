package cuni.software;

import org.slf4j.*;

import java.io.*;
import java.util.*;

public class CustomArticleFinderRunner {

    private static final Logger LOG = LoggerFactory.getLogger(CustomArticleFinderRunner.class.getName());

    private final static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("****\tCustom Article Finder\t****");
        String query = "";

        try {
            List<RssFeed> loadedRssFeeds = RssFeedParse.fromFile(new File(args[0]));
            System.out.println("\n\tLoaded rss feeds:");
            loadedRssFeeds.forEach(System.out::println);
            SearchEngine searchEngine = new SearchEngine(Collections.emptyList());

            while (! query.equalsIgnoreCase("exit")) {
                System.out.println("\n\tEnter the query in order to find related articles. Type 'exit' to quit.");
                query = input.nextLine();

                ArticleFinder articleFinder = new ArticleFinder(query, searchEngine);
                articleFinder.findRelatedArticles().forEach(article -> System.out.println(article.getUri()));
            }
        }
        catch (Exception exception) {
            LOG.error("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}