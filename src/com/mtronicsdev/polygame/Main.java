package com.mtronicsdev.polygame;

import com.mtronicsdev.polygame.entities.Entity;
import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.entities.Module;
import com.mtronicsdev.polygame.serialization.EntitySerializationEngine;
import com.mtronicsdev.polygame.util.math.Vector3f;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by maxis on 31.07.2015.
 */
public class Main {

    public static void main(String... args) {
        Entity e = new Entity(new Module() {
            String test = "HI!";
            @Override
            public void update() {
                super.update();
            }
        });

        new Entity3D(new Vector3f(1, 2, 3), new Vector3f(4, 5, 6), new Module() {
            @Override
            public void update() {
                super.update();
            }
        }, new Module() {
            @Override
            public void update() {
                super.update();
            }
        }).setParent(e);

        new Entity3D(new Vector3f(7, 8, 9), new Vector3f(10, 11, 12), new Module() {
            @Override
            public void update() {
                super.update();
            }
        }, new Module() {
            @Override
            public void update() {
                super.update();
            }
        }).setParent(e);

        File f = new File("serial.ized");
        try {
            if ((!f.exists() && f.createNewFile()) || f.exists()) {
                FileOutputStream outputStream = new FileOutputStream(f);
                outputStream.write(EntitySerializationEngine.serialize(e));
                outputStream.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
