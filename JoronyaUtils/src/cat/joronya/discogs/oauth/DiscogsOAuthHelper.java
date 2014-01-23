package cat.joronya.discogs.oauth;

import java.io.UnsupportedEncodingException;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthProviderListener;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.http.HttpResponse;
import oauth.signpost.signature.HmacSha1MessageSigner;

public class DiscogsOAuthHelper 
{
	private String mRequestTokenURL;
	private String mAuthorizeURL;
	private String mAccessTokenURL;
	private String mConsumerKey;
	private String mConsumerSecret;
	private String mUserAgent;
	
	private CommonsHttpOAuthConsumer mConsumer;
	private CommonsHttpOAuthProvider mProvider;
	private String mCallbackURL;
	
	public DiscogsOAuthHelper(){}
	
	public void init()
		throws UnsupportedEncodingException 
	{
		mConsumer = new CommonsHttpOAuthConsumer(mConsumerKey, mConsumerSecret);
		
		mConsumer.setMessageSigner(new HmacSha1MessageSigner()); 
	    mProvider = new CommonsHttpOAuthProvider(mRequestTokenURL, mAccessTokenURL, mAuthorizeURL);
		
		mProvider.setOAuth10a(true);
	    mProvider.setListener(new OAuthProviderListener() 
	    {
			@Override
			public void prepareSubmission(HttpRequest request) throws Exception 
			{
			}
			
			@Override
			public void prepareRequest(HttpRequest request) throws Exception 
			{
				// set extra headers before sign
				request.setHeader("User-Agent", mUserAgent);
				request.setHeader("Accept-Encoding", "gzip");
			}
			
			@Override
			public boolean onResponseReceived(HttpRequest request, HttpResponse response) throws Exception 
			{
				return false;
			}
		});
	    
	    mCallbackURL = (mCallbackURL == null)?OAuth.OUT_OF_BAND:mCallbackURL;
	}
	
	public String getRequestToken() 
		throws OAuthMessageSignerException, OAuthNotAuthorizedException,
			OAuthExpectationFailedException, OAuthCommunicationException 
	{
		String authUrl = mProvider.retrieveRequestToken(mConsumer, mCallbackURL);
		return authUrl;
	}
	
	public String[] getAccessToken(String verifier)
		throws OAuthMessageSignerException, OAuthNotAuthorizedException,
			OAuthExpectationFailedException, OAuthCommunicationException 
	{
		mProvider.retrieveAccessToken(mConsumer, verifier);
		return new String[]{mConsumer.getToken(), mConsumer.getTokenSecret()};
	}
	
	public void setRequestTokenURL(String requestTokenURL) 
	{
		this.mRequestTokenURL = requestTokenURL;
	}

	public void setAuthorizeURL(String authorizeURL) 
	{
		this.mAuthorizeURL = authorizeURL;
	}

	public void setAccessTokenURL(String accessTokenURL) 
	{
		this.mAccessTokenURL = accessTokenURL;
	}

	public void setConsumerKey(String consumerKey) 
	{
		this.mConsumerKey = consumerKey;
	}

	public void setConsumerSecret(String consumerSecret) 
	{
		this.mConsumerSecret = consumerSecret;
	}
	
	public void setCallbackURL(String callbackURL)
	{
		this.mCallbackURL = callbackURL;
	}
}
