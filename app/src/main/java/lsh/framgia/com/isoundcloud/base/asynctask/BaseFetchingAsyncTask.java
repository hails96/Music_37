package lsh.framgia.com.isoundcloud.base.asynctask;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class BaseFetchingAsyncTask<U, P, R> extends AsyncTask<U, P, R> {

    private static final int FIRST_PARAM = 0;
    private static final String KEY_CONTENT_TYPE = "Content-Type";
    private static final String VALUE_APP_JSON = "application/json";
    private static final int RESPONSE_CODE_OK = 200;
    private static final int CONNECT_TIME_OUT = 5000;
    private static final int READ_TIME_OUT = 5000;

    private OnResponseListener mOnResponseListener;
    private Exception mException;

    @Override
    protected R doInBackground(U... u) {
        R result = null;
        try {
            URL url = new URL((String) u[FIRST_PARAM]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty(KEY_CONTENT_TYPE, VALUE_APP_JSON);
            urlConnection.setConnectTimeout(CONNECT_TIME_OUT);
            urlConnection.setReadTimeout(READ_TIME_OUT);
            if (urlConnection.getResponseCode() == RESPONSE_CODE_OK) {
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                result = convertInputStreamToObject(inputStream);
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            mException = e;
        }
        return result;
    }

    @Override
    protected void onPostExecute(R result) {
        super.onPostExecute(result);
        if (mOnResponseListener == null) return;
        if (mException != null) mOnResponseListener.onError(mException);
        else mOnResponseListener.onSuccess(result);
    }

    public BaseFetchingAsyncTask setOnResponseListener(OnResponseListener onResponseListener) {
        mOnResponseListener = onResponseListener;
        return this;
    }

    protected abstract R convertInputStreamToObject(InputStream inputStream)
            throws IOException, JSONException;

    public interface OnResponseListener<R> {
        void onSuccess(@Nullable R result);

        void onError(Exception e);
    }
}
