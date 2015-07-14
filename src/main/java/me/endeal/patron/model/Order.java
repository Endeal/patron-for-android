/**
 * @author James Whiteman
 *
 * Device ID:
 * The device ID is the APID generated by Urban Airship.
 * It uniquely identifies the device running the application.
 *
 * Device Type:
 * The device type specifies what kind of device is placing
 * the order. This information is needed by Urban Airship.
 * 0 = iPhone
 * 1 = Android
 * 2 = Windows 8
 * 3 = Windows Mobile
 * 4 = Blackberry
 *
 */

package me.endeal.patron.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.math.RoundingMode;
import java.lang.Exception;

import me.endeal.patron.system.Globals;
import me.endeal.patron.model.Station;
import me.endeal.patron.model.Card;
import me.endeal.patron.model.Funder;
import me.endeal.patron.model.Vendor;

import com.google.gson.Gson;

public class Order implements Serializable
{
    private static final long serialVersionUID = 1L;

	public enum Status
	{
		WAITING, READY, SCANNED, COMPLETED, REJECTED
	}

	private String id;
	private List<Fragment> fragments;
	private List<Voucher> vouchers;
	private Price tip;
	private String comment;
    private Retrieval retrieval;
    private long time;
	private Status status;
	private Funder funder;
	private Vendor vendor;
    private String code;

    public Order(String id, List<Fragment> fragments, List<Voucher> vouchers, Price tip, String comment,
            Retrieval retrieval, long time, Status status, Funder funder, Vendor vendor, String code)
    {
        setId(id);
        setFragments(fragments);
        setVouchers(vouchers);
        setTip(tip);
        setComment(comment);
        setRetrieval(retrieval);
        setTime(time);
        setStatus(status);
        setFunder(funder);
        setVendor(vendor);
        setCode(code);
    }

	// Main Methods
	public Price getTotalPrice()
	{
        Price total = new Price(0, "USD");
        if (fragments != null)
		for (int i = 0; i < fragments.size(); i++)
		{
			total.add(fragments.get(i).getPrice());
		}
		total.add(getTax());
		total.add(getTip());
		return total;
	}

    public Price getPrice()
    {
        Price total = new Price(0, "USD");
        if (fragments != null)
		for (int i = 0; i < fragments.size(); i++)
		{
			total.add(fragments.get(i).getPrice());
		}
		return total;
    }

    public Price getTax()
    {
        Price total = new Price(0, "USD");
        if (fragments != null)
		for (int i = 0; i < fragments.size(); i++)
		{
			total.add(fragments.get(i).getPrice());
		}
        total.multiply(Globals.getVendor().getTaxRate());
        return total;
    }

    public BigDecimal getTipPercent()
    {
        BigDecimal total = new BigDecimal(0);
        if (fragments != null)
        for (int i = 0; i < fragments.size(); i++)
        {
            total = total.add(new BigDecimal(fragments.get(i).getPrice().getValue() / 100));
        }
        if  (tip.getValue() > 0)
        {
            BigDecimal tipPercent = new BigDecimal(tip.getValue() / 100);
            return tipPercent.divide(total, 2, BigDecimal.ROUND_HALF_EVEN);
        }
        return new BigDecimal(0);
    }

	public static Status getIntStatus(int i)
	{
		switch (i)
		{
		case 0:
			return Status.WAITING;
		case 1:
			return Status.READY;
		case 2:
			return Status.SCANNED;
		case 3:
			return Status.COMPLETED;
		default:
			return Status.REJECTED;
		}
	}

	public static int getStatusInt(Status status)
	{
		switch (status)
		{
		case WAITING:
			return 0;
		case READY:
			return 1;
		case SCANNED:
			return 2;
		case COMPLETED:
			return 3;
		default:
			return 4;
		}
	}

	public static String getStatusText(Status status)
	{
		switch (status)
		{
		case WAITING:
			return "Waiting";
		case READY:
			return "Ready";
		case SCANNED:
			return "Scanned";
		case COMPLETED:
			return "Completed";
		default:
			return "Rejected";
		}
	}

	public String getOrderText()
	{
		String s = "";
		for (int i = 0; i < fragments.size(); i++)
		{
			if (i > 0)
			{
				s = s + "\n";
			}
			Fragment fragment = fragments.get(i);
			int quantity = fragments.get(i).getQuantity();
			String name;
			if (fragments.get(i).getItem() != null &&
					fragments.get(i).getItem().getName() != null)
			{
				name = fragments.get(i).getItem().getName();
			}
			else
			{
				name = "";
			}
			s = s + quantity  +
					"\u0009" +
					name;
			for (int j = 0; j < fragment.getSelections().size(); j++)
			{
				if (j == 0)
				{
					s = s + " with ";
				}
				else
				{
					s = s + ", ";
				}
				String optionName = fragment.getSelections().get(j).getOption().getName();
				s = s + optionName;
			}
		}
		return s;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setFragments(List<Fragment> fragments)
	{
		this.fragments = fragments;
	}

	public void setVouchers(List<Voucher> vouchers)
	{
        this.vouchers = vouchers;
	}

	public void setTip(Price tip)
	{
		this.tip = tip;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

    public void setRetrieval(Retrieval retrieval)
    {
        this.retrieval = retrieval;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

	public void setStatus(Status status)
	{
		this.status = status;
	}

	public void setFunder(Funder funder)
	{
		this.funder = funder;
	}

	public void setVendor(Vendor vendor)
	{
		this.vendor = vendor;
	}

    public void setCode(String code)
    {
        this.code = code;
    }

	// Getters
	public String getId()
	{
		return this.id;
	}

	public List<Fragment> getFragments()
	{
		return this.fragments;
	}

    public List<Voucher> getVouchers()
    {
        return this.vouchers;
    }

	public Price getTip()
	{
		return this.tip;
	}

	public String getComment()
	{
		return this.comment;
	}

    public Retrieval getRetrieval()
    {
        return this.retrieval;
    }

    public long getTime()
    {
        return this.time;
    }

	public Status getStatus()
	{
		return this.status;
	}

	public Funder getFunder()
	{
		return this.funder;
	}

	public Vendor getVendor()
	{
		return this.vendor;
	}

    public String getCode()
    {
        return this.code;
    }

    @Override
    public String toString()
    {
        String text = "";
        List<Fragment> fragments = getFragments();
        Price sum = new Price(0, "USD");
        for (int i = 0; i < fragments.size(); i++)
        {
            Fragment fragment = fragments.get(i);
            Item item = fragment.getItem();
            String name = item.getName();
            Price price = item.getPrice();
            int quantity = fragment.getQuantity();
            text = text + name + " (" + price.toString() + ")";
            List<Selection> selections = fragment.getSelections();
            if (selections != null && selections.size() > 0)
            for (int j = 0; j < selections.size(); j++)
            {
                Selection selection = selections.get(j);
                Option option = selection.getOption();
                text = text + "\n  " + option.getName() + "(" + option.getPrice().toString() + ")";
            }
            List<Option> supplements = fragment.getOptions();
            if (supplements != null && supplements.size() > 0)
            for (int j = 0; j < supplements.size(); j++)
            {
                Option supplement = supplements.get(j);
                text = text + "\n    " + supplement.getName() + "(" + supplement.getPrice().toString() + ")";
            }
            sum.add(fragment.getPrice());
            text = text + "\n x " + quantity + " = " + fragment.getPrice().toString() + " (" + sum.toString() + ")";
            text = text + "\n\n";
        }
        double taxRate = getVendor().getTaxRate() * 100;
        text = text + "Tax: " + getTax().toString() + " (" + taxRate + "% of " + getPrice().toString() + ")";
        sum.add(getTax());
        text = text + "\n  = " + sum.toString();
        text = text + "\n\nTip: " + getTip().toString();
        sum.add(getTip());
        text = text + "\n  = " + sum.toString();
        text = text + "\n\nTotal: " + getTotalPrice().toString();
        return text;
    }
}
