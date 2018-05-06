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

    @GET("raw/E3uy8eqJ")
    Call<JsonObject> testJson();

    @Headers({ "Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    @FormUrlEncoded
    @POST(PARTIAL_URL)
    Call<ResponseWS> getExperiencias(
            @Field("apartado") String apartado,
            @Field("key") String key,
            @Field("accion") String accion
    );
}

