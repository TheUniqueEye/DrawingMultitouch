package cs190i.cs.ucsb.edu.jingyandrawingmultitouch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by EYE on 20/04/2017.
 */

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    public Bitmap bitmap;
    private Canvas paintCanvas;
    private Paint paint;

    private Path mPath;
    private ArrayList<Path> mPaths = new ArrayList<>();
    public int brushSize;
    public ArrayList<Integer> brushSizes = new ArrayList<>();

    private boolean isPainted = false;

    private float x = 0;
    private float y = 0;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    public CanvasView(Context context, AttributeSet attrSet) {
        super(context, attrSet);

        holder = getHolder();
        holder.addCallback(this);
        mPath = new Path();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        if(!isPainted) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            width = displayMetrics.widthPixels;
            height = displayMetrics.heightPixels;
            int size = Math.max(width,height);

            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            paintCanvas = new Canvas();
            paintCanvas.setBitmap(bitmap);

            isPainted = true;

        }


        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        holder.unlockCanvasAndPost(canvas);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        x = event.getX();
        y = event.getY();
        //Log.d("position X", x+"");
        //Log.d("position Y", y+"");

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);

        // draw all paths
        paint.setStrokeWidth(brushSize);
        paintCanvas.drawPath(mPath, paint);

        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        holder.unlockCanvasAndPost(canvas);
        Log.d("how many strokes", mPaths.size() + "");

        return true;

    }

    private void touch_start(float x, float y) {

        mPath.moveTo(x, y);
        mX = x;
        mY = y;

    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }

        // draw current path
        //Log.i("brushSize",brushSize+"");
        paint.setStrokeWidth(brushSize);
        paintCanvas.drawPath(mPath, paint);
    }

    private void touch_up() {

        mPath.lineTo(mX, mY);

        mPaths.add(mPath);
        brushSizes.add(brushSize);

        mPath.reset();

    }


    public void setBrushSize(int size) {
        brushSize = size/2;
    }

    public void clearCanvas() {
        mPaths.clear();
        brushSizes.clear();

        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        paintCanvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        holder.unlockCanvasAndPost(canvas);

    }

    public void refreshView() {

    }


}
