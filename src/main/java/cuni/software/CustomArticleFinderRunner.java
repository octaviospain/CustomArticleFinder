package cuni.software;

import org.slf4j.*;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

public class CustomArticleFinderRunner {

    private static final Logger LOG = LoggerFactory.getLogger(CustomArticleFinderRunner.class.getName());

    private final static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("****\tCustom Article Finder\t****");
        String query = "";

        if (args.length == 0) {
            LOG.error("The first argument should be the file with RSS feeds.");
            return;
        }

        List<RssFeed> loadedRssFeeds;
        try {
            loadedRssFeeds = RssFeedParse.fromFile(new File(args[0]));
        } catch (IOException e) {
            LOG.error("The first argument should be the file with RSS feeds.", e);
            return;
        }

        SearchEngine searchEngine;
        try {
            System.out.println("\n\t\tLoaded rss feeds:");
            loadedRssFeeds.forEach(feed -> {
                System.out.println("\t\t" + feed.getUrl().toString() + "\n\tLoaded articles:");
                feed.getArticles().forEach(System.out::println);
            });

            List<Article> allArticles = loadedRssFeeds.stream()
                                                      .flatMap(feed -> feed.getArticles().stream())
                                                      .collect(Collectors.toList());

            searchEngine = new SearchEngine();
            searchEngine.addArticles(allArticles);
        }
        catch (Exception e) {
            LOG.error("Error during loading of articles:", e);
            return;
        }

        try {
            menu();
            query = input.nextLine();

            while (! query.equalsIgnoreCase("exit")) {

                if (query.equalsIgnoreCase("menu")) {
                    menu();
                } else if (query.startsWith("search query ")) {
                    try {
                        List<Article> relatedArticles = searchEngine.findRelatedArticles(query.substring(13));
                        System.out.println("Found " + relatedArticles.size() + " articles");
                        relatedArticles.forEach(article -> System.out.println(article.getUri()));
                    }
                    catch (Exception e) {
                        LOG.error("Error during finding:", e);
                    }
                } else if (query.startsWith("search date ")) {
                    String[] dates = query.substring(12).split("/");
                    try {
                        LocalDate date = LocalDate.of(Integer.parseInt(dates[2]), Integer.parseInt(dates[1]), Integer.parseInt(dates[0]));
                        try {
                            //searchEngine.findRelatedArticles(date).forEach(article -> System.out.println(article.getUri()));
                        }
                        catch (Exception e) {
                            LOG.error("Error during finding:", e);
                        }
                    }
                    catch (Exception e) {
                        LOG.error("Error during parsing of date:", e);
                    }
                } else {
                    System.out.println("The command '" + query + "' is not valid.");
                    menu();
                }

                query = input.nextLine();
            }
        }
        catch (Exception exception) {
            LOG.error("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private static void menu() {
        System.out.println("\n\tYou can type one of the following commands:");
        System.out.println("'search query YOUR_QUERY' to find articles related to given query.");
        System.out.println("'search date DD/MM/YYYY' to find articles since given date.");
        System.out.println("'menu' to show the list of possible commands.");
        System.out.println("'exit' to quit the program.");
    }
}