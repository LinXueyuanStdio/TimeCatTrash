package com.timecat.module.editor.idea.widget;

import android.content.Context;
import android.content.Intent;

import androidx.core.util.Pair;

import com.timecat.component.data.database.help.CheckListHelper;
import com.timecat.data.room.TimeCatRoomDatabase;
import com.timecat.data.room.record.RoomRecord;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by ywwynm on 2016/9/4. Helper class for execute actions that happen not in ThingsActivity
 * or DetailActivity. For example, action finish in a notification for Reminder will finally call
 * method here.
 */
public class RemoteActionHelper {

    public static final String TAG = "RemoteActionHelper";

    private RemoteActionHelper() {
    }

    public static void toggleChecklistItem(Context context, long id, int itemPos) {
        Pair<RoomRecord, Integer> pair = getThingAndPosition(context, id, -1);
        RoomRecord thing = pair.first;
        if (thing == null) {
            return;
        }
        String updatedContent = CheckListHelper.toggleChecklistItem(thing.getContent(), itemPos);
        thing.setContent(updatedContent);

//        int position   = pair.second;
//        int typeBefore = thing.getType();
        TimeCatRoomDatabase.forFile(context).recordDao().update(thing);
//        if (position == -1) {
//            ThingDAO.getInstance(context).update(typeBefore, thing, false, false);
//        } else {
//            ThingManager.getInstance(context).update(typeBefore, thing, position, false);
//        }
//        updateUiEverywhere(context, thing, position, typeBefore,
//                Def.Communication.RESULT_UPDATE_THING_DONE_TYPE_SAME);
    }

    public static int getPosition(List<RoomRecord> mThings, long id) {
        final int size = mThings.size();
        for (int i = 0; i < size; i++) {
            if (mThings.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    public static Pair<RoomRecord, Integer> getThingAndPosition(Context context, long id, int knownPos) {
        DateTime time = new DateTime().withMillisOfDay(0);
        long start = time.getMillis();
        long end = time.plusDays(1).getMillis();
        List<RoomRecord> mThings = TimeCatRoomDatabase.forFile(context).recordDao().getBetween(start, end);
        RoomRecord thing = null;
        int correctPos = knownPos;
        if (knownPos == -1) {
            correctPos = getPosition(mThings, id);
            if (correctPos == -1) {
                thing = TimeCatRoomDatabase.forFile(context).recordDao().get(id);
            } else {
                thing = mThings.get(correctPos);
            }
        } else {
            final int size = mThings.size();
            if (knownPos >= size || mThings.get(knownPos).getId() != id) {
                for (int i = 0; i < size; i++) {
                    RoomRecord temp = mThings.get(i);
                    if (temp.getId() == id) {
                        thing = temp;
                        correctPos = i;
                        break;
                    }
                }
                if (thing == null) {
                    thing = TimeCatRoomDatabase.forFile(context).recordDao().get(id);
                    correctPos = -1;
                }
            } else {
                thing = mThings.get(knownPos);
            }
        }
        return new Pair<>(thing, correctPos);
    }

    /**
     * Update UI for ThingsActivity and app widgets if a remote action happened.
     * This method will also finish the action if it acts with a thing that under current limit,
     * which means it can be found in {@link ThingManager#mThings}. In this situation, we should
     * call methods in {@link ThingManager}, get their returned values and put them into broadcast
     * {@link Intent}s, as a result of which, ThingsActivity can handle UI update correctly as well
     * as appropriately.
     *
     * @param context    the context where the action happened.
     * @param thing      the thing that the action act with.
     * @param position   position of {@param thing} inside {@link ThingManager#mThings}. This can
     *                   be -1 if {@param thing} couldn't be found under current limit.
     * @param typeBefore used when we are updating {@param thing}'s type.
     * @param resultCode although this method can handle all possible resultCodes declared in
     *                   {@link Def.Communication}, remote actions will
     *                   only produce following resultCodes for the time being:
     *                   1. RESULT_UPDATE_THING_STATE_DIFFERENT: for finishing a Reminder/Goal.
     *                   2. RESULT_UPDATE_THING_DONE_TYPE_SAME: for finishing a Habit once, or delay
     *                   a Reminder.
     *                   3. RESULT_UPDATE_THING_DONE_TYPE_DIFFERENT: for finding a wrong thing or
     *                   not finding a correct related Reminder/Habit instance. For an example,
     *                   you can reference
     *                   {@link RemoteActionHelper#correctIfNoReminder(Context, Thing, int, int)}.
     */
//    public static void updateUiEverywhere(
//            Context context, DBTask thing, int position, int typeBefore, int resultCode) {
//        Log.i(TAG, "updateUiEverywhere called");
//        if (App.isSomethingUpdatedSpecially()) {
//            Log.i(TAG, "App.isSomethingUpdatedSpecially is already true");
//            App.tryToSetNotifyAllToTrue(thing, resultCode);
//        } else {
//            Log.i(TAG, "App.isSomethingUpdatedSpecially is false, set to true");
//            App.setSomethingUpdatedSpecially(true);
//        }
//
//        Intent broadcastIntent = new Intent(
//                Def.Communication.BROADCAST_ACTION_UPDATE_MAIN_UI);
//        broadcastIntent.putExtra(Def.Communication.KEY_RESULT_CODE, resultCode);
//        broadcastIntent.putExtra(Def.Communication.KEY_THING, thing);
//        broadcastIntent.putExtra(Def.Communication.KEY_POSITION, position);
//        broadcastIntent.putExtra(Def.Communication.KEY_TYPE_BEFORE, typeBefore);
//
//        ThingManager thingManager = ThingManager.getInstance(context);
//        if (resultCode == Def.Communication.RESULT_UPDATE_THING_STATE_DIFFERENT) {
//            broadcastIntent.putExtra(Def.Communication.KEY_STATE_AFTER, DBTask.FINISHED);
//            if (position != -1) {
//                boolean shouldCallChange = thingManager.updateState(
//                        thing, position, thing.getLocation(), DBTask.UNDERWAY,
//                        DBTask.FINISHED, false, true);
//                Log.d(TAG, "Updating state from remote action, shouldCallChange: " + shouldCallChange);
//                broadcastIntent.putExtra(Def.Communication.KEY_CALL_CHANGE, shouldCallChange);
//            }
//        } else if (resultCode == Def.Communication.RESULT_UPDATE_THING_DONE_TYPE_DIFFERENT) {
//            if (position != -1) {
//                boolean shouldCallChange =
//                        thingManager.update(typeBefore, thing, position, true) == 1;
//                Log.d(TAG, "Updating type from remote action, shouldCallChange: " + shouldCallChange);
//                broadcastIntent.putExtra(Def.Communication.KEY_CALL_CHANGE, shouldCallChange);
//            }
//        }
//        context.sendBroadcast(broadcastIntent);
//
//        AppWidgetHelper.updateSingleThingAppWidgets(context, thing.getId());
//        AppWidgetHelper.updateThingsListAppWidgetsForType(context, typeBefore);
//        int typeAfter = thing.getType();
//        if (typeBefore != typeAfter) {
//            AppWidgetHelper.updateThingsListAppWidgetsForType(context, typeAfter);
//        }
//
//        App.setLastUpdateUiIntent(broadcastIntent);
//    }

}
