package com.rapido.youtube_rapido.model.response;

public class PageInfo {
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