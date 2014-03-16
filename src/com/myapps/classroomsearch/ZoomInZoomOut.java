package com.myapps.classroomsearch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ZoomInZoomOut extends Activity implements OnTouchListener 
{
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
    
    ImageView Imgview;
    EditText classId;
    TextView found;
    TextView classLoc;
    Button imgNav;
   
    String[] block = {"Electrical (EE)", "Information Technology (IT)", "Civil (CE)", "Mechanical (ME)"};
    String[] short_block = {"elec", "it", "civil", "mech"};
    int blockNo;
    char floor;
    int classNo;
    String classID;
    String blockName;
    String sblockName;
    String stairs;
    String floorname;
    int flag;
    
    InputMethodManager inputManager;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Imgview = (ImageView) findViewById(R.id.imageView1);
        Imgview.setOnTouchListener(this);
        
        classId = (EditText) findViewById(R.id.classId);
        
        found = (TextView) findViewById(R.id.found);
        
        classLoc = (TextView) findViewById(R.id.classLoc);
        
        blockNo=-1;
        floor=0;
        classNo=0;
        flag=0;
        
        imgNav = (Button) findViewById(R.id.pNav);
        
        inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
    }
    
    @SuppressLint("DefaultLocale")
	public void showClass(View v)
    {
    	classID = classId.getText().toString();
    	found.setText("");
    	classLoc.setText("");
    	Imgview.setImageDrawable(null);
    	
    	if(classID.length()==6)
    	{
    	blockNo = (int) classID.charAt(2) - (int)'1';
    	blockName="";
  
    	if(blockNo>=0 && blockNo<=3)
    	{
    		blockName = block[blockNo];
    	}
    	
    	sblockName = short_block[blockNo];
    	
    	floor = classID.charAt(3);
    	floorname="";
    	switch(floor)
    	{
    	case 'G' : floorname = "Ground";
    				break;
    	case 'F' : floorname = "First";
    				break;
    	case 'S' : floorname = "Second";
    				break;
    	case 'T' : floorname = "Third";
    	}
    	
    	if(floor=='G')
    		stairs="nostair";
    	else
    		stairs="stair"+1;
    	
    	classNo = (int)classID.charAt(5) - (int)'0';
    	String sub ="";
    	switch(classNo)
    	{
    		case  1 : sub="st";
    					break;
    		case  2 : sub="nd";
						break;
    		case  3 : sub="rd";
						break;
			default : sub="th";
    	}
    	
    	if(blockName=="" || floorname=="" || classNo==0)
    	{
    		found.setText("Invalid Classroom Id !!");
    	}
    	else
    	{
    		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    		
    		found.setText("Proceed Towards :");
    		
    		String finalname = blockName+" Block, "+floorname+" Floor, "+classNo+sub+" Classroom";
    		classLoc.setText(finalname);
    		
    		switch(blockNo)
    		{
    		 case 0 : Imgview.setImageResource(R.drawable.relec);
    		 			break;
    		 case 1 : Imgview.setImageResource(R.drawable.rit);
	 					break;
    		 case 2 : Imgview.setImageResource(R.drawable.rcivil);
	 					break;
    		 case 3 : Imgview.setImageResource(R.drawable.rmech);
	 					break;
    		}
    		
    	}
    	}
    	else
    	{
    	classID = classID.toLowerCase();
    	String nclassID = classID.replaceAll(" ","");
    	classID = nclassID;
    	found.setText("Proceed Towards :");
		
    	if(classID.equals("controlsystem")||classID.equals("electricalengineering")||classID.equals("intrumentationandieal")||classID.equals("processcontrolandmicroprocessor"))
    	{stairs="stair1";floorname="First";flag=1;}

    	else if(classID.equals("ca")||classID.equals("ipm")||classID.equals("linearintegratedcircuit")||classID.equals("microprocessor")||classID.equals("seniormeasurement")||classID.equals("texasinstruments"))
    	{stairs="stair1";floorname="Second";flag=1;}

    	else if(classID.equals("computation")||classID.equals("ail")||classID.equals("dbms")||classID.equals("lans")||classID.equals("programming")||classID.equals("seminarroom"))
    	{stairs="stair1";floorname="Third";flag=1;}

    	else if(classID.equals("roboticsandmachineintelligence")||classID.equals("telecommunication"))
    	{stairs="stair2";floorname="First";flag=1;}

    	else if(classID.equals("computationandinstrumentation")||classID.equals("edusathall")||classID.equals("vlsicad"))
    	{stairs="stair2";floorname="Second";flag=1;}

    	else if(classID.equals("electronics1")||classID.equals("electronics2"))
    	{stairs="stair2";floorname="Third";flag=1;}

    	else if(classID.equals("electricalscience")||classID.equals("juniormachine")||classID.equals("powerelectronics")||classID.equals("projectandresearch")||classID.equals("seniormachine")||classID.equals("pcb"))
    	{stairs="nostair";floorname="Ground";flag=1;}

    	if(flag==0)
    	{
    		found.setText("Invalid Classroom/Lab Id !!");
    	}
    	else
    	{
    		sblockName="elec";
    		found.setText("Proceed Towards :");
    		classLoc.setText("Electrical Block, "+floorname+" floor");
    		Imgview.setImageResource(R.drawable.relec);
    	}
    	
    	}
    	
    }
    
    public void showPhotoNav(View v)
    {
    	Intent intent=new Intent();
		intent.setClass(ZoomInZoomOut.this,ImageFlipper.class);
		intent.putExtra("classID", classID);
		intent.putExtra("blockID", sblockName);
		intent.putExtra("stairID", stairs);
		intent.putExtra("floorID", floorname);
	    startActivity(intent);
    }
    
    public void showMapsNav(View v)
    {

		Toast.makeText(getApplicationContext(), "Internet not available !!" , Toast.LENGTH_LONG).show();
    }
   
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    */
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		
		case R.id.itemSettings:
			startActivity(new Intent(this, credits.class));
			break;
		default: break;
		}
		
		return true;
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