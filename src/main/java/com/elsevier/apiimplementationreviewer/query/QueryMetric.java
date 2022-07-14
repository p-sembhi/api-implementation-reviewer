package com.elsevier.apiimplementationreviewer.query;

import com.elsevier.apiimplementationreviewer.metrics.Metric;

//responsible for abstracting methods (getMetric) in common between author and document classes.
public interface QueryMetric { //renamed metric to query metric to match the package
    Metric getMetric(String id);
}
