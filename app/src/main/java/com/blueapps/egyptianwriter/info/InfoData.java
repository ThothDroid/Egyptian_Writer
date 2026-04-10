package com.blueapps.egyptianwriter.info;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class InfoData {

    private int mode;
    private boolean top;

    private String title;
    private String subtitle;

    private Action action;

    // Constants
    public static final int MODE_SINGLE_ITEM = 0;
    public static final int MODE_TOP_ITEM = 1;
    public static final int MODE_BOTTOM_ITEM = 2;
    public static final int MODE_MIDDLE_ITEM = 3;
    public static final int MODE_GROUP_TITLE_ITEM = 4;

    public InfoData(int mode, boolean top, String title, String subtitle){
        this.mode = mode;
        this.top = top;
        this.title = title;
        this.subtitle = subtitle;
    }

    public InfoData(int mode, boolean top, String title, String subtitle, Action action){
        this.mode = mode;
        this.top = top;
        this.title = title;
        this.subtitle = subtitle;
        this.action = action;
    }

    public void triggerAction(Context context){
        if (action != null) action.trigger(context);
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isTop() {
        return top;
    }

    public boolean isGroup(){
        return mode == MODE_TOP_ITEM || mode == MODE_MIDDLE_ITEM || mode == MODE_BOTTOM_ITEM;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public static class Action {

        private final String type;
        private String data;

        public Action(String type, String data){
            this.type = type;
            this.data = data;
        }

        public void trigger(Context context){

            if (type.equals("copy")) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", data);
                clipboard.setPrimaryClip(clip);
            } else if (type.equals("open")){
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(data));
                context.startActivity(i);
            }

        }

        public void setData(String data){
            this.data = data;
        }

    }
}
