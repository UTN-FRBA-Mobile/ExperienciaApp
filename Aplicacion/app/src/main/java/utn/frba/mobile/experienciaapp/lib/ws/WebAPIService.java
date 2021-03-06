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
            @Field("search") int search,
            @Field("token") String token
    );

    /**
     *
     */
    @Headers({ "Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    @FormUrlEncoded
    @POST(PARTIAL_URL)
    Call<ResponseWS> filterExperiencia(
            @Field("apartado") String apartado,
            @Field("key") String key,
            @Field("accion") String accion,
            @Field("intereses") String intereses,
            @Field("fecha_inicio") String fecha_inicio,
            @Field("fecha_fin") String fecha_fin,
            @Field("precio_inicio") String precio_inicio,
            @Field("precio_fin") String precio_fin,
            @Field("latitud") String latitud,
            @Field("longitud") String longitud,
            @Field("distancia") String distancia
    );

    @Headers({ "Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    @FormUrlEncoded
    @POST(PARTIAL_URL)
    Call<ResponseWS> signInTurista(
            @Field("apartado") String apartado,
            @Field("key") String key,
            @Field("accion") String accion,
            @Field("email") String email,
            @Field("firebase_token") String firebase_token,
            @Field("last_longitud") String last_longitud,
            @Field("last_latitud") String last_latitud
    );

    @Headers({ "Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    @FormUrlEncoded
    @POST(PARTIAL_URL)
    Call<ResponseWS> loginTurista(
            @Field("apartado") String apartado,
            @Field("key") String key,
            @Field("accion") String accion,
            @Field("email") String email,
            @Field("firebase_token") String firebase_token
    );

    @Headers({ "Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    @FormUrlEncoded
    @POST(PARTIAL_URL)
    Call<ResponseWS> reservarExperiencia(
            @Field("apartado") String apartado,
            @Field("key") String key,
            @Field("accion") String accion,
            @Field("turista") String turista_id,
            @Field("token") String token,
            @Field("fecha") String fecha_id,
            @Field("cantidad") String cantidad,
            @Field("total") String total
    );

    @Headers({ "Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    @FormUrlEncoded
    @POST(PARTIAL_URL)
    Call<ResponseWS> borrarReservaTurista(
            @Field("apartado") String apartado,
            @Field("key") String key,
            @Field("accion") String accion,
            @Field("login_token") String email,
            @Field("turista") String turista_id,
            @Field("reserva") String reserva_id
    );
}

