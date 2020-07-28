package com.timecat.module.main.miniapp.apps;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore.Audio.Media;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.timecat.component.alert.ToastUtil;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.adapters.ListItemAdapter;
import com.timecat.module.main.miniapp.models.AudioFileModel;
import com.timecat.module.main.miniapp.models.ListItemModel;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import com.timecat.plugin.window.StandOutFlags;
import com.timecat.plugin.window.StandOutWindow;
import com.timecat.plugin.window.Window;
import com.timecat.plugin.window.WindowAgreement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MusicApp extends StandOutWindow {
    Handler mHandler;
    MediaPlayer mediaPlayer;
    MusicCreator music;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_Music);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_Music);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Music);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.player;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Music);
    }

    public String getHiddenNotificationMessage(int id) {
        return getString(R.string.main_miniapp_mininized);
    }

    public Intent getHiddenNotificationIntent(int id) {
        return WindowAgreement.getShowIntent(this, getClass(), id);
    }

    public Animation getShowAnimation(int id) {
        if (isExistingId(id)) {
            return AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        }
        return super.getShowAnimation(id);
    }

    public Animation getHideAnimation(int id) {
        return AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
    }

    public StandOutLayoutParams getParams(int id, Window window) {
        int h = SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT").equals("") ? 200 : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT"));
        int w = SettingsUtils.GetValue(window.getContext(), getAppName() + "WIDTH").equals("") ? 200 : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "WIDTH"));
        int x = SettingsUtils.GetValue(window.getContext(), getAppName() + "XPOS").equals("") ? Integer.MIN_VALUE : (int) Float.parseFloat(SettingsUtils.GetValue(window.getContext(), getAppName() + "XPOS"));
        int y = SettingsUtils.GetValue(window.getContext(), getAppName() + "YPOS").equals("") ? Integer.MIN_VALUE : (int) Float.parseFloat(SettingsUtils.GetValue(window.getContext(), getAppName() + "YPOS"));
        if (h < GeneralUtils.GetDP(window.getContext(), 200)) {
            h = GeneralUtils.GetDP(window.getContext(), 200);
        }
        if (w < GeneralUtils.GetDP(window.getContext(), 200)) {
            w = GeneralUtils.GetDP(window.getContext(), 200);
        }
        return new StandOutLayoutParams(id, w, h, x, y, GeneralUtils.GetDP(window.getContext(), 56), GeneralUtils.GetDP(window.getContext(), 56));
    }

    public int getFlags(int id) {
        return (((StandOutFlags.FLAG_DECORATION_SYSTEM | StandOutFlags.FLAG_BODY_MOVE_ENABLE) | StandOutFlags.FLAG_WINDOW_HIDE_ENABLE) | StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP) | StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE;
    }

    public List<DropDownListItem> getDropDownItems(int id) {
        List<DropDownListItem> items = new ArrayList<>();
        items.add(new DropDownListItem(R.mipmap.menu_recent_notes, getString(R.string.main_miniapp_AllSongs), () -> music.awesomePager.setCurrentItem(0)));
        items.add(new DropDownListItem(R.mipmap.menu_play, getString(R.string.main_miniapp_NowPlaying), () -> music.awesomePager.setCurrentItem(1)));
        items.add(new DropDownListItem(R.mipmap.menu_shuffle, getString(R.string.main_miniapp_ShuffleONOFF), () -> {
            if (SettingsUtils.GetValue(context, "MUSIC_SHUFFLE").equals("") || SettingsUtils.GetValue(context, "MUSIC_SHUFFLE").equals("NO")) {
                SettingsUtils.SetValue(context, "MUSIC_SHUFFLE", "YES");
                ToastUtil.i(context, "Shuffle On");
                return;
            }
            SettingsUtils.SetValue(context, "MUSIC_SHUFFLE", "NO");
            ToastUtil.i(context, "Shuffle Off");
        }));
        return items;
    }

    public void createAndAttachView(int id, FrameLayout frame) {
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_music, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        this.music = new MusicCreator();
    }

    public void exitPlayer() {
        this.mediaPlayer.stop();
    }

    public class MusicCreator {
        ImageButton buttonNext;
        ImageButton buttonPlayPause;
        ImageButton buttonPrevious;
        int currentIndex = 0;
        ImageView imgThumb;
        int seekBackwardTime = 1000;
        SeekBar seekBar;
        int seekForwardTime = 1000;
        ArrayList<AudioFileModel> filteredAudioFilesList = new ArrayList<>();
        ArrayList<AudioFileModel> audioFilesList = new ArrayList<>();
        private ViewPager awesomePager;
        private EditText etSearch;
        private ListItemAdapter listAdapter;
        private ArrayList<ListItemModel> listItems;
        private ListView listView;
        private TextView tvName;
        private TextView tvNoFiles;
        private TextView tvTime;
        private AwesomePagerAdapter awesomeAdapter = new AwesomePagerAdapter();
        private Runnable mUpdateTimeTask = new Runnable() {
            @Override
            public void run() {
                long totalDuration = (long) mediaPlayer.getDuration();
                tvTime.setText("" + GeneralUtils.milliSecondsToTimer((long) mediaPlayer.getCurrentPosition()));
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                mHandler.postDelayed(this, 100);
            }
        };

        public MusicCreator() {
            this.awesomePager = (ViewPager) publicView.findViewById(R.id.awesomepager);
            this.awesomePager.setAdapter(this.awesomeAdapter);
            this.awesomePager.setCurrentItem(0);
            mediaPlayer = new MediaPlayer();
            mHandler = new Handler();
        }

        public void playSong(int index) {
            this.currentIndex = index;
            AudioFileModel audioFile = (AudioFileModel) this.filteredAudioFilesList.get(index);
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(audioFile.getPath());
                mediaPlayer.prepare();
                mediaPlayer.start();
                this.buttonPlayPause.setImageResource(R.mipmap.menu_pause);
            } catch (IOException e) {
            }
            this.tvName.setText(audioFile.getTitle());
            this.tvTime.setText(audioFile.getDuration());
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    boolean shuffle;
                    buttonPlayPause.setImageResource(R.mipmap.menu_play);
                    if (SettingsUtils.GetValue(context, "MUSIC_SHUFFLE").equals("") || SettingsUtils.GetValue(context, "MUSIC_SHUFFLE").equals("NO")) {
                        shuffle = false;
                    } else {
                        shuffle = true;
                    }
                    if (shuffle) {
                        playSong(new Random().nextInt(((filteredAudioFilesList.size() - 1)) + 1));
                        return;
                    }
                    playNext();
                }
            });
            Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), (long) Integer.valueOf(audioFile.getAlbumId()).intValue());
            ContentResolver res = getApplicationContext().getContentResolver();
            Bitmap artwork = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.player_thumb);
            try {
                artwork = BitmapFactory.decodeStream(res.openInputStream(uri));
            } catch (Exception e2) {
            }
            this.imgThumb.setImageBitmap(artwork);
            this.seekBar.setMax(mediaPlayer.getDuration());
            this.seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress);
                    }
                }
            });
            updateProgressBar();
        }

        public void playNext() {
            if (this.currentIndex < this.listItems.size() - 1) {
                playSong(this.currentIndex + 1);
            } else {
                playSong(0);
            }
        }

        public void updateProgressBar() {
            mHandler.postDelayed(this.mUpdateTimeTask, 100);
        }

        void stopPlayer() {
            mediaPlayer.stop();
            this.tvName.setText("No songs playing");
            this.tvTime.setText("0:00");
            this.imgThumb.setImageResource(R.mipmap.player_thumb);
            this.buttonPlayPause.setImageResource(R.mipmap.menu_play);
            this.seekBar.setProgress(0);
        }

        class AwesomePagerAdapter extends PagerAdapter {
            private int NUM_AWESOME_VIEWS = 2;
            private View fragment1;
            private View fragment2;
            private View fragment3;

            public AwesomePagerAdapter() {
                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                this.fragment1 = inflater.inflate(R.layout.app_music_fragment1, null);
                this.fragment2 = inflater.inflate(R.layout.app_music_fragment2, null);
                this.fragment3 = inflater.inflate(R.layout.app_music_fragment3, null);
                listView = (ListView) this.fragment1.findViewById(R.id.listView);
                tvName = (TextView) this.fragment2.findViewById(R.id.textViewSongName);
                tvTime = (TextView) this.fragment2.findViewById(R.id.textViewSongTime);
                tvNoFiles = (TextView) this.fragment1.findViewById(R.id.textViewNoFiles);
                buttonPlayPause = (ImageButton) this.fragment2.findViewById(R.id.buttonPlayPause);
                buttonNext = (ImageButton) this.fragment2.findViewById(R.id.buttonNext);
                buttonPrevious = (ImageButton) this.fragment2.findViewById(R.id.buttonPrevious);
                listItems = new ArrayList<>();
                listAdapter = new ListItemAdapter(context, listItems);
                listView.setAdapter(listAdapter);
                try {
                    String[] projection = new String[]{"_data", "title", "album", "artist", "duration", "album_id"};
                    Cursor mCursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, projection, "is_music != 0", null, "title");
                    while (mCursor.moveToNext()) {
                        int totalSec = Integer.parseInt(mCursor.getString(4)) / 1000;
                        int min = totalSec / 60;
                        String secs = (totalSec % 60) + "";
                        if (secs.length() == 1) {
                            secs = "0" + secs;
                        }
                        String time = min + ":" + secs;
                        AudioFileModel audio = new AudioFileModel();
                        audio.setPath(mCursor.getString(0));
                        audio.setTitle(mCursor.getString(1));
                        audio.setAlbum(mCursor.getString(2));
                        audio.setArtist(mCursor.getString(3));
                        audio.setDuration(time);
                        audio.setAlbumId(mCursor.getString(5));
                        audioFilesList.add(audio);
                    }
                } catch (Exception e) {
                }
                listItems.clear();
                for (int i = 0; i < audioFilesList.size(); i++) {
                    String artist = ((AudioFileModel) audioFilesList.get(i)).getArtist().trim();
                    String title = ((AudioFileModel) audioFilesList.get(i)).getTitle();
                    if (artist.equals("") || artist.toLowerCase().trim().equals("<unknown>")) {
                        artist = "Unknown";
                    }
                    listItems.add(new ListItemModel(R.mipmap.player, title, artist, ((AudioFileModel) audioFilesList.get(i)).getDuration()));
                }
                listAdapter.refreshItems();
                if (listItems.size() == 0) {
                    listView.setVisibility(View.INVISIBLE);
                    tvNoFiles.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                    tvNoFiles.setVisibility(View.INVISIBLE);
                }
                seekBar = (SeekBar) this.fragment2.findViewById(R.id.seekBarMusic);
                imgThumb = (ImageView) this.fragment2.findViewById(R.id.imageView);
                final MusicCreator musicCreator = MusicCreator.this;
                listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        playSong(position);
                        awesomePager.setCurrentItem(1);
                    }
                });
                final MusicCreator musicCreator2 = MusicCreator.this;
                buttonPlayPause.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (mediaPlayer.isPlaying()) {
                            if (mediaPlayer != null) {
                                mediaPlayer.pause();
                                buttonPlayPause.setImageResource(R.mipmap.menu_play);
                            }
                        } else if (mediaPlayer != null) {
                            mediaPlayer.start();
                            buttonPlayPause.setImageResource(R.mipmap.menu_pause);
                        }
                    }
                });
                final MusicCreator musicCreator22 = MusicCreator.this;
                buttonNext.setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        boolean shuffle;
                        if (SettingsUtils.GetValue(context, "MUSIC_SHUFFLE").equals("") || SettingsUtils.GetValue(context, "MUSIC_SHUFFLE").equals("NO")) {
                            shuffle = false;
                        } else {
                            shuffle = true;
                        }
                        if (shuffle) {
                            playSong(new Random().nextInt(((filteredAudioFilesList.size() - 1) + 0) + 1) + 0);
                            return;
                        }
                        playNext();
                    }
                });
                final MusicCreator musicCreator222 = MusicCreator.this;
                buttonPrevious.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (currentIndex > 0) {
                            playSong(currentIndex - 1);
                        } else {
                            playSong(listItems.size() - 1);
                        }
                    }
                });
                etSearch = (EditText) this.fragment1.findViewById(R.id.editTextSearch);
                final MusicCreator musicCreator2222 = MusicCreator.this;
                etSearch.addTextChangedListener(new TextWatcher() {
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void afterTextChanged(Editable s) {
                        filteredAudioFilesList.clear();
                        Iterator it = audioFilesList.iterator();
                        while (it.hasNext()) {
                            AudioFileModel item = (AudioFileModel) it.next();
                            if (item.getTitle().toLowerCase().trim().contains(s.toString().toLowerCase().trim())) {
                                filteredAudioFilesList.add(item);
                            }
                        }
                        listItems.clear();
                        for (int i = 0; i < filteredAudioFilesList.size(); i++) {
                            String artist = ((AudioFileModel) filteredAudioFilesList.get(i)).getArtist().trim();
                            ArrayList access$700 = listItems;
                            String title = ((AudioFileModel) filteredAudioFilesList.get(i)).getTitle();
                            if (artist.equals("") || artist.toLowerCase().trim().equals("<unknown>")) {
                                artist = "Unknown";
                            }
                            access$700.add(new ListItemModel(R.mipmap.player, title, artist, ((AudioFileModel) filteredAudioFilesList.get(i)).getDuration()));
                        }
                        listAdapter.refreshItems();
                    }
                });
                filteredAudioFilesList.clear();
                filteredAudioFilesList.addAll(audioFilesList);
                listAdapter.refreshItems();
            }

            public Object instantiateItem(ViewGroup collection, int position) {
                if (position == 0) {
                    collection.addView(this.fragment1, 0);
                    return this.fragment1;
                } else if (position == 1) {
                    collection.addView(this.fragment2, 0);
                    return this.fragment2;
                } else {
                    collection.addView(this.fragment3, 0);
                    return this.fragment3;
                }
            }

            public int getCount() {
                return this.NUM_AWESOME_VIEWS;
            }

            public void destroyItem(ViewGroup collection, int position, Object view) {
                collection.removeView((View) view);
            }

            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            public void finishUpdate(ViewGroup arg0) {
            }

            public void restoreState(Parcelable arg0, ClassLoader arg1) {
            }

            public Parcelable saveState() {
                return null;
            }

            public void startUpdate(ViewGroup arg0) {
            }
        }
    }
}
