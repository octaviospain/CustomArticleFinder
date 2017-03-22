package cuni.software;

import java.util.*;

public class Article {

    private String uri;
    private Set<String> tags;

    public Article(String uri) {
        this.uri = uri;
        tags = new HashSet<>();
    }

    public String getUri() {
        return uri;
    }

    public void addTags(Set<String> newTags) {
        tags.addAll(newTags);
    }

    public Set<String> getTags() {
        return tags;
    }
}