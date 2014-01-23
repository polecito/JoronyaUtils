package cat.joronya.discogs;

import cat.joronya.utils.sync.RESTSyncer;

public class IdentitySyncer extends RESTSyncer
{
	public static final int REQUEST = 0;
	
	public IdentitySyncer()
	{
		super(REQUEST);
		
		// configure REST request
		mUrl = "http://api.discogs.com/oauth/identity";

		mItemsKey = null;
		mPaginated = false;
		mAuthenticated = true;
		mGziped = true;
	}
}
