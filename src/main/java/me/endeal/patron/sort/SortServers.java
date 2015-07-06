package me.endeal.patron.sort;

import java.util.ArrayList;

import me.endeal.patron.model.Vendor;

public class SortServers
{
	public static ArrayList<Vendor> getByName(ArrayList<Vendor> servers, boolean ascending)
	{
        /*
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
						Vendor tempServer = servers.get(i);
						servers.remove(i);
						servers.add(0, tempServer);
					}
				}
			}
		}
        */
		return servers;
	}

	public static ArrayList<Vendor> getByAddress(ArrayList<Vendor> servers, boolean ascending)
	{
        /*
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
						Vendor tempServer = servers.get(i);
						servers.remove(i);
						servers.add(0, tempServer);
					}
				}
			}
		}
        */
		return servers;
	}

	public static ArrayList<Vendor> getByCity(ArrayList<Vendor> servers, boolean ascending)
	{
        /*
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
						Vendor tempServer = servers.get(i);
						servers.remove(i);
						servers.add(0, tempServer);
					}
				}
			}
		}
        */
		return servers;
	}

	public static ArrayList<Vendor> getByState(ArrayList<Vendor> servers, boolean ascending)
	{
        /*
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
						Vendor tempServer = servers.get(i);
						servers.remove(i);
						servers.add(0, tempServer);
					}
				}
			}
		}
        */
		return servers;
	}

	public static ArrayList<Vendor> getByZip(ArrayList<Vendor> servers, boolean ascending)
	{
        /*
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
						Vendor tempServer = servers.get(i);
						servers.remove(i);
						servers.add(0, tempServer);
					}
				}
			}
		}
        */
		return servers;
	}

	public static ArrayList<Vendor> getByPhone(ArrayList<Vendor> servers, boolean ascending)
	{
        /*
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
						Vendor tempServer = servers.get(i);
						servers.remove(i);
						servers.add(0, tempServer);
					}
				}
			}
		}
        */
		return servers;
	}
}
