package com.an_ant_on_the_sun.distancetraveled.model;

import com.yandex.mapkit.geometry.Point;

import java.util.ArrayList;
import java.util.List;

public class PointsOfTheRoute {
    private static final PointsOfTheRoute ourInstance = new PointsOfTheRoute();
    private static final double VERY_SMALL_NUMBER = 0.002d;

    public static PointsOfTheRoute getInstance() {
        return ourInstance;
    }

    public List<Point> getPointList() {
        return pointList;
    }

    private List<Point> pointList = new ArrayList<>();

    private PointsOfTheRoute() {
    }
}
