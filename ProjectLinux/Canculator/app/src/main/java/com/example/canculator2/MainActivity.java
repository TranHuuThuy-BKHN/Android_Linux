package com.example.canculator2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Random;
import java.util.Stack;


public class MainActivity extends AppCompatActivity {

    //TextView hiển thị kết quả và các phím số khi nhập vào
    TextView textResult;
    TextView mode;
    String res = "0";
    boolean checkMode = true;
    final NumberFormat n = NumberFormat.getNumberInstance(Locale.ENGLISH);

    //sử dụng stack để giải quyết bài đơn giản này, stack có kich thước bằng 1 chính là kết quả
    Stack<String> stack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Chương trình tính toán của nhóm 06 - Linux", Toast.LENGTH_LONG).show();
        textResult = (TextView) findViewById(R.id.textRessult);
        mode = (TextView) findViewById(R.id.mode);
    }

    /**
     * phương thức xư lý sự kiện cho các phím số và dấu '.'
     *
     * @param v
     */
    public void clickNumber(View v) {
        final Button b = (Button) v;
        if (res.equals("0")) res = "";
        res += b.getText().toString();
        try {
            if (b.getText().toString().equals(".")) {
                textResult.setText(textResult.getText() + ".");
            } else textResult.setText(n.format(n.parse(res).doubleValue()));
        } catch (ParseException e) {
            Toast.makeText(MainActivity.this, "Sai định dạng số", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * phương thức xử lý sự kiện cho các toán hai ngôi như +, - , x, /...
     *
     * @param v
     */
    public void clickOperator(View v) {
        Button operator = (Button) v;
        switch (stack.size()) {
            case 0:
                try {
                    stack.push(n.format(n.parse(res)));
                } catch (ParseException e) {
                    Toast.makeText(MainActivity.this, "Sai định dạng số", Toast.LENGTH_LONG).show();
                }
                stack.push(operator.getText().toString());
                break;
            case 1:
                stack.pop();
                try {
                    stack.push(n.format(n.parse(textResult.getText().toString())));
                } catch (ParseException e) {
                    Toast.makeText(MainActivity.this, "Sai định dạng số", Toast.LENGTH_LONG).show();
                }
                stack.push(operator.getText().toString());
                break;
            default:
                stack.pop();
                stack.push(operator.getText().toString());
        }
        res = "";
        textResult.setText(res);
    }

    /**
     * phương thức xử lý cho toán tử đảo dấu
     *
     * @param v
     */
    public void operatorSigned(View v) {
        if (res.equals("0") == false) {
            if (res.charAt(0) != '-')
                res = "-" + res;
            else res = res.substring(1);
        }
        if (stack.size() == 1) {
            stack.pop();
            stack.push(res);
        }
        textResult.setText(res);
    }

    /**
     * phương thức tính toán khi nhấn phím '='
     *
     * @param v
     */
    public void clickResult(View v) {
        switch (stack.size()) {
            case 1:
                res = stack.pop();
                break;
            case 0:
                break;
            default:
                if (textResult.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Nhập số hạng thứ 2", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        double value2 = n.parse(textResult.getText().toString()).doubleValue();
                        String operator = stack.pop();
                        double value1 = n.parse(stack.pop()).doubleValue();
                        stack.clear();
                        switch (operator) {
                            case "+":
                                res = n.format(value1 + value2);
                                break;
                            case "-":
                                res = n.format(value1 - value2);
                                break;
                            case "×":
                                res = n.format(value1 * value2);
                                break;
                            case "÷":
                                res = n.format(value1 / value2);
                                break;
                            case "%":
                                res = n.format(value1 % value2);
                                break;
                            case "xⁿ":
                                res = n.format(Math.pow(value1, value2));
                                break;

                            case "ⁿ√x":
                                res = n.format(Math.pow(value1, 1 / value2));
                                break;

                            case "Pₓⁿ":
                                if (value1 != (int) value1 || value2 != (int) value2 || value2 > value1
                                        || value2 < 0 || value1 < 0) {
                                    res = "Lỗi";
                                } else {
                                    double value = 1;
                                    if (value2 == 0) res = "0";
                                    else {
                                        for (int i = (int) (value1 - value2 + 1); i <= value1; i++)
                                            value *= i;
                                        res = n.format(value);
                                    }
                                }
                                break;

                            case "Cₓⁿ":
                                if (value1 != (int) value1 || value2 != (int) value2 || value2 > value1
                                        || value2 < 0 || value1 < 0) {
                                    res = "Lỗi";
                                } else {
                                    double value3 = 1, value4 = 1;
                                    if (value2 == 0) res = "0";
                                    else {
                                        for (int i = (int) (value1 - value2 + 1); i <= value1; i++)
                                            value3 *= i;
                                        for (int i = 1; i <= value2; i++)
                                            value4 *= i;
                                        res = n.format(value3 / value4);
                                    }
                                }
                                break;
                        }
                        stack.push(res);
                    } catch (ParseException e) {
                        Toast.makeText(MainActivity.this, "Sai định dang số", Toast.LENGTH_LONG).show();
                    }
                }

        }
        textResult.setText(res);
    }

    /**
     * phương thức xử lý bới nút back
     *
     * @param v
     */
    public void clickBack(View v) {
        if (res.length() == 1 || (res.length() == 2 && res.charAt(0) == '-'))
            res = "0";
        else res = res.substring(0, res.length() - 1);
        textResult.setText(res);
    }

    /**
     * phương thức khi nhấn các toán tử 1 ngôi như 2^, x^2,...
     *
     * @param v
     */
    public void operatorI(View v) {
        Button b = (Button) v;
        double value = 0.0;
        if (textResult.getText().equals("") == false) {
            try {
                if (b.getText().toString().equals("e") == false &&
                        b.getText().toString().equals("π") == false) {
                    value = n.parse(textResult.getText().toString()).doubleValue();
                }
                switch (b.getText().toString()) {
                    case "2ⁿ":
                        res = n.format(Math.pow(2, value));
                        break;
                    case "x²":
                        res = n.format(value * value);
                        break;
                    case "x³":
                        res = n.format(value * value * value);
                        break;
                    case "eⁿ":
                        res = n.format(Math.exp(value));
                        break;
                    case "10ⁿ":
                        res = n.format(Math.pow(10, value));
                        break;
                    case "1/x":
                        res = n.format(1 / value);
                        break;
                    case "√x":
                        if (value < 0) {
                            res = "Lỗi";
                        } else res = n.format(Math.sqrt(value));
                        break;
                    case "³√x":
                        res = n.format(Math.cbrt(value));
                        break;
                    case "log":
                        if (value <= 0) res = "Lỗi";
                        else res = n.format(Math.log10(value));
                        break;
                    case "ln":
                        if (value <= 0) res = "Lỗi";
                        else res = n.format(Math.log(value));
                        break;
                    case "x!":
                        if (value == (int) value && value >= 0) {
                            long longValue = 1;
                            for (int i = 1; i <= value; i++) {
                                longValue *= i;
                            }
                            res = n.format(longValue);
                        } else res = "Lỗi";
                        break;
                    case "sin":
                        if (checkMode) {
                            res = n.format(Math.sin(Math.toRadians(value)));
                        } else res = n.format(Math.sin(value));
                        break;

                    case "sinh":
                        res = n.format(Math.sinh(value));
                        break;

                    case "cos":
                        if (checkMode) {
                            res = n.format(Math.cos(Math.toRadians(value)));
                        } else res = n.format(Math.cos(value));
                        break;

                    case "cosh":
                        res = n.format(Math.cosh(value));
                        break;

                    case "tan":
                        if (value % 90 == 0) {
                            res = "Lỗi";
                        } else if (checkMode) {
                            res = n.format(Math.tan(Math.toRadians(value)));
                        } else res = n.format(Math.tan(value));
                        break;

                    case "cotan":
                        if (value % 180 == 0) {
                            res = "Lỗi";
                        } else if (checkMode) {
                            res = n.format(1 / Math.tan(Math.toRadians(value)));
                        } else res = n.format(1 / Math.tan(value));
                        break;

                    case "tanh":
                        res = n.format(Math.tanh(value));
                        break;

                    case "e":
                        res = n.format(Math.E);
                        break;
                    case "π":
                        res = n.format(Math.PI);
                        break;

                    case "Rand":
                        res = n.format(new Random().nextDouble());
                        break;
                }
                textResult.setText(res);
            } catch (ParseException e) {
                Toast.makeText(MainActivity.this, "Số không hợp lệ !", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void clickCancel(View v) {
        res = "0";
        textResult.setText(res);
        stack.clear();
    }

    public void mode(View v) {
        Button b = (Button) v;
        if (checkMode) {
            b.setText("Deg");
            mode.setText("Rad");
            checkMode = false;
        } else {
            b.setText("Rad");
            mode.setText("Deg");
            checkMode = true;
        }
    }
}