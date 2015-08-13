package com.endeal.patron.model;

import java.lang.StringBuilder;
import java.io.Serializable;
import java.util.List;

public class Nutrition implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int calories;
    private int fat;
    private int protein;
    private int sugar;
    private int sodium;
    private int carbs;
    private int fiber;
    private List<String> ingredients;
    private boolean organic;
    private boolean vegetarian;
    private boolean vegan;
    private boolean kosher;
    private boolean glutenFree;
    private boolean lactoseFree;
    private boolean gmoFree;
    private boolean nutFree;
    private boolean alcoholic;

    public Nutrition(int calories, int fat, int protein, int sugar, int sodium, int carbs, int fiber,
            List<String> ingredients, boolean organic, boolean vegetarian, boolean vegan, boolean kosher,
            boolean glutenFree, boolean lactoseFree, boolean gmoFree, boolean nutFree, boolean alcoholic)
    {
        setCalories(calories);
        setFat(fat);
        setProtein(protein);
        setSugar(sugar);
        setSodium(sodium);
        setCarbs(carbs);
        setFiber(fiber);
        setIngredients(ingredients);
        setOrganic(organic);
        setVegetarian(vegetarian);
        setVegan(vegan);
        setKosher(kosher);
        setGlutenFree(glutenFree);
        setLactoseFree(lactoseFree);
        setGmoFree(gmoFree);
        setNutFree(nutFree);
        setAlcoholic(alcoholic);
    }

    public void setCalories(int calories)
    {
        this.calories = calories;
    }

    public void setFat(int fat)
    {
        this.fat = fat;
    }

    public void setProtein(int protein)
    {
        this.protein = protein;
    }

    public void setSugar(int sugar)
    {
        this.sugar = sugar;
    }

    public void setSodium(int sodium)
    {
        this.sodium = sodium;
    }

    public void setCarbs(int carbs)
    {
        this.carbs = carbs;
    }

    public void setFiber(int fiber)
    {
        this.fiber = fiber;
    }

    public void setIngredients(List<String> ingredients)
    {
        this.ingredients = ingredients;
    }

    public void setOrganic(boolean organic)
    {
        this.organic = organic;
    }

    public void setVegetarian(boolean vegetarian)
    {
        this.vegetarian = vegetarian;
    }

    public void setVegan(boolean vegan)
    {
        this.vegan = vegan;
    }

    public void setKosher(boolean kosher)
    {
        this.kosher = kosher;
    }

    public void setGlutenFree(boolean glutenFree)
    {
        this.glutenFree = glutenFree;
    }

    public void setLactoseFree(boolean lactoseFree)
    {
        this.lactoseFree = lactoseFree;
    }

    public void setGmoFree(boolean gmoFree)
    {
        this.gmoFree = gmoFree;
    }

    public void setNutFree(boolean nutFree)
    {
        this.nutFree = nutFree;
    }

    public void setAlcoholic(boolean alcoholic)
    {
        this.alcoholic = alcoholic;
    }

    public int getCalories()
    {
        return this.calories;
    }

    public int getFat()
    {
        return this.fat;
    }

    public int getProtein()
    {
        return this.protein;
    }

    public int getSugar()
    {
        return this.sugar;
    }

    public int getSodium()
    {
        return this.sodium;
    }

    public int getCarbs()
    {
        return this.carbs;
    }

    public int getFiber()
    {
        return this.fiber;
    }

    public List<String> getIngredients()
    {
        return this.ingredients;
    }

    public boolean getOrganic()
    {
        return this.organic;
    }

    public boolean getVegetarian()
    {
        return this.vegetarian;
    }

    public boolean getVegan()
    {
        return this.vegan;
    }

    public boolean getKosher()
    {
        return this.kosher;
    }

    public boolean getGlutenFree()
    {
        return this.glutenFree;
    }

    public boolean getLactoseFree()
    {
        return this.lactoseFree;
    }

    public boolean getGmoFree()
    {
        return this.gmoFree;
    }

    public boolean getNutFree()
    {
        return this.nutFree;
    }

    public boolean getAlcoholic()
    {
        return this.alcoholic;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Calories: " + getCalories() + "kCal\n");
        builder.append("Fat: " + getFat() + "g\n");
        builder.append("Protein: " + getProtein() + "g\n");
        builder.append("Sugar: " + getSugar() + "g\n");
        builder.append("Sodium: " + getSodium() + "mg\n");
        builder.append("Carbohydrates: " + getCarbs() + "g\n");
        builder.append("Fiber: " + getFiber() + "g\n");

        String organic = "No";
        String vegetarian = "No";
        String vegan = "No";
        String kosher = "No";
        String glutenFree = "No";
        String nutFree = "No";
        String alcoholic = "No";

        if (getOrganic())
            organic = "Yes";
        if (getVegetarian())
            vegetarian = "Yes";
        if (getVegan())
            vegan = "Yes";
        if (getKosher())
            kosher = "Yes";
        if (getGlutenFree())
            glutenFree = "Yes";
        if (getNutFree())
            nutFree = "Yes";
        if (getAlcoholic())
            alcoholic = "Yes";

        builder.append("Organic: " + organic + "\n");
        builder.append("Vegetarian: " + vegetarian + "\n");
        builder.append("Vegan: " + vegan + "\n");
        builder.append("Kosher: " + kosher + "\n");
        builder.append("Gluten-Free: " + glutenFree + "\n");
        builder.append("Nut-Free: " + nutFree + "\n");
        builder.append("Alcoholic: " + alcoholic + "\n");
        builder.append("Ingredients: ");
        if (getIngredients() == null || getIngredients().size() == 0)
        {
            builder.append("N/A");
        }
        else
        {
            for (int i = 0; i < getIngredients().size(); i++)
            {
                builder.append(getIngredients().get(i));
                if (i < getIngredients().size() - 1)
                    builder.append(", ");
            }
        }
        return builder.toString();
    }
}
