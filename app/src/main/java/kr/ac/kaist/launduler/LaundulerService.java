package kr.ac.kaist.launduler;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by kyukyukyu on 29/05/2017.
 */
public interface LaundulerService {
    @Headers({"Accept: application/json"})
    @GET("/place/{placeId}/machine")
    Call<ExploreFragment> machines(@Path("placeId") String placeId);
}
