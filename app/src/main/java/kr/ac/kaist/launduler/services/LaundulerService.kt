package kr.ac.kaist.launduler.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.reactivex.Observable
import kr.ac.kaist.launduler.models.Machine
import kr.ac.kaist.launduler.models.Reservation
import kr.ac.kaist.launduler.models.Place
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by kyukyukyu on 29/05/2017.
 */
val laundulerService = Retrofit.Builder()
        .baseUrl("http://143.248.48.156:5500/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
        .build()
        .create(LaundulerService::class.java)

interface LaundulerService {
    @Headers("Accept: application/json")
    @GET("machine")
    fun getMachinesInPlace(@Query("location") placeId: Long): Observable<List<Machine>>

    @Headers("Accept: application/json")
    @GET("machine/{machineId}")
    fun getMachine(@Path("machineId") machineId: String): Observable<Machine>

    @Headers("Accept: application/json")
    @GET("reservation/")
    fun getReservation(@Query("userId") userId : String): Observable<List<Reservation>>

    @Headers("Accept: application/json")
    @GET("place")
    fun getPlaces(): Observable<List<Place>>
}
