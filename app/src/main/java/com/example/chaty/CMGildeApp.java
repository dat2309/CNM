package com.example.chaty;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
@GlideModule
public class CMGildeApp extends AppGlideModule{
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}



