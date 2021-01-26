package com.becomedigital.sdk.identity.becomedigitalsdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * **************************************************************
 * Copyright (c) 2016 - 2016 Avanza, All rights reserved
 * <p/>
 * -
 * Descripcion de la clase
 * -
 * Autor:		Carlos Arturo Reyes Romero
 * email:		carr900@gmail.com
 * Creado:   	25/04/2016
 * Proyecto: 	barcode-reader
 * ****************************************************************
 */
public class InfoDNI implements Parcelable, Serializable {

    private String primerApellido = "";
    private String segundoApellido = "";
    private String primerNombre = "";
    private String segundoNombre = "";
    private String cedula = "";
    private String rh = "";
    private String fechaNacimiento = "";
    private String sexo = "";
    private String fechaExpedicion = "";
    private String lugarExpedicion = "";
    private String estado = "";
    private String resolucion = "";
    private String fechaResolucion = "";
    private String fechaConsulta = "";
    private String fuenteFallo = "";
    private String numeroDocumento = "";
    private String tipoDocumento = "";
    private String pais = "";
    private float tipoNombre = 0;


    public InfoDNI() {

    }

    protected InfoDNI(Parcel in) {
        primerApellido = in.readString();
        segundoApellido = in.readString();
        primerNombre = in.readString();
        segundoNombre = in.readString();
        cedula = in.readString();
        rh = in.readString();
        fechaNacimiento = in.readString();
        sexo = in.readString();
        fechaExpedicion = in.readString();
        lugarExpedicion = in.readString();
        estado = in.readString();
        resolucion = in.readString();
        fechaResolucion = in.readString();
        fechaConsulta = in.readString();
        fuenteFallo = in.readString();
        numeroDocumento = in.readString();
        tipoDocumento = in.readString();
        pais = in.readString();
        tipoNombre = in.readFloat();
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getRh() {
        return rh;
    }

    public void setRh(String rh) {
        this.rh = rh;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(String fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public String getLugarExpedicion() {
        return lugarExpedicion;
    }

    public void setLugarExpedicion(String lugarExpedicion) {
        this.lugarExpedicion = lugarExpedicion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getResolucion() {
        return resolucion;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }

    public String getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(String fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public String getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(String fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getFuenteFallo() {
        return fuenteFallo;
    }

    public void setFuenteFallo(String fuenteFallo) {
        this.fuenteFallo = fuenteFallo;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public float getTipoNombre() {
        return tipoNombre;
    }

    public void setTipoNombre(float tipoNombre) {
        this.tipoNombre = tipoNombre;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(primerApellido);
        dest.writeString(segundoApellido);
        dest.writeString(primerNombre);
        dest.writeString(segundoNombre);
        dest.writeString(cedula);
        dest.writeString(rh);
        dest.writeString(fechaNacimiento);
        dest.writeString(sexo);
        dest.writeString(fechaExpedicion);
        dest.writeString(lugarExpedicion);
        dest.writeString(estado);
        dest.writeString(resolucion);
        dest.writeString(fechaResolucion);
        dest.writeString(fechaConsulta);
        dest.writeString(fuenteFallo);
        dest.writeString(numeroDocumento);
        dest.writeString(tipoDocumento);
        dest.writeString(pais);
        dest.writeFloat(tipoNombre);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InfoDNI> CREATOR = new Creator<InfoDNI>() {
        @Override
        public InfoDNI createFromParcel(Parcel in) {
            return new InfoDNI(in);
        }

        @Override
        public InfoDNI[] newArray(int size) {
            return new InfoDNI[size];
        }
    };

    @Override
    public String toString() {
        return "InfoTarjeta{" +
                "primerApellido='" + primerApellido + '\'' +
                ", segundoApellido='" + segundoApellido + '\'' +
                ", primerNombre='" + primerNombre + '\'' +
                ", segundoNombre='" + segundoNombre + '\'' +
                ", cedula='" + cedula + '\'' +
                ", rh='" + rh + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", sexo='" + sexo + '\'' +
                ", fechaExpedicion='" + fechaExpedicion + '\'' +
                ", lugarExpedicion='" + lugarExpedicion + '\'' +
                ", estado='" + estado + '\'' +
                ", resolucion='" + resolucion + '\'' +
                ", fechaResolucion='" + fechaResolucion + '\'' +
                ", fechaConsulta='" + fechaConsulta + '\'' +
                ", fuenteFallo='" + fuenteFallo + '\'' +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", pais='" + pais + '\'' +
                ", tipoNombre='" + tipoNombre + '\'' +
                '}';
    }


}
