package com.becomedigital.sdk.identity.becomedigitalsdk.models;

public class PersonaVO {
    private String fechaExpedicion;
    private String lugarExpedicion;
    private String estado;
    private String resolucion;
    private String fechaResolucion;
    private String fechaConsulta;
    private String fuenteFallo;
    private String numeroDocumento;
    private String tipoDocumento;
    private String pais;
    private String primerNombre;
    private float tipoNombre;

    public PersonaVO(String fechaExpedicion, String lugarExpedicion, String estado, String resolucion, String fechaResolucion, String fechaConsulta, String fuenteFallo, String numeroDocumento, String tipoDocumento, String pais, String primerNombre, float tipoNombre) {
        this.fechaExpedicion = fechaExpedicion;
        this.lugarExpedicion = lugarExpedicion;
        this.estado = estado;
        this.resolucion = resolucion;
        this.fechaResolucion = fechaResolucion;
        this.fechaConsulta = fechaConsulta;
        this.fuenteFallo = fuenteFallo;
        this.numeroDocumento = numeroDocumento;
        this.tipoDocumento = tipoDocumento;
        this.pais = pais;
        this.primerNombre = primerNombre;
        this.tipoNombre = tipoNombre;
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

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public float getTipoNombre() {
        return tipoNombre;
    }

    public void setTipoNombre(float tipoNombre) {
        this.tipoNombre = tipoNombre;
    }
}
