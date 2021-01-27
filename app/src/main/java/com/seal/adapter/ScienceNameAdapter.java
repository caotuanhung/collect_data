package com.seal.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.seal.collectnature.PlantActivity;
import com.seal.collectnature.R;
import com.seal.collectnature.UpdatePlantActivity;
import com.seal.model.Plant;

import java.util.List;

public class ScienceNameAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Plant> plantsList;

    public ScienceNameAdapter() {

    }

    public ScienceNameAdapter(Context context, int layout, List<Plant> plantsList) {
        this.context = context;
        this.layout = layout;
        this.plantsList = plantsList;
    }

    @Override
    public int getCount() {
        return plantsList.size();
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
        TextView txtScienName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtScienName = convertView.findViewById(R.id.txtScienceName);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        Plant plant = plantsList.get(position);
        holder.txtScienName.setText(plant.getScienceName());

        return convertView;
    }
}
