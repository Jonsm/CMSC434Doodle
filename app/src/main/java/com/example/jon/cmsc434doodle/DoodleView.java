package com.example.jon.cmsc434doodle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.w3c.dom.Attr;

import java.util.ArrayList;

/**
 * Created by Jon on 10/31/16.
 */

public class DoodleView extends View{
    private static float smoothRadius = 6f;
    private static float totalRainbowCycleDistance = 400f;

    private Paint paintDoodle = new Paint();
    private Path path = null;
    private Bitmap lastBitmap = null;

    private boolean rainbow = false;
    private int rainbowColor = Color.RED;
    private PointF lastRainbowPoint = null;

    public DoodleView(Context context) {
        super(context);
        init(null, 0);
    }

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DoodleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        paintDoodle.setAntiAlias(true);
        paintDoodle.setStyle(Paint.Style.STROKE);
        paintDoodle.setColor(Color.BLACK);
        paintDoodle.setStrokeCap(Paint.Cap.ROUND);
        CornerPathEffect effect = new CornerPathEffect(smoothRadius);
        paintDoodle.setPathEffect(effect);
//        lastBitmap = Bitmap.createBitmap(super.getWidth(), super.getHeight(), Bitmap.Config.RGB_565);
    }

    public void setRainbow() {
        rainbow = !(rainbow);
    }

    public void setWidth(float width) {
        paintDoodle.setStrokeWidth(width);
    }

    public void setColor(int color) {
        paintDoodle.setStyle(Paint.Style.STROKE);
        paintDoodle.setColor(color);
    }

    public void clear() {
        lastBitmap = null;
        path = null;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lastBitmap != null) canvas.drawBitmap(lastBitmap, 0, 0, null);
        if (path != null) canvas.drawPath(path, paintDoodle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float touchX = motionEvent.getX();
        float touchY = motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rainbow) {
                    lastRainbowPoint = new PointF(touchX, touchY);
                } else {
                    path = new Path();
                    path.moveTo(touchX, touchY);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (rainbow) {
                    paintDoodle.setColor(rainbowColor);
                    PointF currentPoint = new PointF(touchX, touchY);
                    float ds = (float)Math.sqrt(Math.pow(currentPoint.x - lastRainbowPoint.x, 2) +
                            Math.pow(currentPoint.y - lastRainbowPoint.y, 2));
                    Bitmap bitmap = null;
                    bitmap = Bitmap.createBitmap(super.getWidth(), super.getHeight(), Bitmap.Config.RGB_565);
                    bitmap.eraseColor(Color.WHITE);
                    Canvas canvas = new Canvas();
                    canvas.setBitmap(bitmap);
                    if (lastBitmap != null) canvas.drawBitmap(lastBitmap, 0, 0, null);
                    canvas.drawLine(lastRainbowPoint.x, lastRainbowPoint.y, currentPoint.x, currentPoint.y, paintDoodle);
                    lastBitmap = bitmap;

                    float[] hsv = new float[3];
                    Color.colorToHSV(rainbowColor, hsv);
                    hsv[0] = hsv[0] + 360f * ds / totalRainbowCycleDistance;
                    while (hsv[0] > 360) hsv[0]-= 360f;
                    rainbowColor = Color.HSVToColor(255, hsv);
                    lastRainbowPoint = currentPoint;
                } else {
                    path.lineTo(touchX, touchY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!rainbow) {
                    Bitmap bitmap = null;
                    bitmap = Bitmap.createBitmap(super.getWidth(), super.getHeight(), Bitmap.Config.RGB_565);
                    bitmap.eraseColor(Color.WHITE);
                    Canvas canvas = new Canvas();
                    canvas.setBitmap(bitmap);
                    if (lastBitmap != null) canvas.drawBitmap(lastBitmap, 0, 0, null);
                    canvas.drawPath(path, paintDoodle);
                    lastBitmap = bitmap;
                    path = null;
                }
                break;
        }

        invalidate();
        return true;
    }
}
