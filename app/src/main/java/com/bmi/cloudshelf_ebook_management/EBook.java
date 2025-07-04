package com.bmi.cloudshelf_ebook_management;

public class EBook {
    private String title;
    private String url;

    public EBook() {
        // Empty constructor needed for Firestore
    }

    public EBook(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url=url;
}

}
