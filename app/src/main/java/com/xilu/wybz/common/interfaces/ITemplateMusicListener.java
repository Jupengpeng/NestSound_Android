package com.xilu.wybz.common.interfaces;

import com.xilu.wybz.bean.TemplateBean;

/**
 * Created by Zning on 2015/9/16.
 */
public interface ITemplateMusicListener {
    void onPlayMusic(TemplateBean tb);

    void onStopMusic();

    void onPauseMusic();

    void onResumeMusic();
}
