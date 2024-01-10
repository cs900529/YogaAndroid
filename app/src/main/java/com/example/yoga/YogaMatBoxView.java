package com.example.yoga;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class YogaMatBoxView extends View {

    private Paint paint;
    private float[] coordinates;

    public YogaMatBoxView(Context context) {
        super(context);
        init();
    }

    public YogaMatBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public YogaMatBoxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        // 指定四边形的坐标
        float[] defaultCoordinates = {0.9f , 0.97f ,
                0.1f , 0.97f ,
                0.15f , 0.76f ,
                0.85f , 0.76f};
        setCoordinates(defaultCoordinates);
    }

    public void setCoordinates(float[] coordinates) {
        if (coordinates.length == 8) {
            this.coordinates = coordinates;
            invalidate(); // Request a redraw
        } else {
            throw new IllegalArgumentException("The coordinates array must have exactly 8 values.");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (coordinates != null) {
            Path path = new Path();
            path.moveTo(coordinates[0] * getWidth(), coordinates[1] * getHeight());
            path.lineTo(coordinates[2] * getWidth(), coordinates[3] * getHeight());
            path.lineTo(coordinates[4] * getWidth(), coordinates[5] * getHeight());
            path.lineTo(coordinates[6] * getWidth(), coordinates[7] * getHeight());
            path.close();

            canvas.drawPath(path, paint);
        }
    }
}
