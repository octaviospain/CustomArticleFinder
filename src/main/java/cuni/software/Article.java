package cuni.software;

import com.google.common.collect.*;

import java.time.*;
import java.util.*;

public class Article {

    private static int articleSequenceId = 0;

    private int id;
    private String uri;
    private Multiset<String> terms;
    private LocalDate pubDate;

	public Article(String uri) {
        id = ++ articleSequenceId;
        this.uri = uri;
        terms = HashMultiset.create();
    }

    public String getUri() {
        return uri;
    }

    public LocalDate getPubDate() {
        return pubDate;
    }

    public void setPubDate(LocalDate pubDate) {
        this.pubDate = pubDate;
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

    @Override
    public String toString() {
        return "[" + id + "][" + uri + "]";
    }
}