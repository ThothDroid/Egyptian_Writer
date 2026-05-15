package com.blueapps.egyptianwriter.editor.document.settings;

import static com.blueapps.egyptianwriter.editor.document.FileMaster.TAG_NAME_SETTINGS;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.blueapps.egyptianwriter.editor.document.EditorViewModel;
import com.blueapps.egyptianwriter.editor.document.FileMaster;
import com.blueapps.maat.ValuePair;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class PropertiesManager extends ViewModel {

    private static final String TAG = "PropertiesManager";

    private EditorViewModel editorViewModel;
    
    private HashMap<String, String> items = new HashMap<>();

    // Properties
    private final MutableLiveData<Integer> textSize = new MutableLiveData<>(40);
    private final MutableLiveData<Integer> writingLayout = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> verticalOrientation = new MutableLiveData<>(1);
    private final MutableLiveData<Integer> writingDirection = new MutableLiveData<>(0);

    // Constants
    // EnumMaps
    public static final HashMap<String, Integer> VERTICAL_ORIENTATION_MAP = new HashMap<>();
    public static final HashMap<String, Integer> WRITING_LAYOUT_MAP = new HashMap<>();
    public static final HashMap<String, Integer> WRITING_DIRECTION_MAP = new HashMap<>();
    static {
        VERTICAL_ORIENTATION_MAP.put("TOP", 0);
        VERTICAL_ORIENTATION_MAP.put("MIDDLE", 1);
        VERTICAL_ORIENTATION_MAP.put("BOTTOM", 2);

        WRITING_LAYOUT_MAP.put("LINES", 0);
        WRITING_LAYOUT_MAP.put("COLUMNS", 1);

        WRITING_DIRECTION_MAP.put("LTR", 0);
        WRITING_DIRECTION_MAP.put("RTL", 1);
    }

    // Keys
    public static final String KEY_TEXT_SIZE = "textSize";
    public static final String KEY_VERTICAL_ORIENTATION = "verticalOrientation";
    public static final String KEY_WRITING_LAYOUT = "writingLayout";
    public static final String KEY_WRITING_DIRECTION = "writingDirection";
    // XML
    public static final String TAG_NAME_ITEM = "item";
    public static final String ATTR_TYPE = "type";

    public void extractData(ViewModelStoreOwner owner){
        // get ViewModel
        editorViewModel = new ViewModelProvider(owner).get(EditorViewModel.class);

        Document settingsDocument = editorViewModel.getFileMaster().getSettings();

        if (settingsDocument != null){
            if (settingsDocument.hasChildNodes()){
                Element rootElement = settingsDocument.getDocumentElement();
                if (Objects.equals(rootElement.getTagName(), TAG_NAME_SETTINGS)){
                    NodeList nodeList = rootElement.getChildNodes();
                    // Loop through nodeList in reversed order
                    for (int i = nodeList.getLength(); i >= 0; i--){
                        Node node = nodeList.item(i);
                        if (node instanceof Element){
                            Element element = (Element) node;
                            if (Objects.equals(element.getTagName(), TAG_NAME_ITEM)){
                                String type = element.getAttribute(ATTR_TYPE);

                                String value = getValue(element);

                                if (!type.isEmpty() && !value.isEmpty()){
                                    items.put(type, value);
                                }
                            }
                        }
                    }

                    // Save entries from HashMap into variables
                    extractFromHashMap(items);
                }
            }
        } else {
            saveSettings();
        }
    }

    public void saveSettings(){
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            //root elements
            Document settingsDocument = docBuilder.newDocument();
            Element rootElement = settingsDocument.createElement(TAG_NAME_SETTINGS);

            HashMap<String, String> map = createHashMap();
            for (Map.Entry<String, String> entry: map.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();

                Element element = settingsDocument.createElement(TAG_NAME_ITEM);
                element.setAttribute(ATTR_TYPE, key);
                Text text = settingsDocument.createTextNode(value);
                element.appendChild(text);
                rootElement.appendChild(element);
            }

            settingsDocument.appendChild(rootElement);
            FileMaster fileMaster = editorViewModel.getFileMaster();
            fileMaster.setSettings(settingsDocument);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            // TODO: Error handling
        }

    }

    private static String getValue(Element element) {
        NodeList textList = element.getChildNodes();
        String value = "";
        for (int j = 0; j < textList.getLength(); j++){
            Node textNode = textList.item(j);
            if (textNode instanceof Text){
                Text text = (Text) textNode;
                value = text.getWholeText();
            }
        }
        return value;
    }

    public LiveData<Integer> getTextSize(){
        return textSize;
    }

    public void setTextSize(int textSize){
        this.textSize.postValue(textSize);
        saveSettings();
    }

    public int increaseTextSize(){
        int textSize = getTextSize().getValue();
        if (textSize < 999) {
            textSize++;
        }
        return textSize;
    }

    public int decreaseTextSize(){
        int textSize = getTextSize().getValue();
        if (textSize > 1) {
            textSize--;
        }
        return textSize;
    }

    public LiveData<Integer> getWritingLayout() {
        return writingLayout;
    }

    public void setWritingLayout(int writingLayout){
        this.writingLayout.postValue(writingLayout);
        // postValue delayed -> old value used for saving
        saveSettings();
    }

    public LiveData<Integer> getVerticalOrientation() {
        return verticalOrientation;
    }

    public void setVerticalOrientation(int verticalOrientation){
        this.verticalOrientation.postValue(verticalOrientation);
        saveSettings();
    }

    public LiveData<Integer> getWritingDirection() {
        return writingDirection;
    }

    public void setWritingDirection(int writingDirection){
        this.writingDirection.postValue(writingDirection);
        saveSettings();
    }

    private HashMap<String, String> createHashMap(){
        HashMap<String, String> map = new HashMap<>();

        // TextSize
        map.put(KEY_TEXT_SIZE, textSize.getValue().toString());

        // VerticalOrientation
        String verticalOrientationValue = enumToInt(verticalOrientation.getValue(), VERTICAL_ORIENTATION_MAP);
        if (verticalOrientationValue != null) map.put(KEY_VERTICAL_ORIENTATION, verticalOrientationValue);

        // WritingDirection
        String writingDirectionValue = enumToInt(writingDirection.getValue(), WRITING_DIRECTION_MAP);
        if (writingDirectionValue != null) map.put(KEY_WRITING_DIRECTION, writingDirectionValue);

        // WritingLayout
        String writingLayoutValue = enumToInt(writingLayout.getValue(), WRITING_LAYOUT_MAP);
        if (writingLayoutValue != null) map.put(KEY_WRITING_LAYOUT, writingLayoutValue);

        return map;
    }

    private void extractFromHashMap(@NonNull HashMap<String, String> map){

        // TextSize
        String textSizeVal = map.get(KEY_TEXT_SIZE);
        int textSizeInt = extractInt(textSizeVal, 1, 999);
        if (textSizeInt != -1) textSize.setValue(textSizeInt);

        // VerticalOrientation
        String verticalOrientationVal = map.get(KEY_VERTICAL_ORIENTATION);
        int verticalOrientationInt = extractEnum(verticalOrientationVal, VERTICAL_ORIENTATION_MAP);
        if (verticalOrientationInt != -1) verticalOrientation.setValue(verticalOrientationInt);

        // WritingDirection
        String writingDirectionVal = map.get(KEY_WRITING_DIRECTION);
        int writingDirectionInt = extractEnum(writingDirectionVal, WRITING_DIRECTION_MAP);
        if (writingDirectionInt != -1) writingDirection.setValue(writingDirectionInt);

        // WritingLayout
        String writingLayoutVal = map.get(KEY_WRITING_LAYOUT);
        int writingLayoutInt = extractEnum(writingLayoutVal, WRITING_LAYOUT_MAP);
        if (writingLayoutInt != -1) writingLayout.setValue(writingLayoutInt);

    }

    private static int extractInt(String s, int min, int max){
        if (s != null) {
            try {
                int i = Integer.parseInt(s);
                if (i < min) {
                    Log.d(TAG, "Value is smaller than minimum: value: " + i + " min: " + min);
                } else if (i > max) {
                    Log.d(TAG, "Value is bigger than maximum: value: " + i + " max: " + max);
                } else {
                    return i;
                }
            } catch (NumberFormatException e) {
                Log.d(TAG, "Value is not an integer: " + s);
            }
        }
        return -1;
    }

    private static int extractEnum(String s, HashMap<String, Integer> enumMap){
        Integer I = enumMap.get(s);
        if (I != null){
            return I;
        }
        return -1;
    }

    private static String enumToInt(int i, HashMap<String, Integer> enumMap){
        for (Map.Entry<String, Integer> entry : enumMap.entrySet()) {
            if (Objects.equals(i, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

}