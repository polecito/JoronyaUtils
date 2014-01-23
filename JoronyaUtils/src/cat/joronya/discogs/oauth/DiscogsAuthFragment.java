package cat.joronya.discogs.oauth;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public class DiscogsAuthFragment extends Fragment
{
	private DiscogsOAuthHelper mHelper;
	private OnDiscogsAuth mOnDiscogsAuth;
	private RequestTokenTask mRequestTokenTask;
	private AccessTokenTask mAccessTokenTask;
	
	public interface OnDiscogsAuth 
	{
		public void authorizationSuccess(String uri);
		public void authorizationFailure();
		public void accessSuccess(String accessToken, String accessSecret);
		public void accessFailure();
	}
	
	public interface OnSignedREST
	{
		public void success(String message);
		public void error();
	}
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
        try {
        	mOnDiscogsAuth = (OnDiscogsAuth)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDiscogsAuth");
        }
	}
	
	@Override
	public void onDetach() 
	{
		mOnDiscogsAuth = null;
		super.onDetach();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		setRetainInstance(true);
		super.onActivityCreated(savedInstanceState);
	}
	
	/**
	 * Cridat quan es retorna de l'authorization URL, al rebre la callback URL.
	 */
	public void getAccessToken(Uri uri)
	{
		mAccessTokenTask = new AccessTokenTask();
		mAccessTokenTask.execute(uri);
	}

	/**
	 * Inicia el procés de login amb OAuth. Es un procés asincron,
	 * es rep resposta al metode 
	 */
	public void requestToken(String consumerKey, String consumerSecret, 
			String requestTokenURL, String accessTokenURL, String authorizationURL,
			String callbackURL) 
	{
		mHelper = new DiscogsOAuthHelper();
		mHelper.setConsumerKey(consumerKey);
		mHelper.setConsumerSecret(consumerSecret);
		mHelper.setRequestTokenURL(requestTokenURL);
		mHelper.setAccessTokenURL(accessTokenURL);
		mHelper.setAuthorizeURL(authorizationURL);
		mHelper.setCallbackURL(callbackURL);
		
		mRequestTokenTask = new RequestTokenTask();
		mRequestTokenTask.execute();
	}
	
	/**
	 *  Async task to request temporal token and get authorization Uri.
	 *  It's async, so it won't block UI thread.
	 */
	private class RequestTokenTask extends AsyncTask<String, Integer, String> 
	{
		@Override
		protected String doInBackground(String... params) 
		{
			try 
			{
				// inicialitzem el helper
				mHelper.init();
				
				String uri = mHelper.getRequestToken();
				return uri;
			}catch(Exception e) 
			{
				Log.d("JoronyaUtils", "Error on DiscogsOAuthHelper requestToken: "+e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String uri) 
		{
			// si no tenim listener, res
			if( mOnDiscogsAuth == null )
				return;

			// si tenim uri, responem success amb uri a obrir
			if(  uri != null ) 
				mOnDiscogsAuth.authorizationSuccess(uri);
			// altrament, error autenticant
			else
				mOnDiscogsAuth.authorizationFailure();
		}
	}
	
	/**
	 *  Async task to request access token and access secret.
	 *  It's async, so it won't block UI thread.
	 */
	private class AccessTokenTask extends AsyncTask<Uri, Integer, String[]> 
	{
		@Override
		protected String[] doInBackground(Uri... uri) 
		{
			String[] token = getVerifier(uri[0]);
			
			try {
				String[] accessTokens = mHelper.getAccessToken(token[1]);
				return accessTokens;
			} 
			catch(Exception e) 
			{
				Log.d("JoronyaUtils", "DiscogsAuthFragment.getAccessToken("+token[1]+") error: "+e.getMessage());
				return null;
			}
		}

		@Override
		protected void onPostExecute(String[] token) 
		{
			// si tenim tokens, responem success
			if(  token != null ) 
				mOnDiscogsAuth.accessSuccess(token[0], token[1]);
			// altrament, error autenticant
			else
				mOnDiscogsAuth.accessFailure();
		}
		
		private String[] getVerifier(Uri uri) 
		{
		    // extract the token if it exists
		    if (uri == null) {
		        return null;
		    }
		    
		    String token = uri.getQueryParameter("oauth_token");
		    String verifier = uri.getQueryParameter("oauth_verifier");
		    return new String[] { token, verifier };
		}
	}
}
