package com.timecat.component.bmob.data.own;

import java.io.Serializable;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-07-12
 * @description null
 * @usage null
 */
public class Statistic implements Serializable {
    private Integer likeSum = 0;//获得的点赞总数
    private Integer commentSum = 0;//评论总数

    private Integer noteSum = 0;//生产的文章总数
    private Integer bookSum = 0;//拥有的笔记本总数
    private Integer sayingSum = 0;//发表的说说总数

    private Integer focusNoteSum = 0;//关注的文章总数
    private Integer focusBookSum = 0;//关注的笔记本总数
    private Integer focusSayingSum = 0;//关注的说说总数

    private Integer followSum = 0;//关注总数
    private Integer followerSum = 0;//粉丝总数

    private Integer level = 0;//等级

    //region static tool
    public static Statistic defaultStatistic() {
        return Statistic.newBuilder()
                        .withCommentSum(0)
                        .withLikeSum(0)

                        .withNoteSum(0)
                        .withSayingSum(0)
                        .withBookSum(0)

                        .withFocusNoteSum(0)
                        .withFocusSayingSum(0)
                        .withFocusBookSum(0)

                        .withFollowerSum(0)
                        .withFollowSum(0)

                        .withLevel(0)
                        .build();
    }

    public void increaseLikeSum() {
        setLikeSum(getLikeSum() + 1);
    }
    public void decreaseLikeSum() {
        setLikeSum(getLikeSum() - 1);
    }

    public void increaseCommentSum() {
        setCommentSum(getCommentSum() + 1);
    }
    public void decreaseCommentSum() {
        setCommentSum(getCommentSum() - 1);
    }

    public void increaseNoteSum() {
        setNoteSum(getNoteSum() + 1);
    }
    public void decreaseNoteSum() {
        setNoteSum(getNoteSum() - 1);
    }
    public void increaseSayingSum() {
        setSayingSum(getSayingSum() + 1);
    }
    public void decreaseSayingSum() {
        setSayingSum(getSayingSum() - 1);
    }
    public void increaseBookSum() {
        setBookSum(getBookSum() + 1);
    }
    public void decreaseBookSum() {
        setBookSum(getBookSum() - 1);
    }

    public void increaseFocusNoteSum() {
        setFocusNoteSum(getFocusNoteSum() + 1);
    }
    public void decreaseFocusNoteSum() {
        setFocusNoteSum(getFocusNoteSum() - 1);
    }
    public void increaseFocusSayingSum() {
        setFocusSayingSum(getFocusSayingSum() + 1);
    }
    public void decreaseFocusSayingSum() {
        setFocusSayingSum(getFocusSayingSum() - 1);
    }
    public void increaseFocusBookSum() {
        setFocusBookSum(getFocusBookSum() + 1);
    }
    public void decreaseFocusBookSum() {
        setFocusBookSum(getFocusBookSum() - 1);
    }

    public void increaseLevelSum() {
        setLevel(getLevel() + 1);
    }
    public void decreaseLevelSum() {
        setLevel(getLevel() - 1);
    }

    public void increaseFollowSum() {
        setFollowSum(getFollowSum() + 1);
    }
    public void decreaseFollowSum() {
        setFollowSum(getFollowSum() - 1);
    }
    public void increaseFollowerSum() {
        setFollowerSum(getFollowerSum() + 1);
    }
    public void decreaseFollowerSum() {
        setFollowerSum(getFollowerSum() - 1);
    }

    //endregion

    //region setter and getter
    public Integer getLikeSum() {
        return likeSum;
    }

    public void setLikeSum(Integer likeSum) {
        this.likeSum = likeSum;
    }

    public Integer getNoteSum() {
        return noteSum;
    }

    public void setNoteSum(Integer noteSum) {
        this.noteSum = noteSum;
    }

    public Integer getCommentSum() {
        return commentSum;
    }

    public void setCommentSum(Integer commentSum) {
        this.commentSum = commentSum;
    }

    public Integer getSayingSum() {
        return sayingSum;
    }

    public void setSayingSum(Integer sayingSum) {
        this.sayingSum = sayingSum;
    }

    public Integer getFollowSum() {
        return followSum;
    }

    public void setFollowSum(Integer followSum) {
        this.followSum = followSum;
    }

    public Integer getFollowerSum() {
        return followerSum;
    }

    public void setFollowerSum(Integer followerSum) {
        this.followerSum = followerSum;
    }

    public Integer getLevel() {
        level = likeSum + commentSum;
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getBookSum() {
        return bookSum;
    }

    public void setBookSum(Integer bookSum) {
        this.bookSum = bookSum;
    }

    public Integer getFocusNoteSum() {
        return focusNoteSum;
    }

    public void setFocusNoteSum(Integer focusNoteSum) {
        this.focusNoteSum = focusNoteSum;
    }

    public Integer getFocusBookSum() {
        return focusBookSum;
    }

    public void setFocusBookSum(Integer focusBookSum) {
        this.focusBookSum = focusBookSum;
    }

    public Integer getFocusSayingSum() {
        return focusSayingSum;
    }

    public void setFocusSayingSum(Integer focusSayingSum) {
        this.focusSayingSum = focusSayingSum;
    }
    //endregion

    private Statistic(Builder builder) {
        setLikeSum(builder.likeSum);
        setCommentSum(builder.commentSum);
        setNoteSum(builder.noteSum);
        setBookSum(builder.bookSum);
        setSayingSum(builder.sayingSum);
        setFocusNoteSum(builder.focusNoteSum);
        setFocusBookSum(builder.focusBookSum);
        setFocusSayingSum(builder.focusSayingSum);
        setFollowSum(builder.followSum);
        setFollowerSum(builder.followerSum);
        setLevel(builder.level);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Statistic copy) {
        Builder builder = new Builder();
        builder.likeSum = copy.getLikeSum();
        builder.commentSum = copy.getCommentSum();
        builder.noteSum = copy.getNoteSum();
        builder.bookSum = copy.getBookSum();
        builder.sayingSum = copy.getSayingSum();
        builder.focusNoteSum = copy.getFocusNoteSum();
        builder.focusBookSum = copy.getFocusBookSum();
        builder.focusSayingSum = copy.getFocusSayingSum();
        builder.followSum = copy.getFollowSum();
        builder.followerSum = copy.getFollowerSum();
        builder.level = copy.getLevel();
        return builder;
    }


    public static final class Builder {
        private Integer likeSum;
        private Integer noteSum;
        private Integer bookSum;
        private Integer commentSum;
        private Integer sayingSum;
        private Integer focusNoteSum;
        private Integer focusBookSum;
        private Integer focusSayingSum;
        private Integer followSum;
        private Integer followerSum;
        private Integer level;

        private Builder() {}

        public Builder withLikeSum(Integer val) {
            likeSum = val;
            return this;
        }

        public Builder withNoteSum(Integer val) {
            noteSum = val;
            return this;
        }

        public Builder withBookSum(Integer val) {
            bookSum = val;
            return this;
        }

        public Builder withCommentSum(Integer val) {
            commentSum = val;
            return this;
        }

        public Builder withSayingSum(Integer val) {
            sayingSum = val;
            return this;
        }

        public Builder withFocusNoteSum(Integer val) {
            focusNoteSum = val;
            return this;
        }

        public Builder withFocusBookSum(Integer val) {
            focusBookSum = val;
            return this;
        }

        public Builder withFocusSayingSum(Integer val) {
            focusSayingSum = val;
            return this;
        }

        public Builder withFollowSum(Integer val) {
            followSum = val;
            return this;
        }

        public Builder withFollowerSum(Integer val) {
            followerSum = val;
            return this;
        }

        public Builder withLevel(Integer val) {
            level = val;
            return this;
        }

        public Statistic build() {
            return new Statistic(this);
        }
    }
    //endregion
}
