package org.idiotnation.bingo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionUtils;
import com.transitionseverywhere.TransitionValues;

public class RotateTransition extends Transition {

    private static final String ROTATION_Y = "RotateTransition:rotationY";
    private static final String PIVOT_Z = "RotateTransition:pivotZ";
    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        transitionValues.values.put(ROTATION_Y, view.getRotationY());
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        float rotationXStart = (Float) startValues.values.get(ROTATION_Y);
        float rotationXEnd = (Float) endValues.values.get(ROTATION_Y);
        Animator rxAnimator = ObjectAnimator.ofFloat(startValues.view, "rotationY", rotationXStart, rotationXEnd);
        return rxAnimator;
    }
}
