package com.example.android.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText result;
    private EditText newNumber;
    private TextView displayOperation;

    // Variable to hold the operands and type of calculation
    private Double operand1 = null;
    private String pendingOperation = "=";

    private static final String STATE_PENDING_OPERATION = "PendingOperation";
    public static final String STATE_PENDING_OPERAND = "Operand1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (EditText) findViewById(R.id.result);
        newNumber = (EditText) findViewById(R.id.newNumber);
        displayOperation = (TextView) findViewById(R.id.operation);

        Button button0 = (Button) findViewById(R.id.button0);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button7 = (Button) findViewById(R.id.button7);
        Button button8 = (Button) findViewById(R.id.button8);
        Button button9 = (Button) findViewById(R.id.button9);
        Button buttonDot = (Button) findViewById(R.id.buttonDot);

        Button buttonMultiply = (Button) findViewById(R.id.buttonMultiply);
        Button buttonDivide = (Button) findViewById(R.id.buttonDivide);
        Button buttonPlus = (Button) findViewById(R.id.buttonPlus);
        Button buttonMinus = (Button) findViewById(R.id.buttonMinus);
        Button buttonEquals = (Button) findViewById(R.id.buttonEquals);
        Button buttonClear = (Button) findViewById(R.id.buttonClear);

        //Setup our listener for all the buttons
        View.OnClickListener digitListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) { //all views are listeners
                Button b = (Button) v; //must cast as button to access getText()
                newNumber.append(b.getText().toString());
            }
        };

        //set the listener for all the buttons (can use an array + for loop if many more than this)
        button0.setOnClickListener(digitListener);
        button1.setOnClickListener(digitListener);
        button2.setOnClickListener(digitListener);
        button3.setOnClickListener(digitListener);
        button4.setOnClickListener(digitListener);
        button5.setOnClickListener(digitListener);
        button6.setOnClickListener(digitListener);
        button7.setOnClickListener(digitListener);
        button8.setOnClickListener(digitListener);
        button9.setOnClickListener(digitListener);
        buttonDot.setOnClickListener(digitListener);

        View.OnClickListener operationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                String op = b.getText().toString();
                String value = newNumber.getText().toString();

                try {
                    Double doubleVal = Double.valueOf(value);
                    performOperation(doubleVal, op);
                } catch (NumberFormatException e){
                    newNumber.setText("");
                }

                pendingOperation = op;
                displayOperation.setText(pendingOperation);
            }
        };

        buttonDivide.setOnClickListener(operationListener);
        buttonMultiply.setOnClickListener(operationListener);
        buttonPlus.setOnClickListener(operationListener);
        buttonMinus.setOnClickListener(operationListener);
        buttonEquals.setOnClickListener(operationListener);
        buttonClear.setOnClickListener(operationListener);

        Button buttonNeg = (Button) findViewById(R.id.buttonNeg);


        buttonNeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = newNumber.getText().toString();
                if (value.length() == 0)
                    newNumber.setText("-");
                else {
                    //we're going to be trying for an invalid number
                    try {
                        Double aDouble = Double.valueOf(value);
                        aDouble *= -1;
                        newNumber.setText(aDouble.toString());
                    } catch (NumberFormatException exception){
                        //newNumber was "-" or "." so clear it
                        newNumber.setText("");
                    }
                }
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operand1 = null;
                pendingOperation = "=";

                newNumber.setText("");
                result.setText("");
                displayOperation.setText("");
            }
        });

    }

    private void performOperation(Double value, String op) {
        if (null == operand1) {
            //if operand1 hasn't been assigned and you hit an operation button,
            //it just assigns the value to it
            operand1 = value;

        } else {
            //if operand1 has already been assigned, then you assign this value to
            // the operand2 and the pendingOperation op

            if (pendingOperation.equals("="))
                    pendingOperation = op;
            //operation for each case
            switch (pendingOperation) {
                case "=":
                    operand1 = value;
                    break;
                case "/":
                    if (value == 0)
                        operand1 = 0.0;
                    else
                        operand1 /= value;
                    break;

                case "*":
                    operand1 *= value;
                    break;

                case "-":
                    operand1 -= value;
                    break;

                case "+":
                    operand1 += value;
                    break;

                case "c":
                    operand1 = null;
                    pendingOperation = "=";
            }


        }
        result.setText(operand1.toString());
        newNumber.setText("");
    }


    /**
     * This function saves the state of the Activity  each time.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_PENDING_OPERATION, pendingOperation);
        if (operand1 != null)
            outState.putDouble(STATE_PENDING_OPERAND, operand1);
    }

    /**
     * This function restores the values of the fields in this object
     * when the activity is resumed after coming back from other activities
     * or rotating the phone
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //restore your fields:
        operand1 = savedInstanceState.getDouble(STATE_PENDING_OPERAND);
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION);
        displayOperation.setText(pendingOperation);
    }
}
