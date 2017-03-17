package org.idiotnation.bingo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<Integer> createNumbersList() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i < 49; i++) {
            numbers.add(i);
        }
        return numbers;
    }

    public static List<Integer> createColorOrderedNumbersList() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 0; j < 6; j++) {
                numbers.add(i+(j*8));
            }
        }
        return numbers;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static int getBallColor(int number) {
        int colorNumber = number - (((int) number / 8) * 8);
        switch (colorNumber) {
            case 1:
                return Color.rgb(255, 0, 0);
            case 2:
                return Color.rgb(0, 255, 0);
            case 3:
                return Color.rgb(0, 0, 255);
            case 4:
                return Color.rgb(128, 0, 128);
            case 5:
                return Color.rgb(165, 42, 42);
            case 6:
                return Color.rgb(255, 165, 0);
            case 7:
                return Color.rgb(255, 255, 0);
            case 0:
                return Color.rgb(0, 0, 0);
        }
        return colorNumber;
    }

    public static Drawable getBallDrawable(int number, Context context) {
        int color = getBallColor(number);
        GradientDrawable ballBackground = (GradientDrawable) context.getResources().getDrawable(R.drawable.ball);
        ballBackground.setStroke((int) convertDpToPixel(8, context.getApplicationContext()), color);
        return ballBackground;
    }

}
