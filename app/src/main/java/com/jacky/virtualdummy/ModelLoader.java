package com.jacky.virtualdummy;

import android.content.Context;

import com.google.ar.sceneform.rendering.ModelRenderable;

import java.lang.ref.WeakReference;
import java.util.concurrent.CompletableFuture;

public class ModelLoader {
    private static final String TAG = "ModelLoader";
    private final WeakReference<ModelLoaderCallbacks> owner;
    private CompletableFuture<ModelRenderable> future;

    ModelLoader(ModelLoaderCallbacks owner) {
        this.owner = new WeakReference<>(owner);
    }

    boolean loadModel(Context context, int resourceId) {

        future =
                ModelRenderable.builder()
                        .setSource(context, resourceId)
                        .build()
                        .thenApply(this::setRenderable)
                        .exceptionally(this::onException);
        return future != null;
    }

    ModelRenderable onException(Throwable throwable) {
        ModelLoaderCallbacks listener = owner.get();
        if (listener != null) {
            listener.onLoadException(throwable);
        }
        return null;
    }

    ModelRenderable setRenderable(ModelRenderable modelRenderable) {
        ModelLoaderCallbacks listener = owner.get();
        if (listener != null) {
            listener.setRenderable(modelRenderable);
        }
        return modelRenderable;
    }

    /** Callbacks for handling the loading results. */
    public interface ModelLoaderCallbacks {
        void setRenderable(ModelRenderable modelRenderable);

        void onLoadException(Throwable throwable);
    }
}


