package com.example.mars;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Jsonparser {

    public static metaData mdata;
    public static ArrayList<metaData> publicList;

    public static void parseJsontags(String response) throws JSONException {



            publicList =new ArrayList<>();
        JSONObject obj  =new JSONObject(response.toString());

        if(obj.has("resources"))
        {
            JSONArray jsonArray = obj.getJSONArray("resources");

            for(int i =0;i<jsonArray.length();i++)
            {
                mdata =new metaData();
                JSONObject inObject = jsonArray.getJSONObject(i);
                mdata.setPublicID(inObject.getString("public_id"));
                publicList.add(mdata);
            }
        }

    }
}
