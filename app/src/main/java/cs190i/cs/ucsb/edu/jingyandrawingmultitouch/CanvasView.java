package cs190i.cs.ucsb.edu.jingyandrawingmultitouch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

import static android.content.ContentValues.TAG;

/**
 * Created by EYE on 20/04/2017.
 */

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder holder;
    public Bitmap bitmap;
    private Canvas paintCanvas;
    private Paint paint;

    private Path mPath;
    private ArrayList<Path> mPaths = new ArrayList<>();
    public float brushSize;
    public ArrayList<Float> brushSizes = new ArrayList<>();

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
    public boolean onTouchEvent(MotionEvent event){

        x = event.getX();
        y = event.getY();
        Log.d("position X", x+"");
        Log.d("position Y", y+"");

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

        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);

        for(int i=0; i<mPaths.size(); i++){
            paint.setStrokeWidth(brushSizes.get(i));
            canvas.drawPath(mPaths.get(i),paint);
        }

        canvas.drawBitmap(bitmap, 0, 0, null);
        holder.unlockCanvasAndPost(canvas);

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
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {

        mPath.lineTo(mX, mY);

        mPaths.add(mPath);
        brushSizes.add(brushSize);

        mPath.reset();

    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        paintCanvas = new Canvas();
        paintCanvas.setBitmap(bitmap);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        //paint.setStrokeWidth(brushSize);
        paint.setColor(Color.RED);

        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        holder.unlockCanvasAndPost(canvas);



    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }




    public void setBrushSize(float size){
        brushSize = size;
    }

    public void clearCanvas(){
        mPath.reset();

        bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        paintCanvas = new Canvas();
        paintCanvas.setBitmap(bitmap);

        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        holder.unlockCanvasAndPost(canvas);

    }

    public void refreshView(){

    }


}
