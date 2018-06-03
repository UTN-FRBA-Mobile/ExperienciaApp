package utn.frba.mobile.experienciaapp.agenda.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPageAdapter extends PagerAdapter {
    private Context context;
    private List<String> imagesUrls;

    public ViewPageAdapter(Context context, List<String> imagesUrls){
        this.context=context;
        this.imagesUrls=imagesUrls;

    }

    @Override
    public int getCount() {
        return this.imagesUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //return super.instantiateItem(container, position);
        ImageView imageView=new ImageView(context);
        Picasso.get().load(this.imagesUrls.get(position)).fit().centerCrop().into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }




}
