package cuni.software;

import java.net.*;
import java.util.*;

public class Article {

    private URL url;
    private Set<String> tags;

    public Article(URL url) {
        this.url = url;
        tags = new HashSet<>();
    }

    public URL getUrl() {
        return url;
    }

    public void addTags(Set<String> newTags) {
        tags.addAll(newTags);
    }

    public Set<String> getTags() {
        return tags;
    }
}