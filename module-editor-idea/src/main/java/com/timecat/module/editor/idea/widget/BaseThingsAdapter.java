package com.timecat.module.editor.idea.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.cardview.widget.CardView;
import androidx.collection.LongSparseArray;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.timecat.component.data.database.help.CheckListHelper;
import com.timecat.component.resource.base.Attr;
import com.timecat.component.setting.DEF;
import com.timecat.data.room.record.RoomRecord;
import com.timecat.module.editor.idea.R;
import com.timecat.ui.block.adapter.CheckListAdapter;
import com.timecat.ui.block.base.BaseViewHolder;
import com.timecat.ui.block.temp.Def;
import com.timecat.ui.block.temp.FrequentSettings;
import com.timecat.ui.block.util.DeviceUtil;
import com.timecat.ui.block.util.DisplayUtil;
import com.timecat.ui.block.view.InterceptTouchCardView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by ywwynm on 2016/7/31. Basic things adapter for RecyclerView. Created for re-use.
 */
public abstract class BaseThingsAdapter extends
        RecyclerView.Adapter<BaseThingsAdapter.BaseThingViewHolder> {

    public static final String TAG = "BaseThingsAdapter";

    protected LayoutInflater mInflater;
    protected float mDensity;
    private Context mContext;

    private LongSparseArray<CheckListAdapter> mCheckListAdapters;

    private RequestManager mImageRequestManager;

    private int mCardWidth;
    private boolean mShouldShowPrivateContent = false;
    private int mChecklistMaxItemCount = 8;

    public BaseThingsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mDensity = DisplayUtil.getScreenDensity(context);

        mContext = context;

        mCheckListAdapters = new LongSparseArray<>();

        mImageRequestManager = Glide.with(context);

        mCardWidth = DisplayUtil.getThingCardWidth(context);
    }

    protected abstract int getCurrentMode();

    protected abstract List<RoomRecord> getThings();

    public void setCardWidth(int cardWidth) {
        mCardWidth = cardWidth;
    }

    public void setShouldShowPrivateContent(boolean shouldShowPrivateContent) {
        mShouldShowPrivateContent = shouldShowPrivateContent;
    }

    public void setChecklistMaxItemCount(int checklistMaxItemCount) {
        mChecklistMaxItemCount = checklistMaxItemCount;
    }

    @NotNull
    @Override
    public BaseThingViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new BaseThingViewHolder(mInflater.inflate(R.layout.card_thing, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull BaseThingViewHolder holder, int position) {
        RoomRecord thing = getThings().get(position);
        setContentViewAppearance(holder, thing);
        setCardAppearance(holder, thing.taskLabelColor(), thing.isFinished());
    }

    private void setContentViewAppearance(final BaseThingViewHolder holder, RoomRecord thing) {
        updateCardForStickyOrOngoingNotification(holder, thing);
        updateCardForTitle(holder, thing);

        if (thing.isPrivate() && !mShouldShowPrivateContent) {
            holder.ivPrivateThing.setVisibility(View.VISIBLE);
            holder.flImageAttachment.setVisibility(View.GONE);
            holder.tvContent.setVisibility(View.GONE);
            holder.rvChecklist.setVisibility(View.GONE);
            holder.llAudioAttachment.setVisibility(View.GONE);
            holder.rlReminder.setVisibility(View.GONE);
            holder.rlHabit.setVisibility(View.GONE);
        } else {
            holder.ivPrivateThing.setVisibility(View.GONE);

            updateCardForContent(holder, thing);
            updateCardForReminder(holder, thing);
            updateCardForHabit(holder, thing);
            updateCardForAudioAttachment(holder, thing);
            updateCardForImageAttachment(holder, thing);

            updateCardSeparatorsIfNeeded(holder);

            enlargeAudioLayoutIfNeeded(holder);
        }

        updateCardForDoing(holder, thing);
    }

    private void updateCardForStickyOrOngoingNotification(BaseThingViewHolder holder, RoomRecord thing) {
        boolean sticky = thing.isPined();
        boolean ongoing = FrequentSettings.getLong(Def.Meta.KEY_ONGOING_THING_ID) == thing.getId();
        if (!sticky && !ongoing) {
            holder.ivStickyOngoing.setVisibility(View.GONE);
        } else {
            holder.ivStickyOngoing.setVisibility(View.VISIBLE);

            holder.ivStickyOngoing.setImageResource(sticky
                    ? R.drawable.ic_sticky
                    : R.drawable.ic_ongoing_notication);
            @StringRes int cdRes = sticky ? R.string.sticky_thing : R.string.ongoing_thing;
            holder.ivStickyOngoing.setContentDescription(mContext.getString(cdRes));
        }
    }

    private void updateCardForTitle(BaseThingViewHolder holder, RoomRecord thing) {
        String title = thing.getTitle();
        if (!title.isEmpty()) {
            int p = (int) (mDensity * 16);
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setPadding(p, p, p, 0);
            holder.tvTitle.setText(title);
            holder.tvTitle.setTextColor(Attr.getPrimaryTextColor(mContext));
        } else {
            holder.tvTitle.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NewApi")
    private void updateCardForContent(BaseThingViewHolder holder, RoomRecord thing) {
        int p = (int) (mDensity * 16);
        String content = thing.getContent();
        if (!content.isEmpty()) {
            if (!CheckListHelper.isCheckListStr(content)) {
                holder.rvChecklist.setVisibility(View.GONE);
                holder.tvContent.setVisibility(View.VISIBLE);

                int length = content.length();
                if (length <= 60) {
                    holder.tvContent.setTextSize(-0.14f * length + 24.14f);
                } else {
                    holder.tvContent.setTextSize(16);
                }

                holder.tvContent.setPadding(p, p, p, 0);
                holder.tvContent.setText(content);
                holder.tvContent.setTextColor(Attr.getPrimaryTextColor(mContext));
            } else {
                holder.tvContent.setVisibility(View.GONE);
                holder.rvChecklist.setVisibility(View.VISIBLE);

                long id = thing.getId();
                List<String> items = CheckListHelper.toCheckListItems(content, false);
                CheckListAdapter adapter = mCheckListAdapters.get(id);
                if (adapter == null) {
                    adapter = new CheckListAdapter(mContext, CheckListAdapter.TEXTVIEW, items);
                    mCheckListAdapters.put(id, adapter);
                } else {
                    adapter.setItems(items);
                }
                adapter.setMaxItemCount(mChecklistMaxItemCount);
                onChecklistAdapterInitialized(holder, adapter, thing);
                holder.rvChecklist.setAdapter(adapter);
                holder.rvChecklist.setLayoutManager(new LinearLayoutManager(mContext));

                int rp = (int) (mDensity * 6);
                holder.rvChecklist.setPaddingRelative(rp, p, p, 0);
            }
        } else {
            holder.tvContent.setVisibility(View.GONE);
            holder.rvChecklist.setVisibility(View.GONE);
        }
    }

    private void setImgRes(ImageView iv, @DrawableRes int drawable) {
        iv.setImageResource(drawable);
        iv.getDrawable().setColorFilter(Attr.getIconColor(mContext), PorterDuff.Mode.SRC_IN);
    }

    protected void onChecklistAdapterInitialized(
            BaseThingViewHolder holder, CheckListAdapter adapter, RoomRecord thing) {
        // do nothing here
    }

    private void updateCardForReminder(BaseThingViewHolder holder, RoomRecord thing) {
        holder.rlReminder.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    private void updateCardForHabit(BaseThingViewHolder holder, RoomRecord thing) {
        holder.rlHabit.setVisibility(View.GONE);
    }

    private void updateCardForImageAttachment(final BaseThingViewHolder holder, RoomRecord thing) {
        List<String> photos = thing.getPhoto();
        if (photos.size()<=0) return;
        String firstImageTypePathName = photos.get(0);
        if (firstImageTypePathName != null) {
            holder.flImageAttachment.setVisibility(View.VISIBLE);

            int imageW = mCardWidth;
            int imageH = imageW * 3 / 4;

            // without this, the first card with an image won't display well on lollipop device
            // and I don't know the reason...
            LinearLayout.LayoutParams paramsLayout = (LinearLayout.LayoutParams)
                    holder.flImageAttachment.getLayoutParams();
            paramsLayout.width = imageW;

            // set height at first to get a placeHolder before image has been loaded
            FrameLayout.LayoutParams paramsImage = (FrameLayout.LayoutParams)
                    holder.ivImageAttachment.getLayoutParams();
            paramsImage.height = imageH;

            // set height of cover
            FrameLayout.LayoutParams paramsCover = (FrameLayout.LayoutParams)
                    holder.vImageCover.getLayoutParams();
            paramsCover.height = imageH;

            // before lollipop, set margins to negative number to remove ugly stroke
            if (!DeviceUtil.hasLollipopApi()) {
                int m = (int) (mDensity * -8);
                paramsLayout.setMargins(0, m, 0, 0);
            }

            String pathName = firstImageTypePathName;
            mImageRequestManager
                    .load(pathName)
                    .apply(new RequestOptions().centerInside())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                       DataSource dataSource, boolean isFirstResource) {
                            holder.pbLoading.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.ivImageAttachment);

            // if thing has only an image/video, there should be no margins for ImageView
            if (holder.tvTitle.getVisibility() == View.GONE
                    && holder.tvContent.getVisibility() == View.GONE
                    && holder.rvChecklist.getVisibility() == View.GONE
                    && holder.llAudioAttachment.getVisibility() == View.GONE
                    && holder.rlReminder.getVisibility() == View.GONE
                    && holder.rlHabit.getVisibility() == View.GONE) {
                holder.vPaddingBottom.setVisibility(View.GONE);
            } else {
                holder.vPaddingBottom.setVisibility(View.VISIBLE);
            }

            holder.tvImageCount.setText(String.valueOf(photos.size()));

            // when this card is unselected in selecting/moving mode, image should be covered
            holder.vImageCover.setVisibility(View.GONE);
        } else {
            holder.vPaddingBottom.setVisibility(View.VISIBLE);
            holder.flImageAttachment.setVisibility(View.GONE);
        }
    }

    private void updateCardForAudioAttachment(BaseThingViewHolder holder, RoomRecord thing) {
        List<String> sound = thing.getSound();
        if (sound.size()<=0) {
            holder.llAudioAttachment.setVisibility(View.GONE);
            return;
        }
        String str = String.valueOf(sound.size());
        holder.llAudioAttachment.setVisibility(View.VISIBLE);
        int p = (int) (mDensity * 16);
        holder.llAudioAttachment.setPadding(p, p / 4 * 3, p, 0);

        holder.tvAudioCount.setText(str);

        setImgRes(holder.ivAudioCount, R.drawable.card_audio_attachment);
        holder.tvAudioCount.setTextColor(Attr.getPrimaryTextColor(mContext));
    }

    private void updateCardSeparatorsIfNeeded(BaseThingViewHolder holder) {
        if (holder.flImageAttachment.getVisibility() == View.VISIBLE
                && holder.tvTitle.getVisibility() == View.GONE
                && holder.tvContent.getVisibility() == View.GONE
                && holder.rvChecklist.getVisibility() == View.GONE
                && holder.llAudioAttachment.getVisibility() == View.GONE) {
            if (holder.rlReminder.getVisibility() == View.VISIBLE) {
                holder.vReminderSeparator.setVisibility(View.GONE);
            } else if (holder.rlHabit.getVisibility() == View.VISIBLE) {
                holder.vHabitSeparator1.setVisibility(View.GONE);
            }
        } else {
            if (holder.rlReminder.getVisibility() == View.VISIBLE) {
                holder.vReminderSeparator.setVisibility(View.VISIBLE);
            } else if (holder.rlHabit.getVisibility() == View.VISIBLE) {
                holder.vHabitSeparator1.setVisibility(View.VISIBLE);
            }
        }
    }

    private void enlargeAudioLayoutIfNeeded(BaseThingViewHolder holder) {
        if (holder.llAudioAttachment.getVisibility() != View.VISIBLE) {
            return;
        }

        LinearLayout.LayoutParams llp1 = (LinearLayout.LayoutParams)
                holder.ivAudioCount.getLayoutParams();
        LinearLayout.LayoutParams llp2 = (LinearLayout.LayoutParams)
                holder.tvAudioCount.getLayoutParams();
        int dp1 = (int) (mDensity * 1);
        int dp8 = (int) (mDensity * 8);
        int dp12 = (int) (mDensity * 12);
        int dp16 = (int) (mDensity * 16);
        if (holder.flImageAttachment.getVisibility() == View.GONE
                && holder.tvTitle.getVisibility() == View.GONE
                && holder.tvContent.getVisibility() == View.GONE
                && holder.rvChecklist.getVisibility() == View.GONE) {
            llp1.height = (int) (mDensity * 16);
            llp1.topMargin = dp1;
            holder.tvAudioCount.setTextSize(18);

            llp2.setMargins(dp12, llp2.topMargin, llp2.rightMargin, llp2.bottomMargin);
            if (DeviceUtil.hasJellyBeanMR1Api()) {
                llp2.setMarginStart(dp12);
            }

            holder.llAudioAttachment.setPadding(dp16, dp16, dp16, 0);
        } else {
            llp1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            llp1.topMargin = 0;
            holder.tvAudioCount.setTextSize(11);

            llp2.setMargins(dp8, llp2.topMargin, llp2.rightMargin, llp2.bottomMargin);
            if (DeviceUtil.hasJellyBeanMR1Api()) {
                llp2.setMarginStart(dp8);
            }

            holder.llAudioAttachment.setPadding(dp16, dp16 / 4 * 3, dp16, 0);
        }
        holder.ivAudioCount.requestLayout();
    }

    private void updateCardForDoing(final BaseThingViewHolder holder, RoomRecord thing) {
        if (DEF.widget().getLong("DoingThingId", -1L) == thing.getId()) {
            holder.flDoing.setVisibility(View.VISIBLE);
            holder.cv.post(new Runnable() {
                @Override
                public void run() {
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)
                            holder.flDoing.getLayoutParams();
                    lp.width = holder.cv.getWidth();
                    lp.height = holder.cv.getHeight();
                    Log.i(TAG, "setting doing cover for thing card, " +
                            "width[" + lp.width + ", height[" + lp.height + "]");
                    holder.flDoing.requestLayout();
                }
            });
        } else {
            holder.flDoing.setVisibility(View.GONE);
        }
    }

    private void setCardAppearance(final BaseThingViewHolder holder, int color,
                                   final boolean selected) {
//        if (color == ContextCompat.getColor(mContext, R.color.pine_green)) {
//            color = ContextCompat.getColor(mContext, R.color.aein_red);
//        }
        final CardView cv = holder.cv;
        int currentMode = getCurrentMode();

        cv.setCardBackgroundColor(color);

        // Wrong warning here since FrameLayout#setForeground(Drawable) was provided on API 1
        holder.cv.setForeground(ContextCompat.getDrawable(
                mContext, R.drawable.selectable_item_background));
    }

    @Override
    public int getItemViewType(int position) {
        return getThings().get(position).getLabel();
    }

    @Override
    public int getItemCount() {
        return getThings().size();
    }

    public static class BaseThingViewHolder extends BaseViewHolder {

        public final InterceptTouchCardView cv;
        public final View vPaddingBottom;

        public final ImageView ivStickyOngoing;
        public final FrameLayout flDoing;

        public final FrameLayout flImageAttachment;
        public final ImageView ivImageAttachment;
        public final TextView tvImageCount;
        public final ProgressBar pbLoading;
        public final View vImageCover;

        public final TextView tvTitle;
        public final ImageView ivPrivateThing;

        public final TextView tvContent;
        public final RecyclerView rvChecklist;

        public final LinearLayout llAudioAttachment;
        public final ImageView ivAudioCount;
        public final TextView tvAudioCount;

        public final RelativeLayout rlReminder;
        public final View vReminderSeparator;
        public final ImageView ivReminder;
        public final TextView tvReminderTime;

        public final RelativeLayout rlHabit;
        public final View vHabitSeparator1;
        public final TextView tvHabitSummary;
        public final TextView tvHabitNextReminder;
        public final View vHabitSeparator2;
        public final LinearLayout llHabitRecord;
        public final TextView tvHabitLastFive;
        public final TextView tvHabitFinishedThisT;

        public BaseThingViewHolder(View item) {
            super(item);

            cv = f(R.id.cv_thing);
            vPaddingBottom = f(R.id.view_thing_padding_bottom);

            ivStickyOngoing = f(R.id.iv_thing_sticky_ongoing);
            flDoing = f(R.id.fl_thing_doing_cover);

            flImageAttachment = f(R.id.fl_thing_image);
            ivImageAttachment = f(R.id.iv_thing_image);
            tvImageCount = f(R.id.tv_thing_image_attachment_count);
            pbLoading = f(R.id.pb_thing_image_attachment);
            vImageCover = f(R.id.view_thing_image_cover);

            tvTitle = f(R.id.tv_thing_title);
            ivPrivateThing = f(R.id.iv_private_thing);

            tvContent = f(R.id.tv_thing_content);
            rvChecklist = f(R.id.rv_check_list);

            llAudioAttachment = f(R.id.ll_thing_audio_attachment);
            ivAudioCount = f(R.id.iv_thing_audio_attachment_count);
            tvAudioCount = f(R.id.tv_thing_audio_attachment_count);

            rlReminder = f(R.id.rl_thing_reminder);
            vReminderSeparator = f(R.id.view_reminder_separator);
            ivReminder = f(R.id.iv_thing_reminder);
            tvReminderTime = f(R.id.tv_thing_reminder_time);

            rlHabit = f(R.id.rl_thing_habit);
            vHabitSeparator1 = f(R.id.view_habit_separator_1);
            tvHabitSummary = f(R.id.tv_thing_habit_summary);
            tvHabitNextReminder = f(R.id.tv_thing_habit_next_reminder);
            vHabitSeparator2 = f(R.id.view_habit_separator_2);
            llHabitRecord = f(R.id.ll_thing_habit_record);
            tvHabitLastFive = f(R.id.tv_thing_habit_last_five_record);
            tvHabitFinishedThisT = f(R.id.tv_thing_habit_finished_this_t);

            int pbColor = ContextCompat.getColor(item.getContext(), R.color.app_accent);
            pbLoading.getIndeterminateDrawable().setColorFilter(pbColor, PorterDuff.Mode.SRC_IN);
        }

    }

}
