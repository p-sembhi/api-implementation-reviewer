package com.elsevier.apiimplementationreviewer.helper.metrics;

//responsible for abstracting methods (toCSVString) in common between author and document classes.
public interface Metric {
    String toCSVString();
}
