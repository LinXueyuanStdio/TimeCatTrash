package com.timecat.component.bmob.data.msg;

import com.timecat.component.bmob.data._User;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;

/**
 * Created by yc on 2018/2/27.
 * 关注
 */

public class MessageFans implements Message.IMessage {

    private _User initiator;
    private String acceptor_id;

    public MessageFans() {
        super();
    }
    public MessageFans(String acceptor_id) {
        this();
        this.acceptor_id = acceptor_id;
    }

    public MessageFans(_User initiator, String acceptor_id) {
        this(acceptor_id);
        this.initiator = initiator;
    }

    public MessageFans(_User initiator, _User acceptor) {
        this(initiator, acceptor.getId());
    }

    public _User getInitiator() {
        return initiator;
    }

    public void setInitiator(_User initiator) {
        this.initiator = initiator;
    }

    public void setAcceptor(_User acceptor) {
        setAcceptor_id(acceptor.getId());
    }

    public String getAcceptor_id() {
        return acceptor_id;
    }

    public void setAcceptor_id(String acceptor_id) {
        this.acceptor_id = acceptor_id;
    }

    public static class Data {
        String he;
        String hisName;
        String hisHead;
        String hisIntro;

        public Data(String he, String hisName, String hisHead, String hisIntro) {
            this.he = he;
            this.hisName = hisName;
            this.hisHead = hisHead;
            this.hisIntro = hisIntro;
        }

        public String getHe() {
            return he;
        }

        public String getHisName() {
            return hisName;
        }

        public String getHisHead() {
            return hisHead;
        }

        public String getHisIntro() {
            return hisIntro;
        }
    }

    public static Data parse(String s) {
        try {
            JSONObject msg = new JSONObject(s);
            return new Data(msg.optString("he"),
                    msg.optString("hisName"),
                    msg.optString("hisHead"),
                    msg.optString("hisIntro"));
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
            msg.put("he", initiator.getId());
            msg.put("hisName", initiator.getNickName());
            msg.put("hisHead", initiator.getAvatar());
            msg.put("hisIntro", initiator.getBrief_intro());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msg.toString();
    }

    @Override
    @NonNull
    public Message toMessage() {
        Message message = new Message();
        message.setType(Message.MESSAGE_FANS);
        message.setMessage(this);
        message.setAcceptor(new _User(acceptor_id));
        return message;
    }
}
