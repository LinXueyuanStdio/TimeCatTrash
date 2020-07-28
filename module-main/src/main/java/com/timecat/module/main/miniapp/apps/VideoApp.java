package com.timecat.module.main.miniapp.apps;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.provider.MediaStore.Video.Media;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.ViewSwitcher;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VideoApp extends StandOutWindow {
    public static int id = 15;
    VideoView videoView;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_Video);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_Video);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Video);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.video;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Video);
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
        return ((StandOutFlags.FLAG_DECORATION_SYSTEM | StandOutFlags.FLAG_BODY_MOVE_ENABLE) | StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP) | StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE;
    }

    public List<DropDownListItem> getDropDownItems(final int id) {
        List<DropDownListItem> items = new ArrayList<>();
        items.add(new DropDownListItem(R.mipmap.menu_recent_notes, getString(R.string.main_miniapp_AllVideos), new Runnable() {
            public void run() {
                ((VideoCreator) GeneralUtils.VideoMap.get(Integer.valueOf(id))).switchView(0);
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_play, getString(R.string.main_miniapp_NowPlaying), new Runnable() {
            public void run() {
                if (((VideoCreator) GeneralUtils.VideoMap.get(Integer.valueOf(id))).isVideoInitialized()) {
                    ((VideoCreator) GeneralUtils.VideoMap.get(Integer.valueOf(id))).switchView(1);
                } else {
                    ToastUtil.i(context, "No video playing");
                }
            }
        }));
        return items;
    }

    public void createAndAttachView(int id, FrameLayout frame) {
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_video, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        GeneralUtils.VideoMap.put(Integer.valueOf(id), new VideoCreator());
    }

    public class VideoCreator {
        ArrayList<AudioFileModel> audioFilesList = new ArrayList<>();
        ArrayList<AudioFileModel> filteredAudioFilesList = new ArrayList<>();
        Handler mHandler;
        SeekBar seekBar;
        int videoDuration = 0;
        private EditText etSearch;
        private ImageButton imgBtnPlayPause;
        private ListItemAdapter listAdapter;
        private ArrayList<ListItemModel> listItems;
        private ListView listView;
        private Runnable onEverySecond = new Runnable() {
            @Override
            public void run() {
                if (videoView.isPlaying()) {
                    seekBar.setProgress(videoView.getCurrentPosition());
                    tvTime.setText("" + GeneralUtils.milliSecondsToTimer((long) videoView.getCurrentPosition()));
                }
                seekBar.postDelayed(onEverySecond, 1000);
            }
        };
        private RelativeLayout relativeLayoutVideo;
        private TextView tvNoFiles;
        private TextView tvTime;
        private ViewSwitcher viewSwitcher1;
        private ViewSwitcher viewSwitcher2;
        private ViewSwitcher viewSwitcher3;

        public VideoCreator() {
            this.viewSwitcher1 = (ViewSwitcher) publicView.findViewById(R.id.viewSwitcher1);
            this.viewSwitcher2 = (ViewSwitcher) publicView.findViewById(R.id.viewSwitcher2);
            this.viewSwitcher3 = (ViewSwitcher) publicView.findViewById(R.id.viewSwitcher3);
            videoView = (VideoView) publicView.findViewById(R.id.videoView);
            this.relativeLayoutVideo = (RelativeLayout) publicView.findViewById(R.id.relativeLayoutVideo);
            this.listView = (ListView) publicView.findViewById(R.id.listView);
            this.tvNoFiles = (TextView) publicView.findViewById(R.id.textViewNoFiles);
            this.tvTime = (TextView) publicView.findViewById(R.id.textViewVideoTime);
            this.listItems = new ArrayList<>();
            this.listAdapter = new ListItemAdapter(context, this.listItems);
            this.listView.setAdapter(this.listAdapter);
            this.listItems.clear();
            this.mHandler = new Handler();
            try {
                String[] projection = new String[]{"_data", "title", "_size", "duration"};
                Cursor mCursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, projection, null, null, "title");
                while (mCursor.moveToNext()) {
                    int totalSec = Integer.parseInt(mCursor.getString(3)) / 1000;
                    int min = totalSec / 60;
                    int hr = min / 60;
                    min %= 60;
                    String secs = (totalSec % 60) + "";
                    if (secs.length() == 1) {
                        secs = "0" + secs;
                    }
                    String time = hr + ":" + min + ":" + secs;
                    AudioFileModel video = new AudioFileModel();
                    video.setPath(mCursor.getString(0));
                    video.setTitle(mCursor.getString(1));
                    video.setAlbum(mCursor.getString(2));
                    video.setDuration(time);
                    video.setTotalDuration(totalSec);
                    this.audioFilesList.add(video);
                }
            } catch (Exception e) {
            }
            for (int i = 0; i < this.audioFilesList.size(); i++) {
                this.listItems.add(new ListItemModel((int) R.mipmap.video, ((AudioFileModel) this.audioFilesList.get(i)).getTitle(), ((AudioFileModel) this.audioFilesList.get(i)).getDuration()));
            }
            this.listAdapter.refreshItems();
            if (this.listItems.size() == 0) {
                this.listView.setVisibility(View.INVISIBLE);
                this.tvNoFiles.setVisibility(View.VISIBLE);
            } else {
                this.listView.setVisibility(View.VISIBLE);
                this.tvNoFiles.setVisibility(View.INVISIBLE);
            }
            this.seekBar = (SeekBar) publicView.findViewById(R.id.seekBarVideo);
            this.imgBtnPlayPause = (ImageButton) publicView.findViewById(R.id.imageButtonPlayPause);
            final VideoApp videoApp = VideoApp.this;
            this.imgBtnPlayPause.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (videoView.isPlaying()) {
                        videoView.pause();
                        imgBtnPlayPause.setImageResource(R.mipmap.menu_play);
                        return;
                    }
                    videoView.start();
                    imgBtnPlayPause.setImageResource(R.mipmap.menu_pause);
                }
            });
            final VideoApp videoApp2 = VideoApp.this;
            this.listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    videoView.setVideoPath(((AudioFileModel) filteredAudioFilesList.get(position)).getPath());
                    videoView.start();
                    imgBtnPlayPause.setImageResource(R.mipmap.menu_pause);
                    videoDuration = ((AudioFileModel) filteredAudioFilesList.get(position)).getTotalDuration();
                    switchView(1);
                }
            });
            LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
//            layoutParams.addRule(14, -1);
//            layoutParams.addRule(15, -1);
            videoView.setLayoutParams(layoutParams);
            videoView.invalidate();
            final VideoApp videoApp22 = VideoApp.this;
            videoView.setOnPreparedListener(new OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(videoView.getDuration());
                    seekBar.postDelayed(onEverySecond, 1000);
                    seekBar.setOnSeekBarChangeListener(new C03341());
                }

                class C03341 implements OnSeekBarChangeListener {
                    C03341() {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            videoView.seekTo(progress);
                        }
                    }
                }
            });
            videoView.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    imgBtnPlayPause.setImageResource(R.mipmap.menu_play);
                    videoView.seekTo(0);
                    seekBar.setProgress(0);
                }
            });
            this.etSearch = (EditText) publicView.findViewById(R.id.editTextSearch);
            final VideoApp videoApp2222 = VideoApp.this;
            this.etSearch.addTextChangedListener(new TextWatcher() {
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
                        listItems.add(new ListItemModel((int) R.mipmap.video, ((AudioFileModel) filteredAudioFilesList.get(i)).getTitle(), ((AudioFileModel) filteredAudioFilesList.get(i)).getDuration()));
                    }
                    listAdapter.refreshItems();
                }
            });
            this.filteredAudioFilesList.clear();
            this.filteredAudioFilesList.addAll(this.audioFilesList);
            this.listAdapter.refreshItems();
            switchView(0);
        }

        public void switchView(int index) {
            switch (index) {
                case 0:
                    this.viewSwitcher1.setDisplayedChild(0);
                    this.viewSwitcher2.setDisplayedChild(0);
                    return;
                case 1:
                    this.viewSwitcher1.setDisplayedChild(0);
                    this.viewSwitcher2.setDisplayedChild(1);
                    return;
                case 2:
                    this.viewSwitcher1.setDisplayedChild(1);
                    this.viewSwitcher3.setDisplayedChild(0);
                    return;
                case 3:
                    this.viewSwitcher1.setDisplayedChild(1);
                    this.viewSwitcher3.setDisplayedChild(1);
                    return;
                default:
                    return;
            }
        }

        public boolean isVideoInitialized() {
            return videoView.getDuration() > 0;
        }

    }
}
