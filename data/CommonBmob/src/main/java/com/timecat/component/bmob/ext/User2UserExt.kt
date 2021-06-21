package com.timecat.component.bmob.ext

import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.User2User
import com.timecat.identity.data.user_user.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/7
 * @description null
 * @usage null
 */
fun relation(source: _User, target: _User, @User2UserType type: Int) = User2User(source, target, type, "", 0)

infix fun _User.like(target: _User): User2User = relation(this, target, User2User_Like)
infix fun _User.follow(target: _User): User2User = relation(this, target, User2User_Follow)
infix fun _User.ding(target: _User): User2User = relation(this, target, User2User_Ding)
infix fun _User.score(target: _User): User2User = relation(this, target, User2User_Score)

