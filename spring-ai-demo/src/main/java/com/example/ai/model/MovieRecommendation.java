package com.example.ai.model;

/**
 * Structured output model for AI-generated movie recommendations.
 * Used to demonstrate Spring AI structured output extraction.
 */
public class MovieRecommendation {

    private String title;
    private String genre;
    private int year;
    private String director;
    private String reason;

    public MovieRecommendation() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "MovieRecommendation{" +
                "title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", year=" + year +
                ", director='" + director + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}

