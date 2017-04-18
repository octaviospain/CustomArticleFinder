package cuni.software;

import java.util.*;

public class TermStatistics {

    private final String term;

    /**
     * Number of documents containing the term
     * that is represented by this instance
     */
    private long documentFrequency = 0;

    private double inverseDocumentFrequency = 0.0;

    /**
     * The highest frequency of the term represented by this
     * instance over the entire collection of articles
     */
    private long highestFrequency = 0;

    public TermStatistics(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public long getDocumentFrequency() {
        return documentFrequency;
    }

    public void setDocumentFrequency(long documentFrequency) {
        this.documentFrequency = documentFrequency;
    }

    public void incrementDocumentFrequency() {
        documentFrequency++;
    }

    public double getInverseDocumentFrequency() {
        return inverseDocumentFrequency;
    }

    public void setInverseDocumentFrequency(double inverseDocumentFrequency) {
        this.inverseDocumentFrequency = inverseDocumentFrequency;
    }

    public long getHighestFrequency() {
        return highestFrequency;
    }

    public void setHighestFrequency(long highestFrequency) {
        this.highestFrequency = highestFrequency;
    }

    public void incrementHighestFrequency() {
        highestFrequency++;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(term);
    }

    @Override
    public boolean equals(Object object) {
        boolean equals = false;
        if (object instanceof TermStatistics) {
            TermStatistics objectTerm = (TermStatistics) object;
            if (objectTerm.getTerm().equals(this.term))
                equals = true;
        }
        return equals;
    }
}