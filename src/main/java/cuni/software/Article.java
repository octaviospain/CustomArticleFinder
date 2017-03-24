package cuni.software;

import com.google.common.collect.*;

import java.util.*;

public class Article {

    private int id;
    private String uri;
    private Set<String> tags;
    private Multiset<String> terms;

    public Article(int id, String uri) {
        this.id = id;
        this.uri = uri;
        tags = new HashSet<>();
        terms = HashMultiset.create();
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

    public void addTerm(String term) {
        terms.add(term);
    }

    public int f(String term) {
        return 0;
    }

    public int getId() {
        return id;
    }
}