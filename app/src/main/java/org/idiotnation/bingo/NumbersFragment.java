
package org.idiotnation.bingo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NumbersFragment extends Fragment {

    int[] resourceIds = {R.id.bingoNumberOne, R.id.bingoNumberTwo, R.id.bingoNumberThree, R.id.bingoNumberFour, R.id.bingoNumberFive, R.id.bingoNumberSix};
    private List<Integer> numbers, checked;
    private View parent;

    public NumbersFragment(){
    }

    public void setNumbers(List<Integer> numbers, List<Integer> checked){
        this.numbers = numbers;
        this.checked = checked;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.numbers_fragment_layout, null);
        for(int i=0; i<6;i++){
            TextView textView = (TextView) rootView.findViewById(resourceIds[i]);
            textView.setText(numbers.get(i)+"");
            if(checked.contains(i)){
                textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
            }
        }
        return rootView;
    }
}
