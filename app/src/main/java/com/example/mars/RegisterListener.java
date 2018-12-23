package com.example.mars;

public class RegisterListener
{
    private static ApiRequestResponse listener;
    public static void registerListener(ApiRequestResponse mlistener){
        listener = mlistener;

    }
    public static void sendResponse(String response, int code){
        if(code==0){
            if(listener!=null) {
                listener.success(response);
            }
        }
        else {
            listener.failure(response);
        }
    }
    public static void removelistener(){
        listener = null;
    }
}

