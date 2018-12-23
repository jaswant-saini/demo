package com.example.mars;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class NetworkOperations {
    public static RequestQueue requestQueue;

    public static void callApi(final String url, final Context context) {
        try {


            StringRequest stringRequest = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("Api Url is::" + url);
                            RegisterListener.sendResponse(response, 0);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                if (error.toString() != null) {
                                    if (!error.toString().contains("com.android.volley.NoConnectionError")) {
                                        RegisterListener.sendResponse(error.toString(), 1);
                                        System.out.println(error);
                                    }
                                    if (error.networkResponse == null) {
                                        if (error instanceof TimeoutError) {
                                            // Show timeout error message
                                            // Toast.makeText(context, "Volly time out", Toast.LENGTH_SHORT).show();
                                            RegisterListener.sendResponse("Poor Internet Conntection", 1);
                                        }
                                    }
                                }
                            }
                            catch(NullPointerException ex){
                                ex.printStackTrace();
                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });

            requestQueue = Volley.newRequestQueue(context);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}