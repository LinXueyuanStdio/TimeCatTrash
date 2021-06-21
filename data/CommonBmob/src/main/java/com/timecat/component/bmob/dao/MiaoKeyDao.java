package com.timecat.component.bmob.dao;

import com.timecat.component.bmob.data.own.MiaoKey;
import com.timecat.component.bmob.data._User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/3/20
 * @description null
 * @usage null
 */
public class MiaoKeyDao {

  public static void getMiaoKey(_User user, final FindListener<MiaoKey> listener) {
    BmobQuery<MiaoKey> query = new BmobQuery<>();
    query.addWhereEqualTo("user", user);
    query.order("-updatedAt");
    query.include("user");
    query.findObjects(new FindListener<MiaoKey>() {
      @Override
      public void done(List<MiaoKey> list, BmobException e) {
        if (e == null) {
          if (list != null && list.size() > 0) {
            listener.done(list, null);
          } else {
            listener.done(null, null);
          }
        } else {
          listener.done(list, e);
        }
      }
    });
  }
}
