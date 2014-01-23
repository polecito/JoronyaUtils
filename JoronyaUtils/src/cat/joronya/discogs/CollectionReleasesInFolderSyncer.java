package cat.joronya.discogs;

import cat.joronya.utils.sync.RESTSyncer;

public class CollectionReleasesInFolderSyncer extends RESTSyncer
{
	public static final int REQUEST = 3;
	
	public CollectionReleasesInFolderSyncer(String username, int folderId, boolean authenticated)
	{
		super(REQUEST);
		
		// configure REST request
		mUrl = "http://api.discogs.com/users/"+username+"/collection/folders/"+folderId+"/releases?sort=added&sort_order=asc&per_page=25";
		
		mItemsKey = "releases";
		mPaginated = true;
		mAuthenticated = authenticated;
		mGziped = true;
	}
}
