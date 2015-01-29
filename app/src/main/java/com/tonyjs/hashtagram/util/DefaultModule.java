package com.tonyjs.hashtagram.util;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.volley.VolleyGlideModule;
import com.bumptech.glide.load.DecodeFormat;

/**
 * Created by tonyjs on 15. 1. 29..
 */
public class DefaultModule extends VolleyGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.ALWAYS_ARGB_8888);
    }
}
