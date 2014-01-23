package cat.joronya.utils.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import cat.joronya.utils.R;

public abstract class DrawerActivity extends BaseActionBarActivity 
{
	protected String[] mDrawerTitles;
	protected String[] mDrawerIcons;
	
    protected DrawerLayout mDrawerLayout;
    protected ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
        
        // init drawer layout
        mDrawerTitle = getResources().getString(R.string.app_name);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new DrawerAdapter(this, R.layout.drawer_item, getDrawerItems()));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) 
        {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) 
            {
            	getSupportActionBar().setTitle(mTitle);
            	ActivityCompat.invalidateOptionsMenu(DrawerActivity.this);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) 
            {
            	getSupportActionBar().setTitle(mDrawerTitle);
            	ActivityCompat.invalidateOptionsMenu(DrawerActivity.this);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    } 
    
    // get items for drawer
    protected abstract DrawerItem[] getDrawerItems();
    
    
    @Override
    public void setTitle(CharSequence title) 
    {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) 
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) 
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) 
    {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        
        for(int i=0; i< menu.size(); i++)
        	menu.getItem(i).setVisible(!drawerOpen);
        
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    
    private class DrawerItemClickListener implements ListView.OnItemClickListener 
    {
       @SuppressWarnings("rawtypes")
       public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    
    /** 
     * Swaps fragments in the main content view 
     */
    protected abstract void selectItem(int position);
}
