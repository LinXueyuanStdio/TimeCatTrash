package com.timecat.module.data;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.timecat.component.readonly.RouterHub;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/6/16
 * @description 权限+喵密钥
 * @usage null
 */
@Route(path = RouterHub.Data_DataManagerActivity)
public class DataManagerActivity extends AppCompatActivity implements OnClickListener {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    ARouter.getInstance().inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.data_activity_datamanager);

    setSupportActionBar(findViewById(R.id.toolbar));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("数据控制中心");

    TextView task = findViewById(R.id.task);
    TextView note = findViewById(R.id.note);
    TextView notebook = findViewById(R.id.notebook);
    TextView plan = findViewById(R.id.plan);
    TextView subplan = findViewById(R.id.subplan);
    TextView habit = findViewById(R.id.habit);
    task.setOnClickListener(this);
    note.setOnClickListener(this);
    notebook.setOnClickListener(this);
    plan.setOnClickListener(this);
    subplan.setOnClickListener(this);
    habit.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    int i = v.getId();
    if (i == R.id.task) {
      go("任务");
    } else if (i == R.id.note) {
      go("笔记");
    } else if (i == R.id.notebook) {
      go("笔记本");
    } else if (i == R.id.plan) {
      go("计划");
    } else if (i == R.id.subplan) {
      go("子计划");
    } else if (i == R.id.habit) {
      go("习惯");
    }
  }

  private void go(String title) {
    ARouter.getInstance().build(RouterHub.Data_DataDetailActivity)
        .withString("title", title).navigation(this);
  }
}
