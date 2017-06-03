package kr.ac.kaist.launduler.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.reactivex.Observable
import kr.ac.kaist.launduler.models.Machine
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Created by kyukyukyu on 29/05/2017.
 */
val laundulerService = Retrofit.Builder()
        .baseUrl("http://launduler.kaist.ac.kr/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
        .build()
        .create(LaundulerService::class.java)

interface LaundulerService {
    @Headers("Accept: application/json")
    @GET("/place/{placeId}/machine")
    fun getMachinesInPlace(@Path("placeId") placeId: String): Observable<List<Machine>>

    @Headers("Accept: application/json")
    @GET("/machine/{machineId}")
    fun getMachine(@Path("machineId") machineId: String): Observable<Machine>
}
