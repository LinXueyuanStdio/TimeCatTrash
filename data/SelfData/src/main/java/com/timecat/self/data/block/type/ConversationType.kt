package com.timecat.self.data.block.type

import androidx.annotation.IntDef

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-18
 * @description  BLOCK_CONVERSATION 类型的 subType
 * @usage null
 */

@IntDef(
    BLOCK_Chat,
    BLOCK_GroupChat,
    BLOCK_ChatRoom,
    BLOCK_DiscussionGroup,
    BLOCK_HelpDesk,
    BLOCK_SettingTabs,
    BLOCK_Terminal,
    BLOCK_Github,
    BLOCK_Git,
    BLOCK_Skin,
    BLOCK_Plugin,
    BLOCK_Record
)
@Retention(AnnotationRetention.SOURCE)
annotation class ConversationType

const val BLOCK_Chat: Int = 0
const val BLOCK_GroupChat: Int = 1
const val BLOCK_ChatRoom: Int = 2
const val BLOCK_DiscussionGroup: Int = 3
const val BLOCK_HelpDesk: Int = 4
const val BLOCK_SettingTabs: Int = 5
const val BLOCK_Terminal: Int = 6
const val BLOCK_Github: Int = 7
const val BLOCK_Git: Int = 8
const val BLOCK_Skin: Int = 9
const val BLOCK_Plugin: Int = 10
const val BLOCK_Record: Int = 11
