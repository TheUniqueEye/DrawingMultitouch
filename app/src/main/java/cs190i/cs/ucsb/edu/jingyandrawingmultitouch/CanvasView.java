package cs190i.cs.ucsb.edu.jingyandrawingmultitouch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private HashMap<Integer, Float> mXs = new HashMap<Integer, Float>();
    private HashMap<Integer, Float> mYs = new HashMap<Integer, Float>();
    private Map<Integer, Path> mapPaths = new HashMap<>();

    private int[] colors = { Color.BLUE, Color.GREEN, Color.MAGENTA,
            Color.BLACK, Color.CYAN, Color.GRAY, Color.RED, Color.DKGRAY,
            Color.LTGRAY, Color.YELLOW };

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

        //x = event.getX();
        //y = event.getY();
        //Log.d("position X", x+"");
        //Log.d("position Y", y+"");

        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();

        // add a new point
        PointF f = new PointF();
        f.x = event.getX(pointerIndex);
        f.y = event.getY(pointerIndex);


        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
                //touch_start(x, y);
                for(int i=0; i<event.getPointerCount(); i++) {
                    Path path = new Path();
                    path.moveTo(event.getX(i), event.getY(i));
                    mapPaths.put(event.getPointerId(i), path);
                    mXs.put(event.getPointerId(i), event.getX(i));
                    mYs.put(event.getPointerId(i), event.getY(i));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //touch_move(x, y);
                for(int i=0; i<event.getPointerCount(); i++) {
                    int id = event.getPointerId(i);
                    Path p = mapPaths.get(id);
                    if(p!=null){
                        float x = event.getX(i);
                        float y = event.getY(i);
                        p.quadTo(mXs.get(event.getPointerId(i)), mYs.get(event.getPointerId(i)), (x + mXs.get(event.getPointerId(i))) / 2,
                                (y + mYs.get(event.getPointerId(i))) / 2);
                        mXs.put(event.getPointerId(i), event.getX(i));
                        mYs.put(event.getPointerId(i), event.getY(i));
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //touch_up();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                for(int i=0; i<event.getPointerCount(); i++) {
                    Path path = new Path();
                    path.moveTo(event.getX(i), event.getY(i));
                    mapPaths.put(event.getPointerId(i), path);
                    mXs.put(event.getPointerId(i), event.getX(i));
                    mYs.put(event.getPointerId(i), event.getY(i));
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int id = event.getPointerId(i);
                    Path p = mapPaths.get(id);
                    if (p != null) {
                        p.lineTo(event.getX(i), event.getY(i));
                        invalidate();
                        mapPaths.remove(event.getPointerId(i));
                        mXs.remove(event.getPointerId(i));
                        mYs.remove(event.getPointerId(i));
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);

        // draw all paths

        paint.setStrokeWidth(brushSize);
        for (int size = mapPaths.size(), i = 0; i < size; i++) {
            paint.setColor(colors[i]);
            Path path = mapPaths.get(i);
            if (path != null) {
                paintCanvas.drawPath(path, paint);
            }
        }


        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        holder.unlockCanvasAndPost(canvas);
        //Log.d("how many strokes", mPaths.size() + "");

        return true;

    }


    public void setBrushSize(int size) {
        brushSize = size/2;
    }

    public void clearCanvas() {
        mPaths.clear();
        brushSizes.clear();

        mapPaths.clear();
        mXs.clear();
        mYs.clear();

        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        paintCanvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        holder.unlockCanvasAndPost(canvas);

    }

    // comment: functions for single finger drawing

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


}
