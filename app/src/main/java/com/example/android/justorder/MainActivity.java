package com.example.android.justorder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    int quantity = 0;
    String orderSummary;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton sendOrder = findViewById(R.id.send_order);
        sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeSMSMessage(orderSummary);
            }
        });
    }
    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        EditText nameField = findViewById(R.id.name_edit_textView);
        name = nameField.getText().toString();

        EditText mobileNoField = findViewById(R.id.mobile_no_edit_textView);
        String mobileNum = mobileNoField.getText().toString();

        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_check_box);
        Boolean hasChocolateToppings = chocolateCheckBox.isChecked();

        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_check_box);
        Boolean hasWhippedCreamToppings = whippedCreamCheckBox.isChecked();
        //Log.v("Main Activity","Has whipped cream"+hasWhippedCreamToppings);

        int price = calculatePrice(hasChocolateToppings,hasWhippedCreamToppings);

        orderSummary =
                createOrderSummary(name,mobileNum,price,hasWhippedCreamToppings,hasChocolateToppings);
        displayMessage(orderSummary);

    }
    public void composeSMSMessage(String message){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:"));//This ensures that only SMS apps respond.
        intent.putExtra("sms_body",message);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }

    }
    public int calculatePrice(Boolean toppingCholocate,Boolean toppingWhippedCream){
        int basePrice = 20;
        if(toppingCholocate){
             basePrice = basePrice + 7;
        }
        if(toppingWhippedCream ){
            basePrice = basePrice + 5;
        }
        return quantity * basePrice;
    }
    public String createOrderSummary(String name,String mobileNum,
                                     int price,Boolean whippedCream,Boolean chocolate){
        String orderSummary = getString(R.string.name)+name +
                "\n"+ getString(R.string.mobile_num)+ mobileNum +
                "\n"+ getString(R.string.num_of_quantity)+ quantity +
                "\n"+ getString(R.string.add_whipped_cream)+whippedCream+
                "\n"+ getString(R.string.add_Chocolate)+ chocolate+
                "\n"+ getString(R.string.total) + price+ "\n"+ getString(R.string.thank_u);
        return orderSummary;
    }
    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }
    public void increment(View view){
        if(quantity>100){
            Toast.makeText(this,"You can not have more than 100 cups of coffee",
                    Toast.LENGTH_SHORT).show();
            return;
        }else{
            quantity = quantity + 1;
        }
        display(quantity);
    }
    public void decrement(View view){
          if(quantity<1){
              Toast.makeText(this,"You can not have less than 1 cup of coffee",
                      Toast.LENGTH_SHORT).show();
              return;
          }else{
              quantity = quantity - 1;
          }
          display(quantity);
    }
    public void displayMessage(String message){
        TextView orderSummaryTextView = findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }
}
