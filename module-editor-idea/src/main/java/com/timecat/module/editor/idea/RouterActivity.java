package com.timecat.module.editor.idea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.timecat.component.data.core.LoginNavigationCallbackImpl;
import com.timecat.component.readonly.RouterHub;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/7/3
 * @description null
 * @usage null
 */
public class RouterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Intent intent = getIntent();
            if (RouterHub.HABIT_HabitDetailActivity.equals(intent.getAction())) {
                ARouter.getInstance().build(RouterHub.HABIT_HabitDetailActivity)
                        .setUri(intent.getData())
                        .withLong("id", intent.getLongExtra("id", -1))
                        .navigation(this, new LoginNavigationCallbackImpl());
                finish();
            } else if (RouterHub.TASK_TaskDetailActivity.equals(intent.getAction())) {
                ARouter.getInstance().build(RouterHub.TASK_TaskDetailActivity)
                        .withLong("id", intent.getLongExtra("id", -1))
                        .navigation(this);
                finish();
            } else if (RouterHub.NOTE_NoteDetailActivity.equals(intent.getAction())) {
                ARouter.getInstance().build(RouterHub.NOTE_NoteDetailActivity)
                        .withLong("id", intent.getLongExtra("id", -1))
                        .navigation(this);
                finish();
            } else if (RouterHub.TASK_PlanDetailActivity.equals(intent.getAction())) {
                ARouter.getInstance().build(RouterHub.TASK_PlanDetailActivity)
                        .withLong("id", intent.getLongExtra("id", -1))
                        .navigation(this);
                finish();
            } else if (RouterHub.SMALL_EDITOR_BlockSmallEditorActivity.equals(intent.getAction())) {
                ARouter.getInstance().build(RouterHub.SMALL_EDITOR_BlockSmallEditorActivity)
                        .navigation(this);
                finish();
            }
        } catch (Throwable ignored) {
        }
    }

    public static Intent infoOperation(Context context) {
        Intent intent = new Intent(context, RouterActivity.class);
        intent.setAction(RouterHub.SMALL_EDITOR_BlockSmallEditorActivity);
        return intent;
    }

    public static Intent mainActivity(Context context) {
        Intent intent = new Intent(context, RouterActivity.class);
        intent.setAction(RouterHub.MASTER_MainActivity);
        return intent;
    }
}
