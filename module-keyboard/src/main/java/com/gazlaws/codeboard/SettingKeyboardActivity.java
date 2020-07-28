package com.gazlaws.codeboard;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.timecat.component.readonly.RouterHub;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/2/19
 * @description null
 * @usage null
 */
@Route(path = RouterHub.KEYBOARD_SettingKeyboardActivity)
public class SettingKeyboardActivity extends KeyBoardActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setToolBar();
  }

  private void setToolBar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    assert getSupportActionBar() != null;
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

}