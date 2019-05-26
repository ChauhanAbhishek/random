package com.rapido.youtube_rapido.model.response;

import java.io.Serializable;

public class PageInfo  implements Serializable {
    private float totalResults;
    private float resultsPerPage;

    public float getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(float totalResults) {
        this.totalResults = totalResults;
    }

    public float getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(float resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }
}