package com.patron.system;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.patron.model.Attribute;
import com.patron.model.Category;
import com.patron.model.Code;
import com.patron.model.Fragment;
import com.patron.model.Item;
import com.patron.model.Option;
import com.patron.model.Order;
import com.patron.model.Selection;
import com.patron.model.Supplement;
import com.patron.model.User;
import com.patron.model.Vendor;
import com.patron.model.Station;
import com.patron.model.Card;
import com.patron.model.BankAccount;
import com.patron.model.Funder;

public class Parser
{
	public static User getUser(JSONObject rawUser, String password) throws JSONException
	{
		String patronId = rawUser.getString("patronId");
		String email = rawUser.getString("email");
		String firstName = rawUser.getString("firstName");
		String lastName = rawUser.getString("lastName");
		String birthday = rawUser.getString("birthday");
		String balancedId = rawUser.getString("balancedId");
		String facebookId = rawUser.getString("facebookId");
		String twitterId = rawUser.getString("twitterId");
		String googlePlusId = rawUser.getString("googlePlusId");
		JSONObject cardsObject = rawUser.getJSONObject("cards");
		JSONArray rawCards = cardsObject.getJSONArray("cards");
		JSONObject bankAccountsObject = rawUser.getJSONObject("bankAccounts");
		JSONArray rawBankAccounts = bankAccountsObject.getJSONArray("bank_accounts");
		List<Funder> funders = new ArrayList<Funder>();
		for (Card card : getCards(rawCards))
		{
			funders.add(card);
		}
		for (BankAccount bankAccount : getBankAccounts(rawBankAccounts))
		{
			funders.add(bankAccount);
		}
		return new User(patronId, firstName, lastName, email, password, birthday, balancedId,
			facebookId, twitterId, googlePlusId, funders);
	}

	public static List<Card> getCards(JSONArray rawCards) throws JSONException
	{
		List<Card> cards = new ArrayList<Card>();
		for (int i = 0; i < rawCards.length(); i++)
		{
			JSONObject rawCard = rawCards.getJSONObject(i);
			JSONObject rawLinks = rawCard.getJSONObject("links");
			JSONObject rawAddress = rawCard.getJSONObject("address");
			String cardId = rawCard.getString("id");
			String name = rawCard.getString("name");
			String number = rawCard.getString("number");
			String expirationMonth = rawCard.getString("expiration_month");
			String expirationYear = rawCard.getString("expiration_year");
			String type = rawCard.getString("type");
			String bankName = rawCard.getString("bank_name");
			String address = rawAddress.getString("line1");
			String city = rawAddress.getString("city");
			String state = rawAddress.getString("state");
			String postalCode = rawAddress.getString("postal_code");
			String href = rawCard.getString("href");
			String createdAt = rawCard.getString("created_at");
			boolean verified = rawCard.getBoolean("is_verified");
			Card card = new Card(cardId, name, number, expirationMonth, expirationYear, type, bankName,
				address, city, state, postalCode, href, createdAt, verified);
			cards.add(card);
		}
		return cards;
	}

	public static List<BankAccount> getBankAccounts(JSONArray rawBankAccounts) throws JSONException
	{
		List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
		for (int i = 0; i < rawBankAccounts.length(); i++)
		{
			JSONObject rawBankAccount = rawBankAccounts.getJSONObject(i);
			JSONObject rawLinks = rawBankAccount.getJSONObject("links");
			JSONObject rawAddress = rawBankAccount.getJSONObject("address");
			String bankAccountId = rawBankAccount.getString("id");
			String name = rawBankAccount.getString("name");
			String bankName = rawBankAccount.getString("bank_name");
			String number = rawBankAccount.getString("account_number");
			String type = rawBankAccount.getString("account_type");
			String routing = rawBankAccount.getString("routing_number");
			String address = rawAddress.getString("line1");
			String city = rawAddress.getString("city");
			String state = rawAddress.getString("state");
			String postalCode = rawAddress.getString("postal_code");
			String href = rawBankAccount.getString("href");
			String createdAt = rawBankAccount.getString("created_at");
			boolean creditable = rawBankAccount.getBoolean("can_credit");
			boolean debitable = rawBankAccount.getBoolean("can_debit");
			BankAccount bankAccount = new BankAccount(bankAccountId, name, bankName, number, type, routing,
				address, city, state, postalCode, href, createdAt, creditable, debitable);
			bankAccounts.add(bankAccount);
		}
		return bankAccounts;
	}

	public static List<Vendor> getVendors(JSONArray rawVendors)
	{
		ArrayList<Vendor> vendors = new ArrayList<Vendor>();
		try
		{
			for (int i = 0; i < rawVendors.length(); i++)
			{
				JSONObject rawVendor = rawVendors.getJSONObject(i);
				Vendor vendor = getVendor(rawVendor);
				vendors.add(vendor);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return vendors;
	}
	
	public static Vendor getVendor(JSONObject rawVendor) throws JSONException
	{
		String vendorId = rawVendor.getString("vendorId");
		String name = rawVendor.getString("name");
		String address = rawVendor.getString("address");
		String city = rawVendor.getString("city");
		String state = rawVendor.getString("state");
		String zip = rawVendor.getString("zip");
		String phone = rawVendor.getString("phone");
		JSONArray rawStations = rawVendor.getJSONArray("stations");
		List<Station> stations = getStations(rawStations);
		Vendor vendor = new Vendor(vendorId, name, address, city, state,
				zip, phone, null, null, stations);
		return vendor;
	}

	public static List<Station> getStations(JSONArray rawStations) throws JSONException
	{
		List<Station> stations = new ArrayList<Station>();
		for (int i = 0; i < rawStations.length(); i++)
		{
			JSONObject rawStation = rawStations.getJSONObject(i);
			String stationId = rawStation.getString("stationId");
			String name = rawStation.getString("name");
			Station station = new Station(stationId, name);
			stations.add(station);
		}
		return stations;
	}

	public static List<Item> getItems(JSONArray rawItems)
	{
		ArrayList<Item> items = new ArrayList<Item>();
		try
		{
			for (int i = 0; i < rawItems.length(); i++)
			{
				JSONObject rawItem = rawItems.getJSONObject(i);
				Item item = getItem(rawItem);
				items.add(item);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return items;
	}
	
	public static Item getItem(JSONObject rawItem) throws JSONException
	{
		String supply = rawItem.getString("supply");
		String unlimited = rawItem.getString("unlimited");
		rawItem = rawItem.getJSONObject("item");
		String itemId = rawItem.getString("itemId");
		String name = rawItem.getString("name");
		BigDecimal price = new BigDecimal(rawItem.getString("price"));
		int maxSupplements = rawItem.getInt("maxSupplements");
		JSONArray rawCategories = rawItem.getJSONArray("categories");
		JSONArray rawAttributes = rawItem.getJSONArray("attributes");
		JSONArray rawSupplements = rawItem.getJSONArray("supplements");

		// Add the categories.
		ArrayList<Category> categories = new ArrayList<Category>();
		if (rawCategories != null)
		for (int j = 0; j < rawCategories.length(); j++)
		{
			JSONObject rawCategory = rawCategories.getJSONObject(j);
			Category category = getCategory(rawCategory);
			boolean hasCategory = false;
			for (int k = 0; k < Globals.getCategories().size(); k++)
			{
				if (Globals.getCategories().get(k).getCategoryId().
						equals(category.getCategoryId()))
					hasCategory = true;
			}
			if (!hasCategory)
			{
				List<Category> newCategories = Globals.getCategories();
				newCategories.add(category);
				Globals.setCategories(newCategories);
			}
			categories.add(category);
		}

		// Add the attributes.
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		if (rawAttributes != null)
		for (int j = 0; j < rawAttributes.length(); j++)
		{
			JSONObject rawAttribute = rawAttributes.getJSONObject(j);
			Attribute attribute = getAttribute(rawAttribute);
			attributes.add(attribute);
		}

		// Add the supplements.
		ArrayList<Supplement> supplements = new ArrayList<Supplement>();
		if (rawSupplements != null)
		for (int j = 0; j < rawSupplements.length(); j++)
		{
			JSONObject rawSupplement = rawSupplements.getJSONObject(j);
			Supplement supplement = getSupplement(rawSupplement);
			supplements.add(supplement);
		}

		// Create the item
		Item item = new Item(itemId, name, price, maxSupplements,
				categories, attributes, supplements);
		return item;
	}
	
	public static Category getCategory(JSONObject rawCategory) throws JSONException
	{
		String categoryId = rawCategory.getString("categoryId");
		String categoryName = rawCategory.getString("name");
		Category category = new Category(categoryId, categoryName);
		return category;
	}
	
	public static Attribute getAttribute(JSONObject rawAttribute) throws JSONException
	{
		JSONArray rawOptions = rawAttribute.getJSONArray("options");
		String attributeId = rawAttribute.getString("attributeId");
		String attributeName = rawAttribute.getString("name");

		// Add the options to the attribute.
		ArrayList<Option> options = new ArrayList<Option>();
		for (int k = 0; k < rawOptions.length(); k++)
		{
			JSONObject rawOption = rawOptions.getJSONObject(k);
			Option option = getOption(rawOption);
			options.add(option);
		}
		Attribute attribute = new Attribute(attributeId, attributeName, options);
		return attribute;
	}
	
	public static Option getOption(JSONObject rawOption) throws JSONException
	{
		String optionId = rawOption.getString("optionId");
		String optionName = rawOption.getString("name");
		BigDecimal optionPrice = new BigDecimal(rawOption.getString("price"));
		Option option = new Option(optionId, optionName, optionPrice);
		return option;
	}
	
	public static Supplement getSupplement(JSONObject rawSupplement) throws JSONException
	{
		String supplementId = rawSupplement.getString("supplementId");
		String supplementName = rawSupplement.getString("name");
		BigDecimal supplementPrice = new BigDecimal(rawSupplement.getString("price"));
		Supplement supplement = new Supplement(supplementId, supplementName, supplementPrice);
		return supplement;
	}
	
	public static Fragment getFragment(JSONObject rawFragment) throws JSONException
	{
		String fragmentId = rawFragment.getString("fragmentId");
		int quantity = rawFragment.getInt("quantity");
		
		// Get the item.
		JSONObject rawItem = rawFragment.getJSONObject("item");
		Item item = getItem(rawItem);
		
		// Get the selections.
		JSONArray rawSelections = rawFragment.getJSONArray("selections");
		List<Selection> selections = new ArrayList<Selection>();
		for (int i = 0; i < rawSelections.length(); i++)
		{
			JSONObject rawSelection = rawSelections.getJSONObject(i);
			Selection selection = getSelection(rawSelection);
			selections.add(selection);
		}
		
		// Get the supplements.
		JSONArray rawSupplements = rawFragment.getJSONArray("supplements");
		List<Supplement> supplements = new ArrayList<Supplement>();
		for (int i = 0; i < rawSupplements.length(); i++)
		{
			JSONObject rawSupplement = rawSupplements.getJSONObject(i);
			Supplement supplement = getSupplement(rawSupplement);
			supplements.add(supplement);
		}
		
		Fragment fragment = new Fragment(fragmentId, item, selections, supplements, quantity);
		return fragment;
	}
	
	public static Selection getSelection(JSONObject rawSelection) throws JSONException
	{
		JSONObject rawAttribute = rawSelection.getJSONObject("attribute");
		JSONObject rawOption = rawSelection.getJSONObject("option");
		Attribute attribute = getAttribute(rawAttribute);
		Option option = getOption(rawOption);
		Selection selection = new Selection(attribute, option);
		return selection;
	}
	
	public static Order getOrder(JSONObject rawOrder) throws JSONException
	{
		String orderId = rawOrder.getString("orderId");
		String vendorId = rawOrder.getString("vendorId");
		int status = rawOrder.getInt("status");
		String stationId = rawOrder.getString("stationId");
		BigDecimal tip = new BigDecimal(rawOrder.getString("tip"));
		String comment = rawOrder.getString("comment");
		List<Object> coupons = null;
		String funderId = rawOrder.getString("funderId");

		// Get the objects from the data.
		Station station = null;
		for (Station vendorStation : Globals.getVendor().getStations())
		{
			if (vendorStation.getId().equals(stationId))
			{
				station = vendorStation;
			}
		}
		Funder funder = null;
		for (Funder userFunder : Globals.getUser().getFunders())
		{
			if (userFunder.getId().equals(funderId))
			{
				funder = userFunder;
			}
		}
		
		// Get the fragments.
		List<Fragment> fragments = new ArrayList<Fragment>();
		JSONArray rawFragments = rawOrder.getJSONArray("fragments");
		for (int i = 0; i < rawFragments.length(); i++)
		{
			JSONObject rawFragment = rawFragments.getJSONObject(i);
			Fragment fragment = getFragment(rawFragment);
			fragments.add(fragment);
		}

		Order order = new Order(orderId, vendorId, fragments, Order.getIntStatus(status), station,
			funder, tip, coupons, comment);
		return order;
	}

	public static Station getStation(JSONObject rawStation) throws JSONException
	{
		String stationId = rawStation.getString("stationId");
		String name = rawStation.getString("name");
		return new Station(stationId, name);
	}
	
	public static Code getCode(JSONObject rawCode)throws JSONException, ParseException
	{
		String timeReceived = rawCode.getString("timestamp");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
		Date parsedDate = dateFormat.parse(timeReceived);
		Timestamp parsedTimestamp = new Timestamp(parsedDate.getTime());
		long timestamp = parsedTimestamp.getTime();
		
		JSONObject rawOrder = rawCode.getJSONObject("order");
		Order order = getOrder(rawOrder);
		Code code = new Code(timestamp, order);
		return code;
	}
}
