package com.example.ejemplo1;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;
import retrofit2.Call;


public interface TracksAPI {
    String ENDPOINT = "http://10.0.2.2:8080";

    @GET("/dsaApp/tracks")
    Call<List<Track>> getTracks();

    @POST("/dsaApp/tracks")
    Call<Track> createTrack(@Body Track track);

    @PUT("/dsaApp/tracks")
    Call<Track> editTrack(@Body Track track);

    @DELETE("/dsaApp/tracks/{id}")
    Call<Void> deleteTrack(@Path("id") String id);


    /*
    @GET("/repos/{owner}/{repo}/issues")
    Single<List<Track>> getIssues(@Path("owner") String owner, @Path("repo") String repository);


    @GET("/dsaApp/tracks/{id}")
    Call<Track> getTrack(@Path("id") String id);

     */
}
