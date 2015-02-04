package com.greenapp.greenabomination.compass;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kabardinov133238 on 04.02.2015.
 */
public class CompassView extends View {


    public CompassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCompassView();
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCompassView();
    }

    public CompassView(Context context) {
        super(context);
        initCompassView();
    }

    protected void initCompassView() {
        setFocusable(true);
    }
}
