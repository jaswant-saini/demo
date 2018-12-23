package com.example.mars;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


public class Adapter extends  BaseAdapter {
    private Context mContext;
    private static LayoutInflater inflater = null;

    public Adapter(Context c) {
        mContext = c;
        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // comments--->
    @Override
    public int getCount() {
        if(Jsonparser.publicList!=null) {
            return Jsonparser.publicList.size();
        }
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Adapter.Holder holder = new Adapter.Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.adapter_inflater, null);
        holder.img = rowView.findViewById(R.id.chaneel_image);

        holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        try {
            Picasso
                    .with(mContext)
                    .load("https://res.cloudinary.com/dm7rrqesk/image/upload/v1542451961/"+Jsonparser.publicList.get(position).getPublicID()+".jpg")
                    .into(holder.img);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rowView;
    }

    public class Holder {
        ImageView img;
        TextView title;
    }

}


