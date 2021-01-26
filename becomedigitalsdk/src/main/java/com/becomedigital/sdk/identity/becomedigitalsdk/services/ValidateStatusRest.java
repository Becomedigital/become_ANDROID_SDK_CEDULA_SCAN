package com.becomedigital.sdk.identity.becomedigitalsdk.services;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.becomedigital.sdk.identity.becomedigitalsdk.MyApplication;
import com.becomedigital.sdk.identity.becomedigitalsdk.R;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.AsynchronousTask;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.SharedParameters;
import com.becomedigital.sdk.identity.becomedigitalsdk.models.PersonaVO;
import com.becomedigital.sdk.identity.becomedigitalsdk.models.ResponseIV;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ValidateStatusRest {
    /* access modifiers changed from: private */
    public static final String TAG = ValidateStatusRest.class.getSimpleName();
    private final int USERRESPONSE = 0;
    private final int INITAUTHRESPONSE = 1;

    public void getAuth(final Activity activity, String clientID, String clientSecret, final AsynchronousTask asynchronousTask) {

        AsyncTask.execute(() -> {
            try {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
                String serverUrl = preferences.getString(SharedParameters.URL_AUTH, SharedParameters.url_auth);

                MediaType JSON = MediaType.parse("application/json");
                JSONObject json = new JSONObject();
                json.put("client_id", clientID);
                json.put("client_secret", clientSecret);

                String jsonString = json.toString();
                Log.d("JSON SEND:", jsonString);
                RequestBody body = RequestBody.create(JSON, jsonString);
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(activity.getResources().getInteger(R.integer.timeOut), TimeUnit.SECONDS)
                        .readTimeout(activity.getResources().getInteger(R.integer.timeOut), TimeUnit.SECONDS)
                        .writeTimeout(activity.getResources().getInteger(R.integer.timeOut), TimeUnit.SECONDS).build();

                Request request = new Request.Builder()
                        .url(serverUrl)
                        .header("Content-Type", "application/json")
                        .post(body)
                        .build();

                Call call = client.newCall(request);

                call.enqueue(new Callback() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onFailure(Call call, IOException e) {
                        asynchronousTask.onErrorTransaction(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            String jsonData = response.body().string();
                            JSONObject Jobject = new JSONObject(jsonData);
                            if (!Jobject.has("msg")) {
                                asynchronousTask.onReceiveResultsTransaction(new ResponseIV(ResponseIV.SUCCES, Jobject.getString("access_token")), INITAUTHRESPONSE);
                            } else {
                                asynchronousTask.onErrorTransaction(Jobject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            asynchronousTask.onErrorTransaction(e.getLocalizedMessage());
                        }
                    }
                });


            } catch (Exception e) {
                asynchronousTask.onErrorTransaction(e.getLocalizedMessage());
                e.printStackTrace();
            }
        });
    }

    public void getDataAutentication(String national_id_number, String expedition_date, final Activity activity, final AsynchronousTask asynchronousTask) {
        AsyncTask.execute(() -> {
            try {

                MediaType JSON = MediaType.parse("application/json");
                JSONObject json = new JSONObject();
                json.put("contract_id", ((MyApplication) activity.getApplicationContext()).getContractId());
                json.put("national_id_number", national_id_number);
                json.put("expedition_date", expedition_date);
                String jsonString = json.toString();
                Log.d("JSON SEND:", jsonString);

                RequestBody body = RequestBody.create(JSON, jsonString);

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(activity.getResources().getInteger(R.integer.timeOut), TimeUnit.SECONDS)
                        .readTimeout(activity.getResources().getInteger(R.integer.timeOut), TimeUnit.SECONDS)
                        .writeTimeout(activity.getResources().getInteger(R.integer.timeOut), TimeUnit.SECONDS).build();
                ;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);

                String serverUrl = preferences.getString(SharedParameters.URL_GET_DATA, SharedParameters.url_get_data);
//                Headers.Builder hb = new Headers.Builder();
//                hb.add("Content-Type", "application/json");
//                hb.add("Authorization", "Bearer " + ((MyApplication) activity.getApplicationContext()).getAccess_token());
                Request request = new Request.Builder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer " + ((MyApplication) activity.getApplicationContext()).getAccess_token())
                        .url(serverUrl)
                        .post(body)
                        .build();

                Call call = client.newCall(request);
                call.enqueue(new Callback() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onFailure(Call call, IOException e) {
                        asynchronousTask.onErrorTransaction(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            String jsonData = response.body().string();
                            JSONObject Jobject = new JSONObject(jsonData);

                            if (Jobject.has("msg")) {
                                asynchronousTask.onReceiveResultsTransaction(new ResponseIV(ResponseIV.PENDING, Jobject.getString("apimsg")), USERRESPONSE);
                            } else if (Jobject.has("personaVO") &&
                                    Jobject.has("fechaExpedicion") &&
                                    Jobject.has("lugarExpedicion") &&
                                    Jobject.has("estado") &&
                                    Jobject.has("resolucion") &&
                                    Jobject.has("fechaResolucion") &&
                                    Jobject.has("fechaConsulta") &&
                                    Jobject.has("fuenteFallo")) {
                                if (Jobject.getString("fuenteFallo").equals("NO")) {
                                    JSONObject JpersonaVO = new JSONObject(Jobject.getString("personaVO"));
                                    JSONObject Jnombres = new JSONObject(JpersonaVO.getString("nombres"));
                                    JSONObject JECC = new JSONObject(Jnombres.getString("ESTADO-CEDULA-COLOMBIA"));
                                    PersonaVO personaVO = new PersonaVO(Jobject.getString("fechaExpedicion"),
                                            Jobject.getString("lugarExpedicion"),
                                            Jobject.getString("estado"),
                                            Jobject.getString("resolucion"),
                                            Jobject.getString("fechaResolucion"),
                                            Jobject.getString("fechaConsulta"),
                                            Jobject.getString("fuenteFallo"),
                                            JpersonaVO.getString("numeroDocumento"),
                                            JpersonaVO.getString("tipoDocumento"),
                                            JpersonaVO.getString("pais"),
                                            JECC.getString("primerNombre"),
                                            JECC.getInt("tipoNombre")
                                    );
                                    ResponseIV responseIV = new ResponseIV(personaVO);
                                    asynchronousTask.onReceiveResultsTransaction(responseIV, USERRESPONSE);
                                } else {
                                    asynchronousTask.onReceiveResultsTransaction(new ResponseIV(ResponseIV.ERROR, activity.getString(R.string.text_request_fail)), USERRESPONSE);

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            asynchronousTask.onErrorTransaction(e.getLocalizedMessage());
                        }
                    }
                });


            } catch (Exception e) {
                asynchronousTask.onErrorTransaction(e.getLocalizedMessage());
                e.printStackTrace();
            }
        });
    }

}
