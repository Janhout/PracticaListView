package com.example.administrador.practicalistview;

import android.graphics.Bitmap;

import java.text.Collator;
import java.util.Locale;

public class Juego implements Comparable<Juego> {
    private String nombre;
    private int publicacion;
    private String informacion;
    private String tipo;
    private Bitmap foto;
    private int dificultad;
    private int puntuacion;
    private boolean expansion;

    public Juego(){}

    public Juego(String nombre, int publicacion, String informacion, String tipo, Bitmap foto, int dificultad, int puntuacion, boolean expansion) {
        this.nombre = nombre;
        this.publicacion = publicacion;
        this.informacion = informacion;
        this.tipo = tipo;
        this.foto = foto;
        this.dificultad = dificultad;
        this.puntuacion = puntuacion;
        this.expansion = expansion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isExpansion() {
        return expansion;
    }

    public void setExpansion(boolean expansion) {
        this.expansion = expansion;
    }

    public int getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(int publicacion) {
        this.publicacion = publicacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    @Override
    public String toString() {
        return nombre + "\n" + tipo + "\n" + informacion + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Juego juego = (Juego) o;

        if (publicacion != juego.publicacion) return false;
        if (!nombre.equals(juego.nombre)) return false;
        return tipo.equals(juego.tipo);

    }

    @Override
    public int hashCode() {
        int result = nombre.hashCode();
        result = 31 * result + publicacion;
        result = 31 * result + tipo.hashCode();
        return result;
    }

    @Override
    public int compareTo(Juego j) {
        Collator collator = Collator.getInstance(Locale.getDefault());
        int compara = collator.compare(this.nombre, j.nombre);
        if(compara == 0){
            compara = this.publicacion>=j.publicacion ?1:0;
        }
        return compara;
    }
}
