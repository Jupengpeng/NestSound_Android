package com.xilu.wybz.http.cookie;


import com.xilu.wybz.http.cookie.store.CookieStore;
import com.xilu.wybz.http.cookie.store.HasCookieStore;
import com.xilu.wybz.http.utils.Exceptions;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by June on 16/3/10.
 */
public class CookieJarImpl implements CookieJar, HasCookieStore {
    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore) {
        if (cookieStore == null) Exceptions.illegalArgument("cookieStore can not be null.");
        this.cookieStore = cookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.add(url, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.get(url);
    }

    @Override
    public CookieStore getCookieStore() {
        return cookieStore;
    }
}
