package com.timecat.module.data;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bin.david.form.core.SmartTable;
import com.timecat.component.readonly.RouterHub;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/6/16
 * @description 权限+喵密钥
 * @usage null
 */
@Route(path = RouterHub.Data_DataDetailActivity)
public class DataDetailActivity extends AppCompatActivity {

    @Autowired
    String title = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_activity_datadetail);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        SmartTable table = findViewById(R.id.table);
        table.setZoom(true);
        //TODO
//        table.setData()
    }

}
