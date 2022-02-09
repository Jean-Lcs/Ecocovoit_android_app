package com.ecocovoit.ecocovoit.map.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import com.ecocovoit.ecocovoit.R;
import com.ecocovoit.ecocovoit.entities.geo.Location;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**This View class rely on the Leaflet javascript library to display a map on himself (as a WebView).
 * The code of the showed page is in res/raw/leaflet.html.*/
public class OsmMapWebView extends WebView {

    public static final String TAG = "Mr Sauvage";

    /** Holds all javascript functions we may use to perform actions on the map.
     * Their descriptions and implementation are in the res/raw/leaflet.html file.*/
    private enum LeafletJsFunction {
        LEAFLET_JS_FUNCTION_INIT_MAP("functionInitMap"),
        LEAFLET_JS_FUNCTION_DRAW_PATH("functionDrawPath"),
        LEAFLET_JS_FUNCTION_REMOVE_PATH("functionRemovePath"),
        LEAFLET_JS_FUNCTION_DRAW_LOCATION_MARKER("functionDrawLocationMarker"),
        LEAFLET_JS_FUNCTION_REMOVE_LOCATION_MARKER("functionRemoveLocationMarker"),
        LEAFLET_JS_FUNCTION_MOVE_CURRENT_LOCATION("functionMoveCurrentLocation"),
        LEAFLET_JS_FUNCTION_SHOW_PATH("functionShowPath"),
        LEAFLET_JS_FUNCTION_SHOW_LOCATION("functionShowLocation"),
        LEAFLET_JS_FUNCTION_DRAW_ICONS("functionDrawIcons");

        public String name;
        LeafletJsFunction(String name) {
            this.name = name;
        }

        /**Return the list of all js functions names.
         * Use solely to load these in the javascript before loading the html page,
         * so that we can access them in the Java context by calling the evaluateJavaScript() Java function.*/
        static List<LeafletJsFunction> getAll() {
            List<LeafletJsFunction> functions = new ArrayList<>();
            functions.add(LEAFLET_JS_FUNCTION_INIT_MAP);
            functions.add(LEAFLET_JS_FUNCTION_DRAW_PATH);
            functions.add(LEAFLET_JS_FUNCTION_REMOVE_PATH);
            functions.add(LEAFLET_JS_FUNCTION_DRAW_LOCATION_MARKER);
            functions.add(LEAFLET_JS_FUNCTION_REMOVE_LOCATION_MARKER);
            functions.add(LEAFLET_JS_FUNCTION_MOVE_CURRENT_LOCATION);
            functions.add(LEAFLET_JS_FUNCTION_SHOW_PATH);
            functions.add(LEAFLET_JS_FUNCTION_SHOW_LOCATION);
            functions.add(LEAFLET_JS_FUNCTION_DRAW_ICONS);
            return functions;
        }
    }

    /**Hold the variable holding information we want to send to the html page we load.*/
    private enum LeafletJsVariable {
        /**Variable to send starting location.
         * AND most importantly sync location in Java context with location javascript context.*/
        LEAFLET_JS_VAR_LOCATION("mLocation"),
        /**Variable to send the path to display by leaflet.
         * AND most importantly sync path in Java context with path javascript context.*/
        LEAFLET_JS_VAR_PATH("mPath"),
        /**Variable to send the icons to be drawn on the map,
         * AND sync the icons in the Java context with the icons in the javascript context.*/
        LEAFLET_JS_VAR_ICONS("mIcons");

        public String name;
        LeafletJsVariable(String name) {
            this.name = name;
        }
    }

    private Location location;

    private List<Location> path;

    private List<IconOnMap> icons;

    @SuppressLint("SetJavaScriptEnabled")
    public OsmMapWebView(
            Context context,
            @NonNull List<Location> path,
            @NonNull Location location,
            @NonNull List<IconOnMap> icons) {
        super(context);
        this.path = path;
        this.location = location;
        this.icons = icons;

        // to have evaluateJavascript() and addJavacriptInterface() functions.
        this.getSettings().setJavaScriptEnabled(true);

        // Listen for html/javascript console messages.
        listenConsole();

        // load the page.
        update();
    }

    public OsmMapWebView(Context context) {
        this(context,
                new ArrayList<Location>(),
                new Location(0, 0, 0),
                new ArrayList<IconOnMap>());
    }

    public Location getCurrentLocation() {
        return location;
    }

    public void setCurrentLocation(Location location) {
        this.location = location;
    }

    public List<Location> getPath() {
        return path;
    }

    public List<IconOnMap> getIconUrls() {
        return this.icons;
    }

    /**Call the specified function in the currently loaded page.*/
    private void callLeafletJsFunction(LeafletJsFunction function) {
        evaluateJavascript(function.name+"()", null);
    }

    /**Load our js functions names in the page using Javascript Interfaces.*/
    private void loadLeafletJsFunctions() {
        List<LeafletJsFunction> functions = LeafletJsFunction.getAll();
        for(final LeafletJsFunction function : functions) {
            this.addJavascriptInterface(new Object(){
                @NonNull
                @Override
                @JavascriptInterface
                public String toString() {
                    return function.name;
                }
            }, function.name);
        }
    }

    /**Load our js variables names in the page using Javascript Interfaces.*/
    private void loadLeafletJsVariables() {
        // Load the location variable
        this.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public double lon() { return location.getLongitude(); }
            @JavascriptInterface
            public double lat() { return location.getLatitude(); }
            @NonNull
            @Override
            @JavascriptInterface
            public String toString() {
                return LeafletJsVariable.LEAFLET_JS_VAR_LOCATION.name;
            }
        }, LeafletJsVariable.LEAFLET_JS_VAR_LOCATION.name);
        // Load the path
        this.addJavascriptInterface(new Object(){
            @JavascriptInterface
            public int getLocationsCount() { return path.size(); }
            @JavascriptInterface
            public Location getLocation(int index) { return path.get(index); }
            @NonNull
            @Override
            @JavascriptInterface
            public String toString() {
                return LeafletJsVariable.LEAFLET_JS_VAR_PATH.name;
            }
        }, LeafletJsVariable.LEAFLET_JS_VAR_PATH.name);
        // Load the icons
        this.addJavascriptInterface(new Object(){
            @JavascriptInterface
            public int getIconsCount() { return icons.size(); }
            @JavascriptInterface
            public IconOnMap getIcon(int index) { return icons.get(index); }
            @NonNull
            @Override
            @JavascriptInterface
            public String toString() {
                return LeafletJsVariable.LEAFLET_JS_VAR_ICONS.name;
            }
        }, LeafletJsVariable.LEAFLET_JS_VAR_ICONS.name);
    }

    /**Load/Reload the page showing the current location.*/
    public void update() {
        String code = loadLeafletCode();

        Log.i(TAG, "OsmMapWebView: Code = "+getCodeDisplay(code));

        loadLeafletJsVariables();
        loadLeafletJsFunctions();

        String encodedCode = Base64.encodeToString(code.getBytes(), Base64.NO_PADDING);
        this.loadData(encodedCode, "text/html", "base64");
    }

    /**Change the position of the Location Marker on the map to the current location.*/
    public void moveToCurrentLocation() {
        Log.i(TAG, "moveToCurrentLocation: (lon="+location.getLongitude()+", lat="+this.location.getLatitude()+")");
        callLeafletJsFunction(LeafletJsFunction.LEAFLET_JS_FUNCTION_REMOVE_LOCATION_MARKER);
        callLeafletJsFunction(LeafletJsFunction.LEAFLET_JS_FUNCTION_DRAW_LOCATION_MARKER);
    }

    /**Move the map screen to show the current location.*/
    public void showCurrentLocation() {
        callLeafletJsFunction(LeafletJsFunction.LEAFLET_JS_FUNCTION_SHOW_LOCATION);
    }

    /**Move the map screen to entirely show the current path.*/
    public void showPath() {
        callLeafletJsFunction(LeafletJsFunction.LEAFLET_JS_FUNCTION_SHOW_PATH);
    }

    /**Change the current location to (lon, lat)
     * and move the Location Marker to that new Location on the map.*/
    public void moveTo(double lon, double lat) {
        this.location.setLongitude(lon);
        this.location.setLatitude(lat);
        Log.i(TAG, "moveTo: (lon="+location.getLongitude()+", lat="+this.location.getLatitude()+")");
        moveToCurrentLocation();
    }

    /**Change the current location by translating it by the vector (vectLon, vectLat)
     * and move the Location Marker to that new Location on the map.*/
    public void translate(double vectLon, double vectLat) {
        location.setLongitude(this.location.getLongitude()+vectLon);
        location.setLatitude(this.location.getLatitude()+vectLat);
        Log.i(TAG, "translate: (lon="+location.getLongitude()+", lat="+this.location.getLatitude()+")");
        moveToCurrentLocation();
    }

    /**Change the current path, erase the old one from the map and draw the new one.
     * Note: The path is not showed, to move the map screen to show the path, use {@link #showPath()} */
    public void updatePath(List<Location> path) {
        this.path = path;
        callLeafletJsFunction(LeafletJsFunction.LEAFLET_JS_FUNCTION_REMOVE_PATH);
        callLeafletJsFunction(LeafletJsFunction.LEAFLET_JS_FUNCTION_DRAW_PATH);
    }

    /**Change the current icons, erase the old ones from the map and draw the new ones.
     * */
    public void updateIcons(List<IconOnMap> icons) {
        this.icons = icons;
        callLeafletJsFunction(LeafletJsFunction.LEAFLET_JS_FUNCTION_DRAW_ICONS);
    }

    private void listenConsole() {
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.i(TAG, "onConsoleMessage, line "+consoleMessage.lineNumber()+" source : "+consoleMessage.sourceId()+" : "+consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });
    }

    /**Listen for the WebView console messages.*/
    private String loadLeafletCode() {
        InputStream s = getContext().getResources().openRawResource(R.raw.leaflet);
        StringBuilder code = new StringBuilder();
        try {
            int readSize;
            do {
                byte[] codeChunk = new byte[100];
                readSize = s.read(codeChunk);
                code.append(new String(codeChunk));
            }
            while(readSize > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return code.toString();
    }

    /*Useless function <(-_-)>*/
    private static String getCodeDisplay(String code) {
        StringBuilder formated = new StringBuilder();
        int line = 0;

        formated.append(line);
        formated.append('\t');

        for(int i=0; i<code.length(); i++) {
            char c = code.charAt(i);
            formated.append(c);

            if(c == '\n') {
                formated.append(line);
                formated.append('\t');
                line++;
            }
        }
        return formated.toString();
    }
}
