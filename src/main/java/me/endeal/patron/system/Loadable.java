package me.endeal.patron.system;

public interface Loadable
{
	void beginLoading();
	void load();
	void endLoading();
	void update();
	void message(String msg);
}
