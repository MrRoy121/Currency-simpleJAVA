package com.example.currency;

public class countrylist {
    String cname, ccur, cimage, mcap;

    public countrylist(String cname, String ccur, String cimage, String mcap) {
        this.cname = cname;
        this.ccur = ccur;
        this.cimage = cimage;
        this.mcap = mcap;
    }

    public countrylist() {
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCcur() {
        return ccur;
    }

    public void setCcur(String ccur) {
        this.ccur = ccur;
    }

    public String getCimage() {
        return cimage;
    }

    public void setCimage(String cimage) {
        this.cimage = cimage;
    }

    public String getMcap() {
        return mcap;
    }

    public void setMcap(String mcap) {
        this.mcap = mcap;
    }
}
