package android.prada.lab.goodthingmap.network;

import android.prada.lab.goodthingmap.model.GoodThingData;
import android.prada.lab.goodthingmap.model.GoodThingsData;
import android.prada.lab.goodthingmap.model.LikeResult;
import android.prada.lab.goodthingmap.model.CheckinResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by prada on 6/28/14.
 */
public interface GoodThingService {
    @GET("/good_thing/mobile/findTopStory")
    GoodThingData getTopStory();

    @GET("/good_thing/mobile/findGoodThings")
    void listStory(@Query("type") int type, Callback<GoodThingsData> callback);

    @GET("/good_thing/mobile/findGoodThings")
    void listStory(@Query("type") int type, @Query("lat") double latitude, @Query("lon") double longitude
        ,Callback<GoodThingsData> callback);

    @GET("/good_thing/mobile/findGoodThings")
    void listStory(@Query("lat") double latitude, @Query("lon") double longitude, Callback<GoodThingsData> callback);

    @GET("/good_thing/mobile/findGoodThings")
    void listStory(Callback<GoodThingsData> callback);

    @GET("/good_thing/mobile/getLikeNum")
    void requestLikeNum(@Query("rid") int rid,  Callback<LikeResult> callback);

    @GET("/good_thing/mobile/getCheckinNum")
    void requestCheckinNum(@Query("rid") int rid, Callback<CheckinResult> callback);

    @POST("/good_thing/mobile/addCheckin")
    void reportCheckin(@Query("uid") String uid, @Query("rid") int rid, @Query("cid") int checkinId, Callback<CheckinResult> callback);

    @POST("/good_thing/mobile/addLike")
    void likeGoodThing(@Query("uid") String uid, @Query("rid") int rid, Callback<LikeResult> callback);

    @POST("/good_thing/mobile/post")
    void postComment(@Query("uid") String uid, @Query("rid") int rid, @Query("message") String message, Callback<LikeResult> callback);
}
