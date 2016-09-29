package com.xilu.wybz.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.ActBean;
import com.xilu.wybz.bean.CollectionBean;
import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.bean.FansBean;
import com.xilu.wybz.bean.Lyricat;
import com.xilu.wybz.bean.MainBean;
import com.xilu.wybz.bean.MsgCommentBean;
import com.xilu.wybz.bean.MusicTalk;
import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.bean.SystemBean;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.bean.TokenBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.bean.ZambiaBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseUtils {
    //伴奏
    public static List<TemplateBean> parseTemplateList(Context context, String response) {
        List<TemplateBean> templateList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                templateList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<TemplateBean>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
            return templateList;
        } catch (Exception e) {
            e.printStackTrace();
            return templateList;
        }
    }
    //歌单
    public static List<SongAlbum> getSongAlbumsData(Context context, String response) {
        List<SongAlbum> templateList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                templateList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<SongAlbum>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return templateList;
    }
    //词库
    public static List<Lyricat> getLyricatsData(Context context, String response) {
        List<Lyricat> lyricats = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                lyricats = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<Lyricat>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lyricats;
    }
    //评论列表
    public static List<CommentBean> getCommentsData(Context context, String response) {
        List<CommentBean> commentBeanList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                commentBeanList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<CommentBean>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commentBeanList;
    }
    //评论列表
    public static List<TemplateBean> getTemplatesData(Context context, String response) {
        List<TemplateBean> commentBeanList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                commentBeanList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<TemplateBean>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commentBeanList;
    }
    //消息收藏列表
    public static List<CollectionBean> getFavsData(Context context, String response) {
        List<CollectionBean> commentBeanList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                commentBeanList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<CollectionBean>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commentBeanList;
    }
    //消息收藏列表
    public static List<SystemBean> getSystemsData(Context context, String response) {
        List<SystemBean> commentBeanList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                if(!TextUtils.isEmpty(jsonObject.getString("data")))
                commentBeanList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<SystemBean>>() {}.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commentBeanList;
    }
    //消息点赞列表
    public static List<ZambiaBean> getZambiasData(Context context, String response) {
        List<ZambiaBean> commentBeanList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                commentBeanList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<ZambiaBean>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commentBeanList;
    }
    //消息评论列表
    public static List<MsgCommentBean> getMsgCommentsData(Context context, String response) {
        List<MsgCommentBean> commentBeanList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                commentBeanList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<MsgCommentBean>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commentBeanList;
    }
    //粉丝列表
    public static List<FansBean> getFansData(Context context, String response) {
        List<FansBean> fansBeanList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                fansBeanList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<FansBean>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fansBeanList;
    }
    //作品列表
    public static List<ActBean> getActsData(Context context, String response) {
        List<ActBean> actBeans = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                actBeans = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<ActBean>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actBeans;
    }
    //作品列表
    public static WorksData getWorkData(Context context, String response) {
        WorksData worksDatas = new WorksData();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                worksDatas = new Gson().fromJson(jsonObject.getString("data"), WorksData.class);
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return worksDatas;
    }
    //作品列表
    public static List<WorksData> getWorksData(Context context, String response) {
        List<WorksData> worksDatas = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                worksDatas = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<WorksData>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return worksDatas;
    }
    //乐说列表
    public static List<MusicTalk> getMusicTalksData(Context context, String response) {
        List<MusicTalk> worksDatas = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                worksDatas = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<MusicTalk>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return worksDatas;
    }

    //首页数据
    public static MainBean getMainBean(Context context,String response) {
        MainBean dataBean = new MainBean();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                dataBean = new Gson().fromJson(jsonObject.getString("data"),MainBean.class);
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataBean;
    }

    public static DataBean getDataBean(Context context,String response) {
        DataBean dataBean = new DataBean();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                dataBean = new Gson().fromJson(response,DataBean.class);
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataBean;
    }
    //用户
    public static UserBean getUserBean(Context context, String response) {
        UserBean dataBean = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                dataBean = new Gson().fromJson(jsonObject.getString("data"),UserBean.class);
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataBean;
    }
    //七牛的Token
    public static TokenBean getTokenBean(Context context, String response) {
        TokenBean dataBean = new TokenBean();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                dataBean = new Gson().fromJson(jsonObject.getString("data"),TokenBean.class);
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataBean;
    }
    //获取评论Id
    public static int getCommentId(Context context, String response){
        int id = 0;
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                id = jsonObject.getInt("data");
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
    public static boolean checkCode(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            int code = jsonObject.getInt("code");
            return code == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void showMsg(Context context, String msg) {
        ToastUtils.toastLong(context, msg);
    }


}
