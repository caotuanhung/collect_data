package com.seal.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.seal.collectnature.R;
import com.seal.model.Plant;

import java.util.List;

public class PictureAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<byte[]> picturesList;

    public PictureAdapter() {
    }

    public PictureAdapter(Context context, int layout, List<byte[]> picturesList) {
        this.context = context;
        this.layout = layout;
        this.picturesList = picturesList;
    }

    @Override
    public int getCount() {
        return picturesList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        public ImageView imgPlant;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.imgPlant = convertView.findViewById(R.id.imgPlant);
            // set tag to holder
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        // convert picture form byte[] to bitmap
        byte[] picture = picturesList.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);

        // add data to imgPlant
        holder.imgPlant.setImageBitmap(bitmap);
        return convertView;
    }
}
