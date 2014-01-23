package cat.joronya.utils.sync;

/**
 * Stands for JSON object like this:
 * 
 * {
 *  "per_page": 1,
 *  "pages": 14,
 *  "page": 1,
 *  "items": 14,
 *  "urls": {
 *    "next": "http://api.discogs.com/users/example/collection/folders/1/releases?page=2&per_page=1",
 *    "last": "http://api.discogs.com/users/example/collection/folders/1/releases?page=2&per_page=14",
 *  }
 * 
 * @author pol
 */
public class Pagination 
{
	public int page;
	public int pages;
	public int items;
	public int per_page;
	
	public class URLS
	{
		protected String first;
		protected String prev;
		protected String next;
		protected String last;
		
		public URLS(){}
	}
	
	public URLS urls;
	
	public Pagination(){}
}
