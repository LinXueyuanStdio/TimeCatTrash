package com.timecat.component.bmob.data.msg;

import com.timecat.component.bmob.data._User;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by yc on 2018/3/5.
 * 点赞
 */
public class MessageItLike implements Message.IMessage {
    private _User initiator;
    public void setInitiator(_User initiator) {
        setHe(initiator.getObjectId());
        setHisHead(initiator.getAvatar());
        setHisName(initiator.getNickName());
        this.initiator = initiator;
    }

    public _User getInitiator() {
        if (initiator != null) return initiator;
        _User user = new _User(he);
        user.setNickName(hisName);
        BmobFile bmobFile = new BmobFile("头像", "头像", hisHead);
        user.setHeadPortrait(bmobFile);
        return user;
    }

    private String he; // objectId
    private String hisName;
    private String hisHead;
    private String it; // objectId
    private String itsTitle;
    private String itsHead;

    public MessageItLike(String he, String hisName, String hisHead, String it, String itsTitle, String itsHead) {
        this.he = he;
        this.hisName = hisName;
        this.hisHead = hisHead;
        this.it = it;
        this.itsTitle = itsTitle;
        this.itsHead = itsHead;
    }

    public String getHe() {
        return he;
    }

    public void setHe(String he) {
        this.he = he;
    }

    public String getHisName() {
        return hisName;
    }

    public void setHisName(String hisName) {
        this.hisName = hisName;
    }

    public String getHisHead() {
        return hisHead;
    }

    public void setHisHead(String hisHead) {
        this.hisHead = hisHead;
    }

    public String getIt() {
        return it;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public String getItsTitle() {
        return itsTitle;
    }

    public void setItsTitle(String itsTitle) {
        this.itsTitle = itsTitle;
    }

    public String getItsHead() {
        return itsHead;
    }

    public void setItsHead(String itsHead) {
        this.itsHead = itsHead;
    }

    public static MessageItLike parse(String s) {
        try {
            JSONObject msg = new JSONObject(s);
            return new MessageItLike(msg.optString("he"),
                    msg.optString("hisName"),
                    msg.optString("hisHead"),
                    msg.optString("it"),
                    msg.optString("itsTitle"),
                    msg.optString("itsHead"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @NonNull
    public String toMessageJson() {
        JSONObject msg = new JSONObject();
        try {
            msg.put("he", he);
            msg.put("hisName", hisName);
            msg.put("hisHead", hisHead);
            msg.put("it", it);
            msg.put("itsTitle", itsTitle);
            msg.put("itsHead", itsHead);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msg.toString();
    }

    @Override
    @NonNull
    public Message toMessage() {
        Message message = new Message();
        message.setType(Message.MESSAGE_SAYING_LIKE);
        message.setMessage(this);
        message.setAcceptor(getInitiator());
        return message;
    }
}
