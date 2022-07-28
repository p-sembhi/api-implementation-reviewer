package com.elsevier.apiimplementationreviewer.query;

import com.elsevier.apiimplementationreviewer.metrics.Metric;

//responsible for abstracting methods (getMetric) in common between author and document classes.
public interface QueryMetric {
    public Metric getMetric(String id);
}
