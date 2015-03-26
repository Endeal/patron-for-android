package com.patron.system;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
	public static User getUser(JSONObject rawUser) throws JSONException
	{
		String patronId = rawUser.getString("patronId");
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
    JSONArray rawVendors = rawUser.getJSONArray("vendors");
    JSONArray rawItems = rawUser.getJSONArray("items");
		List<Funder> funders = new ArrayList<Funder>();
		for (Card card : Parser.getCards(rawCards))
		{
			funders.add(card);
		}
		for (BankAccount bankAccount : Parser.getBankAccounts(rawBankAccounts))
		{
			funders.add(bankAccount);
		}
        List<String> vendors = new ArrayList<String>();
        for (int i = 0; i < rawVendors.length(); i++)
        {
            JSONObject rawVendor = rawVendors.getJSONObject(i);
            String vendor = rawVendor.getString("vendorId");
            String favorite = rawVendor.getString("favorite");
            if (favorite.equals("1"))
            {
                vendors.add(vendor);
            }
            System.out.println("POOP VENDORS:" + vendors.toString());

            int rawPoints = rawVendor.getInt("points");
            Map<String, Integer> points = Globals.getPoints();
            points.put(vendor, rawPoints);
            Globals.setPoints(points);
            System.out.println("POOP POINTS:" + points.toString());
        }
        List<String> items = new ArrayList<String>();
        for (int i = 0; i < rawItems.length(); i++)
        {
            String item = rawItems.getString(i);
            items.add(item);
        }
		return new User(patronId, firstName, lastName, birthday, balancedId,
			facebookId, twitterId, googlePlusId, funders, vendors, items);
	}

	public static List<Card> getCards(JSONArray rawCards) throws JSONException
	{
		List<Card> cards = new ArrayList<Card>();
		for (int i = 0; i < rawCards.length(); i++)
		{
			JSONObject rawCard = rawCards.getJSONObject(i);
			Card card = Parser.getCard(rawCard);
			cards.add(card);
		}
		return cards;
	}

	public static Card getCard(JSONObject rawCard) throws JSONException
	{
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
		return card;
	}

	public static List<BankAccount> getBankAccounts(JSONArray rawBankAccounts) throws JSONException
	{
		List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
		for (int i = 0; i < rawBankAccounts.length(); i++)
		{
			JSONObject rawBankAccount = rawBankAccounts.getJSONObject(i);
			BankAccount bankAccount = Parser.getBankAccount(rawBankAccount);
			bankAccounts.add(bankAccount);
		}
		return bankAccounts;
	}

	public static BankAccount getBankAccount(JSONObject rawBankAccount) throws JSONException
	{
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
		return bankAccount;
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
        double latitude = 0.0;
        double longitude = 0.0;
        if (rawVendor.getString("latitude") == null)
            latitude = rawVendor.getDouble("latitude");
        if (rawVendor.getString("longitude") == null)
            longitude = rawVendor.getDouble("longitude");
		JSONArray rawStations = rawVendor.getJSONArray("stations");
		JSONArray rawRewardItems = rawVendor.getJSONArray("rewardItems");
		JSONArray rawRewardOptions = rawVendor.getJSONArray("rewardOptions");
		JSONArray rawRewardSupplements = rawVendor.getJSONArray("rewardSupplements");
		List<Station> stations = getStations(rawStations);
		Vendor vendor = new Vendor(vendorId, name, address, city, state,
				zip, phone, null, null, stations, latitude, longitude);

		// 	Get reward items.
		Map<String, Integer> rewardItems = new HashMap<String, Integer>();
		for (int i = 0; i < rawRewardItems.length(); i++)
		{
			JSONObject rawRewardItem = rawRewardItems.getJSONObject(i);
			String itemId = rawRewardItem.getString("itemId");
			int points = rawRewardItem.getInt("points");
			rewardItems.put(itemId, points);
		}
		vendor.setRewardItems(rewardItems);

		return vendor;
	}

	public static List<Station> getStations(JSONArray rawStations) throws JSONException
	{
		List<Station> stations = new ArrayList<Station>();
		for (int i = 0; i < rawStations.length(); i++)
		{
			JSONObject rawStation = rawStations.getJSONObject(i);
			Station station = Parser.getStation(rawStation);
			stations.add(station);
		}
		return stations;
	}

	public static Station getStation(JSONObject rawStation) throws JSONException
	{
		String stationId = rawStation.getString("stationId");
		String name = rawStation.getString("name");
		Station station = new Station(stationId, name);
		return station;
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
		String itemId = rawItem.getString("itemId");
		String name = rawItem.getString("name");
		BigDecimal price = new BigDecimal(rawItem.getString("price"));
		int maxSupplements = rawItem.getInt("maxSupplements");
		int supply = rawItem.getInt("supply");
		JSONArray rawCategories = rawItem.getJSONArray("categories");
		JSONArray rawAttributes = rawItem.getJSONArray("attributes");
		JSONArray rawSupplements = rawItem.getJSONArray("supplements");
		List<Category> categories = Parser.getCategories(rawCategories);
		List<Attribute> attributes = Parser.getAttributes(rawAttributes);
		List<Supplement> supplements = Parser.getSupplements(rawSupplements);

		// Create the item
		Item item = new Item(itemId, name, price, maxSupplements, supply,
				categories, attributes, supplements);
		return item;
	}

	public static List<Category> getCategories(JSONArray rawCategories) throws JSONException
	{
		List<Category> categories = new ArrayList<Category>();
		for (int i = 0; i < rawCategories.length(); i++)
		{
			JSONObject rawCategory = rawCategories.getJSONObject(i);
			Category category = Parser.getCategory(rawCategory);
			boolean hasCategory = false;
			for (int j = 0; j < Globals.getCategories().size(); j++)
			{
				if (Globals.getCategories().get(j).getId().equals(category.getId()))
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
		return categories;
	}

	public static Category getCategory(JSONObject rawCategory) throws JSONException
	{
		String categoryId = rawCategory.getString("categoryId");
		String categoryName = rawCategory.getString("name");
		Category category = new Category(categoryId, categoryName);
		return category;
	}

	public static List<Attribute> getAttributes(JSONArray rawAttributes) throws JSONException
	{
		List<Attribute> attributes = new ArrayList<Attribute>();
		for (int i = 0; i < rawAttributes.length(); i++)
		{
			JSONObject rawAttribute = rawAttributes.getJSONObject(i);
			Attribute attribute = Parser.getAttribute(rawAttribute);
			attributes.add(attribute);
		}
		return attributes;
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

	public static List<Option> getOptions(JSONArray rawOptions) throws JSONException
	{
		List<Option> options = new ArrayList<Option>();
		for (int i = 0; i < rawOptions.length(); i++)
		{
			JSONObject rawOption = rawOptions.getJSONObject(i);
			Option option = Parser.getOption(rawOption);
			options.add(option);
		}
		return options;
	}

	public static Option getOption(JSONObject rawOption) throws JSONException
	{
		String id = rawOption.getString("optionId");
		String name = rawOption.getString("name");
		BigDecimal price = new BigDecimal(rawOption.getString("price"));
		int supply = rawOption.getInt("supply");
		Option option = new Option(id, name, price, supply);
		return option;
	}

	public static List<Supplement> getSupplements(JSONArray rawSupplements) throws JSONException
	{
		List<Supplement> supplements = new ArrayList<Supplement>();
		for (int i = 0; i < rawSupplements.length(); i++)
		{
			JSONObject rawSupplement = rawSupplements.getJSONObject(i);
			Supplement supplement = Parser.getSupplement(rawSupplement);
			supplements.add(supplement);
		}
		return supplements;
	}

	public static Supplement getSupplement(JSONObject rawSupplement) throws JSONException
	{
		String id = rawSupplement.getString("supplementId");
		String name = rawSupplement.getString("name");
		BigDecimal price = new BigDecimal(rawSupplement.getString("price"));
		int supply = rawSupplement.getInt("supply");
		Supplement supplement = new Supplement(id, name, price, supply);
		return supplement;
	}

	public static List<Fragment> getFragments(JSONArray rawFragments) throws JSONException
	{
		List<Fragment> fragments = new ArrayList<Fragment>();
		for (int i = 0; i < rawFragments.length(); i++)
		{
			JSONObject rawFragment = rawFragments.getJSONObject(i);
			Fragment fragment = Parser.getFragment(rawFragment);
			fragments.add(fragment);
		}
		return fragments;
	}

	public static Fragment getFragment(JSONObject rawFragment) throws JSONException
	{
		String fragmentId = rawFragment.getString("fragmentId");
		int quantity = rawFragment.getInt("quantity");

		// Get the item.
		JSONObject rawItem = rawFragment.getJSONObject("item");
		Item item = Parser.getItem(rawItem);

		// Get the selections.
		JSONArray rawSelections = rawFragment.getJSONArray("selections");
		List<Selection> selections = new ArrayList<Selection>();
		if (rawSelections != null && rawSelections.length() > 0)
		{
			for (int i = 0; i < rawSelections.length(); i++)
			{
				JSONObject rawSelection = rawSelections.getJSONObject(i);
				Selection selection = getSelection(rawSelection);
				selections.add(selection);
			}
		}

		// Get the supplements.
		JSONArray rawSupplements = rawFragment.getJSONArray("supplements");
		List<Supplement> supplements = new ArrayList<Supplement>();
		if (rawSupplements != null && rawSupplements.length() > 0)
		{
			for (int i = 0; i < rawSupplements.length(); i++)
			{
				JSONObject rawSupplement = rawSupplements.getJSONObject(i);
				Supplement supplement = getSupplement(rawSupplement);
				supplements.add(supplement);
			}
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
        System.out.println("GO1");
		String orderId = rawOrder.getString("orderId");
        System.out.println("GO2");
		JSONObject rawVendor = rawOrder.getJSONObject("vendor");
        System.out.println("GO3");
		JSONObject rawStation = rawOrder.getJSONObject("station");
        System.out.println("GO4");
		JSONObject rawUser = rawOrder.getJSONObject("patron");
        System.out.println("GO5");
		int status = rawOrder.getInt("status");
        System.out.println("GO6");
		BigDecimal tip = new BigDecimal(rawOrder.getString("tip"));
        System.out.println("GO7");
		String comment = rawOrder.getString("comment");
        System.out.println("GO8");
		List<Object> coupons = null;
        System.out.println("GO9");
		JSONObject rawFunder = rawOrder.getJSONObject("funder");
        System.out.println("GO10");

		// Get the vendor
		Vendor vendor = Parser.getVendor(rawVendor);
        System.out.println("GO11");

		// Get the user
		User patron = Parser.getUser(rawUser);
        System.out.println("GO12");

		// Get the station
		Station station = Parser.getStation(rawStation);
        System.out.println("GO13");

		// Get the funder
		Funder funder = null;
        System.out.println("GO14");
		if (rawFunder.getString("href").toLowerCase().contains("/cards/"))
		{
        System.out.println("GO15");
			funder = Parser.getCard(rawFunder);
        System.out.println("GO16");
		}
		else
		{
        System.out.println("GO17");
			funder = Parser.getBankAccount(rawFunder);
        System.out.println("GO18");
		}
        System.out.println("GO19");

		// Get the fragments.
		JSONArray rawFragments = rawOrder.getJSONArray("fragments");
        System.out.println("GO20");
		List<Fragment> fragments = Parser.getFragments(rawFragments);
        System.out.println("GO21");
		Order order = new Order(orderId, vendor, patron, fragments, Order.getIntStatus(status), station,
			funder, tip, coupons, comment);
        System.out.println("GO22");
		return order;
	}

	public static List<Code> getCodes(JSONArray rawCodes) throws JSONException, ParseException
	{
		List<Code> codes = new ArrayList<Code>();
		for (int i = 0; i < rawCodes.length(); i++)
		{
			JSONObject rawCode = rawCodes.getJSONObject(i);
			Code code = getCode(rawCode);
			codes.add(code);
		}
		return codes;
	}

	public static Code getCode(JSONObject rawCode) throws JSONException, ParseException
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
