package me.endeal.patron.model;

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

    public Nutrition(int calories, int fat, int protein, int sugar, int sodium, int carbs, int fiber,
            List<String> ingredients, boolean organic, boolean vegetarian, boolean vegan, boolean kosher,
            boolean glutenFree, boolean lactoseFree, boolean gmoFree, boolean nutFree)
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
}
