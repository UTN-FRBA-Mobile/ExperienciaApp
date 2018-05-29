package utn.frba.mobile.experienciaapp.lib.ws;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WebAPIService {
    String PARTIAL_URL = "/clientes/experiencias/";

    /**
     * For testing purpose
     */
    @Headers({ "Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    @FormUrlEncoded
    @POST(PARTIAL_URL)
    Call<String> getRawResponse(
            @Field("apartado") String apartado,
            @Field("key") String key,
            @Field("accion") String accion
    );

    /**
     * Trae informacion dependiendo del paramentro @accion que se le pase
     */
    @Headers({ "Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    @FormUrlEncoded
    @POST(PARTIAL_URL)
    Call<ResponseWS> getGeneralInformation(
            @Field("apartado") String apartado,
            @Field("key") String key,
            @Field("accion") String accion
    );

    /**
     * Trae informacion dependiendo del paramentro @accion que se le pase y el @search, que puede ser un id, un email, ...
     */
    @Headers({ "Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    @FormUrlEncoded
    @POST(PARTIAL_URL)
    Call<ResponseWS> getInformationOf(
            @Field("apartado") String apartado,
            @Field("key") String key,
            @Field("accion") String accion,
            @Field("search") String search
    );
}

