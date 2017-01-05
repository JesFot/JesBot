package me.jesfot.jesbot.youtube;

import java.io.IOException;
import java.util.List;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import me.jesfot.jesbot.Statics;

public class Search
{
	public static final long DEFAULT_RETURNED_VIDEOS = 1;
	
	private static YouTube youtube;
	
	public static List<Video> search(String query, long maxVideos)
	{
		youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
			public void initialize(HttpRequest request) throws IOException
			{
				// Nothing here;
			}
		}).setApplicationName("JesBot").build();
		
		try
		{
			YouTube.Search.List search = youtube.search().list("id");
			search.setKey(Statics.YT_API_KEY);
			search.setQ(query);
			
			search.setType("video");
			
			search.setFields("items(id(kind,videoId))");
			search.setMaxResults(maxVideos);
			
			SearchListResponse response = search.execute();
			List<SearchResult> searchResults = response.getItems();
			
			YouTube.Videos.List vidsearch = youtube.videos().list("id,snippet,statistics,contentDetails");
			vidsearch.setKey(Statics.YT_API_KEY);
			
			for(SearchResult r : searchResults)
			{
				if(r.getId().getKind().equals("youtube#video"))
				{
					vidsearch.setId(vidsearch.getId() + "," + r.getId().getVideoId());
				}
			}
			
			vidsearch.setFields("etag,eventId,items,kind,nextPageToken,pageInfo,prevPageToken,tokenPagination,visitorId");
			vidsearch.setMaxResults(maxVideos);
			VideoListResponse r = vidsearch.execute();
			List<Video> vids = r.getItems();
			return vids;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}