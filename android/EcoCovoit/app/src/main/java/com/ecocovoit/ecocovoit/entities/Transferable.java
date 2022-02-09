package com.ecocovoit.ecocovoit.entities;

import org.json.JSONException;
import org.json.JSONObject;

public interface Transferable {
    JSONObject toJson() throws JSONException;
}
