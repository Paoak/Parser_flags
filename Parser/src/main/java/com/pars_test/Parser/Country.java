package com.pars_test.Parser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

    private String name;
    private String flag;

    public String getName() {
        return name;
    }

    public String getFlag() {
        return flag;
    }

    public Country(){

    }

    public Country(String name, String flag) {
        this.name = name;
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
