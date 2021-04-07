package com.quadrant.govolt;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CreditCardNumberFormattingTextWatcher_One implements TextWatcher {

    private boolean lock;
    private  EditText edittext;
    private Context con;

    public CreditCardNumberFormattingTextWatcher_One(EditText card_form, Context con) {
         edittext = card_form;
         con = con;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (lock || s.length() == 16) {
            return;
        }
        lock = true;
        for (int i = 4; i < s.length(); i += 5) {
            if (s.toString().charAt(i) != ' ') {
                s.insert(i, " ");
            }
        }
        lock = false;

        String answerString = edittext.getText().toString();

        if (answerString.length() == 19) {

            Drawable myIcon = con.getResources().getDrawable(R.drawable.ic_mastercard_small);
            myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth(), myIcon.getIntrinsicHeight());
            edittext.setCompoundDrawablesWithIntrinsicBounds(null, null, myIcon, null);

        } else {

            Drawable myIcon = con.getResources().getDrawable(R.drawable.without_card);
            myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth(), myIcon.getIntrinsicHeight());
            edittext.setCompoundDrawablesWithIntrinsicBounds(null, null, myIcon, null);
        }
    }
}