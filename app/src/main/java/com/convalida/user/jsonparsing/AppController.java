package com.convalida.user.jsonparsing;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Convalida on 7/24/2017.
 */

class AppController {
    private static AppController mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    private AppController(Context context){
        mCtx=context;
        mRequestQueue=getRequestQueue();
    }

    public static synchronized AppController getInstance(Context context){
        if(mInstance==null){
            mInstance=new AppController(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if(mRequestQueue==null){
            mRequestQueue= Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }



    public void addToRequestQueue(@NotNull final Request request){
        getRequestQueue().add(request);
    }
    public void addToRequestQueueWithTag(@NotNull final Request request,String tag){
        request.setTag(tag);
        getRequestQueue().add(request);
    }
}
