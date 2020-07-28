package com.timecat.module.moment.activity.circledemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.timecat.component.lightui.base.BaseTitleBarActivity;
import com.timecat.component.readonly.RouterHub;
import com.timecat.module.moment.R;

/**
 * Created by 大灯泡 on 2018/10/26.
 * <p>
 * 实际上就是mainActivity那套，针对Fragment用
 */
@Route(path = RouterHub.MOMENT_MomentFriendCircleFragmentActivity)
public class MomentFriendCircleFragmentActivity extends BaseTitleBarActivity {
    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moment_activity_main_with_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new MomentFriendCircleFragment())
                    .commit();
        }
    }

}
