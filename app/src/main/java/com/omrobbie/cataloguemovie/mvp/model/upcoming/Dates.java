/*
 * Created by omrobbie.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 9/27/17 9:25 AM.
 */

package com.omrobbie.cataloguemovie.mvp.model.upcoming;

import com.google.gson.annotations.SerializedName;

public class Dates {

    @SerializedName("maximum")
    private String maximum;

    @SerializedName("minimum")
    private String minimum;

    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }

    public String getMaximum() {
        return maximum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getMinimum() {
        return minimum;
    }

    @Override
    public String toString() {
        return
                "Dates{" +
                        "maximum = '" + maximum + '\'' +
                        ",minimum = '" + minimum + '\'' +
                        "}";
    }
}