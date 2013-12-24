package com.flashvip.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.flashvip.main.Globals;

public class Parser
{
	public static Item getItem(JSONObject rawItem) throws JSONException
	{
		String itemId = rawItem.getString("item_id");
		String name = rawItem.getString("name");
		BigDecimal price = new BigDecimal(rawItem.getString("price"));
		int maxSupplements = rawItem.getInt("max_supplements");
		JSONArray rawCategories = rawItem.getJSONArray("categories");
		JSONArray rawAttributes = rawItem.getJSONArray("attributes");
		JSONArray rawSupplements = rawItem.getJSONArray("supplements");

		// Add the categories.
		ArrayList<Category> categories = new ArrayList<Category>();
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
				ArrayList<Category> newCategories = Globals.getCategories();
				newCategories.add(category);
				Globals.setCategories(newCategories);
			}
			categories.add(category);
		}

		// Add the attributes.
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		for (int j = 0; j < rawAttributes.length(); j++)
		{
			JSONObject rawAttribute = rawAttributes.getJSONObject(j);
			Attribute attribute = getAttribute(rawAttribute);
			attributes.add(attribute);
		}

		// Add the supplements.
		ArrayList<Supplement> supplements = new ArrayList<Supplement>();
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
		String categoryId = rawCategory.getString("category_id");
		String categoryName = rawCategory.getString("name");
		Category category = new Category(categoryId, categoryName);
		return category;
	}
	
	public static Attribute getAttribute(JSONObject rawAttribute) throws JSONException
	{
		JSONArray rawOptions = rawAttribute.getJSONArray("options");
		String attributeId = rawAttribute.getString("attribute_id");
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
		String optionId = rawOption.getString("option_id");
		String optionName = rawOption.getString("name");
		BigDecimal optionPrice = new BigDecimal(rawOption.getString("price"));
		Option option = new Option(optionId, optionName, optionPrice);
		return option;
	}
	
	public static Supplement getSupplement(JSONObject rawSupplement) throws JSONException
	{
		String supplementId = rawSupplement.getString("supplement_id");
		String supplementName = rawSupplement.getString("name");
		BigDecimal supplementPrice = new BigDecimal(rawSupplement.getString("price"));
		Supplement supplement = new Supplement(supplementId, supplementName, supplementPrice);
		return supplement;
	}
	
	public static Fragment getFragment(JSONObject rawFragment) throws JSONException
	{
		String fragmentId = rawFragment.getString("fragment_id");
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
	
	public static Order getOrder(JSONObject rawOrder)
	{
		String orderId = rawOrder.getString("order_id");
		String vendorId = rawOrder.getString("vendor_id");
		String status = rawOrder.getString("status");
		
		// Get the fragments.
		List<Fragment> fragments = new ArrayList<Fragment>();
		JSONArray rawFragments = rawOrder.getJSONArray("fragments");
		for (int i = 0; i < rawFragments.length(); i++)
		{
			JSONObject rawFragment = rawFragments.getJSONObject(i);
			Fragment fragment = getFragment(rawFragment);
			fragments.add(fragment);
		}
		
		Order order = new Order(orderId, vendorId, fragments, );
		return order;
	}
}
