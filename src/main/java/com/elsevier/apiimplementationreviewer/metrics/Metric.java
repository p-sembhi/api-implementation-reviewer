package com.elsevier.apiimplementationreviewer.metrics;

//responsible for abstracting methods (toCSVString) in common between author and document classes.
public interface Metric {
    public String toCSVString();
}
