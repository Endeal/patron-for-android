/**
 * This class contains a list of all the different links/URLs
 * that the app will use. This is essentially for easily changing
 * the address of where the PHP scripts and database is found if
 * they change places.
 */

package me.endeal.patron.lists;

public class ListLinks
{
	private static final String BASE_URL = "http://emporium.elasticbeanstalk.com/v1/patron/";

	public static final String API_ADD_ACCOUNT = BASE_URL + "account/addPatron.php";
	public static final String API_REMOVE_ACCOUNT = BASE_URL + "account/removePatron.php";
	public static final String API_UPDATE_ACCOUNT = BASE_URL + "account/updatePatron.php";
	public static final String API_RESET_PASSWORD = BASE_URL + "account/requestPassword.php";
	public static final String API_LOGIN_PATRON = BASE_URL + "account/login.php";
	public static final String API_GET_ITEMS = BASE_URL + "get/getItems.php";
	public static final String API_GET_VENDORS = BASE_URL + "get/getVendors.php";
	public static final String API_GET_CATEGORIES = BASE_URL + "get/getCategories.php";
	public static final String API_GET_CODES = BASE_URL + "get/getCodes.php";
	public static final String API_GET_SCAN = BASE_URL + "get/getCodeImage.php";
	public static final String API_ADD_ORDER = BASE_URL + "add/addOrder.php";
	public static final String API_ADD_CARD = BASE_URL + "add/addCard.php";
    public static final String API_ADD_FAVORITE_VENDOR = BASE_URL + "add/addFavoriteVendor.php";
    public static final String API_ADD_FAVORITE_ITEM = BASE_URL + "add/addFavoriteItem.php";
	public static final String API_REMOVE_CARD = BASE_URL + "remove/removeCard.php";
    public static final String API_REMOVE_FAVORITE_VENDOR = BASE_URL + "remove/removeFavoriteVendor.php";
    public static final String API_REMOVE_FAVORITE_ITEM = BASE_URL + "remove/removeFavoriteItem.php";
}
