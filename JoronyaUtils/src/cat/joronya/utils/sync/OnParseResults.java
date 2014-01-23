package cat.joronya.utils.sync;

/**
 * Abstract interface that a syncer must implement to
 * parse results of a syncer request or manage errors.
 *  
 * @author pol
 */
public interface OnParseResults 
{
	public abstract void parseResults(int request, String items, int page, int pages);
	public abstract void parseError(int request, String error);
}
