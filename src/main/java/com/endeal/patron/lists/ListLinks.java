/**
 * This class contains a list of all the different links/URLs
 * that the app will use. This is essentially for easily changing
 * the address of where the PHP scripts and database is found if
 * they change places.
 */

package com.endeal.patron.lists;

public class ListLinks
{
	private static final String BASE_URL = "http://emporium.endeal.me/api/v1/";

	public static final String API_ADD_ACCOUNT = BASE_URL + "patrons/";
	public static final String API_REMOVE_ACCOUNT = BASE_URL + "patrons/"; // Append ID of patron to delete
	public static final String API_UPDATE_ACCOUNT = BASE_URL + "patrons/"; // Append ID of patron to update
	public static final String API_RESET_PASSWORD = BASE_URL + "reset/patrons/"; // Append ID of patron to reset
	public static final String API_LOGIN_PATRON = BASE_URL + "login/patrons/"; // Append ID of patron to login
	public static final String API_GET_ITEMS = BASE_URL + "items/"; // Append ID of vendor to get items for
	public static final String API_GET_VENDORS = BASE_URL + "vendors/";
	public static final String API_GET_ORDERS = BASE_URL + "orders/";
	public static final String API_GET_CODES = BASE_URL + "patrons/orders/"; // Append ID of patron to get orders for
	public static final String API_GET_SCAN = BASE_URL + "patrons/orders/scan/"; // Append ID of order to get scan for
	public static final String API_ADD_ORDER = BASE_URL + "orders/";
	public static final String API_ADD_FUNDER = BASE_URL + "funder/patrons/"; // Append ID of patron to add card to
	public static final String API_REMOVE_FUNDER = BASE_URL + "funder/patrons/"; // Append ID of patron to add card to
    public static final String API_ADD_FAVORITE_VENDOR = BASE_URL + "patrons/vendors/"; // Append ID of patron to add vendor to
    public static final String API_ADD_FAVORITE_ITEM = BASE_URL + "patrons/items/"; // Append ID of patron to add item to
    public static final String API_REMOVE_FAVORITE_VENDOR = BASE_URL + "patrons/vendors/"; // Append ID of patron to remove vendor from
    public static final String API_REMOVE_FAVORITE_ITEM = BASE_URL + "patrons/items/"; // Append ID of patron to remove item from
}
