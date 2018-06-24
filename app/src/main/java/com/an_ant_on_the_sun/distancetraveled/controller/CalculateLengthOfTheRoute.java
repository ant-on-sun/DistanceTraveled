package com.an_ant_on_the_sun.distancetraveled.controller;

import com.an_ant_on_the_sun.distancetraveled.model.PointsOfTheRoute;
import com.yandex.mapkit.geometry.Point;

import java.util.List;

public class CalculateLengthOfTheRoute {
    private static PointsOfTheRoute pointsOfTheRoute = PointsOfTheRoute.getInstance();
    private static List<Point> pointList = pointsOfTheRoute.getPointList();

    public static int calculateIt(){
        int result = 0;
        if (pointList.size() < 2){
            return result;
        }
        for (int i = 0; i < pointList.size() - 1; i++){
            float lastLatitude = (float)pointList.get(i).getLatitude();
            float lastLongitude = (float)pointList.get(i).getLongitude();
            float currentLatitude = (float)pointList.get(i + 1).getLatitude();
            float currentLongitude = (float)pointList.get(i + 1).getLongitude();
            result = Calculator.calculateLengthOfTheInterval(
                    lastLatitude,
                    lastLongitude,
                    currentLatitude,
                    currentLongitude,
                    result);
        }
        return result;
    }
}
