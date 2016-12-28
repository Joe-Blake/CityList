package com.demo.joe.citylist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.joe.citylist.adapter.CitiesAdapter;
import com.demo.joe.citylist.bean.CitiesBean;
import com.demo.joe.citylist.view.QuickIndexView;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private QuickIndexView quickIndexView;
    private RecyclerView recyclerView;
    private CitiesAdapter adapter;
    private RelativeLayout mSuspensionBar;
    private TextView mSuspensionTv;

    private int mCurrentPosition = 0;
    private int mSuspensionHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initView() {
        quickIndexView = (QuickIndexView) findViewById(R.id.quickIndexView);
        mSuspensionBar = (RelativeLayout) findViewById(R.id.suspension_bar);
        mSuspensionBar.setVisibility(View.INVISIBLE);
        mSuspensionTv = (TextView) findViewById(R.id.tv_time);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        Gson gson = new Gson();
        CitiesBean citiesBean = gson.fromJson(Data.citiesJson, CitiesBean.class);

        adapter = new CitiesAdapter(this,citiesBean.getOpenCity());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mSuspensionHeight = mSuspensionBar.getHeight();

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mCurrentPosition != 0) {
                    mSuspensionBar.setVisibility(View.VISIBLE);
                } else {
                    mSuspensionBar.setVisibility(View.INVISIBLE);
                }

                if (adapter.getItemViewType(mCurrentPosition + 1) == CitiesAdapter.WORD) {

                    View view = linearLayoutManager.findViewByPosition(mCurrentPosition + 1);
                    if (view != null) {
                        if (view.getTop() <= mSuspensionHeight) {
                            mSuspensionBar.setY(-(mSuspensionHeight - view.getTop()));
                        } else {
                            mSuspensionBar.setY(0);
                        }
                    }
                }

                if (mCurrentPosition != linearLayoutManager.findFirstVisibleItemPosition()) {
                    mCurrentPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    mSuspensionBar.setY(0);
                    if (mCurrentPosition > 0) {
                        if (adapter.getItemViewType(mCurrentPosition) == CitiesAdapter.WORD) {
                            View view = linearLayoutManager.findViewByPosition(mCurrentPosition);
                            TextView tvWord = (TextView) view.findViewById(R.id.textWord);
                            mSuspensionTv.setText(tvWord.getText());
                        } else {
                            View view = linearLayoutManager.findViewByPosition(mCurrentPosition);
                            TextView tvWord = (TextView) view.findViewById(R.id.textCity);
                            mSuspensionTv.setText(tvWord.getTag().toString());
                        }
                    }
                }
            }
        });
    }


    private void initEvent() {
        quickIndexView.setOnIndexChangeListener(new QuickIndexView.OnIndexChangeListener() {
            @Override
            public void onIndexChange(String words) {
                if(words.equals("A")){
                    LinearLayoutManager llm = (LinearLayoutManager) recyclerView
                            .getLayoutManager();
                    llm.scrollToPositionWithOffset(0, 0);
                    return;
                }
                List<CitiesBean.OpenCityEntity> datas = adapter.getData();
                if(datas!=null && datas.size()>0) {
                    int count = 0;
                    for (int i = 0; i < datas.size(); i++) {
                        CitiesBean.OpenCityEntity datasBean = datas.get(i);
                        if(datasBean.getIndex().equals(words)){
                            LinearLayoutManager llm = (LinearLayoutManager) recyclerView
                                    .getLayoutManager();
                            llm.scrollToPositionWithOffset(count+1, 0);
                            return;
                        }
                        count+=datasBean.getCitySet().size()+1;
                    }
                }
            }
        });
    }
}
