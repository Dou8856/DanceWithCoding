package com.example.expenseappllication;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by 23060814 on 6/9/15.
 */

public class AddItemDialog extends DialogFragment{

    /*
    *Define an interface with method for each type of click event
    * Then implement the interface in order to receive event callback
    * */
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        LinearLayout dialogLayout = (LinearLayout) inflater.inflate(R.layout.add_item_dialog,
                null);
        builder.setPositiveButton(R.string.dialog_add_item_title, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Save the date to database
                Toast.makeText(getActivity(), "Data saved", Toast.LENGTH_SHORT).show();
            }
        });

        final EditText editName = (EditText) dialogLayout.findViewById(R.id.item_name);
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editName.setText("");
            }
        });

        final EditText editCost = (EditText) dialogLayout.findViewById(R.id.item_cost);
        editCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCost.setText("");
            }
        });


        final EditText editCategory = (EditText) dialogLayout.findViewById(R.id.item_category);
        editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCategory.setText("");
            }
        });

        builder.setView(dialogLayout);
        return builder.create();
    }

    public interface  AddItemListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    AddItemListener mAddItemListener;

    //Override the Fragment.onAttach() method to instantiate the

}
