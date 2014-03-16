package com.myapps.classroomsearch;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
 
public class ImageFlipper extends Activity implements OnTouchListener {
 
	ViewFlipper viewFlipper;
	Button Next, Previous;
	ImageView i1,i2,i3,i4;
	TextView t1,t2,t3;
	String classID;
	String blockID;
	String stairID;
	String floorID;
	
	 private static final String TAG = "Touch";
	    @SuppressWarnings("unused")
	    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

	    // These matrices will be used to scale points of the image
	    Matrix matrix = new Matrix();
	    Matrix savedMatrix = new Matrix();

	    // The 3 states (events) which the user is trying to perform
	    static final int NONE = 0;
	    static final int DRAG = 1;
	    static final int ZOOM = 2;
	    int mode = NONE;

	    // these PointF objects are used to record the point(s) the user is touching
	    PointF start = new PointF();
	    PointF mid = new PointF();
	    float oldDist = 1f;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.images);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            classID = extras.getString("classID");
            blockID = extras.getString("blockID");
            stairID = extras.getString("stairID");
            floorID =  extras.getString("floorID");
        }
        
        viewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper01);
        
        i1 = (ImageView) findViewById(R.id.image1);
        i2 = (ImageView) findViewById(R.id.image2);
        i3 = (ImageView) findViewById(R.id.image3);
        i4 = (ImageView) findViewById(R.id.image4);
        
        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        t3 = (TextView) findViewById(R.id.t3);
        
        i1.setOnTouchListener(this);
        i2.setOnTouchListener(this);
        i3.setOnTouchListener(this);
        i4.setOnTouchListener(this);
        
        setImage(classID,blockID,stairID,floorID);
        
        Next = (Button) findViewById(R.id.Next);
        Previous = (Button) findViewById(R.id.Previous);
        
        Next.setOnClickListener(new View.OnClickListener() {
			
		public void onClick(View v) {
			// TODO Auto-generated method stub
				
			viewFlipper.showNext();
		}
	});
        
        Previous.setOnClickListener(new View.OnClickListener() {
			
		public void onClick(View v) {
			// TODO Auto-generated method stub
				
			viewFlipper.showPrevious();
		}
	});
    }
    
	public void setImage(String classID, String blockID, String stairID, String floorID)
    {
    	classID = classID.toLowerCase();
    	i1.setImageResource(getResources().getIdentifier(blockID, "drawable", getPackageName()));
    	i2.setImageResource(getResources().getIdentifier(stairID, "drawable", getPackageName()));
    	i3.setImageResource(getResources().getIdentifier(classID, "drawable", getPackageName()));
    	//i4.setImageResource(getResources().getIdentifier(realID, "drawable", getPackageName()));
    	
    	t1.setText("Reach "+blockID+" block");
    	t2.setText("Reach Ground Floor\n"+"Your Destination : "+classID+", "+blockID+" block, "+floorID+" floor");
    	t3.setText("Reach "+floorID+" floor\n"+"Your Destination : "+classID+", "+blockID+" block, "+floorID+" floor");
    	/*
    	i1.setImageResource(R.drawable.elec1);
    	i2.setImageResource(R.drawable.elec2);
    	i3.setImageResource(R.drawable.elec3);
    	i4.setImageResource(R.drawable.elec4);
    	*/
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) 
    {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        dumpEvent(event);
        // Handle touch events here...

        switch (event.getAction() & MotionEvent.ACTION_MASK) 
        {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                                                savedMatrix.set(matrix);
                                                start.set(event.getX(), event.getY());
                                                Log.d(TAG, "mode=DRAG"); // write to LogCat
                                                mode = DRAG;
                                                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

                                                mode = NONE;
                                                Log.d(TAG, "mode=NONE");
                                                break;

            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                                                oldDist = spacing(event);
                                                Log.d(TAG, "oldDist=" + oldDist);
                                                if (oldDist > 5f) {
                                                    savedMatrix.set(matrix);
                                                    midPoint(mid, event);
                                                    mode = ZOOM;
                                                    Log.d(TAG, "mode=ZOOM");
                                                }
                                                break;

            case MotionEvent.ACTION_MOVE:

                                                if (mode == DRAG) 
                                                { 
                                                    matrix.set(savedMatrix);
                                                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                                                } 
                                                else if (mode == ZOOM) 
                                                { 
                                                    // pinch zooming
                                                    float newDist = spacing(event);
                                                    Log.d(TAG, "newDist=" + newDist);
                                                    if (newDist > 5f) 
                                                    {
                                                        matrix.set(savedMatrix);
                                                        scale = newDist / oldDist; // setting the scaling of the
                                                                                    // matrix...if scale > 1 means
                                                                                    // zoom in...if scale < 1 means
                                                                                    // zoom out
                                                        matrix.postScale(scale, scale, mid.x, mid.y);
                                                    }
                                                }
                                                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true; // indicate event was handled
    }

    /*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */

    private float spacing(MotionEvent event) 
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private void midPoint(PointF point, MotionEvent event) 
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(MotionEvent event) 
    {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) 
        {
            //sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) 
        {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
    }
 
  
}