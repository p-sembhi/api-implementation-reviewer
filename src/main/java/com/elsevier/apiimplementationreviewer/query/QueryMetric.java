package com.elsevier.apiimplementationreviewer.helper.query;

import com.elsevier.apiimplementationreviewer.helper.metrics.Metric;

//responsible for abstracting methods (getMetric) in common between author and document classes.
public interface QueryMetric { //renamed metric to query metric to match the package
    Metric getMetric(String id);
}
