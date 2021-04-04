package com.i3developer.shayari;

import java.util.List;

public class ReferralListData {
    private String note;
    private List<String> referrals;
    private int earnedMoney;
    private int eCPR;

    public ReferralListData() {
    }

    public ReferralListData(String note, List<String> referrals, int earnedMoney, int eCPR) {
        this.note = note;
        this.referrals = referrals;
        this.earnedMoney = earnedMoney;
        this.eCPR = eCPR;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getReferrals() {
        return referrals;
    }

    public void setReferrals(List<String> referrals) {
        this.referrals = referrals;
    }

    public int getEarnedMoney() {
        return earnedMoney;
    }

    public void setEarnedMoney(int earnedMoney) {
        this.earnedMoney = earnedMoney;
    }
}
