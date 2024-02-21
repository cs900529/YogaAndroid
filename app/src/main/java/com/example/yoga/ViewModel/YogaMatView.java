package com.example.yoga.ViewModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.yoga.R;

public class YogaMatView extends View {

    private static final int BITMAP_WIDTH = 64;
    private static final int BITMAP_HEIGHT = 100;

    private Paint paint;
    private Bitmap leftBitmap, rightBitmap;
    private float leftImageX, leftImageY, rightImageX, rightImageY;
    private Matrix rotationMatrix;
    private Bitmap rotatedBitmap1, rotatedBitmap2;

    public YogaMatView(Context context) {
        super(context);
        init();
    }

    public YogaMatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public YogaMatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.GREEN);

        // 加载两张 PNG 图片
        leftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.left_feet);
        rightBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.right_feet);

        rotationMatrix = new Matrix();
        rotatedBitmap1 = rotateBitmap(leftBitmap);
        rotatedBitmap2 = rotateBitmap(rightBitmap);
    }

    private Bitmap rotateBitmap(Bitmap originalBitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(0);
        return Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
    }

    private Bitmap scaleBitmap(Bitmap originalBitmap) {
        return Bitmap.createScaledBitmap(originalBitmap, BITMAP_WIDTH, BITMAP_HEIGHT, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制绿色背景
        canvas.drawColor(Color.GREEN);

        // 绘制 PNG 图片，调整大小和位置
        canvas.drawBitmap(scaleBitmap(rotatedBitmap1), leftImageX - 0.5f * BITMAP_WIDTH, leftImageY -  BITMAP_HEIGHT, paint);
        canvas.drawBitmap(scaleBitmap(rotatedBitmap2), rightImageX - 0.5f * BITMAP_WIDTH, rightImageY - BITMAP_HEIGHT, paint);

        // 绘制文字
        paint.setColor(Color.BLACK);
        paint.setTextSize(100);

//        String text1 = "F X: " + leftImageX / getWidth() + ", R: " + rightImageX / getWidth();
//        canvas.drawText(text1, 10, getHeight() - 30, paint);

//        String text2 = "SX: " + secondImageX + ", Y: " + secondImageY;
//        canvas.drawText(text2, 10, getHeight() - 10, paint);
    }

    // 新增方法：设置第一张图片位置
    public void setLeftFeetPosition(float x, float y) {
        leftImageX = x * getWidth();
        leftImageY = (1 - y) * getHeight();
        invalidate(); // 通知 View 重新绘制
    }

    // 新增方法：设置第二张图片位置
    public void setRightFeetPosition(float x, float y) {
        rightImageX = x * getWidth();
        rightImageY = (1 - y) * getHeight();
        invalidate(); // 通知 View 重新绘制
    }
}
