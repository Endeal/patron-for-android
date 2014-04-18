package com.flashvip.system;

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

import com.flashvip.model.Attribute;
import com.flashvip.model.Category;
import com.flashvip.model.Code;
import com.flashvip.model.Fragment;
import com.flashvip.model.Item;
import com.flashvip.model.Option;
import com.flashvip.model.Order;
import com.flashvip.model.Selection;
import com.flashvip.model.Supplement;
import com.flashvip.model.Vendor;

public class Parser
{
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
		Vendor vendor = new Vendor(vendorId, name, address, city, state,
				zip, phone, null, null);
		return vendor;
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
		
		// Get the fragments.
		List<Fragment> fragments = new ArrayList<Fragment>();
		JSONArray rawFragments = rawOrder.getJSONArray("fragments");
		for (int i = 0; i < rawFragments.length(); i++)
		{
			JSONObject rawFragment = rawFragments.getJSONObject(i);
			Fragment fragment = getFragment(rawFragment);
			fragments.add(fragment);
		}

		Order order = new Order(orderId, vendorId, fragments, Order.getIntStatus(status));
		return order;
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
