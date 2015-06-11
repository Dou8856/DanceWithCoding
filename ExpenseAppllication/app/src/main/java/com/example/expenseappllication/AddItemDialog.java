package com.example.expenseappllication;

import android.app.Activity;
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

        builder.setPositiveButton(R.string.dialog_add_item_title, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Button click listener
                Toast.makeText(getActivity(), "Data saved", Toast.LENGTH_SHORT).show();
                //Because the host activity implements the AddItemListener,
                // which is enforced by onAttach() callback method. Passing events back to
                // Dialog's host
                String name = editName.getText().toString();
                String cost = editCost.getText().toString();
                String category = editCategory.getText().toString();

                mAddItemListener.onDialogPositiveClick(AddItemDialog.this, name, cost, category);
            }
        });

        builder.setView(dialogLayout);
        return builder.create();
    }

    //Create interface and implement the method in the caller
    /*
    * The activity that creates an instance of this dialog fragment must implement this
    * interface in order to receive event callbacks. Each method passes the DialogFragment in
    * case the host needs to query it
    * */

    // Here we define interface, and implement the interface in the events which will get data,
    // instantiate the listener by casting the activity. Then call the listener function in
    // the implementation of onClickListener in onCreate function.
     public interface  AddItemListener {
        public void onDialogPositiveClick(DialogFragment dialog, String name, String cost,
                String category);
    }

    // Use this instance of the interface to deliver action events
    AddItemListener mAddItemListener;

    //Override the Fragment.onAttach() method to instantiate the AddItemListener

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Verify that the host activity implements the callback interface
        try{
            // Instantiate the listener by casting type, so that we can send the events
            // to the event.
            mAddItemListener = (AddItemListener) activity;
        } catch(ClassCastException cce) {
            // If the activity doesn't implement the interface, we throw an exception.
            //Emily: Java polymorphism, if a class implements an interface,
            // we should be able to cast the type
            throw new ClassCastException(activity.toString() + "must implement AddItemListener");
        }
    }

}
