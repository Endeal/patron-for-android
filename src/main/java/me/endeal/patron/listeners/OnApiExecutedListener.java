package me.endeal.patron.listeners;

import me.endeal.patron.model.ApiResult;

public interface OnApiExecutedListener
{
    void onExecuted(ApiResult result);
}
