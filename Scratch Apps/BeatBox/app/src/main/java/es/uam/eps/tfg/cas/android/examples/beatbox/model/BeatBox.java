package es.uam.eps.tfg.cas.android.examples.beatbox.model;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    private final AssetManager mAssets;
    private final List<Sound> mSoundList = new ArrayList<>();
    private final SoundPool mSoundPool;

    public BeatBox(final Context context) {
        mAssets = context.getAssets();
        //used the deprecated constructor for compatibility
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    private void loadSounds() {
        final String[] soundNames = getSoundNames();
        for (final String fileName : soundNames) {
            try {
                final String assetPath = SOUNDS_FOLDER + "/" + fileName;
                final Sound sound = new Sound(assetPath);

                final int id = loadSound(sound);
                sound.setSoundId(id);
                mSoundList.add(sound);

            } catch (final IOException e) {
                Log.e(TAG, "Couldn't load sound " + fileName, e);
            }
        }
    }

    private int loadSound(final Sound sound) throws IOException {
        final AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        final int soundId = mSoundPool.load(afd, 1);
        return soundId;
    }

    private String[] getSoundNames() {
        final String[] soundNames;
        try {
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds");

        } catch (final IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return null;
        }
        return soundNames;
    }

    public List<Sound> getSounds() {
        return mSoundList;
    }

    public void play(final Integer soundId) {
        if (soundId == null) {
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void release() {
        mSoundPool.release();
    }
}
