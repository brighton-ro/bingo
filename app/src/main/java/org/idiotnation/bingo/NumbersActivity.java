package org.idiotnation.bingo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NumbersActivity extends AppCompatActivity {

    public static List<List<Integer>> userNumbers;
    private ImageView nextButton, shuffleButton, statsButton, addTabView, removeTabView;
    private GridView numbersHolder;
    private NumbersAdapter numbersAdapter;
    private TextView bCredits;
    private SharedPreferences preferences;
    private TabLayout userNumbersTabs;
    private int selectedTabPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numbers_layout);
        init();
        properties();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userNumbers.clear();
        userNumbers.add(new ArrayList<Integer>(6));
        userNumbersTabs.removeAllTabs();
        userNumbersTabs.addTab(userNumbersTabs.newTab().setText("#0"));
        numbersAdapter.selectedNumbers.clear();
        numbersAdapter.selectedNumbers.add(new ArrayList<Integer>(6));
        bCredits.setText("Credits: " + preferences.getInt("BCredits", 0));
    }

    public void init() {
        preferences = getSharedPreferences("org.idiotnation.bingo", MODE_PRIVATE);
        nextButton = (ImageView) findViewById(R.id.buttonNext);
        bCredits = (TextView) findViewById(R.id.bCredits);
        shuffleButton = (ImageView) findViewById(R.id.buttonShuffle);
        statsButton = (ImageView) findViewById(R.id.buttonStats);
        numbersHolder = (GridView) findViewById(R.id.numbersHolder);
        addTabView = (ImageView) findViewById(R.id.addNewNumbers);
        removeTabView = (ImageView) findViewById(R.id.removeNumbersFromList);
        numbersAdapter = new NumbersAdapter(getApplicationContext(), R.layout.grid_item, Utils.createColorOrderedNumbersList());
        userNumbersTabs = (TabLayout) findViewById(R.id.userNumbersTabs);
        userNumbers = new ArrayList<>();
    }

    public void properties() {
        if (preferences.getInt("BCredits", -2910) == -2910) {
            preferences.edit().putInt("BCredits", 100).apply();
        }
        bCredits.setText("Credits: " + preferences.getInt("BCredits", 0));
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs();
            }
        });
        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numbersAdapter.randomSelection();
            }
        });
        shuffleButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int oldSelect = selectedTabPosition;
                for(int i=0; i<userNumbersTabs.getTabCount(); i++){
                    selectedTabPosition = i;
                    numbersAdapter.randomSelection();
                }
                Toast.makeText(getApplicationContext(), "All tickets randomized", Toast.LENGTH_SHORT).show();
                selectedTabPosition = oldSelect;
                return true;
            }
        });
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StatisticsActivity.class));
            }
        });
        addTabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTab(userNumbersTabs.getTabCount());
            }
        });
        removeTabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTab(userNumbersTabs.getSelectedTabPosition());
            }
        });
        numbersHolder.setAdapter(numbersAdapter);
        numbersHolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (numbersAdapter.selectedNumbers.get(selectedTabPosition).contains(i)) {
                    numbersAdapter.setSelection(i, view);
                } else {
                    if (numbersAdapter.selectedNumbers.get(selectedTabPosition).size() < 6) {
                        numbersAdapter.setSelection(i, view);
                    }
                }
            }
        });
        userNumbersTabs.addOnTabSelectedListener (new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTabPosition = userNumbersTabs.getSelectedTabPosition();
                numbersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void validateInputs() {
        boolean isValid = true;
        for (int i=0;i<userNumbers.size(); i++){
            if(userNumbers.get(i).size()!=6){
                isValid = false;
                break;
            }
        }
        if (isValid) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            Bundle extras = new Bundle();
            extras.putIntegerArrayList("userNumbers", new ArrayList(userNumbers));
            i.putExtras(extras);
            startActivity(i);
            preferences.edit().putInt("BCredits", preferences.getInt("BCredits", 0) - userNumbersTabs.getTabCount()).apply();
        } else {
            Toast.makeText(getApplicationContext(), "Odaberite 6 brojeva", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeTab(int position) {
        if (userNumbersTabs.getTabCount() > 1) {
            userNumbersTabs.removeTabAt(position);
            userNumbers.remove(position);
            numbersAdapter.selectedNumbers.remove(position);
        }
    }

    public void addTab(int position) {
        if(userNumbersTabs.getTabCount()<=10){
            userNumbersTabs.addTab(userNumbersTabs.newTab().setText("#" + (Integer.parseInt(userNumbersTabs.getTabAt(position-1).getText().toString().substring(1))+1)));
            userNumbers.add(new ArrayList<Integer>(6));
            numbersAdapter.selectedNumbers.add(new ArrayList<Integer>(6));
            TabLayout.Tab tab = userNumbersTabs.getTabAt(position);
            tab.select();
        }
    }

    private class NumbersAdapter extends ArrayAdapter {

        List<Integer> numbers;
        List<List<Integer>> selectedNumbers;

        public NumbersAdapter(Context context, int resource, List objects) {
            super(context, resource, objects);
            numbers = objects;
            selectedNumbers = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return 48;
        }

        public void setSelection(int position, View selectionView) {
            View highlight = selectionView.findViewById(R.id.highlightLayout);
            if (!selectedNumbers.get(selectedTabPosition).contains(position)) {
                selectedNumbers.get(selectedTabPosition).add(position);
                userNumbers.get(selectedTabPosition).add(numbers.get(position));
                highlight.setBackgroundDrawable(new ColorDrawable(Color.argb(69, 0, 0, 0)));
            } else {
                userNumbers.get(selectedTabPosition).remove(numbers.get(position));
                selectedNumbers.get(selectedTabPosition).remove(selectedNumbers.get(selectedTabPosition).indexOf(position));
                highlight.setBackgroundDrawable(null);
            }
        }

        public void randomSelection() {
            List allNumbers = Utils.createNumbersList();
            Collections.shuffle(allNumbers);
            userNumbers.get(selectedTabPosition).clear();
            userNumbers.get(selectedTabPosition).addAll(allNumbers.subList(0, 6));
            selectedNumbers.get(selectedTabPosition).clear();
            for (int i = 0; i < 6; i++) {
                selectedNumbers.get(selectedTabPosition).add(numbers.indexOf(userNumbers.get(selectedTabPosition).get(i)));
            }
            numbersAdapter.notifyDataSetChanged();
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rootView = convertView;
            if (rootView == null) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rootView = inflater.inflate(R.layout.grid_item, null);
            }
            int currentNumber = numbers.get(position);
            View hightlight = rootView.findViewById(R.id.highlightLayout);
            hightlight.setBackgroundDrawable(selectedNumbers.get(selectedTabPosition).contains(position) ? new ColorDrawable(Color.argb(69, 0, 0, 0)) : null);
            TextView ballNumber = (TextView) rootView.findViewById(R.id.ballNumber);
            ballNumber.setText(currentNumber + "");
            ballNumber.setTextColor(Color.BLACK);
            ballNumber.setBackgroundDrawable(Utils.getBallDrawable(currentNumber, getContext()));
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(parent.getWidth() / 6, parent.getWidth() / 6);
            rootView.setLayoutParams(params);
            return rootView;
        }
    }
}
