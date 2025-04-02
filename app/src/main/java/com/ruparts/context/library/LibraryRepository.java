package com.ruparts.context.library;

import static java.util.Base64.getDecoder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.ruparts.context.task.model.api.LibraryRequest;
import com.ruparts.context.task.service.TaskApiClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class LibraryRepository implements Serializable {

    public static final String SHARED_PREF_NAME = "Library";

    private LibraryModel cachedLibrary;

    private TaskApiClient taskApiClient;

    public LibraryRepository(TaskApiClient taskApiClient) {
        this.taskApiClient = taskApiClient;
    }


    public void init(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor serializedModelEditor = preferences.edit();
        serializedModelEditor.putString("serialized", null);
        String serializedModel = preferences.getString("serialized", null);
        if (serializedModel == null) {
            LibraryModel libraryModel = new LibraryModel();
            libraryModel.taskLibraryModel = taskApiClient.getLibrary(new LibraryRequest());

            serializedModel = serialize(libraryModel);

//            SharedPreferences.Editor serializedModelEditor = preferences.edit();
            serializedModelEditor.putString("serialized", serializedModel);
            serializedModelEditor.commit();

            cachedLibrary = libraryModel;
        } else {
            cachedLibrary = deserialize(serializedModel);
        }
    }



    private String serialize(LibraryModel libraryModel) {
        try (
                ByteArrayOutputStream stringBuffer = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(stringBuffer)
        ) {
                objectOutputStream.writeObject(libraryModel);
                objectOutputStream.flush();

//            return stringBuffer.toString();
            return Base64.getEncoder().encodeToString(stringBuffer.toByteArray());
//            return Base64.getEncoder().encodeToString(stringBuffer.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private LibraryModel deserialize(String serializedData) {

        try {
            byte[] decoded = Base64.getDecoder().decode(serializedData);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decoded);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
//            return (LibraryModel) objectInputStream.readObject();

//            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData.getBytes());
//
//            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            byteArrayInputStream.close();

            return (LibraryModel) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
//        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }

    public LibraryModel getLibrary() {
        return cachedLibrary;
    }
}
