package utn.frba.mobile.experienciaapp.lib.ws;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import utn.frba.mobile.experienciaapp.lib.errors.ErrorTreatment;
import utn.frba.mobile.experienciaapp.models.ModeloGenerico;

public class WSRetrofit {
    private static final String BASE_URL = "http://ingeniar.com.ar/";
    private static Retrofit retrofit;
    private static WebAPIService webApiService;

    private static final String APARTADO = "API";
    private static final String KEY = "14E95809B4E10C0F03D386D0FA96273C759815E5C9204FE26885F25868D04A1D";

    private static final String GET_EXPERIENCIAS = "get_experiencias";

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
                            results.add(specificObject);//TODO: Comprobar que es instancia de clazz
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

    public static Call<ResponseWS> GetExperiencias(){
        return WSRetrofit.getInstance().getExperiencias(
                APARTADO,
                KEY,
                GET_EXPERIENCIAS
        );
    }

    public static Callback<ResponseWS> ParseResponseWS(){
        return new Callback<ResponseWS>() {
            @Override
            public void onResponse(Call<ResponseWS> call, Response<ResponseWS> response) {
                response.body().setResult(ParseResult(response.body()));
                Log.e("RETROFIT","Success");
            }

            @Override
            public void onFailure(Call<ResponseWS> call, Throwable t) {
                // handle execution failures like no internet connectivity
                Log.e("RETROFIT","ERROR");
            }
        };
    }
}

