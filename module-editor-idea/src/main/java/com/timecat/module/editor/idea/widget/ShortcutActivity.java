package com.timecat.module.editor.idea.widget;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.timecat.component.alert.ToastUtil;
import com.timecat.module.editor.idea.RouterActivity;
import com.timecat.ui.block.temp.Def;

/**
 * Created by ywwynm on 2016/10/22. Used to handle shortcut actions.
 */
public class ShortcutActivity extends AppCompatActivity {

    public static final String TAG = "ShortcutActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String action = getIntent().getAction();
        Intent openIntent = null;
        if (Def.Communication.SHORTCUT_ACTION_CREATE.equals(action)) {
            openIntent =RouterActivity.infoOperation(this);
        } else if (Def.Communication.SHORTCUT_ACTION_CHECK_UPCOMING.equals(action)) {
            boolean canCheck = false;
//            List<Thing> things = ThingDAO.getInstance(this)
//                                         .getThingsForDisplay(Def.LimitForGettingThings.ALL_UNDERWAY);
//            Collections.sort(things, ThingsSorter.getThingComparatorByAlarmTime(true));
//            Thing thing = things.get(1); // 0 is header
//            @Type int thingType = thing.getType();
//            if (thingType == RecordKt.HABIT) {
//                canCheck = true;
//            } else if (Thing.isReminderType(thingType)) {
//                Reminder reminder = ReminderDAO.getInstance(this).getReminderById(thing.getId());
//                if (reminder == null || reminder.getState() != Reminder.UNDERWAY) {
//                    canCheck = false;
//                } else {
//                    canCheck = true;
//                }
//            }
//
//            if (canCheck) {TODO
//                openIntent = AuthenticationActivity.getOpenIntent(
//                        this, TAG, thing.getId(), -1,
//                        Def.Communication.AUTHENTICATE_ACTION_VIEW,
//                        getString(R.string.check_private_thing));
        } else {
            ToastUtil.i("没有置顶的记事");
        }
//    } else if(Def.Communication.SHORTCUT_ACTION_CHECK_STICKY.equals(action))

//    {
//            List<Thing> things = ThingDAO.getInstance(this)
//                                         .getThingsForDisplay(Def.LimitForGettingThings.ALL_UNDERWAY);
//            Thing thing = things.get(1);
//            if (thing.getLocation() < 0) {
//                openIntent = AuthenticationActivity.getOpenIntent(
//                        this, TAG, thing.getId(), -1,
//                        Def.Communication.AUTHENTICATE_ACTION_VIEW,
//                        getString(R.string.check_private_thing));
//            } else {
//                ToastUtil.i("没有置顶的记事");
//            }
//        }

//        if (openIntent != null) {
//            startActivity(openIntent);
//        }
//
//        finish();
    }
}
