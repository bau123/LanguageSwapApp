package com.example.pc.run.Objects;

/**
 * Created by Joss on 05/03/2016.
 */
public class Review {

    String rating;
    String comment;
    String type;
    String reviewer;

    public Review(String rating, String comment, String reviewer, String type){
        this.comment = comment;
        this.rating = rating;
        this.reviewer = reviewer;
        this.type = type;

    }


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }
}
