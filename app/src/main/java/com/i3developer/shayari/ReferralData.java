package com.i3developer.shayari;

import com.google.firebase.Timestamp;

import java.util.List;

public class ReferralData {
    private String title;
    private Timestamp expireDate;
    private List<String> terms;

    public ReferralData() {
    }

    public ReferralData(String title, Timestamp expireDate, List<String> terms) {
        this.title = title;
        this.expireDate = expireDate;
        this.terms = terms;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Timestamp expireDate) {
        this.expireDate = expireDate;
    }

    public List<String> getTerms() {
        return terms;
    }

    public void setTerms(List<String> terms) {
        this.terms = terms;
    }
}
