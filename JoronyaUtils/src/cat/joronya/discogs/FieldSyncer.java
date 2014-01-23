package cat.joronya.discogs;

import cat.joronya.utils.sync.RESTSyncer;

public class FieldSyncer extends RESTSyncer
{
	public static final int REQUEST = 2;
	
	public FieldSyncer(String username, boolean authenticated)
	{
		super(REQUEST);
		
		// configure REST request
		mUrl = "http://api.discogs.com/users/"+username+"/collection/fields";
		
		mItemsKey = "fields";
		mPaginated = false;
		mAuthenticated = authenticated;
		mGziped = true;
	}
}
