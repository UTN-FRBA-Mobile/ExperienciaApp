package utn.frba.mobile.experienciaapp.lib.ws;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import utn.frba.mobile.experienciaapp.HomeActivity;
import utn.frba.mobile.experienciaapp.lib.errors.ErrorTreatment;
import utn.frba.mobile.experienciaapp.models.ModeloGenerico;

public class WSRetrofit {
    private static final String BASE_URL = "http://ingeniar.com.ar/";
    private static Retrofit retrofit;
    private static WebAPIService webApiService;

    public static final String APARTADO = "API";
    public static final String KEY = "14E95809B4E10C0F03D386D0FA96273C759815E5C9204FE26885F25868D04A1D";

    public static final String GET_EXPERIENCIAS = "get_experiencias";
    public static final String GET_DETALLE_EXPERIENCIA = "get_detalle_experiencia";
    public static final String GET_INTERESES = "get_intereses";
    public static final String GET_PERFIL_PRODUCTOR = "get_productor_perfil";
    public static final String GET_PUNTOS_INTERES_OF_EXPERIENCIA = "get_puntos_interes_of_experiencia";
    public static final String GET_MI_PERFIL = "get_mi_perfil";

    public static final String FILTER_EXPERIENCIAS = "filter_experiencias";

    private WSRetrofit(){}  //private constructor.

    private static void initializeWebApiService(){
        if(retrofit == null){
            initializeRetrofit();
        }

        webApiService = retrofit.create(WebAPIService.class);
    }

    private static void initializeRetrofit(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static WebAPIService getInstance(){
        if (webApiService == null){ //if there is no instance available... create new one
            initializeWebApiService();
        }

        return webApiService;
    }


    public static List<Object> ParseResult(ResponseWS responseWS){
        List<Object> results = new ArrayList<Object>();
        if(responseWS != null && !responseWS.getResult().isEmpty()) {
            for (Object obj:responseWS.getResult()) {
                if(obj instanceof LinkedTreeMap) {
                    String className = ModeloGenerico.GetClassOf((LinkedTreeMap)obj);

                    if (className != null) {
                        try {
                            Class<?> clazz = Class.forName(className);
                            Gson gson = new GsonBuilder().setLenient().create();
                            Object specificObject = gson.fromJson(gson.toJson(obj), clazz);
                            if(specificObject instanceof ModeloGenerico && specificObject.getClass() == clazz) {
                                results.add(specificObject);
                            }
                        } catch (ClassNotFoundException e) {
                            ErrorTreatment.TreatExeption(e);
                        }
                    } else {
                        ErrorTreatment.TreatExeption(new Exception("Null ModeloGenerico.GetClassOf of " + obj.toString()));
                    }
                }else{
                    ErrorTreatment.TreatExeption(new Exception("Object from ResponseWS not instance of LinkedTreeMap," + obj.toString()));
                }
            }
        }
        return results;
    }

    /*
    public static List<Object> ParseResult(ResponseWS responseWS){
        List<Object> results = new ArrayList<>();
        if(!responseWS.getResult().isEmpty()) {
            for (Object obj:responseWS.getResult()) {
                if(obj instanceof LinkedTreeMap) {
                    String className = ModeloGenerico.GetClassOf((LinkedTreeMap)obj);

                    if (className != null) {
                        try {
                            Class<?> clazz = Class.forName(className);
                            Gson gson = new GsonBuilder().setLenient().create();
                            Object specificObject = gson.fromJson(gson.toJson(obj), clazz);
                            if(specificObject instanceof ModeloGenerico) {
                                ModeloGenerico modeloGenerico = (ModeloGenerico) specificObject;
                                results.add(modeloGenerico);//TODO: Comprobar que es instancia de clazz
                            }
                        } catch (ClassNotFoundException e) {
                            ErrorTreatment.TreatExeption(e);
                        }
                    } else {
                        ErrorTreatment.TreatExeption(new Exception("Null ModeloGenerico.GetClassOf of " + obj.toString()));
                    }
                }else{
                    ErrorTreatment.TreatExeption(new Exception("Object from ResponseWS not instance of LinkedTreeMap," + obj.toString()));
                }
            }
        }
        return results;
    }
    */

    public static Call<String> GetRawResponse(String accion){
        return WSRetrofit.getInstance().getRawResponse(
                APARTADO,
                KEY,
                accion
        );
    }

    public static Call<ResponseWS> GetExperiencias(){
        return WSRetrofit.getInstance().getGeneralInformation(
                APARTADO,
                KEY,
                GET_EXPERIENCIAS
        );
    }

    public static Call<ResponseWS> GetPerfilProductor(String id){
        return WSRetrofit.getInstance().getInformationOf(
                APARTADO,
                KEY,
                GET_PERFIL_PRODUCTOR,
                id
        );
    }

    public static Callback<ResponseWS> ParseResponseWS(final ReciveResponseWS reciveResponseWS,final int accion){
        return new Callback<ResponseWS>() {
            @Override
            public void onResponse(Call<ResponseWS> call, Response<ResponseWS> response) {
                if(response != null && response.body() != null) {
                    response.body().setResult(ParseResult(response.body()));
                    reciveResponseWS.ReciveResponseWS(response.body(), accion);
                    Log.e("RETROFIT", "Success");
                }else{
                    Log.e("RETROFIT","ERROR Response NULL");
                    reciveResponseWS.ReciveResponseWS(null, accion);
                }
            }

            @Override
            public void onFailure(Call<ResponseWS> call, Throwable t) {
                // handle execution failures like no internet connectivity
                Log.e("RETROFIT","ERROR");
                reciveResponseWS.ReciveResponseWS(null, accion);
            }
        };
    }

    public static Callback<String> ProcessRawResponse(){
        return new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("RETROFIT","Success");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // handle execution failures like no internet connectivity
                Log.e("RETROFIT","ERROR");
            }
        };
    }
}

