package utn.frba.mobile.experienciaapp.lib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {
    ImageView imageView;

    public DownloadImageTask(ImageView imageView){
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String...urls){
        String urlOfImage = urls[0];
        Bitmap img = null;
        try{
            InputStream is = new URL(urlOfImage).openStream();
            img = BitmapFactory.decodeStream(is);
        }catch(Exception e){ // Catch the download exception
            e.printStackTrace();
        }
        return img;
    }

    protected void onPostExecute(Bitmap result){
        imageView.setImageBitmap(result);
    }
}