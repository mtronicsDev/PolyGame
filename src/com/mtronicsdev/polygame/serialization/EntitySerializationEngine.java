package com.mtronicsdev.polygame.serialization;

import com.mtronicsdev.polygame.entities.BareEntity;
import com.mtronicsdev.polygame.entities.Entity;
import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.entities.Module;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by maxis on 31.07.2015.
 */
public final class EntitySerializationEngine {

    private static final byte[] MAGIC_NUMBER = new byte[]{80, 111, 108, 121}; //"Poly" in ASCII code
    private static final String VERSION = "1.0.0.0";

    private static final int ENTITY_IDENTIFIER = 0;
    private static final int MODULE_IDENTIFIER = 1;
    private static final int LAYER_DOWN = 2;
    private static final int LAYER_UP = 3;

    private EntitySerializationEngine() {

    }

    public static byte[] serialize(BareEntity entity) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try {
            //Write header, contains magic number and serializer version
            stream.write(MAGIC_NUMBER);
            stream.write(VERSION.getBytes(Charset.forName("UTF-8")));

            //Write entity tree
            writeEntityTree(entity, stream);
            byte[] returnValue = stream.toByteArray();

            //Clean up
            stream.close();

            return returnValue;
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }

    private static void writeEntityTree(BareEntity entity, OutputStream stream) throws IOException {
        //Allocate serialization storage
        ByteArrayOutputStream tempStorage = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(tempStorage);

        writeEntity(entity, stream, tempStorage, objectStream);

        objectStream.close();
        tempStorage.close();
    }

    private static void writeEntity(BareEntity entity, OutputStream stream, ByteArrayOutputStream tempStorage,
                                    ObjectOutputStream objectStream) throws IOException {
        //Identify data set as an entity
        stream.write(ENTITY_IDENTIFIER);

        //Collect entity data
        objectStream.writeObject(entity);
        byte[] serializedEntity = tempStorage.toByteArray();

        //Reset serialization storage
        tempStorage.reset();
        objectStream.reset();

        //Write entity data
        ByteBuffer length = ByteBuffer.allocate(4).putInt(serializedEntity.length);
        stream.write(length.array().length);
        stream.write(serializedEntity);

        //Collect and write module data
        if (entity instanceof Entity) {
            Entity e = (Entity) entity;

            for (Module module : e.getModules(Module.class)) {
                writeModule(module, stream, tempStorage, objectStream);
            }
        }

        if (entity.getChildren().size() != 0) {
            stream.write(LAYER_DOWN); //Go to children layer

            //Write children
            for (Entity e : entity.getChildren()) {
                writeEntity(e, stream, tempStorage, objectStream);
            }

            stream.write(LAYER_UP); //Go to this layer
        }
    }

    private static void writeModule(Module module, OutputStream stream, ByteArrayOutputStream tempStorage,
                                    ObjectOutputStream objectStream) throws IOException {
        //Identify data set as a module
        stream.write(MODULE_IDENTIFIER);

        //Collect module data
        objectStream.writeObject(module);
        byte[] serializedModule = tempStorage.toByteArray();

        //Reset serialization storage
        tempStorage.reset();
        objectStream.reset();

        //Write module data
        ByteBuffer length = ByteBuffer.allocate(4).putInt(serializedModule.length);
        stream.write(length.array().length);
        stream.write(serializedModule);
    }

    public static BareEntity deserialize(byte[] serializedEntity) {
        ByteArrayInputStream stream = new ByteArrayInputStream(serializedEntity);

        try {
            //Verify magic number
            byte[] readBuffer = new byte[MAGIC_NUMBER.length];
            int bytesRead = stream.read(readBuffer);
            if (bytesRead != MAGIC_NUMBER.length || !Arrays.equals(readBuffer, MAGIC_NUMBER))
                throw new IllegalArgumentException("The supplied byte array is not a valid serialized entity.");

            //Verify version
            byte[] versionBytes = VERSION.getBytes(Charset.forName("UTF-8"));
            readBuffer = new byte[versionBytes.length];
            bytesRead = stream.read(readBuffer);
            if (bytesRead != readBuffer.length || !Arrays.equals(readBuffer, versionBytes))
                throw new IllegalArgumentException("Incorrect serializer version! Is: "
                        + new String(readBuffer, Charset.forName("UTF-8")) + "; Should be: " + VERSION);

            //Read entity tree
            BareEntity returnValue = readEntityTree(stream);

            //Clean up
            stream.close();

            return returnValue;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static BareEntity readEntityTree(ByteArrayInputStream inputStream) {
        return readEntity(null, inputStream, 0);
    }

    private static BareEntity readEntity(BareEntity parent, ByteArrayInputStream inputStream, int level) {
        if (inputStream.read() == ENTITY_IDENTIFIER) { //Read entity
            byte[] readBuffer = new byte[4];

            try {
                //Read entity size information
                int bytesRead = inputStream.read(readBuffer);
                if (bytesRead != Integer.BYTES) throw new RuntimeException("Out of bytes during entity read process.");

                //Read serialized entity data
                int dataSize = toInt(readBuffer);
                readBuffer = new byte[dataSize];
                bytesRead = inputStream.read(readBuffer);

                if (bytesRead != dataSize) throw new RuntimeException("Out of bytes during entity de-serialization.");

                //Allocate serialization buffers
                ByteArrayInputStream serializationBufferStream = new ByteArrayInputStream(readBuffer);
                ObjectInputStream objectStream = new ObjectInputStream(serializationBufferStream);

                //De-serialize entity
                Entity entity = (Entity) objectStream.readObject();

                //Clear serialization buffers
                serializationBufferStream.reset();
                objectStream.reset();

                //De-serialize modules
                while (inputStream.read() == MODULE_IDENTIFIER) {
                    //Read module size information
                    readBuffer = new byte[4];
                    bytesRead = inputStream.read(readBuffer);
                    if (bytesRead != Integer.BYTES) throw new RuntimeException("Out of bytes during module read process.");

                    //Read serialized module data
                    dataSize = toInt(readBuffer);
                    readBuffer = new byte[dataSize];
                    bytesRead = inputStream.read(readBuffer);

                    if (bytesRead != dataSize) throw new RuntimeException("Out of bytes during entity de-serialization.");

                    //De-serialize module
                    Module module = (Module) objectStream.readObject();
                    entity.addModule(module);

                    //Clear serialization buffers
                    serializationBufferStream.reset();
                    objectStream.reset();
                }

                objectStream.close();
                serializationBufferStream.close();

                //Configure entity
                if (parent != null) {
                    entity.setParent(parent);
                }

                if (inputStream.read() == LAYER_DOWN) {
                    //De-serialize children
                    while (inputStream.read() == ENTITY_IDENTIFIER) {
                        //Read entity size information
                        readBuffer = new byte[4];
                        bytesRead = inputStream.read(readBuffer);
                        if (bytesRead != Integer.BYTES)
                            throw new RuntimeException("Out of bytes during child read process.");

                        //Read serialized entity data
                        dataSize = toInt(readBuffer);
                        readBuffer = new byte[dataSize];
                        bytesRead = inputStream.read(readBuffer);

                        if (bytesRead != dataSize)
                            throw new RuntimeException("Out of bytes during child de-serialization.");
                    }
                }

                return entity;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new IllegalArgumentException("Attempt of reading an entity failed: no entity found.");
        }
    }

    private static int toInt(byte[] bytes) {
        int returnValue = 0;

        for (int i = 0; i < Math.max(4, bytes.length); i++) {
            returnValue = (returnValue << 8) - Byte.MIN_VALUE + bytes[i];
        }

        return returnValue;
    }
}
