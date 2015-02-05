package com.greenapp.greenabomination.compass;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by kabardinov133238 on 04.02.2015.
 */
public class CompassView extends View {

    private float bearing;
    private Paint markerPaint;
    private Paint textPaint;
    private Paint circlePaint;
    private String northString;
    private String eastString;
    private String westString;
    private String southString;
    private int textHeight;


    public void setBearing(float _bearing) {
        bearing = _bearing;
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
    }

    public float getBearing() {
        return bearing;
    }

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

        Resources r = this.getResources();
        circlePaint.setColor(r.getColor(R.color.background_color));
        circlePaint.setStrokeWidth(1);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        northString = r.getString(R.string.cardinal_north);
        eastString = r.getString(R.string.cardinal_east);
        westString = r.getString(R.string.cardinal_west);
        southString = r.getString(R.string.cardinal_south);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(r.getColor(R.color.text_color));
        //очень интересная строчка
        textHeight = (int) textPaint.measureText("yY");

        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(r.getColor(R.color.marker_color));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = measure(widthMeasureSpec);
        int measureHeight = measure(heightMeasureSpec);

        int d = Math.min(measureWidth, measureHeight);

        setMeasuredDimension(d, d);
    }

    private int measure(int measureSpec) {
        int result = 0;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.UNSPECIFIED) {
            result = 200;
        } else {
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int mMeasureWidth = getMeasuredWidth();
        int mMeasureHeight = getMeasuredHeight();

        int px = mMeasureWidth / 2;
        int py = mMeasureHeight / 2;

        int radius = Math.min(px, py);

        canvas.drawCircle(px, py, radius, circlePaint);
        canvas.save();
        canvas.rotate(-bearing, px, py);

        int textWidth = (int) textPaint.measureText("W");
        int cardinalX = px - textWidth / 2;
        int cardinalY = py - radius + textHeight;

        for (int i = 0; i < 24; i++) {
            canvas.drawLine(px, py - radius, px, py - radius + 10, markerPaint);
            canvas.save();
            canvas.translate(0, textHeight);
            if (i % 6 == 0) {
                String dirString = "";
                switch (i) {
                    case (0): {
                        dirString = northString;
                        int arrowY = 2 * textHeight;
                        canvas.drawLine(px, arrowY, px - 5, 3 * textHeight, markerPaint);
                        canvas.drawLine(px, arrowY, px + 5, 3 * textHeight, markerPaint);
                        break;
                    }
                    case (6): {
                        dirString = eastString;
                        break;
                    }
                    case (12): {
                        dirString = southString;
                        break;
                    }
                    case (18): {
                        dirString = westString;
                        break;
                    }
                }
                canvas.drawText(dirString, cardinalX, cardinalY, textPaint);
            } else {
                if (i % 3 == 0) {
                    String angle = String.valueOf(i * 15);
                    float angleTextWidth = textPaint.measureText(angle);

                    int angleTextX = (int) (px - angleTextWidth / 2);
                    int angleTextY = (int) (py - radius + textHeight);
                    canvas.drawText(angle, angleTextX, angleTextY, textPaint);
                }
                canvas.restore();
                canvas.rotate(15, px, py);
            }
            canvas.restore();
        }
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent
            (final AccessibilityEvent event
            ) {
        super.dispatchPopulateAccessibilityEvent(event);

        if (isShown()) {
            String bearingSrt = String.valueOf(bearing);
            if (bearingSrt.length() > AccessibilityEvent.MAX_TEXT_LENGTH)
                bearingSrt = bearingSrt.substring(0, AccessibilityEvent.MAX_TEXT_LENGTH);
            event.getText().add(bearingSrt);
            return true;
        }
        return false;
    }

}
