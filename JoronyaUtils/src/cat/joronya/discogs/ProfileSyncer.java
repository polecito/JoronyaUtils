package cat.joronya.discogs;

import cat.joronya.utils.sync.RESTSyncer;

public class ProfileSyncer extends RESTSyncer
{
	public static final int REQUEST = 1;
	
	public ProfileSyncer(String username, boolean authenticated)
	{
		super(REQUEST);
		
		// configure REST request
		mUrl = "http://api.discogs.com/users/"+username;

		mItemsKey = null;
		mPaginated = false;
		mAuthenticated = authenticated;
		mGziped = true;
	}
}
