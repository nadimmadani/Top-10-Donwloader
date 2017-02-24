package com.nadimmadani.top10downloader;

import java.util.ArrayList;

/**
 * Created by Nadim on 2/22/2017.
 */

public class ParseApplications {
    private String data;
    private ArrayList<Application> applications;

    public ParseApplications(String data) {
        this.data = data;
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }
}
