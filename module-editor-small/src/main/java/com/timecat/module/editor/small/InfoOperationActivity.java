//package com.timecat.module.editor.small;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.graphics.drawable.GradientDrawable;
//import android.graphics.drawable.LayerDrawable;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Vibrator;
//import android.provider.CalendarContract;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.webkit.WebView;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.ToggleButton;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.content.res.AppCompatResources;
//import androidx.core.content.ContextCompat;
//import androidx.core.math.MathUtils;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.alibaba.android.arouter.facade.annotation.Autowired;
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.bigkoo.pickerview.builder.TimePickerBuilder;
//import com.bigkoo.pickerview.listener.CustomListener;
//import com.bigkoo.pickerview.view.TimePickerView;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
//import com.chad.library.adapter.base.viewholder.BaseViewHolder;
//import com.getkeepsafe.taptargetview.TapTarget;
//import com.getkeepsafe.taptargetview.TapTargetSequence;
//import com.github.florent37.viewtooltip.ViewTooltip.Position;
//import com.timecat.component.alert.MaterialDialogProvider;
//import com.timecat.component.alert.ToastUtil;
//import com.timecat.component.bmob.dao.UserModel;
//import com.timecat.component.bmob.data._User;
//import com.timecat.component.commonbase.base.mvp.presenter.ActivityPresenter;
//import com.timecat.component.commonbase.base.mvp.view.BaseSimpleActivity;
//import com.timecat.component.commonbase.utils.ClipboardCopy;
//import com.timecat.component.commonsdk.utils.Calculate;
//import com.timecat.component.commonsdk.utils.ShareUtils;
//import com.timecat.component.commonsdk.utils.UrlCountUtil;
//import com.timecat.component.commonsdk.utils.override.LogUtil;
//import com.timecat.component.commonsdk.utils.string.StringUtil;
//import com.timecat.component.commonsdk.utils.string.TimeUtil;
//import com.timecat.component.data.database.DB;
//import com.timecat.component.data.database.dao.SearchEngineUtil;
//import com.timecat.component.data.model.Converter;
//import com.timecat.component.data.model.DBModel.DBHabit;
//import com.timecat.component.data.model.DBModel.DBNote;
//import com.timecat.component.data.model.DBModel.DBNoteBook;
//import com.timecat.component.data.model.DBModel.DBSubPlan;
//import com.timecat.component.data.model.DBModel.DBTask;
//import com.timecat.component.data.model.DBModel.DBUser;
//import com.timecat.component.data.model.StringUtils;
//import com.timecat.component.data.model.entity.ITag;
//import com.timecat.component.data.model.entity.RoutineClock;
//import com.timecat.component.data.model.events.PersistenceEvents;
//import com.timecat.component.data.network.SimpleObserver;
//import com.timecat.component.data.service.NotificationService;
//import com.timecat.component.data.service.WidgetService;
//import com.timecat.component.data.service.WindowService;
//import com.timecat.component.data.system.SystemCalendarManager;
//import com.timecat.component.readonly.Constants;
//import com.timecat.component.readonly.RouterHub;
//import com.timecat.component.router.app.NAV;
//import com.timecat.component.setting.DEF;
//import com.timecat.component.ui.utils.ViewUtil;
//import com.timecat.component.ui.view.MiniVerticalActionView;
//import com.timecat.component.ui.view.SwipeSpinnerHelper;
//import com.timecat.component.ui.view.keyboardManager.SmartKeyboardManager;
//import com.timecat.component.ui.view.label_tag_view.TagCloudView;
//import com.timecat.component.ui.view.richText.TEditText;
//
//import org.greenrobot.eventbus.EventBus;
//import org.joda.time.DateTime;
//
//import java.net.URLEncoder;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.TimeZone;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import eu.davidea.flipview.FlipView;
//import io.reactivex.ObservableOnSubscribe;
//import my.shouheng.palmmarkdown.MarkdownViewer;
//import rx.Observable;
//import rx.Subscriber;
//import rx.schedulers.Schedulers;
//
///**
// * @author dlink
// * @date 2018/2/14
// * @description 信息操作页面, 包括创建、修改、转化,用activity实现dialog
// * @usage ARouter.getInstance().build(RouterHub.MASTER_InfoOperationActivity) .withObject("task", item) .withObject("note", item) .withObject("habit", item) .withString("str",
// * item.getContent()) .navigation();
// */
////@Route(path = RouterHub.MASTER_InfoOperationActivity)
//public class InfoOperationActivity extends BaseSimpleActivity
//        implements ActivityPresenter, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
//
//    //region 数据
//    //region static数据
//    public static final String STR = "str";
//    public static final String TASK = "task";
//    public static final String NOTE = "note";
//    public static final String HABIT = "habit";
//    /**
//     * 铃声选择按钮的requestCode
//     */
//    private static final int REQUEST_RING_SELECT = 1;
//    /**
//     * 督促按钮的requestCode
//     */
//    private static final int REQUEST_NAP_EDIT = 2;
//    //endregion
//
//    //region Data数据区
//    @Autowired
//    String str;
//    @Autowired
//    DBTask task;
//    @Autowired
//    DBNote note;
//    @Autowired
//    DBHabit habit;
//    @Autowired
//    DBSubPlan subPlan_toAttach;
//    @Autowired
//    DBNoteBook noteBook_toAttach;
//    @Nullable
//    @Autowired(name = RouterHub.GLOBAL_WindowServiceImpl)
//    WindowService windowService;
//    @Nullable
//    @Autowired(name = RouterHub.GLOBAL_WidgetServiceImpl)
//    WidgetService widgetService;
//    @Nullable
//    @Autowired(name = RouterHub.GLOBAL_NotificationServiceImpl)
//    NotificationService notificationService;
//    int important_urgent_label;
//    //用户是否编辑了title，编辑即视为有自定义的需求，取用户自定义的title，不再同步content里的到title
//    boolean isSelfEdit = false;
//    boolean is_all_day = false;
//    @NonNull
//    DateTime startDateTime = new DateTime();
//    @NonNull
//    DateTime endDateTime = new DateTime().plusDays(1).withMillisOfDay(0).minusMillis(1);
//    int repeat;
//    String[] text_color_set = new String[]{"#f44336", "#ff9800", "#2196f3", "#4caf50"};
//    String[] background_color_set = new String[]{"#50f44336", "#50ff9800", "#502196f3", "#504caf50"};
//    String[] label_str_set = new String[]{"重要且紧急", "重要不紧急", "紧急不重要", "不重要不紧急",};
//    List<ITag> allTags = new ArrayList<>();
//    List<ITag> curTags = new ArrayList<>();
//    int nextContentPosInFlipView = 0;
//    RemindType currentRemindType = RemindType.noRemind;
//
//    private String title;
//    private String content;
//    private Type type;
//    //endregion
//
//    //region UI显示区
//    //region UI field
//    private TEditText dialog_add_task_et_title;
//    private TEditText dialog_add_task_et_content;
//    private ImageView clear;
//    private TextView dialog_add_task_tv_important_urgent;
//    private TextView dialog_add_task_tv_start_datetime;
//    private TextView dialog_add_task_tv_end_datetime;
//    private TextView dialog_add_task_tv_remind;
//    private TextView dialog_add_task_tv_calendar;
//    private TextView dialog_add_task_tv_tag;
//    private TextView dialog_add_task_tv_notebook;
//    private TextView dialog_add_task_tv_plan;
//    private TagCloudView dialog_add_task_tag_cloud;
//    private RecyclerView switch_type;
//    private View moreCard;
//    private Button dialog_add_task_footer_bt_submit;
//    private ViewGroup dialog_add_task_ll_content;
//    private FlipView switch_content;
//    private MarkdownViewer schedule_task_tv_content;
//    private MiniVerticalActionView miniActionView;
//    //endregion
//    //region 软键盘管理
//    SmartKeyboardManager mSmartKeyboardManager;
//    //endregion
//    //region 重要紧急选择面板
//    private LinearLayout dialog_add_task_select_ll_important_urgent;
//    private TextView select_tv_important_urgent;
//    private TextView select_tv_important_not_urgent;
//    private TextView select_tv_not_important_urgent;
//    private TextView select_tv_not_important_not_urgent;
//    //endregion
//    //region start datetime 选择面板
//    private FrameLayout select_fl_start_datetime;
//    private LinearLayout select_ll_start_datetime;
//
//    List<Calendar> calendarList;
//    private TimePickerView pickStartDateTime;
//    //endregion
//    //region end datetime 选择面板
//    private FrameLayout select_fl_end_datetime;
//    private LinearLayout select_ll_end_datetime;
//    private TimePickerView pickEndDateTime;
//    //endregion
//    //region 提醒选择面板
//    private ScrollView dialog_add_task_select_sv_container;
//    /**
//     * 铃声描述
//     */
//    private TextView mRingDescribe;
//    private TextView mTimePickerTv;
//
//    /**
//     * 闹钟实例
//     */
//    private RoutineClock mAlarmClock;
//    //endregion
//    //region 标签选择面板
//    private RelativeLayout dialog_add_task_select_ll_tag;
//    private TagCloudView tagCloudView;
//    private TextView editTag;
//    private TextView addTag;
//
//    private int lastPickedColor;//存argb
//    //endregion
//    //endregion
//    //endregion
//
//    //region 生命周期
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        ARouter.getInstance().inject(this);
//        super.onCreate(savedInstanceState);
//        setTheme(R.style.EditDialogStyle);
//        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//
//        //region 功能归类分区方法，必须调用
//        preInit();
//        initView();
//        initData();
//        postInit();
//        initEvent();
//        //endregion
//    }
//
//    @Override
//    protected void onResume() {
//        mSmartKeyboardManager.hideAllCustomKeyboard(true);
//        showInputMethod4Content();
//        super.onResume();
//    }
//
//    @Override
//    protected void onDestroy() {
//
//        super.onDestroy();
//    }
//
//    @Override
//    public boolean needCommonTheme() {
//        return false;
//    }
//    //endregion
//
//    //region UI显示区
//    @Override
//    public void initView() {//必须调用
//        setWindow();
//        mAlarmClock = new RoutineClock();
//
//        dialog_add_task_et_title = findViewById(R.id.dialog_add_task_et_title);
//        dialog_add_task_et_content = findViewById(R.id.dialog_add_task_et_content);
//        clear = findViewById(R.id.clear);
//
//        dialog_add_task_tv_important_urgent = findViewById(R.id.dialog_add_task_tv_important_urgent);
//        dialog_add_task_tv_start_datetime = findViewById(R.id.dialog_add_task_tv_date);
//        dialog_add_task_tv_end_datetime = findViewById(R.id.dialog_add_task_tv_time);
//        dialog_add_task_tv_remind = findViewById(R.id.dialog_add_task_tv_remind);
//        dialog_add_task_tv_calendar = findViewById(R.id.dialog_add_task_tv_calendar);
//        dialog_add_task_tv_tag = findViewById(R.id.dialog_add_task_tv_tag);
//        dialog_add_task_tv_notebook = findViewById(R.id.dialog_add_task_tv_notebook);
//        dialog_add_task_tv_plan = findViewById(R.id.dialog_add_task_tv_plan);
//        dialog_add_task_tag_cloud = findViewById(R.id.dialog_add_task_tag_cloud);
//
//        switch_type = findViewById(R.id.switch_type);
//        schedule_task_tv_content = findViewById(R.id.schedule_task_tv_content);
//        moreCard = findViewById(R.id.more_card);
//
//        dialog_add_task_footer_bt_submit = findViewById(R.id.dialog_add_task_footer_bt_submit);
//
//        dialog_add_task_ll_content = findViewById(R.id.dialog_add_task_ll_content);
//        switch_content = findViewById(R.id.switch_content);
//        miniActionView = findViewById(R.id.miniActionView);
//
//        // 重要紧急选择面板
//        dialog_add_task_select_ll_important_urgent = findViewById(
//                R.id.dialog_add_task_select_ll_important_urgent);
//        select_tv_important_urgent = findViewById(R.id.select_tv_important_urgent);
//        select_tv_important_not_urgent = findViewById(R.id.select_tv_important_not_urgent);
//        select_tv_not_important_urgent = findViewById(R.id.select_tv_not_important_urgent);
//        select_tv_not_important_not_urgent = findViewById(R.id.select_tv_not_important_not_urgent);
//        // date选择面板
//        select_fl_start_datetime = findViewById(R.id.select_fl_start_date);
//        select_ll_start_datetime = findViewById(R.id.select_ll_start_date);
//        // time选择面板
//        select_fl_end_datetime = findViewById(R.id.select_fl_end_date);
//        select_ll_end_datetime = findViewById(R.id.select_ll_end_date);
//        // 提醒选择面板
//        dialog_add_task_select_sv_container = findViewById(R.id.dialog_add_task_select_sv_container);
//        mTimePickerTv = findViewById(R.id.dialog_add_task_select_tv_remain_time);
//        // 标签选择面板
//        dialog_add_task_select_ll_tag = findViewById(R.id.dialog_add_task_select_ll_tag);
//        tagCloudView = findViewById(R.id.tag_cloud_view);
//        editTag = findViewById(R.id.edit_tag);
//        addTag = findViewById(R.id.add_tag);
//    }
//
//    private void postInit() {
//        setSelectImportantUrgentPanel();
//        setSelectStartDateTimePanel();
//        setSelectEndDateTimePanel();
//        setSelectRemindPanel();
//        setSelectCalendarPanel();
//
//        setSelectTagPanel();
//
//        showTutorial();
//        setFlip();
//    }
//
//    private void showTutorial() {
//        if (DEF.setting().getBoolean("first_InfoOperationActivity", true)) {
//            new TapTargetSequence(this)
//                    .targets(
//                            TapTarget.forView(switch_type, "上下滚动，切换为添加笔记、任务或习惯"),
//                            TapTarget.forView(moreCard, "更多自定义设置",
//                                    "笔记：标签、附加到文集\n"
//                                            + "任务：开始日期、标签、提醒、附加到计划"),
//                            TapTarget.forView(miniActionView, "对当前内容操作",
//                                    "复制、编辑器打开、自然语言处理（分词、OCR）、翻译、搜索...\n"
//                                            + "长按有小提示")
//                    )
//                    .listener(new TapTargetSequence.Listener() {
//                        // This listener will tell us when interesting(tm) events happen in regards
//                        // to the sequence
//                        @Override
//                        public void onSequenceFinish() {
//                            // Yay
//                            DEF.setting().putBoolean("first_InfoOperationActivity", false);
//                        }
//
//                        @Override
//                        public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
//                            // Perfom action for the current target
//                        }
//
//                        @Override
//                        public void onSequenceCanceled(TapTarget lastTarget) {
//                            // Boo
//                        }
//                    });
//        }
//    }
//
//    /**
//     * 设置窗口样式
//     */
//    private void setWindow() {
//        //窗口对齐屏幕宽度
//        Window win = this.getWindow();
//        win.getDecorView().setPadding(0, 0, 0, 0);
//        WindowManager.LayoutParams lp = win.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.gravity = Gravity.BOTTOM;//设置对话框置顶显示
//        win.setAttributes(lp);
//    }
//
//    private void setFlip() {
//        if (Build.VERSION.SDK_INT >= 21) {
//            WebView.enableSlowWholeDocumentDraw();
//        }
//        schedule_task_tv_content.getFastScrollDelegate().setThumbDrawable(
//                getResources().getDrawable(R.drawable.fast_scroll_bar_dark));
//        schedule_task_tv_content.getFastScrollDelegate().setThumbSize(16, 40);
//        schedule_task_tv_content.getFastScrollDelegate().setThumbDynamicHeight(false);
//        schedule_task_tv_content.setHtmlResource(false);
//        schedule_task_tv_content.getSettings().setJavaScriptEnabled(true);
//        schedule_task_tv_content.getSettings().setDomStorageEnabled(true);
//
//        miniActionView.onInfoMode();
//        miniActionView.tintAllWithColor(Color.WHITE);
//        miniActionView.setAllWithWidthAndHeight(ViewUtil.dp2px(40), ViewUtil.dp2px(40));
//
//        switch_content.setOnFlippingListener((flipView, checked) -> {
//            if (flipView.getDisplayedChild() == 1) {
//                schedule_task_tv_content.parseMarkdown(content);
//            }
//        });
//    }
//
//    /**
//     * 设置重要紧急选择面板
//     */
//    private void setSelectImportantUrgentPanel() {
//        setTv_important_urgent(label_str_set[0], Color.parseColor(text_color_set[0]));
//        TextView[] select_tv_important_urgent_set = new TextView[]{
//                select_tv_important_urgent,
//                select_tv_important_not_urgent,
//                select_tv_not_important_urgent,
//                select_tv_not_important_not_urgent
//        };
//        for (int i = 0; i < 4; i++) {
//            int finalI = i;
//            select_tv_important_urgent_set[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setTv_important_urgent(label_str_set[finalI], Color.parseColor(text_color_set[finalI]));
//
//                    for (int j = 0; j < 4; j++) {
//                        if (j == finalI) {
//                            select_tv_important_urgent_set[j]
//                                    .setBackgroundColor(Color.parseColor(background_color_set[j]));
//                        } else {
//                            select_tv_important_urgent_set[j].setBackgroundColor(Color.parseColor("#ffffff"));
//                        }
//                    }
//
//                    important_urgent_label = finalI;
//                }
//            });
//        }
//
//    }
//
//    /**
//     * 设置 任务开始时间 选择面板
//     */
//    private void setSelectStartDateTimePanel() {
//        Calendar selectedDate = Calendar.getInstance();//系统当前时间
//        selectedDate.set(startDateTime.getYear(),
//                startDateTime.getMonthOfYear() - 1,
//                startDateTime.getDayOfMonth(),
//                startDateTime.getHourOfDay(),
//                startDateTime.getMinuteOfHour());
//        Calendar startDate = Calendar.getInstance();
//        startDate.set(2014, 1, 23);
//        Calendar endDate = Calendar.getInstance();
//        endDate.set(2069, 2, 28);
//        //时间选择器 ，自定义布局
//        pickStartDateTime = new TimePickerBuilder(this, (date, v) -> {//选中事件回调
//            startDateTime = startDateTime.withMillis(date.getTime());
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 EEEE HH:mm", Locale.CHINA);
//            String s = sdf.format(date);
//            dialog_add_task_tv_start_datetime.setText("开始于 " + s);
//            setSelectEndDateTimePanel();
//        })
//                .setDate(selectedDate)
//                .setRangDate(startDate, endDate)
//                .setLayoutRes(R.layout.editor_pickerview_custom_lunar, new CustomListener() {
//
//                    @Override
//                    public void customLayout(final View v) {
//                        final TextView tvMiddle = (TextView) v.findViewById(R.id.tv_middle);
//                        tvMiddle.setText("任务开始时间");
//                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
//                        tvSubmit.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                pickStartDateTime.returnData();
//                            }
//                        });
//                        //公农历切换
//                        CheckBox cb_lunar = (CheckBox) v.findViewById(R.id.cb_lunar);
//                        cb_lunar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                            @Override
//                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                pickStartDateTime.setLunarCalendar(!pickStartDateTime.isLunarCalendar());
//                                //自适应宽
//                                setTimePickerChildWeight(v, isChecked ? 0.8f : 1f, isChecked ? 1f : 1.1f);
//                            }
//                        });
//                    }
//
//                    /**
//                     * 公农历切换后调整宽
//                     * @param v
//                     * @param yearWeight
//                     * @param weight
//                     */
//                    private void setTimePickerChildWeight(View v, float yearWeight, float weight) {
//                        ViewGroup timePicker = (ViewGroup) v.findViewById(R.id.timepicker);
//                        View year = timePicker.getChildAt(0);
//                        LinearLayout.LayoutParams lp = ((LinearLayout.LayoutParams) year.getLayoutParams());
//                        lp.weight = yearWeight;
//                        year.setLayoutParams(lp);
//                        for (int i = 1; i < timePicker.getChildCount(); i++) {
//                            View childAt = timePicker.getChildAt(i);
//                            LinearLayout.LayoutParams childLp = ((LinearLayout.LayoutParams) childAt
//                                    .getLayoutParams());
//                            childLp.weight = weight;
//                            childAt.setLayoutParams(childLp);
//                        }
//                    }
//                })
//                .setType(new boolean[]{true, true, true, true, true, false})
//                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .setDividerColor(Color.parseColor("#03a9f4"))
//                .isDialog(false)
//                .setOutSideCancelable(false)
//                .setDecorView(select_fl_start_datetime)//非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
//                .build();
//        pickStartDateTime.show(false);
//    }
//
//    /**
//     * 设置 任务结束时间 选择面板
//     */
//    private void setSelectEndDateTimePanel() {
//        Calendar selectedDate = Calendar.getInstance();//系统当前时间
//        selectedDate.set(endDateTime.getYear(),
//                endDateTime.getMonthOfYear() - 1,
//                endDateTime.getDayOfMonth(),
//                endDateTime.getHourOfDay(),
//                endDateTime.getMinuteOfHour());
//        Calendar startDate = Calendar.getInstance();
//        startDate.set(startDateTime.getYear(),
//                startDateTime.getMonthOfYear() - 1,
//                startDateTime.getDayOfMonth(),
//                startDateTime.getHourOfDay(),
//                startDateTime.getMinuteOfHour());
//        Calendar endDate = Calendar.getInstance();
//        endDate.set(2069, 2, 28);
//        //时间选择器 ，自定义布局
//        pickEndDateTime = new TimePickerBuilder(this, (date, v) -> {//选中事件回调
//            startDateTime = startDateTime.withMillis(date.getTime());
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 EEEE HH:mm", Locale.CHINA);
//            String s = sdf.format(date);
//            dialog_add_task_tv_end_datetime.setText("结束于 " + s);
//            LogUtil.e(s);
//        })
//                .setDate(selectedDate)
//                .setRangDate(startDate, endDate)
//                .setLayoutRes(R.layout.editor_pickerview_custom_lunar, new CustomListener() {
//
//                    @Override
//                    public void customLayout(final View v) {
//                        final TextView tvMiddle = (TextView) v.findViewById(R.id.tv_middle);
//                        tvMiddle.setText("任务结束时间");
//                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
//                        tvSubmit.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                pickEndDateTime.returnData();
//                            }
//                        });
//                        //公农历切换
//                        CheckBox cb_lunar = (CheckBox) v.findViewById(R.id.cb_lunar);
//                        cb_lunar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                            @Override
//                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                pickEndDateTime.setLunarCalendar(!pickEndDateTime.isLunarCalendar());
//                                //自适应宽
//                                setTimePickerChildWeight(v, isChecked ? 0.8f : 1f, isChecked ? 1f : 1.1f);
//                            }
//                        });
//                    }
//
//                    /**
//                     * 公农历切换后调整宽
//                     * @param v
//                     * @param yearWeight
//                     * @param weight
//                     */
//                    private void setTimePickerChildWeight(View v, float yearWeight, float weight) {
//                        ViewGroup timePicker = (ViewGroup) v.findViewById(R.id.timepicker);
//                        View year = timePicker.getChildAt(0);
//                        LinearLayout.LayoutParams lp = ((LinearLayout.LayoutParams) year.getLayoutParams());
//                        lp.weight = yearWeight;
//                        year.setLayoutParams(lp);
//                        for (int i = 1; i < timePicker.getChildCount(); i++) {
//                            View childAt = timePicker.getChildAt(i);
//                            LinearLayout.LayoutParams childLp = ((LinearLayout.LayoutParams) childAt
//                                    .getLayoutParams());
//                            childLp.weight = weight;
//                            childAt.setLayoutParams(childLp);
//                        }
//                    }
//                })
//                .setType(new boolean[]{true, true, true, true, true, false})
//                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .setDividerColor(Color.parseColor("#03a9f4"))
//                .isDialog(false)
//                .setOutSideCancelable(false)
//                .setDecorView(select_fl_end_datetime)//非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
//                .build();
//        pickEndDateTime.show(false);
//    }
//
//    /**
//     * 设置 提醒 选择面板
//     */
//    private void setSelectRemindPanel() {
//        bindCompoundListener(findViewById(R.id.no_remind), RemindType.noRemind);
//        bindCompoundListener(findViewById(R.id.remind0min), RemindType.remind0min);
//        bindCompoundListener(findViewById(R.id.remind5min), RemindType.remind5min);
//
//        // 初始化铃声
//        initRing();
//        // 初始化音量
//        initVolume();
//        // 初始化振动、督促、天气提示
//        initToggleButton();
//    }
//
//    /**
//     * Bind compound listener.
//     *
//     * @param remind Compound layout.
//     * @param id     which is checked.
//     */
//    private void bindCompoundListener(final TextView remind, RemindType id) {
//        remind.setOnClickListener(v -> {
//            findViewById(R.id.no_remind).setSelected(false);
//            findViewById(R.id.remind0min).setSelected(false);
//            findViewById(R.id.remind5min).setSelected(false);
//            remind.setSelected(true);
//            currentRemindType = id;
//            if (id == RemindType.noRemind || is_all_day) {
//                dialog_add_task_tv_remind.setText("不提醒");
//                displayCountDown(false, is_all_day ? "全天，无法设置提醒" : "不提醒");
//                mAlarmClock.setOnOff(false);
//                return;
//            }
//            mAlarmClock.setOnOff(true);
//            DateTime alarmDateTime = startDateTime;
//            switch (id) {
//                case remind0min:
//                    dialog_add_task_tv_remind.setText("开始时提醒");
//                    alarmDateTime = startDateTime;
//                    break;
//                case remind5min:
//                    dialog_add_task_tv_remind.setText("前5分钟提醒");
//                    alarmDateTime = startDateTime.plusMinutes(-5);
//                    break;
//            }
//            mAlarmClock.setHour(alarmDateTime.getHourOfDay());
//            mAlarmClock.setMinute(alarmDateTime.getMinuteOfHour());
//            // 计算倒计时显示
//            displayCountDown(true, null);
//        });
//    }
//
//    /**
//     * 计算显示倒计时信息
//     */
//    private void displayCountDown(boolean isRemind, String msg) {
//        if (!isRemind) {
//            mTimePickerTv.setText(msg);
//            return;
//        }
//        // 取得下次响铃时间
//        long nextTime = Calculate.calculateNextTime(mAlarmClock.getHour(),
//                mAlarmClock.getMinute(),
//                mAlarmClock.getWeeks());
//        // 系统时间
//        long now = System.currentTimeMillis();
//        // 距离下次响铃间隔毫秒数
//        long ms = nextTime - now;
//
//        // 单位秒
//        int ss = 1000;
//        // 单位分
//        int mm = ss * 60;
//        // 单位小时
//        int hh = mm * 60;
//        // 单位天
//        int dd = hh * 24;
//
//        // 不计算秒，故响铃间隔加一分钟
//        ms += mm;
//        // 剩余天数
//        long remainDay = ms / dd;
//        // 剩余小时
//        long remainHour = (ms - remainDay * dd) / hh;
//        // 剩余分钟
//        long remainMinute = (ms - remainDay * dd - remainHour * hh) / mm;
//
//        // 响铃倒计时
//        // 当剩余天数大于0时显示【X天X小时X分】格式
//        String countDown;
//        if (remainDay > 0) {
//            countDown = getString(R.string.countdown_day_hour_minute);
//            mTimePickerTv.setText(String.format(countDown, remainDay,
//                    remainHour, remainMinute));
//            // 当剩余小时大于0时显示【X小时X分】格式
//        } else if (remainHour > 0) {
//            countDown = getResources()
//                    .getString(R.string.countdown_hour_minute);
//            mTimePickerTv.setText(String.format(countDown, remainHour,
//                    remainMinute));
//        } else {
//            // 当剩余分钟不等于0时显示【X分钟】格式
//            if (remainMinute != 0) {
//                countDown = getString(R.string.countdown_minute);
//                mTimePickerTv.setText(String.format(countDown, remainMinute));
//                // 当剩余分钟等于0时，显示【1天0小时0分】
//            } else {
//                countDown = getString(R.string.countdown_day_hour_minute);
//                mTimePickerTv.setText(String.format(countDown, 1, 0, 0));
//            }
//
//        }
//    }
//
//    /**
//     * 设置铃声
//     */
//    private void initRing() {
//        // 取得铃声选择配置信息
//        SharedPreferences share = getContext().getSharedPreferences(
//                Constants.EXTRA_SHARED_PREFERENCES, Activity.MODE_PRIVATE);
//        String ringName = share.getString(Constants.RING_NAME, getString(R.string.default_ring));
//        String ringUrl = share.getString(Constants.RING_URL, Constants.DEFAULT_RING_URL);
//
//        // 初始化闹钟实例的铃声名
//        mAlarmClock.setRingName(ringName);
//        // 初始化闹钟实例的铃声播放地址
//        mAlarmClock.setRingUrl(ringUrl);
//        // 铃声控件
//        ViewGroup ring = findViewById(R.id.ring_llyt);
//        ring.setOnClickListener(this);
//        mRingDescribe = findViewById(R.id.ring_describe);
//        mRingDescribe.setText(ringName);
//    }
//
//    /**
//     * 设置音量
//     */
//    private void initVolume() {
//        final SharedPreferences share = getContext().getSharedPreferences(
//                Constants.EXTRA_SHARED_PREFERENCES, Activity.MODE_PRIVATE);
//        // 音量
//        final int volume = share.getInt(Constants.AlARM_VOLUME, 8);
//        // 音量控制seekBar
//        SeekBar volumeSkBar = findViewById(R.id.volumn_sk);
//        // 设置当前音量显示
//        volumeSkBar.setProgress(volume);
//        volumeSkBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                // 保存设置的音量
//                mAlarmClock.setVolume(seekBar.getProgress());
//
//                final SharedPreferences.Editor editor = share.edit();
//                editor.putInt(Constants.AlARM_VOLUME, seekBar.getProgress());
//                editor.apply();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress,
//                                          boolean fromUser) {
//
//            }
//        });
//        // 初始化闹钟实例的音量
//        mAlarmClock.setVolume(volume);
//    }
//
//    /**
//     * 设置振动、督促
//     */
//    private void initToggleButton() {
//        // 初始化闹钟实例的振动，默认振动
//        mAlarmClock.setVibrate(true);
//
//        // 初始化闹钟实例的督促信息
//        // 默认督促
//        mAlarmClock.setNap(true);
//        // 督促间隔10分钟
//        mAlarmClock.setNapInterval(10);
//        // 督促3次
//        mAlarmClock.setNapTimes(3);
//
//        // 初始化闹钟实例的天气提示，默认开启
//        mAlarmClock.setWeaPrompt(true);
//
//        // 振动
//        ToggleButton vibrateBtn = findViewById(R.id.vibrate_btn);
//
//        // 督促
//        ToggleButton napBtn = findViewById(R.id.nap_btn);
//        // 督促组件
//        ViewGroup nap = findViewById(R.id.nap_llyt);
//        nap.setOnClickListener(this);
//
//        vibrateBtn.setOnCheckedChangeListener(this);
//        napBtn.setOnCheckedChangeListener(this);
//    }
//
//    /**
//     * 设置 写入系统日历 功能
//     */
//    private void setSelectCalendarPanel() {
//        dialog_add_task_tv_calendar.setOnClickListener(v -> {
//            _User u = UserModel.getCurrentUser();
//            String username = u == null ? "" : u.getUsername();
//            SystemCalendarManager.Event event = getEvent();
//            String s = "标题：" + dialog_add_task_et_title.getText().toString()
//                    + "\n正文：" + dialog_add_task_et_content.getText().toString()
//                    + "\n开始时间：" + startDateTime.toString("yyyy/MM/dd E HH:mm", Locale.CHINA)
//                    + "\n参与人：" + username;
//            new MaterialDialog.Builder(InfoOperationActivity.this)
//                    .title("添加到系统日历")
//                    .content(s)
//                    .positiveText("确定")
//                    .checkBoxPrompt("以后自动添加到系统日历", true,
//                            (buttonView, isChecked) -> DEF.setting()
//                                    .putBoolean("auto_add_to_system_calendar", isChecked))
//                    .onPositive((dialog, which) -> addToSystemCalendar(event))
//                    .show();
//        });
//    }
//
//    private void addToSystemCalendar(SystemCalendarManager.Event event) {
//        long eventId = SystemCalendarManager
//                .insertEvent(this, event.CALENDAR_ID, event);
//        if (eventId < 0) {
//            ToastUtil.e("插入错误，可能缺少权限");
//            LogUtil.e("插入错误，可能缺少权限");
//        } else {
//            ToastUtil.ok("插入成功");
//            LogUtil.e("插入成功");
//        }
//    }
//
//    private SystemCalendarManager.Event getEvent() {
//        long calendarId = SystemCalendarManager.getCalendarId(InfoOperationActivity.this);
//        int minutes = currentRemindType == RemindType.remind5min ? 5 : 0;
//        _User u = UserModel.getCurrentUser();
//        String username = u == null ? "" : u.getUsername();
//        return new SystemCalendarManager.Event(
//                dialog_add_task_et_title.getText().toString(),
//                dialog_add_task_et_content.getText().toString(),
//                "", username, TimeZone.getDefault().getID(),
//                calendarId, startDateTime.getMillis(), endDateTime.getMillis(),
//                CalendarContract.Events.ACCESS_DEFAULT, is_all_day ? 1 : 0,
//                1, 1, 1, minutes
//        );
//    }
//
//    /**
//     * 设置 tag 选择面板
//     */
//    private void setSelectTagPanel() {
//        io.reactivex.Observable.create((ObservableOnSubscribe<List<ITag>>) e -> {
//            allTags = new ArrayList<>();
//            e.onNext(allTags);
//            e.onComplete();
//        })
//                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
//                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
//                .subscribe(new SimpleObserver<List<ITag>>() {
//                    @Override
//                    public void onNext(List<ITag> tagList) {
//                        if (null != tagList) {
//                            tagCloudView.setTags(tagList);
//                        }
//                        dialog_add_task_tag_cloud.setVisibility(dialog_add_task_tag_cloud.isEmpty()
//                                ? View.GONE
//                                : View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//                });
//
//        tagCloudView.setOnTagClickListener(position -> {
//            ITag tag = tagCloudView.getTags().get(position);
//            dialog_add_task_tag_cloud.addTagToList(tag);
//            tagCloudView.removeAt(position);
//            dialog_add_task_tag_cloud
//                    .setVisibility(dialog_add_task_tag_cloud.isEmpty() ? View.GONE : View.VISIBLE);
//        });
//        tagCloudView.setOnTagLongClickListener(position ->
//                MaterialDialogProvider.deleteWithTitle(InfoOperationActivity.this,
//                        "标签", (dialog, which) -> {
////                            try {
//////                                DB.tag().delete(tagCloudView.getTags().get(position).getDbTag());
////                                tagCloudView.removeAt(position);
////                            } catch (SQLException e) {
////                                e.printStackTrace();
////                            }
//                        }).show());
//        dialog_add_task_tag_cloud.setOnTagClickListener(position -> {
//            ITag tag = dialog_add_task_tag_cloud.getTags().get(position);
//            dialog_add_task_tag_cloud.removeAt(position);
//            dialog_add_task_tag_cloud
//                    .setVisibility(dialog_add_task_tag_cloud.isEmpty() ? View.GONE : View.VISIBLE);
//            tagCloudView.addTagToList(tag);
//        });
//
//        editTag.setOnClickListener(v -> {
//            //先把键盘收起来，再跳转
//            mSmartKeyboardManager.hideAllCustomKeyboard(true);
//            NAV.go(InfoOperationActivity.this, RouterHub.MAIN_TagListActivity);
//        });
//        lastPickedColor = ContextCompat.getColor(this, android.R.color.holo_blue_bright);
//        addTag.setOnClickListener(v0 -> {
//            mSmartKeyboardManager.hideAllCustomKeyboard(true);
//            MaterialDialogProvider.titleWithColorPicker(InfoOperationActivity.this,
//                    getResources().getString(R.string.text_add_tags), null, "", lastPickedColor,
//                    (dialog, which, editText, selectedColor) -> {
//                        String name = editText.getText().toString();
//                        lastPickedColor = selectedColor;
//                        if (TextUtils.isEmpty(name)) {
//                            ToastUtil.e("名称不能为空！");
//                            return;
//                        }
////                        DBTag tag = new DBTag(name, lastPickedColor);
////                        DB.tag().save(tag);
////                        tagCloudView.addTagToList(new VTag(tag));
//                    }).show();
//        });
//    }
//
//    /**
//     * 设置软键盘和选择面板的平滑交互
//     */
//    private void setKeyboardManager() {
//        mSmartKeyboardManager = new SmartKeyboardManager.Builder(this)
//                .setContentView(dialog_add_task_ll_content)
//                .setEditText(dialog_add_task_et_content)
//                .addKeyboard(dialog_add_task_tv_important_urgent,
//                        dialog_add_task_select_ll_important_urgent)
//                .addKeyboard(dialog_add_task_tv_start_datetime, select_ll_start_datetime)
//                .addKeyboard(dialog_add_task_tv_end_datetime, select_ll_end_datetime)
//                .addKeyboard(dialog_add_task_tv_remind, dialog_add_task_select_sv_container)
//                .addKeyboard(dialog_add_task_tv_tag, dialog_add_task_select_ll_tag)
//                .addKeyboard(miniActionView.iv_reminder, dialog_add_task_select_sv_container)
//                .addKeyboard(miniActionView.iv_labels, dialog_add_task_select_ll_tag)
//
//                .create();
//        showInputMethod4Content();
//    }
//
//    private void showInputMethod4Content() {
//        //获取焦点 光标出现
//        dialog_add_task_et_content.setFocusable(true);
//        dialog_add_task_et_content.setFocusableInTouchMode(true);
//        dialog_add_task_et_content.requestFocus();
//
//        // 这里给出个延迟弹出键盘，如果直接弹出键盘会和界面view渲染一起，体验不太好
//        new Timer().schedule(new TimerTask() {
//            public void run() {
//                ViewUtil.showInputMethod(dialog_add_task_et_content);
//            }
//        }, 256);
//    }
//    //endregion
//
//    //region Data数据区
//    private void preInit() {
//        important_urgent_label = 0;
//        title = "";
//        content = "";
//        startDateTime = new DateTime();
//        endDateTime = new DateTime().plusDays(1).withMillisOfDay(0).minusMillis(1);
//        repeat = 0;
//        type = Type.NOTE;
//    }
//
//    @SuppressLint("SetTextI18n")
//    @Override
//    public void initData() {//必须调用
//        if (task == null) {
//            task = (DBTask) getIntent().getSerializableExtra(TASK);
//        }
//        if (note == null) {
//            note = (DBNote) getIntent().getSerializableExtra(NOTE);
//        }
//        if (habit == null) {
//            habit = (DBHabit) getIntent().getSerializableExtra(HABIT);
//        }
//        initTextString();
//
//        // collection
//        if (task != null) {
//            refreshViewByTask(task);
//            subPlan_toAttach = task.getSubplan();
//        }
//        if (subPlan_toAttach != null) {
//            refreshViewByAttach(subPlan_toAttach);
//        }
//
//        //note
//        if (note != null) {
//            refreshViewByNote(note);
//            noteBook_toAttach = note.getNoteBook();
//        }
//        if (noteBook_toAttach != null) {
//            refreshViewByAttach(noteBook_toAttach);
//        }
//
//        //habit
//        if (habit != null) {
//            refreshViewByHabit(habit);
//        }
//
//        switch_type.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        initSnapperAndAesthetics(switch_type);
//        DataAdapter adapter = getTypeAdapter();
//        adapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                switch (position) {
//                    case 0:
//                        onSelectTypeNote();
//                        break;
//                    case 1:
//                        onSelectTypeTask();
//                        break;
//                    case 2:
//                        onSelectTypeHabit();
//                        break;
//                }
//            }
//        });
//        switch_type.setAdapter(adapter);
//    }
//
//    private void initTextString() {
//        Intent intent = getIntent();
//        if (TextUtils.isEmpty(str)) {
//            str = intent.getStringExtra(STR);
//        }
//
//        if (TextUtils.isEmpty(str)) {
//            String action = intent.getAction();
//            String type = intent.getType();
//            if (Intent.ACTION_SEND.equals(action) && type != null) {
//                if ("text/plain" .equals(type)) {
//                    String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
//                    str = sharedText;
//                }
//            }
//        } else {
//            DEF.config().save(Constants.UNIVERSAL_SAVE_COTENT, str);
//        }
//
//        if (TextUtils.isEmpty(str)) {
//            str = DEF.config().getString(Constants.UNIVERSAL_SAVE_COTENT, "");
//        }
//
//        title = getShortTitle(str);
//        content = str;
//        dialog_add_task_et_title.setText(title);
//        dialog_add_task_et_content.setText(content);
//        dialog_add_task_et_content.setSelection(content.length());
//        DEF.config().save(Constants.UNIVERSAL_SAVE_COTENT, str);
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void refreshViewByTask(DBTask task) {
//        switch_type.postDelayed(this::onSelectTypeTask, 50);
//        switch_type.smoothScrollToPosition(1);
//        important_urgent_label = task.getLabel();
//        title = getShortTitle(task.getTitle());
//        content = task.getContent();
//        if (task.getSubplan() != null) {
//            String plan_str =
//                    "计划：" + task.getSubplan().getPlan().getTitle() + ", " + task.getSubplan().getTitle();
//            dialog_add_task_tv_plan.setText(plan_str);
//        }
//        is_all_day = task.getIs_all_day();
//        if (!is_all_day) {
//            Date begin_datetime = TimeUtil.formatGMTDateStr(task.getBegin_datetime());
//            Date end_datetime = TimeUtil.formatGMTDateStr(task.getEnd_datetime());
//            if (begin_datetime != null && end_datetime != null) {
//                startDateTime = new DateTime(begin_datetime);
//                endDateTime = new DateTime(end_datetime);
//                dialog_add_task_tv_start_datetime.setText("开始于 " + startDateTime.toString("yyyy年MM月dd日 EEEE HH:mm"));
//                dialog_add_task_tv_end_datetime.setText("结束于 " + endDateTime.toString("yyyy年MM月dd日 EEEE HH:mm"));
//            }
//        } else {
//            dialog_add_task_tv_start_datetime.setText("全天");
//            dialog_add_task_tv_end_datetime.setText("全天");
//        }
//        //region tag
//        curTags = new ArrayList<>();
////        List<RelationTagTask> relationTagTasks = DB.relationTagTask().findAll(task);
////        for (RelationTagTask r : relationTagTasks) {
//////            curTags.add(new VTag(r.getTag()));
////        }
//        dialog_add_task_tag_cloud.setTags(curTags);
//        dialog_add_task_tag_cloud
//                .setVisibility(dialog_add_task_tag_cloud.isEmpty() ? View.GONE : View.VISIBLE);
//        tagCloudView.removeAllWhereContains(curTags);
//        //endregion
//
//        dialog_add_task_et_title.setText(title);
//        dialog_add_task_et_content.setText(content);
//        setTv_important_urgent(task.getlabelStr(), task.getlabelColor());
////    dialog_add_task_tv_important_urgent.setText(label_str_set[task.getLabel()]);
////    dialog_add_task_tv_important_urgent
////        .setTextColor(Color.parseColor(text_color_set[task.getLabel()]));
////    dialog_add_task_tv_important_urgent
////        .setBackgroundColor(Color.parseColor(background_color_set[task.getLabel()]));
//        dialog_add_task_footer_bt_submit.setText("修改");
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void refreshViewByNote(DBNote note) {
//        switch_type.postDelayed(this::onSelectTypeNote, 50);
//        switch_type.smoothScrollToPosition(0);
//        title = getShortTitle(note.getTitle());
//        content = note.getContent();
//
//        //region tag
//        curTags = new ArrayList<>();
//        dialog_add_task_tag_cloud.setTags(curTags);
//        dialog_add_task_tag_cloud
//                .setVisibility(dialog_add_task_tag_cloud.isEmpty() ? View.GONE : View.VISIBLE);
//        tagCloudView.removeAllWhereContains(curTags);
//        //endregion
//
//        dialog_add_task_et_title.setText(title);
//        dialog_add_task_et_content.setText(content);
//        dialog_add_task_footer_bt_submit.setText("修改");
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void refreshViewByHabit(DBHabit habit) {
//        switch_type.postDelayed(this::onSelectTypeHabit, 50);
//        switch_type.smoothScrollToPosition(2);
//        title = getShortTitle(habit.getName());
//        content = habit.getDescription();
//
////        //region tag
////        curTags = new ArrayList<>();
////        List<RelationTagHabit> relationTagTasks = DB.relationTagHabit().findAll(habit);
////        for (RelationTagHabit r : relationTagTasks) {
////            curTags.add(new VTag(r.getTag()));
////        }
////        dialog_add_task_tag_cloud.setTags(curTags);
////        dialog_add_task_tag_cloud
////                .setVisibility(dialog_add_task_tag_cloud.isEmpty() ? View.GONE : View.VISIBLE);
////        tagCloudView.removeAllWhereContains(curTags);
////        //endregion
//
//        dialog_add_task_et_title.setText(title);
//        dialog_add_task_et_content.setText(content);
//        dialog_add_task_footer_bt_submit.setText("修改");
//    }
//
//    private void refreshViewByAttach(DBSubPlan dbSubPlan) {
//        switch_type.postDelayed(this::onSelectTypeTask, 500);
//        switch_type.smoothScrollToPosition(1);
//        DateTime date = new DateTime();
//        date = date.plusYears(50);
//        startDateTime = startDateTime.withYear(date.getYear());
//        endDateTime = endDateTime.withYear(date.getYear());
//        String plan_str = "计划：" + dbSubPlan.getPlan().getTitle() + ", " + dbSubPlan.getTitle();
//        dialog_add_task_tv_plan.setText(plan_str);
//
//    }
//
//    private void refreshViewByAttach(DBNoteBook dbNoteBook) {
//        switch_type.postDelayed(this::onSelectTypeNote, 500);
//        switch_type.smoothScrollToPosition(0);
//        dialog_add_task_tv_notebook.setText("文集：" + dbNoteBook.getTitle());
//    }
//
//    private void setTv_important_urgent(String labelText, int color) {
//        dialog_add_task_tv_important_urgent.setText(labelText);
//        dialog_add_task_tv_important_urgent.setTextColor(color);
//    }
//    //endregion
//
//    //region Event事件区
//    @Override
//    public void initEvent() {//必须调用
//        clear.setOnClickListener(this);
//        dialog_add_task_et_title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    isSelfEdit = true;
//                }
//            }
//        });
//        dialog_add_task_et_title.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!(isSelfEdit && note == null && task == null)) {
//                    title = dialog_add_task_et_title.getText().toString();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        dialog_add_task_et_content.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                //同步content里的到title
//                content = dialog_add_task_et_content.getText().toString();
//                if (!isSelfEdit && note == null && task == null) {
//                    title = getShortTitle(content);
//                    dialog_add_task_et_title.setText(title);
//                }
//
//                DEF.config().save(Constants.UNIVERSAL_SAVE_COTENT, content);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//        dialog_add_task_footer_bt_submit.setOnClickListener(this);
//
//        miniActionView.setMiniAction(new MiniVerticalActionView.MiniAction() {
//            @Override
//            public void onFullEdit(View iv_full_edit) {
//                title = getShortTitle(dialog_add_task_et_title.getText().toString());
//                content = dialog_add_task_et_content.getText().toString();
//                if (StringUtil.isEmpty(title)) {
//                    ToastUtil.e("标题不能为空！");
//                    return;
//                }
//                DBNote dbNote = new DBNote();
//                dbNote.setTitle(title);
//                dbNote.setContent(content);
//                DB.notes().save(dbNote);
//            }
//
//            @Override
//            public void onCopy(View iv_copy) {
//                ClipboardCopy.copyToClipBoard(
//                        InfoOperationActivity.this,
//                        dialog_add_task_et_content.getText().toString()
//                );
//            }
//
//            @Override
//            public void onNLP(View iv_nlp) {
//                onClickTimeCat();
//            }
//
//            @Override
//            public void onTranslate(View iv_translate) {
//                onClickTranslate();
//            }
//
//            @Override
//            public void onSearch(View iv_search) {
//                onClickSearch();
//            }
//
//            @Override
//            public void onShare(View iv_share) {
//                ShareUtils.share(InfoOperationActivity.this,
//                        dialog_add_task_et_content.getText().toString());
//            }
//
//            @Override
//            public void onAttach(View iv_attachment) {
//                ToastUtil.w("用爱发电失败，喵没小鱼干了ԅ(¯﹃¯ԅ)");
//            }
//
//            @Override
//            public void onRemind(View iv_reminder) {
//                ToastUtil.w("用爱发电失败，喵没小鱼干了ԅ(¯﹃¯ԅ)");
//            }
//
//            @Override
//            public void onLabels(View iv_labels) {
//                ToastUtil.w("用爱发电失败，喵没小鱼干了ԅ(¯﹃¯ԅ)");
//            }
//
//            @Override
//            public void onToggleCheckList(View iv_checklist) {
//                ToastUtil.w("用爱发电失败，喵没小鱼干了ԅ(¯﹃¯ԅ)");
//            }
//
//            @Override
//            public void onMore(View iv_more) {
//                switchContentAndKeepSoftInput();
//            }
//        }, Position.LEFT);
//        setKeyboardManager();
//
//        moreCard.setOnClickListener((v) -> switchContentAndKeepSoftInput());
//    }
//
//    private String getShortTitle(@NonNull String content) {
//        return StringUtils.getShortText(content);
//    }
//
//    private void switchContentAndKeepSoftInput() {
//        switch_content.showNext();
//
//        nextContentPosInFlipView++;
//        boolean showRear = (nextContentPosInFlipView % 3) == 2;
//        boolean showCustomKeyBoard = showRear
//                && mSmartKeyboardManager.isSoftKeyboardShowing()
//                && !mSmartKeyboardManager.hasAnyCustomKeyBoardIsShowing();
//        if (showCustomKeyBoard) {
//            switch (type) {
//                case TASK:
//                    dialog_add_task_tv_start_datetime.callOnClick();
//                    break;
//                case NOTE:
//                    dialog_add_task_tv_tag.callOnClick();
//                    break;
//                case HABIT:
//                    dialog_add_task_tv_tag.callOnClick();
//                    break;
//            }
//        }
//    }
//
//    private void setType(Type type) {
//        this.type = type;
//        switchType(type);
//    }
//
//    private void switchType(Type type) {
//        switch (type) {
//            case NOTE:
//                dialog_add_task_tv_important_urgent.setVisibility(View.GONE);
//                dialog_add_task_tv_start_datetime.setVisibility(View.GONE);
//                dialog_add_task_tv_end_datetime.setVisibility(View.GONE);
//                dialog_add_task_tv_remind.setVisibility(View.GONE);
//                dialog_add_task_tv_calendar.setVisibility(View.GONE);
//                dialog_add_task_tv_notebook.setVisibility(View.VISIBLE);
//                dialog_add_task_tv_plan.setVisibility(View.GONE);
//                dialog_add_task_tv_tag.setVisibility(View.VISIBLE);
////        switch_type.smoothScrollToPosition(0);
//                break;
//            case TASK:
//                dialog_add_task_tv_important_urgent.setVisibility(View.VISIBLE);
//                dialog_add_task_tv_start_datetime.setVisibility(View.VISIBLE);
//                dialog_add_task_tv_end_datetime.setVisibility(View.VISIBLE);
//                dialog_add_task_tv_remind.setVisibility(View.VISIBLE);
//                dialog_add_task_tv_calendar.setVisibility(View.VISIBLE);
//                dialog_add_task_tv_notebook.setVisibility(View.GONE);
//                dialog_add_task_tv_plan.setVisibility(View.VISIBLE);
//                dialog_add_task_tv_tag.setVisibility(View.VISIBLE);
////        switch_type.smoothScrollToPosition(1);
//                break;
//            case HABIT:
//                dialog_add_task_tv_important_urgent.setVisibility(View.GONE);
//                dialog_add_task_tv_start_datetime.setVisibility(View.GONE);
//                dialog_add_task_tv_end_datetime.setVisibility(View.GONE);
//                dialog_add_task_tv_remind.setVisibility(View.GONE);
//                dialog_add_task_tv_calendar.setVisibility(View.GONE);
//                dialog_add_task_tv_notebook.setVisibility(View.GONE);
//                dialog_add_task_tv_plan.setVisibility(View.GONE);
//                dialog_add_task_tv_tag.setVisibility(View.VISIBLE);
////        switch_type.smoothScrollToPosition(2);
//                break;
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        int i1 = v.getId();
//        if (i1 == R.id.clear) {
//            dialog_add_task_et_title.setText("");
//            dialog_add_task_et_content.setText("");
//            title = "";
//            content = "";
//
//        } else if (i1 == R.id.dialog_add_task_footer_bt_submit) {
//            onSubmit();
//
//            // 当点击铃声
//        } else if (i1 == R.id.ring_llyt) {// 不响应重复点击
////                if (MyUtil.isFastDoubleClick()) {
////                    return;
////                }TODO
//            // 铃声选择界面
//            ARouter.getInstance().build(RouterHub.APP_ALARM_RingSelectActivity)
//                    .withInt(Constants.RING_REQUEST_TYPE, 0)
//                    .navigation(this, REQUEST_RING_SELECT);
//            // 当点击督促
//        } else if (i1 == R.id.nap_llyt) {// 不响应重复点击
////                if (MyUtil.isFastDoubleClick()) {
////                    return;
////                }TODO
//            // 督促界面
//            ARouter.getInstance().build(RouterHub.APP_ALARM_NapEditActivity)
//                    .withInt(Constants.NAP_INTERVAL, mAlarmClock.getNapInterval())
//                    .withInt(Constants.NAP_TIMES, mAlarmClock.getNapTimes())
//                    .navigation(this, REQUEST_NAP_EDIT);
//        }
//    }
//
//    private void onSelectTypeTask() {
//        setType(Type.TASK);
//        if (task != null) {
//            dialog_add_task_footer_bt_submit.setText("修改");
//        }
//        if (note != null) {
//            dialog_add_task_footer_bt_submit.setText("转化");
//        }
//        if (habit != null) {
//            dialog_add_task_footer_bt_submit.setText("转化");
//        }
//        mSmartKeyboardManager.hideAllCustomKeyboard(true);
//        showInputMethod4Content();
//        dialog_add_task_ll_content.setBackgroundResource(R.drawable.bg_work_edit_task);
//    }
//
//    private void onSelectTypeNote() {
//        setType(Type.NOTE);
//        if (note != null) {
//            dialog_add_task_footer_bt_submit.setText("修改");
//        }
//        if (task != null) {
//            dialog_add_task_footer_bt_submit.setText("转化");
//        }
//        if (habit != null) {
//            dialog_add_task_footer_bt_submit.setText("转化");
//        }
//        mSmartKeyboardManager.hideAllCustomKeyboard(true);
//        showInputMethod4Content();
//        dialog_add_task_ll_content.setBackgroundResource(R.drawable.bg_sishi_edit_task);
//    }
//
//    private void onSelectTypeHabit() {
//        setType(Type.HABIT);
//        if (habit != null) {
//            dialog_add_task_footer_bt_submit.setText("修改");
//        }
//        if (task != null) {
//            dialog_add_task_footer_bt_submit.setText("转化");
//        }
//        if (note != null) {
//            dialog_add_task_footer_bt_submit.setText("转化");
//        }
//        mSmartKeyboardManager.hideAllCustomKeyboard(true);
//        showInputMethod4Content();
//        dialog_add_task_ll_content.setBackgroundResource(R.drawable.bg_health_edit_task);
//    }
//
//    private void onClickTimeCat() {
//        if (TextUtils.isEmpty(content)) {
//            content = "";
//            ToastUtil.w("输入为空！");
//            return;
//        }
//        ARouter.getInstance().build(RouterHub.APP_TimeCatActivity)
//                .withString("str", content)
//                .navigation(this);
//        finishWithAnim();
//    }
//
//    private void onClickSearch() {
//        if (TextUtils.isEmpty(content)) {
//            content = "";
//            ToastUtil.w("输入为空！");
//            return;
//        }
//        UrlCountUtil.onEvent(UrlCountUtil.CLICK_TIMECAT_SEARCH);
//        boolean isUrl = false;
//        Uri uri = null;
//        try {
//            Pattern p = Pattern.compile(
//                    "^((https?|ftp|news):\\/\\/)?([a-z]([a-z0-9\\-]*[\\.。])+([a-z]{2}|aero|arpa|biz|com|coop|edu|gov|info|int|jobs|mil|museum|name|nato|net|org|pro|travel)|(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))(\\/[a-z0-9_\\-\\.~]+)*(\\/([a-z0-9_\\-\\.]*)(\\?[a-z0-9+_\\-\\.%=&]*)?)?(#[a-z][a-z0-9_]*)?$",
//                    Pattern.CASE_INSENSITIVE);
//            Matcher matcher = p.matcher(content);
//            if (!matcher.matches()) {
//                uri = Uri.parse(SearchEngineUtil.getInstance().getSearchEngines()
//                        .get(DEF.config().getInt(Constants.BROWSER_SELECTION, 0)).url + URLEncoder
//                        .encode(content, "utf-8"));
//                isUrl = false;
//            } else {
//                uri = Uri.parse(content);
//                if (!content.startsWith("http")) {
//                    content = "http://" + content;
//                }
//                isUrl = true;
//            }
//
//            boolean t = DEF.config().getBoolean(Constants.USE_LOCAL_WEBVIEW, true);
//            Intent intent2Web;
//            if (t) {
//                if (isUrl) {
//                    ARouter.getInstance().build(RouterHub.APP_WebActivity)
//                            .withString("mUrl", content)
//                            .navigation(this);
//                } else {
//                    ARouter.getInstance().build(RouterHub.APP_WebActivity)
//                            .withString("mQuery", content)
//                            .navigation(this);
//                }
//            } else {
//                intent2Web = new Intent(Intent.ACTION_VIEW, uri);
//                intent2Web.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent2Web);
//                finishWithAnim();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (isUrl) {
//                ARouter.getInstance().build(RouterHub.APP_WebActivity)
//                        .withString("mUrl", content)
//                        .navigation(this);
//            } else {
//                ARouter.getInstance().build(RouterHub.APP_WebActivity)
//                        .withString("mQuery", content)
//                        .navigation(this);
//            }
//            finishWithAnim();
//        }
//    }
//
//    private void onClickTranslate() {
//        if (TextUtils.isEmpty(content)) {
//            content = "";
//            ToastUtil.w("输入为空！");
//            return;
//        }
//        ARouter.getInstance().build(RouterHub.APP_TimeCatActivity)
//                .withString("str", content)
//                .withBoolean("is_translate", true)
//                .navigation(this);
//        finishWithAnim();
//    }
//
//    private void onUpdateTask() {
//
//        task.setTitle(title);
//        task.setContent(content);
//        task.setLabel(important_urgent_label);
//        task.setIs_all_day(is_all_day);
//        task.setRepeatInterval(repeat);
//
//        task.setBegin_datetime(TimeUtil.formatGMTDate(startDateTime.toDate()));
//        task.setEnd_datetime(TimeUtil.formatGMTDate(endDateTime.toDate()));
//
//        if (subPlan_toAttach != null) {
//            task.setSubplan(subPlan_toAttach);
//        }
//        task.setUser(DB.users().getActive());
//        task.setAlarm(mAlarmClock);
//
//        //使用Observable.create()创建被观察者
//        Observable<Calendar> observable1 = Observable.create(subscriber -> {
//            if (calendarList == null || calendarList.size() <= 0) {
//                DB.schedules().safeSaveDBTaskAndFireEvent(task);
//                EventBus.getDefault().post(new PersistenceEvents.TaskUpdateEvent(task));
//                return;
//            }
//
//            for (Calendar calendar : calendarList) {
//                subscriber.onNext(calendar);
//            }
//            subscriber.onCompleted();
//        });
//        //订阅
//        observable1.onBackpressureBuffer(1000)
//                .subscribeOn(Schedulers.newThread())//指定 subscribe() 发生在新的线程
//                .observeOn(Schedulers.io())// 指定 Subscriber 的回调发生在主线程
//                .subscribe(new Subscriber<Calendar>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtil.e(e.toString());
//                    }
//
//                    @Override
//                    public void onNext(Calendar s) {
//                        //请求成功
//                        task.setBegin_datetime(TimeUtil.formatGMTDate(
//                                startDateTime.withYear(s.get(Calendar.YEAR))
//                                        .withMonthOfYear(s.get(Calendar.MONTH) + 1)
//                                        .withDayOfMonth(s.get(Calendar.DAY_OF_MONTH)).toDate()));
//                        task.setEnd_datetime(TimeUtil.formatGMTDate(
//                                endDateTime.withYear(s.get(Calendar.YEAR))
//                                        .withMonthOfYear(s.get(Calendar.MONTH) + 1)
//                                        .withDayOfMonth(s.get(Calendar.DAY_OF_MONTH)).toDate()));
//                        DB.schedules().safeSaveDBTaskAndFireEvent(task);
//                        EventBus.getDefault().post(new PersistenceEvents.TaskUpdateEvent(task));
//
////                        //region 处理 tag
////                        for (DBTag dbTag : tags) {
////                            boolean needToAdd = true;
////                            for (ITag t : curTags) {
////                                if (t.getDbTag().getId() == dbTag.getId()) {
////                                    //当前 tag 在数据库中存在，没必要保存
////                                    needToAdd = false;
////                                }
////                            }
////                            if (needToAdd) {
////                                DB.relationTagTask().safeSaveRelation(new RelationTagTask(dbTag, task));
////                            }
////                        }
////                        for (ITag t : curTags) {
////                            boolean needToRemove = true;
////                            for (DBTag dbTag : tags) {
////                                if (t.getDbTag().getId() == dbTag.getId()) {
////                                    //当前 tag 已经在数据库中存在
////                                    needToRemove = false;
////                                }
////                            }
////                            if (needToRemove) {
////                                DB.relationTagTask().safeDeleteRelation(new RelationTagTask(t.getDbTag(), task));
////                            }
////                        }
////                        //endregion
//
//                        //避免更新而不创建
//                        task.setCreated_datetime(TimeUtil.formatGMTDate(new DateTime().toDate()));
//                        task.setId(-1L);
//
//                        LogUtil.e(task.toString());
//                    }
//                });
//
//        ToastUtil.ok("成功更新[ 任务 ]:" + content);
//        finishWithAnim();
//    }
//
//    private void onCreateTask() {
//        DBTask dbTask = new DBTask();
//        DBUser activeUser = DB.users().getActive();
//        dbTask.setTitle(title);
//        dbTask.setContent(content);
//        dbTask.setLabel(important_urgent_label);
//        dbTask.setCreated_datetime(TimeUtil.formatGMTDate(new Date()));
//
//        dbTask.setRepeatInterval(repeat);
//        dbTask.setIs_all_day(is_all_day);
//        dbTask.setBegin_datetime(TimeUtil.formatGMTDate(startDateTime.toDate()));
//        dbTask.setEnd_datetime(TimeUtil.formatGMTDate(endDateTime.toDate()));
//
//        dbTask.setUser(activeUser);
//        dbTask.setAlarm(mAlarmClock);
//
//        if (subPlan_toAttach != null) {
//            dbTask.setSubplan(subPlan_toAttach);
//        }
//
//        //使用Observable.create()创建被观察者
//        Observable<DBTask> observable1 = Observable.create(subscriber -> {
//            if (calendarList == null || calendarList.size() <= 0) {
//                subscriber.onNext(dbTask);
//                subscriber.onCompleted();
//                return;
//            }
//
//            for (Calendar s : calendarList) {
//                dbTask.setBegin_datetime(TimeUtil.formatGMTDate(
//                        startDateTime.withYear(s.get(Calendar.YEAR))
//                                .withMonthOfYear(s.get(Calendar.MONTH) + 1)
//                                .withDayOfMonth(s.get(Calendar.DAY_OF_MONTH)).toDate()));
//                dbTask.setEnd_datetime(TimeUtil.formatGMTDate(
//                        endDateTime.withYear(s.get(Calendar.YEAR))
//                                .withMonthOfYear(s.get(Calendar.MONTH) + 1)
//                                .withDayOfMonth(s.get(Calendar.DAY_OF_MONTH)).toDate()));
//                //避免更新而不创建
//                dbTask.setCreated_datetime(TimeUtil.formatGMTDate(new DateTime().toDate()));
//                dbTask.setId(-1L);
//                subscriber.onNext(dbTask);
//            }
//            subscriber.onCompleted();
//        });
//        //订阅
//        observable1.onBackpressureBuffer(1000)
//                .subscribeOn(Schedulers.newThread())//指定 subscribe() 发生在新的线程
//                .observeOn(Schedulers.io())// 指定 Subscriber 的回调发生在主线程
//                .subscribe(new Subscriber<DBTask>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtil.e(e.toString());
//                    }
//
//                    @Override
//                    public void onNext(DBTask dbTask) {
//                        LogUtil.e("保存前 " + dbTask.id);
//                        DB.schedules().safeSaveDBTaskAndFireEvent(dbTask);
//
////                        //region 处理 tag
////                        for (DBTag dbTag : tags) {
////                            boolean needToAdd = true;
////                            for (ITag t : curTags) {
////                                if (t.getDbTag().getId() == dbTag.getId()) {
////                                    //当前 tag 在数据库中存在，没必要保存
////                                    needToAdd = false;
////                                }
////                            }
////                            if (needToAdd) {
////                                DB.relationTagTask().safeSaveRelation(new RelationTagTask(dbTag, dbTask));
////                            }
////                        }
////                        for (ITag t : curTags) {
////                            boolean needToRemove = true;
////                            for (DBTag dbTag : tags) {
////                                if (t.getDbTag().getId() == dbTag.getId()) {
////                                    //当前 tag 已经在数据库中存在
////                                    needToRemove = false;
////                                }
////                            }
////                            if (needToRemove) {
////                                DB.relationTagTask().safeDeleteRelation(new RelationTagTask(t.getDbTag(), dbTask));
////                            }
////                        }
////                        //endregion
//
//                        EventBus.getDefault().post(new PersistenceEvents.TaskCreateEvent(dbTask));
//                        LogUtil.e("保存后 " + dbTask.id);
//                        LogUtil.e(dbTask.toString());
//                    }
//                });
//
//        ToastUtil.ok("成功添加[ 任务 ]:\n" + content);
//
//        if (DEF.setting().getBoolean("auto_add_to_system_calendar", true)) {
//            addToSystemCalendar(getEvent());
//        }
//
//        finishWithAnim();
//    }
//
//    private void onUpdateNote() {
//        note.setTitle(title);
//        note.setContent(content);
//        note.setUpdate_datetime(TimeUtil.formatGMTDate(new Date()));
//        note.setUser(DB.users().getActive());
//        if (noteBook_toAttach != null) {
//            note.setNoteBook(noteBook_toAttach);
//        }
//        DB.notes().safeSaveDBNote(note);
//        EventBus.getDefault().post(new PersistenceEvents.NoteUpdateEvent(note));
////
////        //region 处理 tag
////        List<DBTag> tags = getTags();
////        for (DBTag dbTag : tags) {
////            boolean needToAdd = true;
////            for (ITag t : curTags) {
////                if (t.getDbTag().getId() == dbTag.getId()) {
////                    //当前 tag 在数据库中存在，没必要保存
////                    needToAdd = false;
////                }
////            }
////            if (needToAdd) {
////                DB.relationTagNote().safeSaveRelation(new RelationTagNote(dbTag, note));
////            }
////        }
////        for (ITag t : curTags) {
////            boolean needToRemove = true;
////            for (DBTag dbTag : tags) {
////                if (t.getDbTag().getId() == dbTag.getId()) {
////                    //当前 tag 已经在数据库中存在
////                    needToRemove = false;
////                }
////            }
////            if (needToRemove) {
////                DB.relationTagNote().safeDeleteRelation(new RelationTagNote(t.getDbTag(), note));
////            }
////        }
////        //endregion
//
//        LogUtil.e(note.toString());
//        ToastUtil.ok("成功修改[ 笔记 ]:" + content);
//
//        finishWithAnim();
//    }
//
//    private void onCreateNote() {
//        DBNote dbNote = new DBNote();
//        DBUser activeUser = DB.users().getActive();
//        String owner = Converter.getOwnerUrl(activeUser);
//        dbNote.setTitle(title);
//        dbNote.setContent(content);
//        dbNote.setCreated_datetime(TimeUtil.formatGMTDate(new Date()));
//        dbNote.setUpdate_datetime(dbNote.getCreated_datetime());
//        dbNote.setColor(Color.parseColor("#03a9f4"));
//        dbNote.setUser(activeUser);
//        if (noteBook_toAttach != null) {
//            dbNote.setNoteBook(noteBook_toAttach);
//        }
//        DB.notes().safeSaveDBNote(dbNote);
//        LogUtil.e(dbNote.toString());
//        EventBus.getDefault().post(new PersistenceEvents.NoteCreateEvent(dbNote));
//
////        //region 处理 tag
////        List<DBTag> tags = getTags();
////        for (DBTag dbTag : tags) {
////            boolean needToAdd = true;
////            for (ITag t : curTags) {
////                if (t.getDbTag().getId() == dbTag.getId()) {
////                    //当前 tag 在数据库中存在，没必要保存
////                    needToAdd = false;
////                }
////            }
////            if (needToAdd) {
////                DB.relationTagNote().safeSaveRelation(new RelationTagNote(dbTag, dbNote));
////            }
////        }
////        for (ITag t : curTags) {
////            boolean needToRemove = true;
////            for (DBTag dbTag : tags) {
////                if (t.getDbTag().getId() == dbTag.getId()) {
////                    //当前 tag 已经在数据库中存在
////                    needToRemove = false;
////                }
////            }
////            if (needToRemove) {
////                DB.relationTagNote().safeDeleteRelation(new RelationTagNote(t.getDbTag(), dbNote));
////            }
////        }
////        //endregion
//
//        ToastUtil.ok("成功添加[ 笔记 ]:" + content);
//        finishWithAnim();
//    }
//
//    private void onUpdateHabit() {
//        habit.setName(title);
//        habit.setDescription(content);
//        habit.setUpdate_datetime(TimeUtil.formatGMTDate(new Date()));
//        habit.setUser(DB.users().getActive());
//        LogUtil.e(habit.toString());
//        DB.habits().updateAndFireEvent(habit);
//        EventBus.getDefault().post(new PersistenceEvents.HabitUpdateEvent(habit));
//
////        //region 处理 tag
////        List<DBTag> tags = getTags();
////        for (DBTag dbTag : tags) {
////            boolean needToAdd = true;
////            for (ITag t : curTags) {
////                if (t.getDbTag().getId() == dbTag.getId()) {
////                    //当前 tag 在数据库中存在，没必要保存
////                    needToAdd = false;
////                }
////            }
////            if (needToAdd) {
////                DB.relationTagHabit().safeSaveRelation(new RelationTagHabit(dbTag, habit));
////            }
////        }
////        for (ITag t : curTags) {
////            boolean needToRemove = true;
////            for (DBTag dbTag : tags) {
////                if (t.getDbTag().getId() == dbTag.getId()) {
////                    //当前 tag 已经在数据库中存在
////                    needToRemove = false;
////                }
////            }
////            if (needToRemove) {
////                DB.relationTagHabit().safeDeleteRelation(new RelationTagHabit(t.getDbTag(), habit));
////            }
////        }
////        //endregion
//
//        ToastUtil.ok("成功修改[ 习惯 ]:" + title);
//
//        finishWithAnim();
//    }
//
//    private void onCreateHabit() {
//        DBHabit dbHabit = new DBHabit();
//        dbHabit.setCreated_datetime(TimeUtil.formatGMTDate(new Date()));
//        dbHabit.setName(title);
//        dbHabit.setDescription(content);
//        dbHabit.setUpdate_datetime(dbHabit.getCreated_datetime());
//        dbHabit.setColor(Color.parseColor("#03a9f4"));
//        dbHabit.setUser(DB.users().getActive());
//
//        DB.habits().saveAndFireEvent(dbHabit);
//        LogUtil.e(dbHabit.toString());
//        EventBus.getDefault().post(new PersistenceEvents.HabitCreateEvent(dbHabit));
////        //region 处理 tag
////        List<DBTag> tags = getTags();
////        for (DBTag dbTag : tags) {
////            boolean needToAdd = true;
////            for (ITag t : curTags) {
////                if (t.getDbTag().getId() == dbTag.getId()) {
////                    //当前 tag 在数据库中存在，没必要保存
////                    needToAdd = false;
////                }
////            }
////            if (needToAdd) {
////                DB.relationTagHabit().safeSaveRelation(new RelationTagHabit(dbTag, dbHabit));
////            }
////        }
////        for (ITag t : curTags) {
////            boolean needToRemove = true;
////            for (DBTag dbTag : tags) {
////                if (t.getDbTag().getId() == dbTag.getId()) {
////                    //当前 tag 已经在数据库中存在
////                    needToRemove = false;
////                }
////            }
////            if (needToRemove) {
////                DB.relationTagHabit().safeDeleteRelation(new RelationTagHabit(t.getDbTag(), dbHabit));
////            }
////        }
////        //endregion
//        ToastUtil.ok("成功添加[ 习惯 ]:" + title);
//        finishWithAnim();
//    }
//
//    private void onSubmit() {
//        dialog_add_task_footer_bt_submit.setClickable(false);
//        dialog_add_task_footer_bt_submit
//                .postDelayed(() -> dialog_add_task_footer_bt_submit.setClickable(true), 3000);
//        title = getShortTitle(dialog_add_task_et_title.getText().toString());
//        content = dialog_add_task_et_content.getText().toString();
//        if (StringUtil.isEmail(title)) {
//            ToastUtil.e("标题不能为空！");
//            return;
//        }
//        ViewUtil.hideInputMethod(dialog_add_task_et_title);
//        ViewUtil.hideInputMethod(dialog_add_task_et_content);
//        switch (type) {
//            case NOTE:
//                if (note != null) {
//                    onUpdateNote();
//                } else {
//                    onCreateNote();
//                }
//                break;
//            case TASK:
//                if (task != null) {
//                    onUpdateTask();
//                } else {
//                    onCreateTask();
//                }
//                break;
//            case HABIT:
//                if (habit != null) {
//                    onUpdateHabit();
//                } else {
//                    onCreateHabit();
//                }
//                break;
//        }
//        DEF.config().save(Constants.UNIVERSAL_SAVE_COTENT, "");
//        if (notificationService != null) {
//            notificationService.refreshCurrentNotification();
//        }
//    }
//
//    //region onCheckedChanged
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        int i = buttonView.getId();// 振动
//        if (i == R.id.vibrate_btn) {
//            if (isChecked) {
//                vibrate(getContext());
//                mAlarmClock.setVibrate(true);
//            } else {
//                mAlarmClock.setVibrate(false);
//            }
//
//            // 督促
//        } else if (i == R.id.nap_btn) {
//            if (isChecked) {
//                mAlarmClock.setNap(true);
//            } else {
//                mAlarmClock.setNap(false);
//            }
//
//        }
//
//    }
//
//    /**
//     * 振动单次100毫秒
//     *
//     * @param context context
//     */
//    public static void vibrate(Context context) {
//        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(100);
//    }
//    //endregion
//
//    //endregion
//
//    //region Activity
//
//    /**
//     * 启动这个Activity的Intent
//     *
//     * @param context 　上下文
//     * @return 返回intent实例
//     */
//    public static Intent createIntent(Context context) {
//        return new Intent(context, InfoOperationActivity.class);
//    }
//
//    @Override
//    protected int layout() {
//        return R.layout.editor_activity_dialog;
//    }
//
//    @Override
//    protected boolean canBack() {
//        return false;
//    }
//
//    @Override
//    public void onBackPressed() {
//        /*
//         * 拦截返回键操作，如果此时表情布局未隐藏，先隐藏表情布局
//         */
//        if (!mSmartKeyboardManager.interceptBackPressed()) {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != Activity.RESULT_OK) {
//            return;
//        }
//        switch (requestCode) {
//            // 铃声选择界面返回
//            case REQUEST_RING_SELECT:
//                // 铃声名
//                String name = data.getStringExtra(Constants.RING_NAME);
//                // 铃声地址
//                String url = data.getStringExtra(Constants.RING_URL);
//                // 铃声界面
//                int ringPager = data.getIntExtra(Constants.RING_PAGER, 0);
//
//                mRingDescribe.setText(name);
//
//                mAlarmClock.setRingName(name);
//                mAlarmClock.setRingUrl(url);
//                mAlarmClock.setRingPager(ringPager);
//                break;
//            // 督促编辑界面返回
//            case REQUEST_NAP_EDIT:
//                // 督促间隔
//                int napInterval = data.getIntExtra(Constants.NAP_INTERVAL, 10);
//                // 督促次数
//                int napTimes = data.getIntExtra(Constants.NAP_TIMES, 3);
//                mAlarmClock.setNapInterval(napInterval);
//                mAlarmClock.setNapTimes(napTimes);
//                break;
//        }
//    }
//
//    public void finishWithAnim() {
//        finish();
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//    }
//    //endregion
//
//    //region tool function
//    //endregion
//
//    //region switch
//    private void initSnapperAndAesthetics(RecyclerView... recyclerViews) {
//        for (final RecyclerView recyclerView : recyclerViews) {
//            recyclerView.post(new Runnable() {
//                @Override
//                public void run() {
//                    SwipeSpinnerHelper swipeSpinnerHelper = SwipeSpinnerHelper.bindRecyclerView(recyclerView);
//                    final ItemDrawable itemDrawable = new ItemDrawable(swipeSpinnerHelper);
//                    SwipeSpinnerHelper.ScrollCallbacks scrollCallbacks = new SwipeSpinnerHelper.ScrollCallbacks() {
//                        @Override
//                        public void onScrolled(float directedInterpolationFraction) {
//                            Drawable otherDrawable, currentDirection;
//                            otherDrawable =
//                                    directedInterpolationFraction > 0 ? itemDrawable.indicators.getDrawable(0)
//                                            : itemDrawable.indicators.getDrawable(1);
//                            currentDirection =
//                                    directedInterpolationFraction > 0 ? itemDrawable.indicators.getDrawable(1)
//                                            : itemDrawable.indicators.getDrawable(0);
//
//                            int currentDirectionAlpha = 255;
//
//                            currentDirection.setAlpha(currentDirectionAlpha);
//                            otherDrawable.setAlpha(
//                                    MathUtils.clamp(255 - currentDirectionAlpha, ItemDrawable.INIT_ALPHA, 255));
//                            itemDrawable.indicators.invalidateSelf();
//                        }
//
//                        @Override
//                        public void onResetScroll() {
////              LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
////              LogUtil.e("当前选定的 item 位于"+linearLayoutManager.getPosition(linearLayoutManager.getFocusedChild()));
//                            itemDrawable.resetIndicators();
//                        }
//
//                        @Override
//                        public void onSelect(int pos) {
////              LogUtil.e("当前选定的 item 位于 " + pos);
//                            switch (pos) {
//                                case 0:
//                                    onSelectTypeNote();
//                                    break;
//                                case 1:
//                                    onSelectTypeTask();
//                                    break;
//                                case 2:
//                                    onSelectTypeHabit();
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    };
//                    swipeSpinnerHelper.setScrollCallbacks(scrollCallbacks);
//                }
//            });
//
//        }
//    }
//
//    private DataAdapter getTypeAdapter() {
//        ArrayList<String> data = new ArrayList<>();
//        data.add("笔记");
//        data.add("任务");
//        data.add("习惯");
//        DataAdapter<String> adapter = new DataAdapter<>();
//        adapter.addData(data);
//        return adapter;
//    }
//
//    //endregion
//
//    //region 内部类
//    public enum Type {
//        NOTE, TASK, HABIT;
//    }
//
//    enum RemindType {
//        noRemind, remind0min, remind5min
//    }
//
//    class ItemDrawable {
//
//        final static int INIT_ALPHA = 255 / 4;
//        GradientDrawable gradientDrawable;
//        LayerDrawable indicators, finalDrawable;
//
//        ItemDrawable(SwipeSpinnerHelper swipeSpinnerHelper) {
//            View v = swipeSpinnerHelper.getRecyclerView();
//            Drawable[] drawables = new Drawable[2];
//            gradientDrawable = new GradientDrawable();
//            gradientDrawable.setColor(Color.TRANSPARENT);
//            gradientDrawable.setStroke(ViewUtil.dp2px(1), Color.WHITE);
//            gradientDrawable.setCornerRadius(ViewUtil.dp2px(4));//v.getHeight() / 2);
//            drawables[0] = gradientDrawable;
//
//            indicators = (LayerDrawable) AppCompatResources
//                    .getDrawable(getApplicationContext(), swipeSpinnerHelper.isVertical()
//                            ? R.drawable.arrows
//                            : R.drawable.arrows_horizontal)
//                    .mutate();
//            drawables[1] = indicators;
//
//            finalDrawable = new LayerDrawable(drawables);
//            v.setBackground(finalDrawable);
//
//            resetIndicators();
//        }
//
//        public void resetIndicators() {
//            indicators.getDrawable(0).setAlpha(INIT_ALPHA);
//            indicators.getDrawable(1).setAlpha(INIT_ALPHA);
//            indicators.invalidateSelf();
//        }
//    }
//
//    public class DataAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
//
//
//        public DataAdapter() {
//            super(R.layout.base_constraint_textview);
//        }
//
//
//        @Override
//        protected void convert(BaseViewHolder viewHolder, T item) {
//            viewHolder.setText(R.id.text, (CharSequence) item);
//        }
//
//    }
//
//    //endregion
//}
