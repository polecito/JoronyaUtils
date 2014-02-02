package cat.joronya.discogs;

import cat.joronya.utils.sync.RESTSyncer;

public class ReleaseSyncer extends RESTSyncer
{
	public static final int REQUEST = 1;
	
	public ReleaseSyncer(int releaseId, boolean authenticated)
	{
		super(REQUEST);
		
		// configure REST request
		mUrl = "http://api.discogs.com/releases/"+releaseId;

		mItemsKey = null;
		mPaginated = false;
		mAuthenticated = authenticated;
		mGziped = true;
	}
}
