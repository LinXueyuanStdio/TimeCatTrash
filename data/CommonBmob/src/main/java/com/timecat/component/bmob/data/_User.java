package com.timecat.component.bmob.data;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.timecat.component.bmob.data.own.Asset;
import com.timecat.component.bmob.data.own.MiaoKey;

import java.io.Serializable;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by yc on 2018/2/2.
 */

public class _User extends BmobUser implements Serializable {

    //个性
    private BmobFile headPortrait;//头像
    private BmobFile coverPage;//背景图
    private String brief_intro;//个性签名
    private String nickName;

    //个人信息
    private Integer gender;
    private String province;

    //IM
    private String token;
    private String code;

    //社交、价值
    private Asset asset;

    public _User() {
        super();
    }

    public _User(String objId) {
        this();
        setObjectId(objId);
    }

    //region getter and setter
    public BmobFile getHeadPortrait() {
        return headPortrait;
    }

    public void setheadPortrait(BmobFile headPortrait) {
        this.headPortrait = headPortrait;
    }

    public BmobFile getCoverPage() {
        return coverPage;
    }

    public void setCoverPage(BmobFile coverPage) {
        this.coverPage = coverPage;
    }

    public void setBrief_intro(String brief_intro) {
        this.brief_intro = brief_intro;
    }

    @NonNull
    public String getBrief_intro() {
        return this.brief_intro == null ? "" : this.brief_intro;
    }

    @NonNull
    public String getNickName() {
        return nickName == null ? "薛定谔的喵" : nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNick() {
        return getNickName();
    }

    public String getUserid() {
        return getObjectId();
    }

    @NonNull
    public String getAvatar() {
        if (headPortrait == null) {
            return "http://bmob-cdn-22390.b0.upaiyun.com/2019/04/17/531db7e5405a08ae8061b800ae8ad3b6.jpg";
        }
        return headPortrait.getFileUrl();
    }

    public void setAvatar(String avatar) {
        headPortrait = new BmobFile("avatar", "", avatar);
    }

    @NonNull
    public String getCover() {
        if (coverPage == null) {
            return "http://d.hiphotos.baidu.com/zhidao/pic/item/bf096b63f6246b601ffeb44be9f81a4c510fa218.jpg";
        }
        return coverPage.getFileUrl();
    }

    public void setCover(String cover) {
        headPortrait = new BmobFile("cover", "", cover);
    }

    @NonNull
    public Asset getAsset() {
        if (asset == null) {
            asset = new Asset();
        }
        return asset;
    }

    public void setHeadPortrait(BmobFile headPortrait) {
        this.headPortrait = headPortrait;
    }

    public Integer getGender() {
        if (gender == null) {
            return 0;
        }
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    @NonNull
    public String getNickname() {
        return getNick();
    }

    @NonNull
    public String getSignature() {
        return getBrief_intro();
    }

    public void setNickname(@NonNull String nickname) {
        setNickName(nickname);
    }

    public void setSignature(@NonNull String signature) {
        setBrief_intro(signature);
    }

    public String getId() {
        return getObjectId();
    }
    //endregion

    @Override
    @NonNull
    public String toString() {
        return "_User{" +
                "objId=" + getObjectId() +
                ", headPortrait=" + headPortrait +
                ", coverPage=" + coverPage +
                ", email=" + getEmail() +
                ", email verify=" + getEmailVerified() +
                ", phone number=" + getMobilePhoneNumber() +
                ", brief_intro='" + brief_intro + '\'' +
                ", asset=" + asset +
                '}';
    }

    public void useMiaoKey(MiaoKey miaoKey) {
        if (asset == null) {
            asset = new Asset();
        }
        asset.useMiaoKey(miaoKey);
    }

    //region static util

    /**
     * 验证手机号
     */
    public static boolean isMobileNO(String mobileNums) {
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums)) {
            return false;
        } else {
            return mobileNums.matches(telRegex);
        }
    }

    /**
     * 判断email格式是否正确
     */
    public static boolean isEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);

        return p.matcher(email).matches();
    }
    //endregion

    //region builder

    //endregion
}
