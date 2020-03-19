package com.example.myapplication1;

import android.content.Context;
import android.content.Intent;

public class Preferences {
    String surname;

    public Preferences(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    Intent obtenirReglages(Context c) {
        return new Intent(c, getClass());
    }
}
