package com.timecat.self.data.user_user

import androidx.annotation.IntDef

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-09
 * @description null
 * @usage null
 */
@IntDef(
    User2User_Follow,
    User2User_Like,
    User2User_Ding,
    User2User_Score
)
@Retention(AnnotationRetention.SOURCE)
annotation class User2UserType

const val User2User_Follow: Int = 0 //关注
const val User2User_Like: Int = 1 //点赞
const val User2User_Ding: Int = 2 //拍了拍
const val User2User_Score: Int = 3 //评分