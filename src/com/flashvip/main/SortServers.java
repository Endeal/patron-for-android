package com.flashvip.main;

import java.util.ArrayList;

public class SortServers
{
	public static ArrayList<Server> getByName(ArrayList<Server> servers, boolean ascending)
	{
		if (servers != null & !servers.isEmpty())
		{
			boolean changed = true;
			while (changed == true)
			{
				changed = false;
				for (int i = 0; i < servers.size(); i++)
				{
					if ( (ascending == true &&
							servers.get(i).getName().compareTo(servers.get(0).getName()) < 0) ||
							(ascending == false &&
							servers.get(i).getName().compareTo(servers.get(0).getName()) > 0) )
					{
						changed = true;
						Server tempServer = servers.get(i);
						servers.remove(i);
						servers.add(0, tempServer);
					}
				}
			}
		}
		return servers;
	}
	
	public static ArrayList<Server> getByAddress(ArrayList<Server> servers, boolean ascending)
	{
		if (servers != null & !servers.isEmpty())
		{
			boolean changed = true;
			while (changed == true)
			{
				changed = false;
				for (int i = 0; i < servers.size(); i++)
				{
					if ( (ascending == true &&
							servers.get(i).getAddress().compareTo(servers.get(0).getAddress()) < 0) ||
							(ascending == false &&
							servers.get(i).getAddress().compareTo(servers.get(0).getAddress()) > 0) )
					{
						changed = true;
						Server tempServer = servers.get(i);
						servers.remove(i);
						servers.add(0, tempServer);
					}
				}
			}
		}
		return servers;
	}
	
	public static ArrayList<Server> getByCity(ArrayList<Server> servers, boolean ascending)
	{
		if (servers != null & !servers.isEmpty())
		{
			boolean changed = true;
			while (changed == true)
			{
				changed = false;
				for (int i = 0; i < servers.size(); i++)
				{
					if ( (ascending == true &&
							servers.get(i).getCity().compareTo(servers.get(0).getCity()) < 0) ||
							(ascending == false &&
							servers.get(i).getCity().compareTo(servers.get(0).getCity()) > 0) )
					{
						changed = true;
						Server tempServer = servers.get(i);
						servers.remove(i);
						servers.add(0, tempServer);
					}
				}
			}
		}
		return servers;
	}
	
	public static ArrayList<Server> getByState(ArrayList<Server> servers, boolean ascending)
	{
		if (servers != null & !servers.isEmpty())
		{
			boolean changed = true;
			while (changed == true)
			{
				changed = false;
				for (int i = 0; i < servers.size(); i++)
				{
					if ( (ascending == true &&
							servers.get(i).getState().compareTo(servers.get(0).getState()) < 0) ||
							(ascending == false &&
							servers.get(i).getState().compareTo(servers.get(0).getState()) > 0) )
					{
						changed = true;
						Server tempServer = servers.get(i);
						servers.remove(i);
						servers.add(0, tempServer);
					}
				}
			}
		}
		return servers;
	}
	
	public static ArrayList<Server> getByZip(ArrayList<Server> servers, boolean ascending)
	{
		if (servers != null & !servers.isEmpty())
		{
			boolean changed = true;
			while (changed == true)
			{
				changed = false;
				for (int i = 0; i < servers.size(); i++)
				{
					if ( (ascending == true &&
							servers.get(i).getZip().compareTo(servers.get(0).getZip()) < 0) ||
							(ascending == false &&
							servers.get(i).getZip().compareTo(servers.get(0).getZip()) > 0) )
					{
						changed = true;
						Server tempServer = servers.get(i);
						servers.remove(i);
						servers.add(0, tempServer);
					}
				}
			}
		}
		return servers;
	}
	
	public static ArrayList<Server> getByPhone(ArrayList<Server> servers, boolean ascending)
	{
		if (servers != null & !servers.isEmpty())
		{
			boolean changed = true;
			while (changed == true)
			{
				changed = false;
				for (int i = 0; i < servers.size(); i++)
				{
					if ( (ascending == true &&
							servers.get(i).getPhone().compareTo(servers.get(0).getPhone()) < 0) ||
							(ascending == false &&
							servers.get(i).getPhone().compareTo(servers.get(0).getPhone()) > 0) )
					{
						changed = true;
						Server tempServer = servers.get(i);
						servers.remove(i);
						servers.add(0, tempServer);
					}
				}
			}
		}
		return servers;
	}
}
