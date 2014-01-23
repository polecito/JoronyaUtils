package cat.joronya.discogs;

import cat.joronya.utils.sync.RESTSyncer;

public class FolderSyncer extends RESTSyncer
{
	public static final int REQUEST = 2;
	
	public FolderSyncer(String username, boolean authenticated)
	{
		super(REQUEST);
		
		// configure REST request
		mUrl = "http://api.discogs.com/users/"+username+"/collection/folders";
		
		mItemsKey = "folders";
		mPaginated = false;
		mAuthenticated = authenticated;
		mGziped = true;
	}
}
