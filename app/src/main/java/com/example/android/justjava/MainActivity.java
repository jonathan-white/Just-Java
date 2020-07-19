package com.example.android.justjava;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;
    boolean hasWhippedCream;
    boolean hasChocolate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        // Find the user's name
        EditText customerName = (EditText) findViewById(R.id.customer_name);
        String customer = customerName.getText().toString();

        // Calculate and display the price
        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(customer, price, hasWhippedCream, hasChocolate);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, "");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, customer));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void addWhippedCream(View view) {
        CheckBox whippedCreamCheckbox = (CheckBox) findViewById(R.id.whipped_cream);
        hasWhippedCream = whippedCreamCheckbox.isChecked();

        displayTotal();
    }

    public void addChocolate(View view) {
        CheckBox chocolateCheckbox = (CheckBox) findViewById(R.id.chocolate);
        hasChocolate = chocolateCheckbox.isChecked();

        displayTotal();
    }

    /**
     * Calculates the prices of the order based on the current quantity;
     * @param addWhippedCream is whether or not the user wants whipped cream
     * @param addChocolate is whether or not the user wants chocolate
     * @return the result
     */
    private int calculatePrice (boolean addWhippedCream, boolean addChocolate) {
        int basePrice = 5;

        if (addWhippedCream) {
            basePrice += 1;
        }

        if (addChocolate) {
            basePrice += 2;
        }

        return  basePrice * quantity;

    }

    public void increment(View view) {
        quantity += 1;
        displayQuantity(quantity);

        displayTotal();
    }

    public void decrement(View view) {
        if(quantity > 0) {
            quantity -= 1;
        } else {
            quantity = 0;
        }
        displayQuantity(quantity);

        displayTotal();
    }


    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the total price on the screen.
     */
    private void displayTotal() {
        TextView orderTotal = (TextView) findViewById(R.id.order_summary_text_view);
        orderTotal.setText(NumberFormat.getCurrencyInstance().format(calculatePrice(hasWhippedCream,hasChocolate)));
    }

    /**
     * Create summary of the order.
     *
     * @param price price of the order
     * @param name name of the customer
     * @return text summary
     */
    public String createOrderSummary (String name, int price, boolean addWhippedCream, boolean addChocolate) {
        String orderSummary = getString(R.string.order_summary_name, name) + "\n";
        orderSummary += getString(R.string.order_summary_whipped_cream,""+addWhippedCream) +"\n";
        orderSummary += getString(R.string.order_summary_chocolate,""+addChocolate)+"\n";
        orderSummary += getString(R.string.order_summary_quantity, ""+quantity) +"\n";
        orderSummary += getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(price)) +"\n";
        orderSummary += getString(R.string.thank_you);
        return orderSummary;
    }

}