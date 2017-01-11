package com.enping.simplebookmark;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Chjeng-Lun SHIEH on 2017/1/11.
 */

public class BookmarkManager {

    private static BookmarkService service = getInstance();

    public static BookmarkService getInstance(){
        if(service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .build();
            service = retrofit.create(BookmarkService.class);
        }
        return service;
    }

    public interface BookmarkService {
        @GET("bookmarks")
        Call<List<Bookmark>> getBookmarks();

        @POST("bookmarks")
        Call<List<Bookmark>> addBookmark(@Body Bookmark bookmark);

        @PUT("bookmarks/{id}")
        Call<Void> editBookmark(@Path("id") int id, @Body Bookmark bookmark);

        @DELETE("bookmarks/{id}")
        Call<Void> delBookmark(@Path("id") int id);
    }
}
