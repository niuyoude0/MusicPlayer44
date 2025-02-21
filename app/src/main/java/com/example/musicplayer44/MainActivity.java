package com.example.musicplayer44;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Music> musicList= new ArrayList<>();
    ListView listView;
    MyAdapter adapter;
    MyHandler myHandler = new MyHandler();
    MediaPlayer mediaPlayer = new MediaPlayer();
    //    播放状态
    static int PLAYING=1;
    //    暂停状态
    static int PAUSED=2;
    //    停止状态
    static int STOP=3;
    //    设置初始状态
    int state=STOP;
    
    @Override
    protected  void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new MyAdapter();
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(Constant.WEB_SITE+Constant.REQUEST_SHOP_URL).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();
                Message message = new Message();
                message.what = 1;
                message.obj = json;
                myHandler.sendMessage(message);
                Log.d("shit","message"+message);
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i("JRS", "onFailure: "+e.getMessage());
                e.printStackTrace();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //解析json数据，获取musicList
    public List<Music> getMusicList(String json){
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Music>>(){}.getType();
        return gson.fromJson(json,listType);
    }
    //    异步获取json数据
    public class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    if (msg.obj!= null) {
//                    把数据转化为json字符串
                        Log.d("hhh","msg"+msg+"\n"+msg.obj);
                        String json = (String) msg.obj;
                        musicList = getMusicList(json);
                        Log.d("wc","msg"+musicList.get(1).getName());
//                        刷新listview
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }
//    listview适配器


    public class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return musicList.size();
        }
        @Override
        public Object getItem(int i) {
            return null;
        }
        @Override
        public long getItemId(int i) {
            return 0;
        }
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            View view = View.inflate(MainActivity.this, R.layout.itemlayout, null);
//            Log.d("shit5","msg"+view.findViewById(0));
//            TextView textView = view.findViewById(R.id.item_title);
//            textView.setText(musicList.get(position).getName());
////播放按钮
//            final ImageView imageView = view.findViewById(R.id.imageView);
//            imageView.setImageResource(R.drawable.play);
////停止按钮
//            final ImageView imageView2 = view.findViewById(R.id.imageView2);
//            imageView2.setImageResource(R.drawable.stop_grey);
//
//            imageView.setOnClickListener(new View.OnClickListener() {
//
//
//
//
//
//                @Override
//                public void onClick(View view) {
//
//                    if(Music.currentPosition==position){
////                        如果当前歌曲正在播放，则点击暂停
//                        if(state==PLAYING){
//                            mediaPlayer.pause();
//                            state=PAUSED;
//                            imageView.setImageResource(R.drawable.pause2);
////                            如果当前歌曲暂停，则点击播放
//                        } else if (state==PAUSED) {
//                            mediaPlayer.start();
//                            state=PLAYING;
//                            imageView.setImageResource(R.drawable.pause);
////                            如果之前终止播放，那么这次从头开始播放
//                        }else {
//                            try {
//                                String url = Constant.WEB_SITE+musicList.get(position).getPath();
//                                mediaPlayer.reset();
//                                mediaPlayer.setDataSource(url);
//                                mediaPlayer.prepare();
//                                mediaPlayer.start();
//                                state=PLAYING;
//                                imageView.setImageResource(R.drawable.pause);
//                                imageView2.setImageResource(R.drawable.stop);
//                            }catch (IOException e){
//                                Log.i("JRS", "网络错误: "+e.getMessage());
//                                e.printStackTrace();
//                            }
//                        }
//                    }else {
////                        点击新歌曲
//                        if(Music.currentPosition!=-1){
//                            ImageView imageView = listView.getChildAt(Music.currentPosition).findViewById(R.id.imageView);
//                            ImageView imageView2 = listView.getChildAt(Music.currentPosition).findViewById(R.id.imageView2);
////                            还原原来的图标
//                            imageView.setImageResource(R.drawable.play);
//                            imageView2.setImageResource(R.drawable.stop_grey);
//                        }
//                        Music.currentPosition=position;
//                        try {
//                            if(mediaPlayer!=null){
//                                mediaPlayer.stop();
//                                mediaPlayer.reset();
//                            }
//                            String url = Constant.WEB_SITE+musicList.get(position).getPath();
//                            Log.i("JRS", "播放地址: "+url);
//                            mediaPlayer.setDataSource(url);
//                            mediaPlayer.prepare();
//                            mediaPlayer.start();
//                            state=PLAYING;
//                            imageView.setImageResource(R.drawable.pause);
//                            imageView2.setImageResource(R.drawable.stop);
//                        }catch (Exception e){
//                            Log.i("JRS", "网络错误: "+e.getMessage());
//                            e.printStackTrace();
//                        }
//                    }
//                    // 设置播放完成监听器
//                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        @Override
//                        public void onCompletion(MediaPlayer mp) {
//                            imageView.setImageResource(R.drawable.play);
//                            imageView2.setImageResource(R.drawable.stop_grey);
//                            state = STOP;
//                        }
//                    });
//                }
//            });
//            imageView2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(Music.currentPosition==position){
//                        if(state==PLAYING||state==PAUSED){
//                            mediaPlayer.stop();
//                            state=STOP;
//                            imageView.setImageResource(R.drawable.play);
//                            imageView2.setImageResource(R.drawable.stop_grey);
//                        }
//                    }
//                }
//            });
//
//            return view;
//
//
//
//        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                // 使用 ViewHolder 优化性能
                holder = new ViewHolder();
                convertView = View.inflate(MainActivity.this, R.layout.itemlayout, null);
                holder.textView = convertView.findViewById(R.id.item_title);
                holder.imageViewPlay = convertView.findViewById(R.id.imageView);
                holder.imageViewStop = convertView.findViewById(R.id.imageView2);
                holder.seekBar = convertView.findViewById(R.id.seekBar);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 设置歌曲名称
            holder.textView.setText(musicList.get(position).getName());
            holder.seekBar.setMax(100); // 进度条最大值
            holder.seekBar.setProgress(0); // 初始化进度

            // 确保当前条目状态正确
            if (Music.currentPosition == position && state == PLAYING) {
                holder.imageViewPlay.setImageResource(R.drawable.pause);
            } else {
                holder.imageViewPlay.setImageResource(R.drawable.play);
                holder.seekBar.setProgress(0); // 如果不是当前条目，重置进度条
            }

            // 定义一个独立的 Handler 和 Runnable 用于更新进度条
            final Handler progressHandler = new Handler();
            final Runnable progressRunnable = new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null && state == PLAYING && Music.currentPosition == position) {
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        int duration = mediaPlayer.getDuration();
                        if (duration > 0) {
                            int progress = (int) (100.0 * currentPosition / duration);
                            holder.seekBar.setProgress(progress);
                        }
                        progressHandler.postDelayed(this, 500); // 每隔 500ms 更新一次
                    }
                }
            };

            // 播放按钮点击逻辑
            holder.imageViewPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Music.currentPosition == position) {
                        if (state == PLAYING) {
                            mediaPlayer.pause();
                            state = PAUSED;
                            holder.imageViewPlay.setImageResource(R.drawable.play);
                            progressHandler.removeCallbacks(progressRunnable); // 暂停更新进度条
                        } else if (state == PAUSED) {
                            mediaPlayer.start();
                            state = PLAYING;
                            holder.imageViewPlay.setImageResource(R.drawable.pause);
                            progressHandler.post(progressRunnable); // 恢复更新进度条
                        }
                    } else {
                        // 如果切换了播放条目，重置上一个条目的状态
                        if (Music.currentPosition != -1) {
                            View previousView = listView.getChildAt(Music.currentPosition);
                            if (previousView != null) {
                                ImageView previousPlayButton = previousView.findViewById(R.id.imageView);
                                previousPlayButton.setImageResource(R.drawable.play);
                                SeekBar previousSeekBar = previousView.findViewById(R.id.seekBar);
                                previousSeekBar.setProgress(0);
                            }
                            progressHandler.removeCallbacks(progressRunnable); // 停止上一个条目的更新
                        }

                        // 更新当前条目状态
                        Music.currentPosition = position;
                        try {
                            if (mediaPlayer != null) {
                                mediaPlayer.stop();
                                mediaPlayer.reset();
                            }
                            String url = Constant.WEB_SITE + musicList.get(position).getPath();
                            mediaPlayer.setDataSource(url);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            state = PLAYING;

                            holder.imageViewPlay.setImageResource(R.drawable.pause);
                            holder.seekBar.setProgress(0);
                            progressHandler.post(progressRunnable); // 开始更新进度条
                        } catch (Exception e) {
                            Log.i("JRS", "网络错误: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    // 设置播放完成的逻辑
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            holder.imageViewPlay.setImageResource(R.drawable.play);
                            holder.seekBar.setProgress(0);
                            state = STOP;
                            progressHandler.removeCallbacks(progressRunnable); // 停止更新进度条
                        }
                    });
                }
            });

            // 停止按钮点击逻辑
            holder.imageViewStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Music.currentPosition == position) {
                        if (state == PLAYING || state == PAUSED) {
                            mediaPlayer.stop();
                            state = STOP;
                            holder.imageViewPlay.setImageResource(R.drawable.play);
                            holder.imageViewStop.setImageResource(R.drawable.stop_grey);
                            holder.seekBar.setProgress(0);
                            progressHandler.removeCallbacks(progressRunnable); // 停止更新进度条
                        }
                    }
                }
            });

            return convertView;
        }

        // ViewHolder 用于优化性能
        static class ViewHolder {
            TextView textView;
            ImageView imageViewPlay;
            ImageView imageViewStop;
            SeekBar seekBar;
        }



    }



        @Override
    protected void onStop() {
        super.onStop();
        if(state==PLAYING){
            mediaPlayer.pause();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(state==PLAYING){
            mediaPlayer.start();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer=null;
    }


}