package com.patron.listeners;

import java.net.URI;
import java.util.Map;

public interface OnTaskCompletedListener
{
    void onComplete(Map<URI, byte[]> data);
}
