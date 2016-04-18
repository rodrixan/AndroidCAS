package es.uam.eps.tfg.cas.android.examples.beatbox.controller.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import es.uam.eps.tfg.cas.android.examples.beatbox.R;
import es.uam.eps.tfg.cas.android.examples.beatbox.model.BeatBox;
import es.uam.eps.tfg.cas.android.examples.beatbox.model.Sound;

public class BeatBoxFragment extends Fragment {

    private BeatBox mBeatBox;

    public static BeatBoxFragment newInstance() {
        return new BeatBoxFragment();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mBeatBox = new BeatBox(getActivity());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_beat_box, container, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_beat_box_recycler_view);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(new SoundAdapter(mBeatBox.getSounds()));

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBeatBox.release();
    }

    private class SoundHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Button mButton;
        private Sound mSound;

        public SoundHolder(final LayoutInflater inflater, final ViewGroup container) {
            super(inflater.inflate(R.layout.list_item_sound, container, false));
            mButton = (Button) itemView.findViewById(R.id.list_item_sound_button);
            mButton.setOnClickListener(this);
        }

        public void bindSound(final Sound sound) {
            mSound = sound;
            mButton.setText(mSound.getName());
        }

        @Override
        public void onClick(final View v) {
            mBeatBox.play(mSound.getSoundId());
        }
    }

    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder> {

        private final List<Sound> mSoundlist;

        public SoundAdapter(final List<Sound> sounds) {
            mSoundlist = sounds;
        }

        @Override
        public SoundHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new SoundHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(final SoundHolder holder, final int position) {
            final Sound sound = mSoundlist.get(position);
            holder.bindSound(sound);
        }

        @Override
        public int getItemCount() {
            return mSoundlist.size();
        }
    }

}
