package es.uam.eps.tfg.cas.android.examples.beatbox.model;

/**
 * Created by jutna on 04/04/2016.
 */
public class Sound {
    private final String mAssetPath;
    private final String mName;
    private Integer mSoundId;

    public Sound(final String assetPath) {
        mAssetPath = assetPath;
        final String[] components = assetPath.split("/");
        final String filename = components[components.length - 1];
        mName = filename.replace(".wav", "");
    }

    public String getAssetPath() {
        return mAssetPath;
    }

    public String getName() {
        return mName;
    }

    public Integer getSoundId() {
        return mSoundId;
    }

    public void setSoundId(Integer soundId) {
        mSoundId = soundId;
    }
}
