package com.example.scanfindapp;

public class producto {
    public String nombreArt;
    public String descArt;
    public String imagArt;

    public producto(){

    }

    public producto(String nombreArt, String descArt, String imagArt) {
        this.nombreArt = nombreArt;
        this.descArt = descArt;
        this.imagArt = imagArt;
    }

    public String getNombreArt() {
        return nombreArt;
    }

    public void setNombreArt(String nombreArt) {
        this.nombreArt = nombreArt;
    }

    public String getDescArt() {
        return descArt;
    }

    public void setDescArt(String descArt) {
        this.descArt = descArt;
    }

    public String getImagArt() {
        return imagArt;
    }

    public void setImagArt(String imagArt) {
        this.imagArt = imagArt;
    }
}
