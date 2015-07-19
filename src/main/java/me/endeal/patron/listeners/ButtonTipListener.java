package me.endeal.patron.listeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import me.endeal.patron.model.Price;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ButtonTipListener implements OnClickListener
{
    private OnApiExecutedListener listener;

    public ButtonTipListener(OnApiExecutedListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.DialogMain);
        final LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_tip, null);
        builder.setView(dialogView);
        Button buttonDone = (Button)dialogView.findViewById(R.id.dialogTipButtonDone);
        final EditText fieldCustom = (EditText)dialogView.findViewById(R.id.dialogTipFieldCustom);
        final SeekBar seekBarPercent = (SeekBar)dialogView.findViewById(R.id.dialogTipSeekBarPercent);
        final TextView textPercent = (TextView)dialogView.findViewById(R.id.dialogTipTextPercent);
        final AlertDialog dialog = builder.create();
        if (!Globals.getOrder().getTip().toString().equals("0.00"))
        {
            fieldCustom.setText(Globals.getOrder().getTip().toString());
        }
        seekBarPercent.setMax(100);
        int percent = Globals.getOrder().getTipPercent();
        seekBarPercent.setProgress(percent);
        textPercent.setText(percent + "%");
        seekBarPercent.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                BigDecimal value = new BigDecimal(Globals.getOrder().getPrice().getValue() / 100);
                value = value.multiply(new BigDecimal((progress / 100.0) + ""));
                //BigDecimal value = Globals.getOrder().getPrice().multiply(new BigDecimal((progress / 100.0) + ""));
                value = value.setScale(2, BigDecimal.ROUND_DOWN);
                fieldCustom.setText(value.toString());
                textPercent.setText(progress + "%");
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        buttonDone.setOnClickListener(new OnClickListener() {
            public void onClick(View view)
            {
                Price tip;
                try
                {
                    double d = Double.parseDouble(fieldCustom.getText().toString());
                    int value = (int) d * 100;
                    tip = new Price(value, "USD");
                }
                catch(NumberFormatException nfe)
                {
                    tip = new Price(0, "USD");
                }
                Globals.getOrder().setTip(tip);
                listener.onExecuted();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
