/**
 * This class contains a list of all the different links/URLs
 * that the app will use. This is essentially for easily changing
 * the address of where the PHP scripts and database is found if
 * they change places.
 */

package com.flashvip.lists;

public class ListLinks
{
	private static final String BASE_URL = "http://jameswhiteman.info/flash-core/";
	public static final String LINK_GET_ITEMS = BASE_URL + "get/getItems.php";
	public static final String LINK_GET_VENDORS = BASE_URL + "get/getVendors.php";
	public static final String LINK_GET_CATEGORIES = BASE_URL + "get/getCategories.php";
	public static final String LINK_GET_CODES = BASE_URL + "get/getCodes.php";
	public static final String LINK_ADD_ORDER = BASE_URL + "add/addOrder.php";
	public static final String LINK_DIRECTORY_CODES = "http://jameswhiteman.info/";
}