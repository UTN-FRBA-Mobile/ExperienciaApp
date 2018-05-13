package utn.frba.mobile.experienciaapp.models;

import com.google.gson.internal.LinkedTreeMap;

import utn.frba.mobile.experienciaapp.lib.errors.ErrorTreatment;

public class ModeloGenerico {
    private static final String PROPETY_CLASS = "class";

    public static String GetClassOf(LinkedTreeMap obj)
    {
        String clase = null;
        clase = obj.get(PROPETY_CLASS).toString();
        return GetModelsPackage()+clase;
    }

    public static String GetModelsPackage()
    {
        ModeloGenerico mClass = new ModeloGenerico();
        Package pack = mClass.getClass().getPackage();
        return (pack.toString().replace("package ","")).split(",")[0] + ".";
    }


}
