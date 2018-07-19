package com.aperotechnologies.retrofit.service;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;



/**
 * Created by kshivale on 18/05/18.
 */

@SuppressWarnings("ALL")
public class ApiHandlerAsyncTask extends AsyncTask<String, String, String> {

    private static final String TAG = ApiHandlerAsyncTask.class.getSimpleName();
    private final Handler handler;
    private final int level;
    private final int REQUEST_TYPE;

    public ApiHandlerAsyncTask(Handler handler, int level, int requestType) {
        this.handler = handler;
        this.level = level;
        this.REQUEST_TYPE = requestType;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpHandler httpHandler = new HttpHandler();
        String url = strings[0];
        Log.e(TAG, "doInBackground: Url: " + url);
        String bearerToken = strings[1];
        // Making a request to url and getting response
        String jsonStr = httpHandler.makeServiceCall(url, bearerToken);
        Log.e(TAG, "doInBackground: Response: " + jsonStr);
        return jsonStr;
    }

    @Override
    protected void onPostExecute(String response) {
        Message message = new Message();
        message.obj = response;
        message.arg1 = this.level;
        message.arg2 = REQUEST_TYPE;
        handler.handleMessage(message);


    }
}
