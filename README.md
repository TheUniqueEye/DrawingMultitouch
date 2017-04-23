# DrawingMultitouch

## A multi-finger-touch drawing application 

#### This multi-finger application was approached through the following steps:
#### 1. Setup basic layout in XML using LinearLayout.
#### 2. Set layout view to Main_activity, implement interactive functions : set onClickListener to clear button, set onSeekBarChangeListener to seekBar.
#### 3. Create CanvasView class extends the surfaceView and set to the layout XML.
#### 4. Override the onSurfaceChanged function to display canvas, Override the onTouchEvent to implement single-finger drawing functionality. (using Path objects to achieve smooth effect)
#### 5. Implement size slide and clear button to the drawing functionality.
#### 6. Solved the auto refresh problem when screen orientation changed.
#### 7. Implement multi-finger drawing functionality by storing points’ location and paths into HashMap. (The multi-finger part is a bit tricky, I took some time to figure out how Android detects multi touch, what’s the relationship between different touches, and how to store all the date. )
#### 8. Set different color to different fingers. 
#### 9. All done, happy!  : )

#### References

##### 1. Get seeker’s value
##### http://stackoverflow.com/questions/15326290/get-android-seekbar-value-and-display-it-on-screen

##### 2. Single-finger drawing using Path object
##### http://cell0907.blogspot.com/2013/10/tutorial-for-drawing-in-android.html
##### http://stackoverflow.com/questions/36266994/how-to-draw-to-canvas-from-surfaceview
##### http://stackoverflow.com/questions/16650419/draw-in-canvas-by-finger-android
##### http://www.vogella.com/tutorials/AndroidTouch/article.html

##### 3. Get screen width and height
##### http://stackoverflow.com/questions/4743116/get-screen-width-and-height

##### 4. Multi-finger drawing 
##### http://stackoverflow.com/questions/11966692/android-smooth-multi-touch-drawing

