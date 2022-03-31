package com.example.newsaggregator;

public class News {
    private String newsTitle;
    private String newsCategory;
    private String newsCountry;
    private String newsLanguage;

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsCategory() {
        return newsCategory;
    }

    public void setNewsCategory(String newsCategory) {
        this.newsCategory = newsCategory;
    }

    public String getNewsCountry() {
        return newsCountry;
    }

    public void setNewsCountry(String newsCountry) {
        this.newsCountry = newsCountry;
    }

    public String getNewsLanguage() {
        return newsLanguage;
    }

    public void setNewsLanguage(String newsLanguage) {
        this.newsLanguage = newsLanguage;
    }

    public News(String newsTitle, String newsCategory, String newsCountry, String newsLanguage) {
        this.newsTitle = newsTitle;
        this.newsCategory = newsCategory;
        this.newsCountry = newsCountry;
        this.newsLanguage = newsLanguage;
    }
}
