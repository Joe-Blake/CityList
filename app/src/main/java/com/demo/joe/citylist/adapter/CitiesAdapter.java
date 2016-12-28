package com.demo.joe.citylist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.demo.joe.citylist.R;
import com.demo.joe.citylist.bean.CitiesBean;

import java.util.List;

/**
 * Created by joe on 2016/12/23.
 */
public class CitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<CitiesBean.OpenCityEntity> cities;

    public final static int HEAD = 0;
    public final static int WORD = 1;
    public final static int CITY = 2;

    public CitiesAdapter(Context context, List<CitiesBean.OpenCityEntity> cities){
        this.context = context;
        this.cities = cities;
    }

    public List<CitiesBean.OpenCityEntity> getData() {
        return cities;
    }

    @Override
    public int getItemCount() {
        int count = 1;
        if (cities == null || cities.size() == 0) {
            return count;
        }
        count += cities.size();
        for(CitiesBean.OpenCityEntity datasBean:cities){
            count += datasBean.getCitySet().size();
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int count = 0;
        if (position == count) {
            return HEAD;
        }

        for(int i = 0; i < cities.size(); i++){
            count++;
            if (position == count) {
                return WORD;
            }
            List<CitiesBean.OpenCityEntity.CitySetEntity> addressList = cities.get(i).getCitySet();
            for (int j = 0; j < addressList.size(); j++) {
                count++;
                if (position == count) {
                    return CITY;
                }
            }
        }
        return super.getItemViewType(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case HEAD:
                View head = LayoutInflater.from(context).inflate(R.layout.layout_head, parent, false);
                return new HeadViewHolder(head);
            case WORD:
                View word = LayoutInflater.from(context).inflate(R.layout.layout_word, parent, false);
                return new WordViewHolder(word);
            case CITY:
                View city = LayoutInflater.from(context).inflate(R.layout.layout_city, parent, false);
                return new CityViewHolder(city);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int count = 0;
        if (position == 0) {
            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
        }
        for(int i = 0; i < cities.size(); i++){
            count++;
            if (position == count) {
                WordViewHolder wordViewHolder = (WordViewHolder) holder;
                CitiesBean.OpenCityEntity datasBean = cities.get(i);
                wordViewHolder.textWord.setText(datasBean.getIndex());
            }
            List<CitiesBean.OpenCityEntity.CitySetEntity> addressList = cities.get(i).getCitySet();
            for (int j = 0; j < addressList.size(); j++) {
                count++;
                if (position == count) {
                    CityViewHolder cityViewHolder = (CityViewHolder) holder;
                    CitiesBean.OpenCityEntity.CitySetEntity addressListBean = addressList.get(j);
                    cityViewHolder.textCity.setText(addressListBean.getName());
                    cityViewHolder.textCity.setTag(addressListBean.getFirstLetter().toUpperCase());
                }
            }
        }
    }


    public static class HeadViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        Button btnCurrentCity;
        public HeadViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_current);
            btnCurrentCity = (Button) view.findViewById(R.id.btn_current);
        }
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView textWord;
        String index;
        public WordViewHolder(View view) {
            super(view);
            textWord = (TextView) view.findViewById(R.id.textWord);
            index = textWord.getText().toString();
        }
    }
    public static class CityViewHolder extends RecyclerView.ViewHolder {

        TextView textCity;
        public CityViewHolder(View view) {
            super(view);
            textCity = (TextView) view.findViewById(R.id.textCity);
        }

    }
}
