/**
 * This class contains a list of all the different links/URLs
 * that the app will use. This is essentially for easily changing
 * the address of where the PHP scripts and database is found if
 * they change places.
 */

package com.patron.lists;

public class ListLinks
{
	private static final String BASE_URL = "http://flashenv-uscd33pegm.elasticbeanstalk.com/patron/";
	public static final String LINK_ACCOUNT_ADD = BASE_URL + "account/addPatron.php";
	public static final String LINK_ACCOUNT_REMOVE = BASE_URL + "account/removePatron.php";
	public static final String LINK_ACCOUNT_UPDATE = BASE_URL + "account/updatePatron.php";
	public static final String LINK_ACCOUNT_LOGIN = BASE_URL + "account/login.php";
	public static final String LINK_ACCOUNT_PASSWORD = BASE_URL + "account/requestPassword.php";
	public static final String LINK_GET_ITEMS = BASE_URL + "get/getItems.php";
	public static final String LINK_GET_VENDORS = BASE_URL + "get/getVendors.php";
	public static final String LINK_GET_CATEGORIES = BASE_URL + "get/getCategories.php";
	public static final String LINK_GET_CODES = BASE_URL + "get/getCodes.php";
	public static final String LINK_GET_SCAN = BASE_URL + "get/getCodeImage.php";
	public static final String LINK_ADD_ORDER = BASE_URL + "add/addOrder.php";
	public static final String LINK_ADD_CARD = BASE_URL + "add/addCard.php";
	public static final String LINK_ADD_BANK_ACCOUNT = BASE_URL + "add/addBankAccount.php";
	public static final String LINK_REMOVE_CARD = BASE_URL + "remove/removeCard.php";
	public static final String LINK_REMOVE_BANK_ACCOUNT = BASE_URL + "remove/removeBankAccount.php";
	public static final String LINK_DIRECTORY_CODES = "http://jameswhiteman.info/";

	public static final String API_ADD_ACCOUNT = BASE_URL + "account/addPatron.php";
	public static final String API_REMOVE_ACCOUNT = BASE_URL + "account/removePatron.php";
	public static final String API_UPDATE_ACCOUNT = BASE_URL + "account/updatePatron.php";
	public static final String API_LOGIN_PATRON = BASE_URL + "account/login.php";
	public static final String API_REQUEST_PASSWORD = BASE_URL + "account/requestPassword.php";
	public static final String API_GET_ITEMS = BASE_URL + "get/getItems.php";
	public static final String API_GET_VENDORS = BASE_URL + "get/getVendors.php";
	public static final String API_GET_CATEGORIES = BASE_URL + "get/getCategories.php";
	public static final String API_GET_CODES = BASE_URL + "get/getCodes.php";
	public static final String API_GET_SCAN = BASE_URL + "get/getCodeImage.php";
	public static final String API_ADD_ORDER = BASE_URL + "add/addOrder.php";
	public static final String API_ADD_CARD = BASE_URL + "add/addCard.php";
	public static final String API_ADD_BANK_ACCOUNT = BASE_URL + "add/addBankAccount.php";
	public static final String API_REMOVE_CARD = BASE_URL + "remove/removeCard.php";
	public static final String API_REMOVE_BANK_ACCOUNT = BASE_URL + "remove/removeBankAccount.php";
}