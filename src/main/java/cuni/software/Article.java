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

    public void addTerms(Collection<String> newTerms) {
        terms.addAll(newTerms);
    }

    public Multiset<String> getTerms() {
        return terms;
    }

    public int f(String term) {
        return terms.count(term);
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uri);
    }

    @Override
    public boolean equals(Object object) {
        boolean equals = false;
        if (object instanceof Article) {
            Article objectArticle = (Article) object;
            if (objectArticle.getId() == this.id && objectArticle.getUri() == this.uri)
                equals = true;
        }
        return equals;
    }
}