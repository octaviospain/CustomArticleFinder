package cuni.software;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.*;
import com.rometools.rome.io.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Objects;
import java.util.stream.*;

public class RssFeedParse {

    public static List<RssFeed> fromFile(File file) throws IOException {
        ImmutableList<String> lines = Files.asCharSource(file, Charsets.UTF_8).readLines();
        return lines.stream()
                    .map(RssFeedParse::createRssFeedFromUrl)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
    }

    private static RssFeed createRssFeedFromUrl(String url) {
        RssFeed rssFeed = null;
        try {
            rssFeed = new RssFeed(new URL(url));
        }
        catch (IOException | FeedException exception) {
            System.err.println("Invalid url: " + url);
        }
        return rssFeed;
    }
}