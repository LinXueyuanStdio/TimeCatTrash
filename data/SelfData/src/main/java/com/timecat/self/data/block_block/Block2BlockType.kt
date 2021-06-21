package com.timecat.self.data.block_block

import androidx.annotation.IntDef

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-09
 * @description null
 * @usage null
 */
@IntDef(
    Block2Block_Follow,
    Block2Block_Block_has_tag,
    Block2Block_DING,
    Block2Block_SCORE,
    Block2Block_Leaderboard_has_block,
    Block2Block_Role_has_permission
)
@Retention(AnnotationRetention.SOURCE)
annotation class Block2BlockType

const val Block2Block_Follow: Int = 0
const val Block2Block_Block_has_tag: Int = 1
const val Block2Block_DING: Int = 2
const val Block2Block_SCORE: Int = 3
const val Block2Block_Leaderboard_has_block: Int = 4
const val Block2Block_Role_has_permission: Int = 5
const val Block2Block_Identity_has_role: Int = 6