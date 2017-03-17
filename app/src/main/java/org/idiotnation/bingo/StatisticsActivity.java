package org.idiotnation.bingo;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    ListView colorStatsList, numberStatsList;
    ArrayAdapter colorAdapter, numberAdapter;
    List colorsStats, numbersStats, allStats;
    StatisticsDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_layout);
        init();
        properties();
    }

    public void init() {
        colorStatsList = (ListView) findViewById(R.id.colorsStats);
        numberStatsList = (ListView) findViewById(R.id.numberStats);
        databaseHelper = new StatisticsDatabaseHelper(getApplicationContext());
        allStats = new ArrayList();
        colorsStats = new ArrayList();
        numbersStats = new ArrayList();
        setStatistics();
        colorAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, colorsStats);
        numberAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, numbersStats);
    }

    public void properties() {
        colorStatsList.setAdapter(colorAdapter);
        numberStatsList.setAdapter(numberAdapter);
    }

    public void setStatistics() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String sqlStatement = "SELECT * FROM NumberStatistics";
        Cursor c = database.rawQuery(sqlStatement, null);
        if (c.moveToFirst()) {
            do {
                allStats.add(c.getInt(c.getColumnIndex("Number")));
            } while (c.moveToNext());
            c.close();
            database.close();
        }
        if (allStats.size() > 0) {
            double red = 0, blue = 0, green = 0, brown = 0, black = 0, orange = 0, purple = 0, yellow = 0;
            for (int i = 0; i < allStats.size(); i++) {
                if (Utils.getBallColor((int) allStats.get(i)) == Color.rgb(255, 0, 0)) {
                    red++;
                }
                if (Utils.getBallColor((int) allStats.get(i)) == Color.rgb(0, 0, 255)) {
                    blue++;
                }
                if (Utils.getBallColor((int) allStats.get(i)) == Color.rgb(0, 255, 0)) {
                    green++;
                }
                if (Utils.getBallColor((int) allStats.get(i)) == Color.rgb(165, 42, 42)) {
                    brown++;
                }
                if (Utils.getBallColor((int) allStats.get(i)) == Color.rgb(0, 0, 0)) {
                    black++;
                }
                if (Utils.getBallColor((int) allStats.get(i)) == Color.rgb(255, 165, 0)) {
                    orange++;
                }
                if (Utils.getBallColor((int) allStats.get(i)) == Color.rgb(128, 0, 128)) {
                    purple++;
                }
                if (Utils.getBallColor((int) allStats.get(i)) == Color.rgb(255, 255, 0)) {
                    yellow++;
                }
            }
            DecimalFormat df = new DecimalFormat("####0.00");
            red = getPercent(red);
            yellow = getPercent(yellow);
            blue = getPercent(blue);
            purple = getPercent(purple);
            orange = getPercent(orange);
            black = getPercent(black);
            brown = getPercent(brown);
            green = getPercent(green);
            colorsStats.add("red percentage: " + df.format(red) + "%");
            colorsStats.add("yellow percentage: " + df.format(yellow) + "%");
            colorsStats.add("blue percentage: " + df.format(blue) + "%");
            colorsStats.add("purple percentage: " + df.format(purple) + "%");
            colorsStats.add("orange percentage: " + df.format(orange) + "%");
            colorsStats.add("black percentage: " + df.format(black) + "%");
            colorsStats.add("brown percentage: " + df.format(brown) + "%");
            colorsStats.add("green percentage: " + df.format(green) + "%");

            for(int i=1; i<49; i++){
                numbersStats.add(i+ " frequency: " + df.format(getPercent(Collections.frequency(allStats, i)))+"%");
            }
        }
    }
    public double getPercent(double value){
        return (value / allStats.size()) * 100.0;
    }
}
