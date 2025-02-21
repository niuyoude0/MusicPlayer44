package com.example.musicplayer44;

public class Music {
    public static int currentPosition=-1;//当前歌曲的位置
    private int id;
    private String name;
    private String path;
    public Music(int id,String name,String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }
        public int getld() {
            return id;
        }
            public String getName() {
                return name;
            }
                public String getPath() {
                    return path;
                }
}
