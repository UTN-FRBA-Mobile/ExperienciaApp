package utn.frba.mobile.experienciaapp.lib.ws;

public interface ReciveResponseWS {
    int GET_RESERVAS = 1;
    int SIGN_IN_TEST = 2; //TODO: DELETE SOLO PARA TEST

    public void ReciveResponseWS(ResponseWS responseWS,int accion);
}
