package com.xapp.jjh.xplayer;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xapp.jjh.xplayer.adapter.VideoListAdapter;
import com.xapp.jjh.xplayer.bean.VideoInfo;
import com.xapp.jjh.xplayer.utils.VideoUtils;
import com.xapp.jjh.xui.activity.TopBarActivity;
import com.xapp.jjh.xui.inter.PageState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 16/8/20.
 */
public class HomeActivity extends TopBarActivity implements VideoListAdapter.OnItemClickListener {

    private RecyclerView mRecycler;
    private List<VideoInfo> mList = new ArrayList<>();

    private final int MSG_LOAD_OVER = 101;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_LOAD_OVER:
                    setPageState(PageState.SUCCESS);
                    if(mList.size()<=0){
                        showSnackBar("No Videos",null,null);
                        return;
                    }
                    videoListAdapter = new VideoListAdapter(getApplicationContext(),mList);
                    mRecycler.setAdapter(videoListAdapter);
                    videoListAdapter.setOnItemClickListener(HomeActivity.this);
                    break;
            }
        }
    };
    private VideoListAdapter videoListAdapter;

    @Override
    public View getContentView(LayoutInflater layoutInflater, ViewGroup container) {
        return inflate(R.layout.activity_home);
    }

    @Override
    public void findViewById() {
        mRecycler = findView(R.id.recycler);
    }

    @Override
    public void initData() {
        setNavigationVisible(false);
        setSwipeBackEnable(false);
        setTopBarTitle("视频列表");
        mRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        setPageState(PageState.LOADING);
        new Thread(){
            @Override
            public void run() {
                super.run();
                VideoUtils.getVideos(mList, Environment.getExternalStorageDirectory());
                mHandler.sendEmptyMessage(MSG_LOAD_OVER);
            }
        }.start();
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, int position) {
        String path = mList.get(position).getPath();
        String name = mList.get(position).getDisplayName();
        Intent intent = new Intent(getApplicationContext(),PlayerActivity.class);
        intent.putExtra("path",path);
        intent.putExtra("name",name);
        startActivity(intent);
    }
}