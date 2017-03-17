package org.idiotnation.bingo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionUtils;
import com.transitionseverywhere.TransitionValues;

public class MoveTransition extends Transition {

    private static final String SCALE_X = "MoveTransition:scaleX";
    private static final String SCALE_Y = "MoveTransition:scaleY";
    private static final String POS_X = "MoveTransition:posX";
    private static final String POS_Y = "MoveTransition:posY";

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
        transitionValues.values.put(SCALE_X, view.getScaleX());
        transitionValues.values.put(SCALE_Y, view.getScaleY());
        transitionValues.values.put(POS_X, view.getX());
        transitionValues.values.put(POS_Y, view.getY());
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        float scaleXStart = (Float) startValues.values.get(SCALE_X);
        float scaleYStart = (Float) startValues.values.get(SCALE_Y);
        float scaleXEnd = (Float) endValues.values.get(SCALE_X);
        float scaleYEnd = (Float) endValues.values.get(SCALE_Y);
        float posXStart = (Float) startValues.values.get(POS_X);
        float posYStart = (Float) startValues.values.get(POS_Y);
        float posXEnd = (Float) endValues.values.get(POS_X);
        float posYEnd = (Float) endValues.values.get(POS_Y);
        Animator sxAnimator = ObjectAnimator.ofFloat(startValues.view, "scaleX", scaleXStart, scaleXEnd);
        Animator syAnimator = ObjectAnimator.ofFloat(startValues.view, "scaleY", scaleYStart, scaleYEnd);
        Animator pxAnimator = ObjectAnimator.ofFloat(startValues.view, "x", posXStart, posXEnd);
        Animator pyAnimator = ObjectAnimator.ofFloat(startValues.view, "y", posYStart, posYEnd);
        return TransitionUtils.mergeAnimators(TransitionUtils.mergeAnimators(sxAnimator, syAnimator), TransitionUtils.mergeAnimators(pxAnimator, pyAnimator));
    }
}

