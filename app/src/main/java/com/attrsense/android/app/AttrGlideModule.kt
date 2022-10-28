package com.attrsense.android.app

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

/**
 * @author zhangshuai
 * @date 2022/10/28 10:03
 * @description
 */
@GlideModule
class AttrGlideModule : AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}