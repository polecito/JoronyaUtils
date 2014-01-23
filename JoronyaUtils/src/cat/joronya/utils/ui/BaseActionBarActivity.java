package cat.joronya.utils.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * A base ActionBarActivity to customize it.
 * @author pol
 *
 */
public class BaseActionBarActivity extends ActionBarActivity 
{
	public static final String DEVICE_TABLET = "tablet";
	public static final String DEVICE_MOBILE = "mobile";
	
	protected int mLayoutResource;
	protected int mLayoutId;
	
	protected String tag;
	protected boolean isTablet;
    
    public void setmLayoutResource(int mLayoutResource){
		this.mLayoutResource = mLayoutResource;
	}
    
    public void setmLayoutId(int mLayoutId){
    	this.mLayoutId = mLayoutId;
    }
    
    /**
     * Given the tag from the root view, determine if this is a tablet
     * layout or not.
     * @return
     */
    public boolean isTablet()
    {
    	if( tag == null )
    		return false;
    	else
    		return DEVICE_TABLET.equals(tag);
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	
    	// set the layout for content view
        setContentView(mLayoutResource);
        
        View view = findViewById(mLayoutId);
        tag = (String)view.getTag(); 
        
        // definim el home button per tirar enrera
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
