package com.patron.system;

public interface Loadable
{
	void beginLoading();
	void load();
	void endLoading(List<String> results);
	void update();
	void message(String msg);
}
