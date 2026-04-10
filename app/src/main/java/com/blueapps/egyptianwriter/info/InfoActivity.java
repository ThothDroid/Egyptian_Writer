package com.blueapps.egyptianwriter.info;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.databinding.ActivityInfoBinding;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {

    private ActivityInfoBinding binding;
    private static final String TAG = "InfoActivity";


    // Views
    private ImageButton buttonBack;
    private TextView versionCode;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create non fullscreen layout
        binding = ActivityInfoBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set names for views
        buttonBack = binding.buttonBack;
        versionCode = binding.versionCode;
        recyclerView = binding.listView;


        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode.setText(String.format(getString(R.string.about_version_name), packageInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Prepare xml data
        ArrayList<InfoData> data = new ArrayList<>();
        XmlResourceParser infoDataParser = getResources().getXml(R.xml.info_item_list);
        boolean top = true;
        boolean group = false;
        boolean item = false;
        boolean inAction = false;
        String actionData = null;
        InfoData.Action action = null;
        int groupCount = 0;

        try {

            infoDataParser.next();
            int eventType = infoDataParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                // begin document
                if (eventType == XmlPullParser.START_DOCUMENT){
                    Log.d(TAG, "Begin Document");
                }
                // begin text
                else if (eventType == XmlPullParser.START_TAG) {
                    String tagName = infoDataParser.getName();
                    if (tagName.equals("item")){
                        int mode = InfoData.MODE_SINGLE_ITEM;
                        if (group){
                            groupCount++;
                        }
                        String title = infoDataParser.getAttributeValue(null, "title");
                        String subtitle = infoDataParser.getAttributeValue(null, "subtitle");

                        if (title == null) title = "";

                        data.add(new InfoData(mode, top, title, subtitle));

                        if (top) top = false;
                        item = true;
                    } else if (tagName.equals("group")) {
                        String title = infoDataParser.getAttributeValue(null, "title");
                        String subtitle = infoDataParser.getAttributeValue(null, "subtitle");

                        if (title != null) {
                            data.add(new InfoData(InfoData.MODE_GROUP_TITLE_ITEM, top, title, subtitle));
                        }

                        group = true;

                        if (top) top = false;
                    } else if (tagName.equals("action")){
                        if (item) {
                            String type = infoDataParser.getAttributeValue(null, "type");

                            if (type != null) {

                                inAction = true;

                                action = new InfoData.Action(type, actionData);

                            }
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    String tagName = infoDataParser.getName();
                    if (tagName.equals("group")){
                        group = false;

                        if (groupCount == 1){
                            int index = data.size() - 1;
                            InfoData allData = data.get(index);
                            allData.setMode(InfoData.MODE_SINGLE_ITEM);
                            data.set(index, allData);
                        } else if (groupCount > 1) {
                            int lastIndex = data.size() - 1;
                            int firstIndex = data.size() - groupCount;

                            InfoData firstData = data.get(firstIndex);
                            InfoData lastData = data.get(lastIndex);
                            ArrayList<InfoData> betweenData = new ArrayList<>(data.subList(firstIndex + 1, lastIndex));

                            firstData.setMode(InfoData.MODE_TOP_ITEM);
                            lastData.setMode(InfoData.MODE_BOTTOM_ITEM);
                            int counter = firstIndex + 1;
                            for (InfoData infoData: betweenData){
                                infoData.setMode(InfoData.MODE_MIDDLE_ITEM);
                                data.set(counter, infoData);
                                counter++;
                            }
                            data.set(firstIndex, firstData);
                            data.set(lastIndex, lastData);
                        }

                        groupCount = 0;
                    } else if (tagName.equals("item")){
                        item = false;

                        InfoData infoData = data.get(data.size() - 1);
                        infoData.setAction(action);
                        data.set(data.size() - 1, infoData);

                        action = null;

                    } else if (tagName.equals("action")){
                        inAction = false;

                        if (action != null) action.setData(actionData);

                        actionData = null;
                    }
                } else if (eventType == XmlPullParser.TEXT){
                    if (inAction){
                        actionData = infoDataParser.getText();
                    }
                }
                eventType = infoDataParser.next();
            }

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }

        // Create adapter passing in the sample user data
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonBack.setOnClickListener(view -> finish());

    }
}
