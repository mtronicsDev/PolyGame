package com.mtronicsdev.polygame;

import com.mtronicsdev.polygame.io.Preferences;
import com.mtronicsdev.polygame.io.Resources;

public class Main {

    public static void main(String[] args) {
        Resources.registerResourceHandler(file -> "Hi!", String.class);
        System.out.println(Resources.getResource("", String.class));

        Preferences.registerPreferenceHandler(value -> value, String.class);
        System.out.println(Preferences.getPreference("probablyNonExistent", String.class));
    }
}
