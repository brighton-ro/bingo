package org.idiotnation.bingo;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transitionseverywhere.ArcMotion;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Integer> allNumbers;
    List<List<Integer>> userNumbers, checkedNumbers;
    TextView currentNumberText;
    RelativeLayout currentNumber;
    ImageView bingoDrum;
    Handler handler;
    int[] priceArray;
    Runnable drumLoop;
    GridView grid;
    ViewPager numbersPager;
    CustomAdapter adapter;
    CustomPagerAdapter pagerAdapter;
    ConstraintLayout parentLayout;
    SQLiteDatabase stats;
    StatisticsDatabaseHelper databaseHelper;
    SharedPreferences preferences;
    float[] numberPoint;
    int stepTime = 3000; // miliseconds
    int currentNumberIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        init();
        properties();
    }

    public void init() {
        userNumbers = NumbersActivity.userNumbers;
        checkedNumbers = new ArrayList<>();
        for (int i=0; i<userNumbers.size(); i++){
         checkedNumbers.add(new ArrayList<Integer>());
        }
        grid = (GridView) findViewById(R.id.bingoBalls);
        preferences = getSharedPreferences("org.idiotnation.bingo", MODE_PRIVATE);
        currentNumber = (RelativeLayout) findViewById(R.id.currentNumber);
        databaseHelper = new StatisticsDatabaseHelper(getApplicationContext());
        stats = databaseHelper.getWritableDatabase();
        currentNumberText = (TextView) currentNumber.findViewById(R.id.ballNumber);
        currentNumber.setVisibility(View.INVISIBLE);
        priceArray = getResources().getIntArray(R.array.prices);
        bingoDrum = (ImageView) findViewById(R.id.bingoDrum);
        numbersPager = (ViewPager) findViewById(R.id.numbersPager);
        pagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        parentLayout = (ConstraintLayout) findViewById(R.id.parentLayout);
        allNumbers = Utils.createNumbersList();
        Collections.shuffle(allNumbers);
        for(int i=0;i<userNumbers.size(); i++){
            Collections.sort(userNumbers.get(i));
        }
        handler = new Handler();
        adapter = new CustomAdapter(getApplicationContext(), R.layout.grid_item);
        drumLoop = new Runnable() {
            @Override
            public void run() {
                TransitionManager.endTransitions(parentLayout);
                TransitionManager.beginDelayedTransition(parentLayout, new TransitionSet()
                        .addTransition(new Rotate().setDuration(stepTime / 2).addTarget(bingoDrum).addListener(new Transition.TransitionListener() {
                            @Override
                            public void onTransitionStart(Transition transition) {
                            }

                            @Override
                            public void onTransitionEnd(Transition transition) {
                                TransitionManager.endTransitions(parentLayout);
                                TransitionManager.beginDelayedTransition(parentLayout, new TransitionSet()
                                        .addTransition(new Fade())
                                        .addTransition(new Scale())
                                        .setInterpolator(new OvershootInterpolator())
                                        .addTarget(currentNumber)
                                        .setDuration(stepTime / 4)
                                        .addListener(new Transition.TransitionListener() {
                                            @Override
                                            public void onTransitionStart(Transition transition) {
                                            }

                                            @Override
                                            public void onTransitionEnd(Transition transition) {
                                                final int current = allNumbers.get(currentNumberIndex - 1);
                                                for(int i=0; i<userNumbers.size(); i++){
                                                    if (userNumbers.get(i).contains(current)) {
                                                        checkedNumbers.get(i).add(userNumbers.get(i).indexOf(current));
                                                        if (checkedNumbers.get(i).size() == 6) {
                                                            checkedNumbers.get(i).add(-1);
                                                            preferences.edit().putInt("BCredits", preferences.getInt("BCredits", 0) + priceArray[35 - currentNumberIndex]).apply();
                                                            Toast.makeText(getApplicationContext(), "You won " + priceArray[35 - currentNumberIndex] + " credits", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                }
                                                pagerAdapter.notifyDataSetChanged();
                                                TransitionSet set = new TransitionSet()
                                                        .addTransition(new RotateTransition())
                                                        .addTransition(new MoveTransition().addTarget(currentNumber))
                                                        .addListener(new Transition.TransitionListener() {
                                                            @Override
                                                            public void onTransitionStart(Transition transition) {
                                                            }

                                                            @Override
                                                            public void onTransitionEnd(Transition transition) {
                                                                currentNumber.setVisibility(View.INVISIBLE);
                                                                currentNumber.setScaleX(1.5f);
                                                                currentNumber.setScaleY(1.5f);
                                                                adapter.add(allNumbers.get(currentNumberIndex - 1));
                                                            }

                                                            @Override
                                                            public void onTransitionCancel(Transition transition) {

                                                            }

                                                            @Override
                                                            public void onTransitionPause(Transition transition) {

                                                            }

                                                            @Override
                                                            public void onTransitionResume(Transition transition) {

                                                            }
                                                        })
                                                        .setPathMotion(new ArcMotion())
                                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                                        .setDuration(stepTime / 2)
                                                        .addTarget(currentNumber);
                                                TransitionManager.endTransitions(parentLayout);
                                                TransitionManager.beginDelayedTransition(parentLayout, set);
                                                currentNumber.setRotationY(currentNumber.getRotationY() + 720);
                                                currentNumber.setX(getNextViewPosition()[0]);
                                                currentNumber.setY(getNextViewPosition()[1]);
                                                currentNumber.setScaleX(1);
                                                currentNumber.setScaleY(1);
                                                if (currentNumberIndex < 35) {
                                                    handler.postDelayed(drumLoop, stepTime);
                                                }
                                            }

                                            @Override
                                            public void onTransitionCancel(Transition transition) {

                                            }

                                            @Override
                                            public void onTransitionPause(Transition transition) {

                                            }

                                            @Override
                                            public void onTransitionResume(Transition transition) {

                                            }
                                        }));
                                final int current = allNumbers.get(currentNumberIndex);
                                ContentValues values = new ContentValues();
                                values.put("Number", current);
                                stats.insert("NumberStatistics", null, values);
                                currentNumberText.setText(current + "");
                                currentNumberText.setBackgroundDrawable(Utils.getBallDrawable(current, getApplicationContext()));
                                currentNumberIndex++;
                                currentNumber.setX(numberPoint[0]);
                                currentNumber.setY(numberPoint[1]);
                                currentNumber.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onTransitionCancel(Transition transition) {

                            }

                            @Override
                            public void onTransitionPause(Transition transition) {

                            }

                            @Override
                            public void onTransitionResume(Transition transition) {

                            }
                        })));
                bingoDrum.setRotation(bingoDrum.getRotation() + 720);
            }
        };
    }

    public void properties() {
        grid.setAdapter(adapter);
        numbersPager.setAdapter(pagerAdapter);
        bingoDrum.post(new Runnable() {
            @Override
            public void run() {
                currentNumber.setScaleX(1.5f);
                currentNumber.setScaleY(1.5f);
                currentNumber.setLayoutParams(new ConstraintLayout.LayoutParams(grid.getWidth() / 7, grid.getHeight() / 5));
                currentNumber.requestLayout();
                int w = bingoDrum.getRight() - bingoDrum.getLeft();
                int h = bingoDrum.getBottom() - bingoDrum.getTop();
                numberPoint = new float[]{bingoDrum.getLeft() + (w / 2) - ((grid.getWidth() / 7) / 2), bingoDrum.getTop() + (h / 2) - ((grid.getHeight() / 5) / 2)};
            }
        });
        setDrumLoop();

    }

    public float[] getNextViewPosition() {
        View view = grid.getChildAt(grid.getChildCount() - 1);
        int y, x;
        if (view != null) {
            x = (int) view.getX() + view.getWidth();
            if (x + 20 >= grid.getWidth()) {
                x = 0;
                y = (int) view.getY() + view.getHeight();
            } else {
                y = (int) view.getY();
            }
        } else {
            x = 0;
            y = 0;
        }
        return new float[]{x, y};
    }

    public void setDrumLoop() {
        handler.postDelayed(drumLoop, 500);
    }

    public class CustomAdapter extends ArrayAdapter<Integer> {

        public CustomAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Nullable
        @Override
        public Integer getItem(int position) {
            return allNumbers.get(position);
        }

        public View getViewAtPosition(int i) {
            View view = grid.getChildAt(i);
            return view;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int currentNumber = getItem(position);
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.grid_item, null);
            TextView ballNumber = (TextView) root.findViewById(R.id.ballNumber);
            ballNumber.setText(currentNumber + "");
            ballNumber.setTextColor(Color.BLACK);
            ballNumber.setBackgroundDrawable(Utils.getBallDrawable(currentNumber, getContext()));
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(parent.getWidth() / 7, parent.getHeight() / 5);
            root.setLayoutParams(params);
            return root;
        }

    }

    private class CustomPagerAdapter extends FragmentStatePagerAdapter{

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            NumbersFragment numbersFragment = new NumbersFragment();
            numbersFragment.setNumbers(userNumbers.get(position), checkedNumbers.get(position));
            return numbersFragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return userNumbers.size();
        }
    }
}
