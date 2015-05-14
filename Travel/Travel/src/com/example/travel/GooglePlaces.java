package com.example.travel;
import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.jackson.JacksonFactory;
//import com.google.api.client.json.
public class GooglePlaces {

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final String API_KEY = "AIzaSyCRLa4LQZWNQBcjCYcIVYA45i9i8zfClqc";
	private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
	private static final String PLACES_TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
	private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";
	private double _latitude;
	private double _longitude;
	private double _radius;
	public PlaceList search(double latitude, double longitude,double radius,String types) throws Exception {
		this._latitude = latitude;
		this._longitude = longitude;
		this._radius = radius;
		
		HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
		HttpRequest request = httpRequestFactory
				.buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
		request.getUrl().put("key", API_KEY);
		request.getUrl().put("location", _latitude + "," + _longitude);
		request.getUrl().put("radius", _radius);
		if(types != null)
			request.getUrl().put("types", types); 
		PlaceList list = request.execute().parseAs(PlaceList.class);
		return list;
	}
	
	public static HttpRequestFactory createRequestFactory(final HttpTransport transport) {
		return transport.createRequestFactory(new HttpRequestInitializer() {
			public void initialize(HttpRequest request) {
				GoogleHeaders headers = new GoogleHeaders();
				headers.setApplicationName("AndroidHive-Places-Test");
				request.setHeaders(headers);
				JsonHttpParser parser = new JsonHttpParser(new JacksonFactory());
				request.addParser(parser);
				}
			});
		} 
	
	public PlaceDetails getPlaceDetails(String reference) throws Exception {
		try {
			HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
			HttpRequest request = httpRequestFactory
					.buildGetRequest(new GenericUrl(PLACES_DETAILS_URL));
			request.getUrl().put("key", API_KEY);
			request.getUrl().put("reference", reference);
			request.getUrl().put("sensor", "false");
			PlaceDetails place = request.execute().parseAs(PlaceDetails.class);
			return place;
			} catch (HttpResponseException e) {
				
				throw e;
				}
		} 
}
