package cat.joronya.utils.sync;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public abstract class RESTSyncer 
{
	// configuration attributes
	protected int mRequest;
	protected String mUrl;
	protected List<NameValuePair> mParams;
	protected boolean mGziped;
	protected boolean mAuthenticated;
	protected String mConsumerKey;
	protected String mConsumerSecret;
	protected String mAccessToken;
	protected String mAccessSecret;
	protected boolean mPaginated;
	protected String mUserAgent;
	protected String mItemsKey;
	protected OnParseResults mOnParseResultsListener;

	// class attributes
	protected CommonsHttpOAuthConsumer mConsumer;
	protected Pagination mPagination;
	protected int mCode;
	
	public RESTSyncer(int request)
	{
		mRequest = request;
	}
	
	public void setOnParseResultsListener(OnParseResults listener)
	{
		mOnParseResultsListener = listener;
	}
	
	public void sync()
	{
		// if it's an authenticated request, initiate consumer if it's not yet
		if( mAuthenticated && mConsumer == null )
		{
			if( mConsumerKey == null || mConsumerSecret == null || mAccessToken == null || mAccessSecret == null)
				throw new IllegalArgumentException("Authentication tokens needed!");
				
			mConsumer = new CommonsHttpOAuthConsumer(mConsumerKey, mConsumerSecret);
			mConsumer.setTokenWithSecret(mAccessToken, mAccessSecret);	
		}
		
		do
		{
			// si es la 1a volta, URL inicial
			// altrament agafem la seguent URL
			String url = ( mPagination == null )?mUrl:mPagination.urls.next;
			
			// executem el GET al REST
			getREST(url);
		}
		while( mPaginated && mPagination != null && mPagination.urls.next != null );
	}
	
	public void getREST(String url)
	{
		try 
		{
			// preparar GET amb headers (user-agent, gziped...)
			HttpGet request = new HttpGet(url);
			
			if( mUserAgent != null )
				request.setHeader("User-Agent", mUserAgent);
			
			if( mGziped )
				request.setHeader("Accept-Encoding", "gzip");
		
			// signar si cal
			if( mConsumer != null )
				mConsumer.sign(request);
		
			// send the request
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);
			
			// recuperem codi HTTP de la resposta
			mCode = response.getStatusLine().getStatusCode();
			if( mCode != 200 )
				throw new Exception("Response error ("+mCode+")");
			
			// recuperem el cos del missatge
			String message;
			if( mGziped )
			{
				InputStream is = AndroidHttpClient.getUngzippedContent(response.getEntity());
				message = convertStreamToString(is);
			}
			else
			{
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				response.getEntity().writeTo(os);
				message = os.toString(HTTP.UTF_8);
			}
			
			JSONObject oMessage = null;
			if( mPaginated )
			{
				oMessage = new JSONObject(message);
				Object oPagination = oMessage.get("pagination");
				
				// actualitzar el pagination
				Type paginationType = new TypeToken<Pagination>(){}.getType();
				Pagination pagination = new Gson().fromJson(oPagination.toString(), paginationType);
				mPagination = pagination;
			}
			
			// per defecte els items rebuts es tot el json
			String sItems = message;
			if( mItemsKey != null )
			{
				// si ens passen key a tractar, ho fem
				if( oMessage == null )
					oMessage = new JSONObject(message);
				
				Object oItems = oMessage.get(mItemsKey);
				sItems = oItems.toString();
			}
			
			// parsejar resultats, al OnParseResults
			if( mOnParseResultsListener != null )
			{
				int page = (mPagination == null)?0:mPagination.page;
				int pages = (mPagination == null)?0:mPagination.pages;
				mOnParseResultsListener.parseResults(mRequest, sItems, page, pages);
			}
			
			
		}
		catch(Exception e) 
		{
			if( mOnParseResultsListener != null )
				mOnParseResultsListener.parseError(mRequest, e.getMessage());
		}
	}
	
	private String convertStreamToString(InputStream is) 
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try 
		{
			while( (line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				is.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public void setConsumerKey(String consumerKey) {
		this.mConsumerKey = consumerKey;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.mConsumerSecret = consumerSecret;
	}

	public void setAccessToken(String accessToken) {
		this.mAccessToken = accessToken;
	}

	public void setAccessSecret(String accessSecret) {
		this.mAccessSecret = accessSecret;
	}

	public void setUserAgent(String userAgent) {
		this.mUserAgent = userAgent;
	}
}
