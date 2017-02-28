package com.quebec.app;

/**
 * Created by Andrew on 28/02/2017.
 */

public class FragmentTransitionFromRight extends FragmentTransition {

    public FragmentTransitionFromRight() {
        super();
        this.enter = R.anim.enter_from_right;
        this.exit = R.anim.exit_to_left;
        this.popEnter = R.anim.enter_from_left;
        this.popExit = R.anim.exit_to_right;
    }
}

