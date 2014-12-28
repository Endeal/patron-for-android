package com.patron.listeners;

import java.net.URI;
import java.util.Map;

import org.apache.http.HttpEntity;

public interface OnTaskCompletedListener
{
    void onComplete(Map<URI, HttpEntity> data);
}
