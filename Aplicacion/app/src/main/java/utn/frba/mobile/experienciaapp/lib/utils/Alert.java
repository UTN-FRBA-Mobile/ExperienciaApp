package utn.frba.mobile.experienciaapp.lib.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import utn.frba.mobile.experienciaapp.R;


/**
 * Created by Martin Cattaneo on 11/9/17.
 */

public class Alert {

    public Activity act;

    private Dialog alert = null;
    protected Button alertCancel,alertAccept,alertExtra;
    protected TextView title_tv,message_alert;
    private LinearLayout include_ll,edit_ll,loading_ll;
    private EditText et_edit;

    private static Dialog alert_loading;


    public Alert(Activity act){
        this.act = act;
    }

    public void Show(final String text){
        Runnable alert_ui = new Runnable() {
            public void run() {
                alert = new Dialog(act, R.style.CustomDialogTheme);
                alert.setContentView(R.layout.dialog_alert);
                alert.setCancelable(false);

                alertCancel = (Button) alert.findViewById(R.id.button_cancel);
                message_alert = (TextView) alert.findViewById(R.id.message_alert);
                message_alert.setText(text);
                alertCancel.setText("Aceptar");

                alertCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });

                alert.show();

                //bug de tamaño
                Display display =((WindowManager) act.getBaseContext().getSystemService(act.getBaseContext().WINDOW_SERVICE)).getDefaultDisplay();
                int width  = display.getWidth();
                int height = display.getHeight();
                alert.getWindow().setLayout(width,height);
            }
        };

        act.runOnUiThread(alert_ui);
    }

    public void Show(final String text,final String title){
        Runnable alert_ui = new Runnable() {
            public void run() {
                alert = new Dialog(act, R.style.CustomDialogTheme);
                alert.setContentView(R.layout.dialog_alert);
                alert.setCancelable(false);

                title_tv = (TextView) alert.findViewById(R.id.title_tv);
                title_tv.setText(title);

                alertCancel = (Button) alert.findViewById(R.id.button_cancel);
                message_alert = (TextView) alert.findViewById(R.id.message_alert);
                message_alert.setText(text);
                alertCancel.setText("Aceptar");

                alertCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });

                alert.show();

                //bug de tamaño
                Display display =((WindowManager) act.getBaseContext().getSystemService(act.getBaseContext().WINDOW_SERVICE)).getDefaultDisplay();
                int width  = display.getWidth();
                int height = display.getHeight();
                alert.getWindow().setLayout(width,height);
            }
        };

        act.runOnUiThread(alert_ui);
    }

    public void ShowConfirmation(final String text, final String title, final View.OnClickListener onClickListener, final boolean cancel){
        Runnable alert_ui = new Runnable() {
            public void run() {
                alert = new Dialog(act, R.style.CustomDialogTheme);
                alert.setContentView(R.layout.dialog_alert);
                alert.setCancelable(false);

                title_tv = (TextView) alert.findViewById(R.id.title_tv);
                title_tv.setText(title);

                alertAccept = (Button) alert.findViewById(R.id.button_accept);
                alertCancel = (Button) alert.findViewById(R.id.button_cancel);
                message_alert = (TextView) alert.findViewById(R.id.message_alert);
                message_alert.setText(Html.fromHtml(text));
                alertCancel.setText("Cancelar");
                alertAccept.setVisibility(View.VISIBLE);
                alertAccept.setText("Aceptar");
                alertAccept.setOnClickListener(onClickListener);

                alertCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });

                if(!cancel){
                    alertCancel.setVisibility(View.INVISIBLE);
                }

                alert.show();

                //bug de tamaño
                Display display =((WindowManager) act.getBaseContext().getSystemService(act.getBaseContext().WINDOW_SERVICE)).getDefaultDisplay();
                int width  = display.getWidth();
                int height = display.getHeight();
                alert.getWindow().setLayout(width,height);
            }
        };

        act.runOnUiThread(alert_ui);
    }

    public void ShowConfirmation(final String text, final String title, final View.OnClickListener onClickListener){
        ShowConfirmation(text,title,onClickListener,true);
    }

    public void ShowView(final View view,final String title){
        Runnable alert_ui = new Runnable() {
            public void run() {
                alert = new Dialog(act, R.style.CustomDialogTheme);
                alert.setContentView(R.layout.dialog_alert);
                alert.setCancelable(false);

                title_tv = (TextView) alert.findViewById(R.id.title_tv);
                title_tv.setText(title);

                message_alert = (TextView) alert.findViewById(R.id.message_alert);
                message_alert.setVisibility(View.GONE);

                alertCancel = (Button) alert.findViewById(R.id.button_cancel);
                alertCancel.setText("Cancelar");
                alertCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });

                ScrollView scrollView = (ScrollView) alert.findViewById(R.id.scroll_alert);
                int height_px = (int) (250 * Resources.getSystem().getDisplayMetrics().density);
                scrollView.getLayoutParams().height = height_px;
                scrollView.setVisibility(View.VISIBLE);

                include_ll = (LinearLayout) alert.findViewById(R.id.include_ll);

                include_ll.addView(view);

                alert.show();

                //bug de tamaño
                Display display =((WindowManager) act.getBaseContext().getSystemService(act.getBaseContext().WINDOW_SERVICE)).getDefaultDisplay();
                int width  = display.getWidth();
                int height = display.getHeight();
                alert.getWindow().setLayout(width,height);


            }
        };

        act.runOnUiThread(alert_ui);
    }

    public void Loading(){
        Runnable alert_ui = new Runnable() {
            public void run() {
                alert_loading = new Dialog(act, R.style.CustomDialogTheme);
                alert_loading.setContentView(R.layout.dialog_alert);
                alert_loading.setCancelable(true);

                title_tv = (TextView) alert_loading.findViewById(R.id.title_tv);
                title_tv.setText("Procesando...");

                loading_ll = (LinearLayout) alert_loading.findViewById(R.id.ll_progress_bar);
                loading_ll.setVisibility(View.VISIBLE);

                alertCancel = (Button) alert_loading.findViewById(R.id.button_cancel);
                alertCancel.setVisibility(View.GONE);
                message_alert = (TextView) alert_loading.findViewById(R.id.message_alert);
                message_alert.setVisibility(View.GONE);

                alert_loading.show();

                //bug de tamaño
                Display display =((WindowManager) act.getBaseContext().getSystemService(act.getBaseContext().WINDOW_SERVICE)).getDefaultDisplay();
                int width  = display.getWidth();
                int height = display.getHeight();
                alert_loading.getWindow().setLayout(width,height);
            }
        };

        act.runOnUiThread(alert_ui);
    }

    public void DismissLoading(){
        if(alert_loading != null)
            alert_loading.dismiss();
    }

    public void Dismiss(){
        if(alert != null)
            alert.dismiss();
    }
}