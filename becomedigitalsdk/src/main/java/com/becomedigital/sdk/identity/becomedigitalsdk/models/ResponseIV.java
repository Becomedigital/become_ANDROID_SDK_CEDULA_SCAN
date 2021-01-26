package com.becomedigital.sdk.identity.becomedigitalsdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ResponseIV {

    public static final int ERROR = 2;
    public static final int PENDING = 1;
    public static final int SUCCES = 0;
    private String message;
    private Integer responseStatus;
    private PersonaVO personaVO;

    public ResponseIV(Integer responseStatus, String message) {
        this.responseStatus = responseStatus;
        this.message = message;
    }
    public ResponseIV(PersonaVO personaVO) {
        this.personaVO = personaVO;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public PersonaVO getPersonaVO() {
        return personaVO;
    }

    public void setPersonaVO(PersonaVO personaVO) {
        this.personaVO = personaVO;
    }


}
