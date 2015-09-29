package com.endeal.patron.model;

import java.io.Serializable;

import org.joda.time.LocalTime;

public class Hours implements Serializable
{
    private long sundayStart;
    private long sundayEnd;
    private long mondayStart;
    private long mondayEnd;
    private long tuesdayStart;
    private long tuesdayEnd;
    private long wednesdayStart;
    private long wednesdayEnd;
    private long thursdayStart;
    private long thursdayEnd;
    private long fridayStart;
    private long fridayEnd;
    private long saturdayStart;
    private long saturdayEnd;

    public Hours(long sundayStart, long sundayEnd, long mondayStart,
            long mondayEnd, long tuesdayStart, long tuesdayEnd,
            long wednesdayStart, long wednesdayEnd, long thursdayStart,
            long thursdayEnd, long fridayStart, long fridayEnd,
            long saturdayStart, long saturdayEnd)
    {
        setSundayStart(sundayStart);
        setMondayStart(mondayStart);
        setTuesdayStart(tuesdayStart);
        setWednesdayStart(wednesdayStart);
        setThursdayStart(thursdayStart);
        setFridayStart(fridayStart);
        setSaturdayStart(saturdayStart);
        setSundayEnd(sundayEnd);
        setMondayEnd(mondayEnd);
        setTuesdayEnd(tuesdayEnd);
        setWednesdayEnd(wednesdayEnd);
        setThursdayEnd(thursdayEnd);
        setFridayEnd(fridayEnd);
        setSaturdayEnd(saturdayEnd);
    }

    // Setters

    public void setSundayStart(long sundayStart)
    {
        this.sundayStart = sundayStart;
    }

    public void setMondayStart(long mondayStart)
    {
        this.mondayStart = mondayStart;
    }

    public void setTuesdayStart(long tuesdayStart)
    {
        this.tuesdayStart = tuesdayStart;
    }

    public void setWednesdayStart(long wednesdayStart)
    {
        this.wednesdayStart = wednesdayStart;
    }

    public void setThursdayStart(long thursdayStart)
    {
        this.thursdayStart = thursdayStart;
    }

    public void setFridayStart(long fridayStart)
    {
        this.fridayStart = fridayStart;
    }

    public void setSaturdayStart(long saturdayStart)
    {
        this.saturdayStart = saturdayStart;
    }

    public void setSundayEnd(long sundayEnd)
    {
        this.sundayEnd = sundayEnd;
    }

    public void setMondayEnd(long mondayEnd)
    {
        this.mondayEnd = mondayEnd;
    }

    public void setTuesdayEnd(long tuesdayEnd)
    {
        this.tuesdayEnd = tuesdayEnd;
    }

    public void setWednesdayEnd(long wednesdayEnd)
    {
        this.wednesdayEnd = wednesdayEnd;
    }

    public void setThursdayEnd(long thursdayEnd)
    {
        this.thursdayEnd = thursdayEnd;
    }

    public void setFridayEnd(long fridayEnd)
    {
        this.fridayEnd = fridayEnd;
    }

    public void setSaturdayEnd(long saturdayEnd)
    {
        this.saturdayEnd = saturdayEnd;
    }

    // Getters

    public long getSundayStart()
    {
        return this.sundayStart;
    }

    public long getMondayStart()
    {
        return this.mondayStart;
    }

    public long getTuesdayStart()
    {
        return this.tuesdayStart;
    }

    public long getWednesdayStart()
    {
        return this.wednesdayStart;
    }

    public long getThursdayStart()
    {
        return this.thursdayStart;
    }

    public long getFridayStart()
    {
        return this.fridayStart;
    }

    public long getSaturdayStart()
    {
        return this.saturdayStart;
    }

    public long getSundayEnd()
    {
        return this.sundayEnd;
    }

    public long getMondayEnd()
    {
        return this.mondayEnd;
    }

    public long getTuesdayEnd()
    {
        return this.tuesdayEnd;
    }

    public long getWednesdayEnd()
    {
        return this.wednesdayEnd;
    }

    public long getThursdayEnd()
    {
        return this.thursdayEnd;
    }

    public long getFridayEnd()
    {
        return this.fridayEnd;
    }

    public long getSaturdayEnd()
    {
        return this.saturdayEnd;
    }

    @Override
    public String toString()
    {
        int base = 3600;
        LocalTime sundayStartTime = new LocalTime((int)(this.sundayStart / base), (int)((this.sundayStart % base) / 60));
        LocalTime mondayStartTime = new LocalTime((int)(this.mondayStart / base), (int)((this.mondayStart % base) / 60));
        LocalTime tuesdayStartTime = new LocalTime((int)(this.tuesdayStart / base), (int)((this.tuesdayStart % base) / 60));
        LocalTime wednesdayStartTime = new LocalTime((int)(this.wednesdayStart / base), (int)((this.wednesdayStart % base) / 60));
        LocalTime thursdayStartTime = new LocalTime((int)(this.thursdayStart / base), (int)((this.thursdayStart % base) / 60));
        LocalTime fridayStartTime = new LocalTime((int)(this.fridayStart / base), (int)((this.fridayStart % base) / 60));
        LocalTime saturdayStartTime = new LocalTime((int)(this.saturdayStart / base), (int)((this.saturdayStart % base) / 60));
        LocalTime sundayEndTime = new LocalTime((int)(this.sundayEnd / base), (int)((this.sundayEnd % base) / 60));
        LocalTime mondayEndTime = new LocalTime((int)(this.mondayEnd / base), (int)((this.mondayEnd % base) / 60));
        LocalTime tuesdayEndTime = new LocalTime((int)(this.tuesdayEnd / base), (int)((this.tuesdayEnd % base) / 60));
        LocalTime wednesdayEndTime = new LocalTime((int)(this.wednesdayEnd / base), (int)((this.wednesdayEnd % base) / 60));
        LocalTime thursdayEndTime = new LocalTime((int)(this.thursdayEnd / base), (int)((this.thursdayEnd % base) / 60));
        LocalTime fridayEndTime = new LocalTime((int)(this.fridayEnd / base), (int)((this.fridayEnd % base) / 60));
        LocalTime saturdayEndTime = new LocalTime((int)(this.saturdayEnd / base), (int)((this.saturdayEnd % base) / 60));
        String pattern = "h:mma";
        return "S:" + sundayStartTime.toString(pattern) + "-" + sundayEndTime.toString(pattern) +
            "\nM:" + mondayStartTime.toString(pattern) + "-" + mondayEndTime.toString(pattern) +
            "\nT:" + tuesdayStartTime.toString(pattern) + "-" + tuesdayEndTime.toString(pattern) +
            "\nW:" + wednesdayStartTime.toString(pattern) + "-" + wednesdayEndTime.toString(pattern) +
            "\nR:" + thursdayStartTime.toString(pattern) + "-" + thursdayEndTime.toString(pattern) +
            "\nF:" + fridayStartTime.toString(pattern) + "-" + fridayEndTime.toString(pattern) +
            "\nS:" + saturdayStartTime.toString(pattern) + "-" + saturdayEndTime.toString(pattern);
    }
}
