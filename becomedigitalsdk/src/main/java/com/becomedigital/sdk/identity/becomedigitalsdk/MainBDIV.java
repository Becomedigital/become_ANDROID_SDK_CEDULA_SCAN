package com.becomedigital.sdk.identity.becomedigitalsdk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.becomedigital.sdk.identity.becomedigitalsdk.callback.AsynchronousTask;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.BecomeCallBackManager;
import com.becomedigital.sdk.identity.becomedigitalsdk.models.BDIVConfig;
import com.becomedigital.sdk.identity.becomedigitalsdk.models.InfoDNI;
import com.becomedigital.sdk.identity.becomedigitalsdk.models.ResponseIV;
import com.becomedigital.sdk.identity.becomedigitalsdk.services.ValidateStatusRest;
import com.becomedigital.sdk.identity.becomedigitalsdk.utils.BarcodeTracker;
import com.becomedigital.sdk.identity.becomedigitalsdk.utils.BarcodeTrackerFactory;
import com.becomedigital.sdk.identity.becomedigitalsdk.utils.CameraSource;
import com.becomedigital.sdk.identity.becomedigitalsdk.utils.CameraSourcePreview;
import com.becomedigital.sdk.identity.becomedigitalsdk.utils.PermissionsNeeded;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainBDIV extends PermissionsNeeded
        implements BarcodeTracker.BarcodeGraphicTrackerCallback, AsynchronousTask {
    public static final String TAG = MainBDIV.class.getSimpleName();
    public static final String KEY_ERROR = "ErrorMessage";
    public Intent mData = new Intent();
    public androidx.appcompat.widget.Toolbar toolbar;
    private boolean isHomeActivity = true;
    private static ValidateStatusRest autService;
    private ImageButton imgBtnCancel;
    private ImageButton imgBtnBack;
    private BDIVConfig config;
    private BecomeCallBackManager mCallbackManager = BecomeCallBackManager.createNew();
    // Intent request code to handle updating play appusr if needed.
    private static final int RC_HANDLE_GMS = 9001;
    // Permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    private DatePicker dPExpeditionDate;
    private FrameLayout frameInit, frameDP;
    private TextView textInfoServer;
    private Button btnContinue;
    private final int INITAUTHRESPONSE = 1;
    private final int USERRESPONSE = 0;
    private InfoDNI infoDNI = null;
private boolean isCapture = false;
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main_bdiv);
        initialSetups();
    }

    private void initialSetups() {
        mPreview = findViewById(R.id.preview);
        frameInit = findViewById(R.id.frameLoaderInit);
        textInfoServer = findViewById(R.id.text_info_server);
        ImageView imgLoader = findViewById(R.id.imgLoader);// loader inicial
        dPExpeditionDate = findViewById(R.id.dpExpeditionDate);
        frameDP = findViewById(R.id.frameDatePicker);
        btnContinue = findViewById(R.id.btnContinueInfo);
        btnContinue.setOnClickListener(view -> { // Se asigna del evento al botón continuar después de seleccionar la fecha
            frameDP.setVisibility(View.GONE);
            frameInit.setVisibility(View.VISIBLE);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            int day = dPExpeditionDate.getDayOfMonth();
            int month = dPExpeditionDate.getMonth();
            int year = dPExpeditionDate.getYear();

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            String finalDateTime = sdf.format(calendar.getTime());
            textInfoServer.setText(getString(R.string.text_more_full_bar));
            Glide.with(this)
                    .load(R.drawable.load_init)
                    .into(imgLoader);
            autService.getDataAutentication(infoDNI.getCedula(), finalDateTime, MainBDIV.this, MainBDIV.this);
        });

        Glide.with(this)
                .load(R.drawable.rotate_phone)
                .into(imgLoader);
        autService = new ValidateStatusRest();
        getExtrasAndValidateConfig();
        initQRReader();
        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        toolbar.setTitle("Acerca el código de barras al respaldo de la cédula");
        imgBtnCancel = toolbar.findViewById(R.id.btnCancel);
        imgBtnCancel.setOnClickListener(view -> {
            setResulUserCanceled();
        });
        infoDNI = new InfoDNI();
    }


    private void getExtrasAndValidateConfig() { // valida data input user
        if (getIntent().getExtras() != null) {
            this.config = (BDIVConfig) getIntent().getSerializableExtra("BDIVConfig");
            if (config != null) {
                if (this.config.getClienId().isEmpty()) {
                    setResulLoginError("ClienId parameters cannot be empty");
                    return;
                } else if (this.config.getClientSecret().isEmpty()) {
                    setResulLoginError("ClientSecret parameters cannot be empty");
                    return;
                } else if (this.config.getContractId().isEmpty()) {
                    setResulLoginError("ContractId parameters cannot be empty");
                    return;
                }
                // autenticarse
                autService.getAuth(this, this.config.getClienId(), this.config.getClientSecret(), this);
                ((MyApplication) getApplication()).setClientId(this.config.getClienId());
                ((MyApplication) getApplication()).setClientSecret(this.config.getClientSecret());
                ((MyApplication) getApplication()).setContractId(this.config.getContractId());
            }

        }
    }

    private void setResulLoginError(String msnErr) {
        mData.putExtra(KEY_ERROR, msnErr);
        setResult(RESULT_FIRST_USER, mData);
        finish();
    }

    private void initQRReader() {
        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            boolean useFlash = false;
            boolean autoFocus = true;
            createCameraSource(autoFocus, useFlash);
        } else {
            showPermissionsRequestCamera(this);
        }
    }

    // Stops the camera
    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    // Restarts the camera

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }


    /**
     * Creates and starts the camera.
     * <p>
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.PDF417)
                .build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(this);
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());
        if (!barcodeDetector.isOperational()) {
            Log.w(TAG, "Detector dependencies are not yet available.");
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error,
                        Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        CameraSource.Builder builderCamera = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(metrics.widthPixels, metrics.heightPixels)
                .setRequestedFps(24.0f);
        builderCamera = builderCamera.setFocusMode(
                autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null);

        mCameraSource = builderCamera
                .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .build();
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException {
        // check that the device has play appusr available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                setResultError("Unable to start camera source." + e.getMessage());
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onDetectedQrCode(final Barcode barcode) {
        if (barcode != null) {
            runOnUiThread(() -> {
                if (!barcode.displayValue.isEmpty()) {
                    System.out.println("barcode" + barcode.displayValue);
                    Log.d(TAG, "barcode" + barcode.rawValue);
                    String messageError = parseDataCodeLoadInfoDNI(barcode);
                    if (!messageError.isEmpty()) {
                        setResultError(messageError);
                    }
                    // Visualiza selección de fecha de expedición
                    frameDP.setVisibility(View.VISIBLE);
                    if (mPreview != null) {
                        mPreview.stop();
                    }
                }
            });
        }
    }


    private String parseDataCodeLoadInfoDNI(Barcode barcode) {

        String messageError = "";
        if (barcode != null) {

            String barCode = barcode.displayValue;
            //Log.d(TAG, "Barcode length: " + barcode.displayValue.length());
            if (barcode.displayValue.length() < 150) {
                //TODO lanzar excepcion y mensaje
                messageError = getString(R.string.error_length_code);
            }


            String primerApellido = "", segundoApellido = "", primerNombre = "", segundoNombre = "", cedula = "", rh = "", fechaNacimiento = "", sexo = "";

            String alphaAndDigits = barCode.replaceAll("[^\\p{Alpha}\\p{Digit}\\+\\_]+", " ");
            String[] splitStr = alphaAndDigits.split("\\s+");
            /*
            for (int i=0; i<splitStr.length;i++){
                Log.d(TAG, i + "valor: " + splitStr[i]);
            }
            */
            if (!alphaAndDigits.contains("PubDSK")) {
                int corrimiento = 0;


                Pattern pat = Pattern.compile("[A-Z]");
                Matcher match = pat.matcher(splitStr[2 + corrimiento]);
                int lastCapitalIndex = -1;
                if (match.find()) {
                    lastCapitalIndex = match.start();
                    Log.d(TAG, "match.start: " + match.start());
                    Log.d(TAG, "match.end: " + match.end());
                    Log.d(TAG, "splitStr: " + splitStr[2 + corrimiento]);
                    Log.d(TAG, "splitStr length: " + splitStr[2 + corrimiento].length());
                    Log.d(TAG, "lastCapitalIndex: " + lastCapitalIndex);
                }
                cedula = splitStr[2 + corrimiento].substring(lastCapitalIndex - 10, lastCapitalIndex);
                primerApellido = splitStr[2 + corrimiento].substring(lastCapitalIndex);
                segundoApellido = splitStr[3 + corrimiento];
                primerNombre = splitStr[4 + corrimiento];
                /**
                 * Se verifica que contenga segundo nombre
                 */
                if (Character.isDigit(splitStr[5 + corrimiento].charAt(0))) {
                    corrimiento--;
                } else {
                    segundoNombre = splitStr[5 + corrimiento];
                }

                sexo = splitStr[6 + corrimiento].contains("M") ? "Masculino" : "Femenino";
                rh = splitStr[6 + corrimiento].substring(splitStr[6 + corrimiento].length() - 2);
                fechaNacimiento = splitStr[6 + corrimiento].substring(2, 10);

            } else {
                int corrimiento = 0;
                Pattern pat = Pattern.compile("[A-Z]");
                if (splitStr[2 + corrimiento].length() > 7) {
                    corrimiento--;
                }


                Matcher match = pat.matcher(splitStr[3 + corrimiento]);
                int lastCapitalIndex = -1;
                if (match.find()) {
                    lastCapitalIndex = match.start();

                }

                cedula = splitStr[3 + corrimiento].substring(lastCapitalIndex - 10, lastCapitalIndex);
                primerApellido = splitStr[3 + corrimiento].substring(lastCapitalIndex);
                segundoApellido = splitStr[4 + corrimiento];
                primerNombre = splitStr[5 + corrimiento];
                segundoNombre = splitStr[6 + corrimiento];
                sexo = splitStr[7 + corrimiento].contains("M") ? "Masculino" : "Femenino";
                rh = splitStr[7 + corrimiento].substring(splitStr[7 + corrimiento].length() - 2);
                fechaNacimiento = splitStr[7 + corrimiento].substring(2, 10);

            }
            /**
             * Se setea el objeto con los datos
             */
            infoDNI.setPrimerNombre(primerNombre);
            infoDNI.setSegundoNombre(segundoNombre);
            infoDNI.setPrimerApellido(primerApellido);
            infoDNI.setSegundoApellido(segundoApellido);
            infoDNI.setCedula(cedula);
            infoDNI.setSexo(sexo);
            infoDNI.setFechaNacimiento(fechaNacimiento);
            infoDNI.setRh(rh);


        } else {
            Log.d(TAG, getString(R.string.error_length_code));
            messageError = getString(R.string.error_length_code);
        }

        return messageError;
    }


    //region app response
    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        this.mCallbackManager.onActivityResult(i, i2, intent);
        super.onActivityResult(i, i2, intent);
    }


    public void setResultError(String msnErr) {
        mData.putExtra(KEY_ERROR, msnErr);
        setResult(RESULT_FIRST_USER, mData);
        finish();
    }

    public void setResulUserCanceled() {
        setResult(RESULT_CANCELED, mData);
        finish();
    }


    //endregion


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            boolean autoFocus = true;
            boolean useFlash = false;
            createCameraSource(autoFocus, useFlash);
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        showPermissionsRequestCamera(this);
    }

    @Override
    public void onBackPressed() {
        if (!isHomeActivity)
            super.onBackPressed();
    }

    @Override
    public void onReceiveResultsTransaction(ResponseIV responseIV, int transactionId) {
        runOnUiThread(() -> {

            if (transactionId == INITAUTHRESPONSE) {
                Animation animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
                frameInit.startAnimation(animFadeOut);
                frameInit.setVisibility(View.GONE);
                ((MyApplication) getApplicationContext()).setAccess_token(responseIV.getMessage());
            }

            if (transactionId == USERRESPONSE) {
                Animation animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out_fast);
                frameInit.startAnimation(animFadeOut);
                frameInit.setVisibility(View.GONE);
                infoDNI.setFechaExpedicion(responseIV.getPersonaVO().getFechaExpedicion()); // Retorna toda la información obtenida del servidor y la información obtenida del código de barras
                infoDNI.setLugarExpedicion(responseIV.getPersonaVO().getLugarExpedicion());
                infoDNI.setEstado(responseIV.getPersonaVO().getEstado());
                infoDNI.setResolucion(responseIV.getPersonaVO().getResolucion());
                infoDNI.setFechaResolucion(responseIV.getPersonaVO().getFechaResolucion());
                infoDNI.setFechaConsulta(responseIV.getPersonaVO().getFechaConsulta());
                infoDNI.setFuenteFallo(responseIV.getPersonaVO().getFuenteFallo());
                infoDNI.setNumeroDocumento(responseIV.getPersonaVO().getNumeroDocumento());
                infoDNI.setPais(responseIV.getPersonaVO().getPais());
                infoDNI.setTipoDocumento(responseIV.getPersonaVO().getTipoDocumento());
                mData.putExtra("ResponseIV", (Parcelable) infoDNI);
                setResult(RESULT_OK, mData);
                finish();
            }
        });
    }

    @Override
    public void onErrorTransaction(String errorMsn) {

    }
}
