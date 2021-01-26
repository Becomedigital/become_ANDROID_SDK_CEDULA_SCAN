package com.becomedigital.sdk.identity.becomedigitalsdk.callback;


import com.becomedigital.sdk.identity.becomedigitalsdk.models.InfoDNI;

public interface BecomeInterfaseCallback {
    void onSuccess(InfoDNI responseIV);
    void onCancel();
    void onError(LoginError pLoginError);
}
