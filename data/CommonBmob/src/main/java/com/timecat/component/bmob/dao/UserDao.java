package com.timecat.component.bmob.dao;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.timecat.component.bmob.data._User;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/3/20
 * @description null
 * @usage null
 */
public class UserDao extends BaseModel {

    private static UserDao ourInstance = new UserDao();

    public static UserDao getInstance() {
        return ourInstance;
    }

    private UserDao() {
    }

    //region 登录注册、验证邮箱、找回密码

    /**
     * 用户管理：2.1、注册
     */
    public static void register(String username, String password, final LogInListener<_User> listener) {
        if (TextUtils.isEmpty(username)) {
            listener.done(null, new BmobException(CODE_NULL, "请填写用户名"));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            listener.done(null, new BmobException(CODE_NULL, "请填写密码"));
            return;
        }
        final _User user = new _User();
        if (_User.isEmail(username)) {
            user.setEmail(username);
        }
        if (_User.isMobileNO(username)) {
            user.setMobilePhoneNumber(username);
        }
        user.setUsername(username);
        user.setPassword(password);
        user.signUp(new SaveListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                if (e == null) {
                    listener.done(null, null);
                } else {
                    listener.done(null, e);
                }
            }
        });
    }

    /**
     * 用户管理：2.2、登录
     */
    public static void login(String username, String password, final LogInListener<_User> listener) {
        if (TextUtils.isEmpty(username)) {
            listener.done(null, new BmobException(CODE_NULL, "请填写用户名"));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            listener.done(null, new BmobException(CODE_NULL, "请填写密码"));
            return;
        }
        _User.loginByAccount(username, password, new LogInListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                if (e == null) {
                    listener.done(getCurrentUser(), null);
                } else {
                    listener.done(user, e);
                }
            }
        });
    }

    /**
     * 用户管理：2.3、退出登录
     */
    public static void logout() {
        BmobUser.logOut();
    }

    /**
     * 用户管理：2.4、获取当前用户
     */
    @Nullable
    public static _User getCurrentUser() {
        return BmobUser.getCurrentUser(_User.class);
    }

    /**
     * 用户管理：2.5、查询用户
     */
    public static void queryUsers(String username, final int limit, final FindListener<_User> listener) {
        BmobQuery<_User> query = new BmobQuery<>();
        query.addWhereContains("username", username);
        query.setLimit(limit);
        query.order("-createdAt");
        query.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        listener.done(list, e);
                    } else {
                        listener.done(list, new BmobException(CODE_NULL, "查无此人"));
                    }
                } else {
                    listener.done(list, e);
                }
            }
        });
    }

    /**
     * 用户管理：2.5、查询用户是否已被注册
     */
    public static void queryUsersExits(String username, final int limit, final FindListener<_User> listener) {
        BmobQuery<_User> query = new BmobQuery<>();
        query.addWhereEqualTo("username", username);
        query.setLimit(limit);
        query.order("-createdAt");
        query.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() == 0) {
                        listener.done(list, null);
                    } else {
                        listener.done(list, new BmobException(CODE_NULL, "该用户名已被注册"));
                    }
                } else {
                    listener.done(list, e);
                }
            }
        });
    }

    /**
     * 用户管理：2.5、查询用户邮箱是否存在，list == null && e == null 则存在且唯一
     */
    public static void queryEmail(String email, final int limit, final FindListener<_User> listener) {
        BmobQuery<_User> query = new BmobQuery<>();
        query.addWhereEqualTo("email", email);
        query.setLimit(limit);
        query.order("-createdAt");
        query.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
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

    /**
     * 用户管理：2.5、查询用户手机号是否存在，list == null && e == null 则存在且唯一
     */
    public static void queryPhone(String phone, final int limit, final FindListener<_User> listener) {
        BmobQuery<_User> query = new BmobQuery<>();
        query.addWhereEqualTo("mobilePhoneNumber", phone);
        query.setLimit(limit);
        query.order("-createdAt");
        query.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
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

    public static void loadAccountList(int page, int limit, final FindListener<_User> findListener) {
        BmobQuery<_User> query = new BmobQuery<>();
        query.setLimit(limit);
        query.setSkip(page * limit - limit);
        query.order("-createdAt");
        query.findObjects(findListener);
    }

    /**
     * 验证邮箱
     */
    public static void requestEmailVerify(String email, final UpdateListener listener) {
        _User.requestEmailVerify(email, listener);
    }

    /**
     * 找回密码
     */
    public static void resetPasswordByEmail(String email, final UpdateListener listener) {
        _User.resetPasswordByEmail(email, listener);
    }


    /**
     * 注册用户，注册成功后自动登录
     *
     * @param user          ：注册用户
     * @param smsCode：短信验证码
     * @return 包含注册用户的Observable
     */
    public static Observable<_User> signOrLogin(_User user, String smsCode) {
        return user.signOrLoginObservable(_User.class, smsCode);
    }

    /**
     * 使用帐号与密码登录.
     *
     * @param phone 登录用户，包含账号与密码
     * @return
     */
    public static Observable<_User> login(String phone, String password) {
        return BmobUser.loginByAccountObservable(_User.class, phone, password);
    }

    /**
     * 使用帐号与密码登录.
     *
     * @param phone 登录用户，包含账号与密码
     * @return
     */
    public static Observable<_User> signUp(String phone, String password) {
        _User user = new _User();
        user.setUsername(phone);
        if (_User.isEmail(phone)) {
            user.setEmail(phone);
        } else if (_User.isMobileNO(phone)) {
            user.setMobilePhoneNumber(phone);
        }
        user.setPassword(password);
        return user.signUpObservable(_User.class);
    }

    /**
     * 使用短信验证码登录
     *
     * @param phone
     * @param smsCode
     * @return
     */
    public static Observable<_User> loginBySMS(final String phone, final String smsCode) {
        //转换为Observable对象，方便使用RxJava处理
        return Observable.create((final ObservableEmitter<_User> emitter) -> {
            BmobUser.loginBySMSCode(phone, smsCode, new LogInListener<_User>() {
                @Override
                public void done(_User user, BmobException e) {
                    if (e == null) {
                        emitter.onNext(user);
                    } else {
                        emitter.onError(e);
                    }
                }

            });
        });
    }

    /**
     * 请求短信验证码，发送到指定号码的手机上
     *
     * @param phone
     */
    public static void requestSmsCode(String phone) {
        BmobSMS.requestSMSCodeObservable(phone, "one").subscribe();
    }

    /**
     * 更改当前登录用户的密码。
     * 将回调接口转换为Observable为难点
     *
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    public static Observable<Void> updatePassword(final String passwordOld, final String passwordNew) {
        return Observable.create((final ObservableEmitter<Void> subscriber) -> {
            BmobUser.updateCurrentUserPassword(passwordOld, passwordNew, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        subscriber.onNext(null);
                    } else {
                        subscriber.onError(e);
                    }
                }
            });
        });
    }
    //endregion

    //region 查询、修改用户信息
    public interface QueryUserListener {

        void done(_User s, BmobException e);
    }

    /**
     * 用户管理：2.6、查询指定用户信息
     */
    public static void queryUserInfo(String objectId, final QueryUserListener listener) {
        BmobQuery<_User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectId);
        query.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        listener.done(list.get(0), null);
                    } else {
                        listener.done(null, new BmobException(000, "查无此人"));
                    }
                } else {
                    listener.done(null, e);
                }
            }
        });
    }

    public interface UpdateUserAvatar {
        void onUpdateAvatarFail(BmobException e);

        void onUpdateAvatarSuccess(_User avatarUrl);
    }

    public static void updateAvatar(File uri, UpdateUserAvatar updateUserAvatar) {
        final BmobFile file = new BmobFile(uri);
        file.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    _User current_user = UserDao.getCurrentUser();
                    if (current_user == null) {
                        updateUserAvatar.onUpdateAvatarFail(null);
                        return;
                    }
                    current_user.setheadPortrait(file);
                    current_user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                updateUserAvatar.onUpdateAvatarSuccess(current_user);
                            } else {
                                updateUserAvatar.onUpdateAvatarFail(e);
                            }
                        }
                    });
                } else {
                    updateUserAvatar.onUpdateAvatarFail(e);
                }
            }
        });
    }

    /**
     * 将图片上传到 Bmob
     */
    public static void updateAvatar(String uri, UpdateUserAvatar updateUserAvatar) {
        updateAvatar(new File(uri), updateUserAvatar);
    }
    //endregion
}
