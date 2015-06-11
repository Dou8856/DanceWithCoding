package com.example.expenseappllication;

import com.example.expenseappllication.AddItemDialog.AddItemListener;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.LineNumberReader;


//Since we need to get the callback from dialog, we need to implement the listener
public class MainActivity extends Activity implements AddItemListener{
    TableLayout mTableLayout;

    public void onDialogPositiveClick(DialogFragment dialog, String name, String cost,
            String category) {
        addExpense(name, cost, category);
        //When the positive button is clicked, we add a new column to the table
        Toast.makeText(getApplicationContext(), "positive button clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTableLayout = (TableLayout) findViewById(R.id.table_layout);
        TableRow newRow = (TableRow) getLayoutInflater().inflate(R.layout.cell_layout, null);

        TextView cellName = (TextView) newRow.findViewById(R.id.name);
        cellName.setText("Breakfast");

        TextView cellExpense = (TextView) newRow.findViewById(R.id.cost);
        cellExpense.setText("100");

        TextView cellCategory = (TextView) newRow.findViewById(R.id.category);
        cellCategory.setText("breakfast");

        mTableLayout.addView(newRow);

        Button addButton = (Button) findViewById(R.id.button_add);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddItemOption();
            }
        });
    }

    public void showAddItemOption() {
        //Start a dialog
        AddItemDialog dialog = new AddItemDialog();
        FragmentManager fm = getFragmentManager();
        dialog.show(fm, "dialog");
    }

    public void addExpense(String name, String cost, String category) {
        TableRow newRow = (TableRow) getLayoutInflater().inflate(R.layout.cell_layout, null);

        TextView nameView = (TextView) newRow.findViewById(R.id.name);
        nameView.setText(name);


        TextView costView = (TextView) newRow.findViewById(R.id.cost);
        costView.setText(cost);


        TextView categoryView = (TextView) newRow.findViewById(R.id.category);
        categoryView.setText(category);

        mTableLayout.addView(newRow);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
