package com.example.newsaggregator;

public class NewsInfo {
    private String newsTitle;
    private String date;
    private String articleTitle;
    private String author;
    private String imageURL;
    private String text;
    private String url;

    public NewsInfo(String newsTitle, String date, String articleTitle, String author, String imageURL,
                    String url, String text) {
        this.newsTitle = newsTitle;
        this.date = date;
        this.articleTitle = articleTitle;
        this.author = author;
        this.imageURL = imageURL;
        this.text = text;
        this.url=url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = (url == null ? "" : url);
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = (newsTitle == null ? "" : newsTitle);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = (date == null ? "" :
                date);

    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = (articleTitle == null ? "" : articleTitle);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = (author == null ? "" : author);

    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = (imageURL == null ? "" : imageURL);

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = (text == null ? "" : text);
    }
}

