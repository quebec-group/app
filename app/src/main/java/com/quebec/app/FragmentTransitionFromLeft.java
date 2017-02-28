package com.quebec.app;

/**
 * Created by Andrew on 28/02/2017.
 */

public class FragmentTransitionFromLeft extends FragmentTransition {

    public FragmentTransitionFromLeft() {
        super();
        this.enter = R.anim.enter_from_left;
        this.exit = R.anim.exit_to_right;
        this.popEnter = R.anim.enter_from_right;
        this.popExit = R.anim.exit_to_left;
    }
}
