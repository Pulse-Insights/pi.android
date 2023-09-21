package com.pulseinsights.surveysdk.jsontool;

public class ParseHelper<T> {
    public T getObj(T obj, T defaultObj) {
        return (obj == null) ? defaultObj : obj;
    }
}
