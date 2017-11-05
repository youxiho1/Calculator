package com.example.youxihouzainali.calculator;

import android.icu.math.BigDecimal;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import static java.lang.Math.incrementExact;
import static java.lang.Math.sqrt;
@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    String result = "0", numbertemp = "0",number1 = "0",number2 = "0";       //numbertemp是临时操作数
    String number3 = "0";                                     //暂存上次结果中的第二个操作数
    int num = 0;                                                //位数计数器
    int flag = 1, flag1 = 0, flag2 = 0;                             //做的操作，是否键入了第一操作数、第二操作数
    int flagPoint = 0, point = 0;                      //小数点有无，小数点的位置
    double d1 = 0, d2 = 0, d3 = 0;
    int flag3 = 0;                                           //在当次计算中是否按过运算符
    int flag4 = 0;                                           //当前是否处于溢出状态
    int flag5 = 0;                                           //当前backspace是否可按
    //flag4 flag5的集体清零
    public Button Btn0, Btn1, Btn2, Btn3, Btn4, Btn5, Btn6, Btn7, Btn8, Btn9;
    public Button BtnPlus, BtnMinus, BtnMultiply, BtnDivide, BtnSign, BtnReciprocal, BtnPoint;
    public Button BtnEqual, BtnC, BtnCE, BtnRooting, BtnBack, BtnMod, BtnSquare;
    public TextView textView;

    private void setButtonListener(int viewId) {                        //绑定监听器
        Button but = (Button) findViewById(viewId);
        but.setOnClickListener(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtonListener(R.id.Btn0);
        setButtonListener(R.id.Btn1);
        setButtonListener(R.id.Btn2);
        setButtonListener(R.id.Btn3);
        setButtonListener(R.id.Btn4);
        setButtonListener(R.id.Btn5);
        setButtonListener(R.id.Btn6);
        setButtonListener(R.id.Btn7);
        setButtonListener(R.id.Btn8);
        setButtonListener(R.id.Btn9);
        setButtonListener(R.id.BtnPlus);
        setButtonListener(R.id.BtnMinus);
        setButtonListener(R.id.BtnMultiply);
        setButtonListener(R.id.BtnDivide);
        setButtonListener(R.id.BtnSign);
        setButtonListener(R.id.BtnMod);
        setButtonListener(R.id.BtnRooting);
        setButtonListener(R.id.BtnSquare);
        setButtonListener(R.id.BtnReciprocal);
        setButtonListener(R.id.BtnCE);
        setButtonListener(R.id.BtnC);
        setButtonListener(R.id.BtnBack);
        setButtonListener(R.id.BtnEqual);
        setButtonListener(R.id.BtnPoint);
        textView = (TextView) findViewById(R.id.textView) ;
        textView.setText("0");
    }

    public String adjust(String s) {
        int i = 0, j, flagPoint = 0;
        int length = s.length();
        char[] c = new char[20];
        c = s.toCharArray();
        for(i = 0; i < length; i++)
            if(c[i]=='.')
                flagPoint = 1;
        i = length - 1;
        if(flagPoint == 1)
            while(c[i] == '0') {
                c[i] = '\0';
                if(c[i - 1] == '.') {
                    flagPoint = 0;
                    i = i - 2;
                    break;
                }
                i--;
            }
        s = "";
        for(j = 0; j <= i; j++)
            s = s + c[j];
        if(flagPoint == 1)
            length = s.length() - 1;
        else
            length = s.length();
        if(length > 16) {
            if(flagPoint == 1)
                j = 17;
            else
                j = 16;
            if(Double.parseDouble(numbertemp) < 0)
                j++;
			/*if(c[i] > '5') {													//四舍五入
				c[i - 1] = c[i - 1] + 1;
				while(c[i - 1] - 48 >= 10) {
					c[i - 1] = c[i - 1] - 10;
					c[i - 2] = c[i - 2] + 1;
					i--;
				}
			}
			*/
            s = s.substring(0,j);
        }
        if(s.equals("0")||s.equals("-0")||s.equals("0.")||s.equals("-0."))
            s = "0";
        return s;
    }

    public void resume() {
        result = numbertemp = number1 = number2 = number3 = "0";
        num = 0;
        flag = 1;
        flag1 = flag2 = flag3 = flag4 =flagPoint = point= 0;
        d1 = d2 = d3 = 0;
        /*BtnMod.setEnabled(true);
        BtnRooting.setEnabled(true);
        BtnSquare.setEnabled(true);
        BtnReciprocal.setEnabled(true);
        BtnDivide.setEnabled(true);
        BtnMultiply.setEnabled(true);
        BtnMinus.setEnabled(true);
        BtnPlus.setEnabled(true);
        BtnSign.setEnabled(true);
        BtnPoint.setEnabled(true);
        */
    }

    public void pause() {
        flag4 = 1;
        /*
        BtnMod.setEnabled(false);
        BtnRooting.setEnabled(false);
        BtnSquare.setEnabled(false);
        BtnReciprocal.setEnabled(false);
        BtnDivide.setEnabled(false);
        BtnMultiply.setEnabled(false);
        BtnMinus.setEnabled(false);
        BtnPlus.setEnabled(false);
        BtnSign.setEnabled(false);
        BtnPoint.setEnabled(false);
        */
        result = numbertemp = number1 = number3 = "0";                       //清零操作
        num = flag1 = flag2 = flag3 = flagPoint = point = 0;
        flag = 1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Btn0:
                if(flag5 == 1) {
                    numbertemp = "";
                    flag5 = 0;
                }
                if(flag4 == 1)
                    resume();
                if(flag3 == 0 && num == 0 && flagPoint == 0) {
                    flag1 = 1;
                    numbertemp = "0";
                    textView.setText("0");
                }
                if(flag3 == 1 && num == 0 && flagPoint == 0) {
                    flag2 = 1;
                    numbertemp = "0";
                    textView.setText("0");
                }
                if(numbertemp.equals("") || numbertemp.equals("0"))					//字符串比较
                    return;
                if(num < 16){
                    numbertemp = numbertemp + "0";
                    num = num + 1;												//位数计数器+1
                    textView.setText(numbertemp);
                }
                break;
            case R.id.Btn1:
                if(flag5 == 1) {
                    numbertemp = "";
                    flag5 = 0;
                }
                if(flag4 == 1)
                    resume();
                if(flag3 == 0 && num == 0 && flagPoint == 0){                          //第一操作数按过了
                    flag1 = 1;
                    //num = 0;
                    numbertemp = "";
                }
                if(flag3 == 1 && num == 0 && flagPoint == 0){							  //第二操作数按过了
                    flag2 = 1;
                    numbertemp= "";
                    //num = 0;
                }
                if(num < 16){
                    numbertemp = numbertemp + "1";
                    num = num + 1;											          //位数计数器+1
                    textView.setText(numbertemp);
                }
                break;
            case R.id.Btn2:
                if(flag5 == 1) {
                    numbertemp = "";
                    flag5 = 0;
                }
                if(flag4 == 1)
                    resume();
                if(flag3 == 0 && num == 0 && flagPoint == 0){                          //第一操作数按过了
                    flag1 = 1;
                    //num = 0;
                    numbertemp = "";
                }
                if(flag3 == 1 && num == 0 && flagPoint == 0){							  //第二操作数按过了
                    flag2 = 1;
                    numbertemp= "";
                    //num = 0;
                }
                if(num < 16){
                    numbertemp = numbertemp + "2";
                    num = num + 1;											          //位数计数器+1
                    textView.setText(numbertemp);
                }
                break;
            case R.id.Btn3:
                if(flag5 == 1) {
                    numbertemp = "";
                    flag5 = 0;
                }
                if(flag4 == 1)
                    resume();
                if(flag3 == 0 && num == 0 && flagPoint == 0){                          //第一操作数按过了
                    flag1 = 1;
                    numbertemp = "";
                    //num = 0;
                }
                if(flag3 == 1 && num == 0 && flagPoint == 0){							  //第二操作数按过了
                    flag2 = 1;
                    numbertemp= "";
                    //num = 0;
                }
                if(num < 16){
                    numbertemp = numbertemp + "3";
                    num = num + 1;											          //位数计数器+1
                    textView.setText(numbertemp);
                }
                break;
            case R.id.Btn4:
                if(flag5 == 1) {
                    numbertemp = "";
                    flag5 = 0;
                }
                if(flag4 == 1)
                    resume();
                if(flag3 == 0 && num == 0 && flagPoint == 0){                          //第一操作数按过了
                    flag1 = 1;
                    //num = 0;
                    numbertemp = "";
                }
                if(flag3 == 1 && num == 0 && flagPoint == 0){							  //第二操作数按过了
                    flag2 = 1;
                    numbertemp= "";
                    //num = 0;
                }
                if(num < 16){
                    numbertemp = numbertemp + "4";
                    num = num + 1;											          //位数计数器+1
                    textView.setText(numbertemp);
                }
                break;
            case R.id.Btn5:
                if(flag5 == 1) {
                    numbertemp = "";
                    flag5 = 0;
                }
                if(flag4 == 1)
                    resume();
                if(flag3 == 0 && num == 0 && flagPoint == 0){                          //第一操作数按过了
                    flag1 = 1;
                    //num = 0;
                    numbertemp = "";
                }
                if(flag3 == 1 && num == 0 && flagPoint == 0){							  //第二操作数按过了
                    flag2 = 1;
                    numbertemp= "";
                   // num = 0;
                }
                if(num < 16){
                    numbertemp = numbertemp + "5";
                    num = num + 1;											          //位数计数器+1
                    textView.setText(numbertemp);
                }
                break;
            case R.id.Btn6:
                if(flag5 == 1) {
                    numbertemp = "";
                    flag5 = 0;
                }
                if(flag4 == 1)
                    resume();
                if(flag3 == 0 && num == 0 && flagPoint == 0){                          //第一操作数按过了
                    flag1 = 1;
                    //num = 0;
                    numbertemp = "";
                }
                if(flag3 == 1 && num == 0 && flagPoint == 0){							  //第二操作数按过了
                    flag2 = 1;
                    numbertemp= "";
                    //num = 0;
                }
                if(num < 16){
                    numbertemp = numbertemp + "6";
                    num = num + 1;											          //位数计数器+1
                    textView.setText(numbertemp);
                }
                break;
            case R.id.Btn7:
                if(flag5 == 1) {
                    numbertemp = "";
                    flag5 = 0;
                }
                if(flag4 == 1)
                    resume();
                if(flag3 == 0 && num == 0 && flagPoint == 0){                           //第一操作数按过了
                    flag1 = 1;
                    //num = 0;
                    numbertemp = "";
                }
                if(flag3 == 1 && num == 0 && flagPoint == 0){							  //第二操作数按过了
                    flag2 = 1;
                    numbertemp= "";
                    //num = 0;
                }
                if(num < 16){
                    numbertemp = numbertemp + "7";
                    num = num + 1;											          //位数计数器+1
                    textView.setText(numbertemp);
                }
                break;
            case R.id.Btn8:
                if(flag5 == 1) {
                    numbertemp = "";
                    flag5 = 0;
                }
                if(flag4 == 1)
                    resume();
                if(flag3 == 0 && num == 0 && flagPoint == 0){                          //第一操作数按过了
                    flag1 = 1;
                    //num = 0;
                    numbertemp = "";
                }
                if(flag3 == 1 && num == 0 && flagPoint == 0){							  //第二操作数按过了
                    flag2 = 1;
                    numbertemp= "";
                    //num = 0;
                }
                if(num < 16){
                    numbertemp = numbertemp + "8";
                    num = num + 1;											          //位数计数器+1
                    textView.setText(numbertemp);
                }
                break;
            case R.id.Btn9:
                if(flag5 == 1) {
                    numbertemp = "";
                    flag5 = 0;
                }
                if(flag4 == 1)
                    resume();
                if(flag3 == 0 && num == 0 && flagPoint == 0){                          //第一操作数按过了
                    flag1 = 1;
                    //num = 0;
                    numbertemp = "";
                }
                if(flag3 == 1 && num == 0 && flagPoint == 0){							  //第二操作数按过了
                    flag2 = 1;
                    numbertemp= "";
                    //num = 0;
                }
                if(num < 16){
                    numbertemp = numbertemp + "9";
                    num = num + 1;											          //位数计数器+1
                    textView.setText(numbertemp);
                }
                break;
            case R.id.BtnMod:
                Toast.makeText(MainActivity.this, "功能未实现", Toast.LENGTH_SHORT).show();
                break;
            case R.id.BtnRooting:
                if(num == 0 && flagPoint == 0 && flag3 == 0){         //没按过数，没按过符号，应该对上一次结果做开方
                    numbertemp = number1;
                    flag1 = 1;
                }
                else if(num == 0 && flagPoint == 0 && flag3 == 1) {
                    numbertemp = number1;
                    flag2 = 1;
                }
                d1 = Double.parseDouble(numbertemp);
                if(d1 < 0) {
                    textView.setText("无效输入");
                    pause();
                }
                else {
                    result = numbertemp = String.valueOf(sqrt(d1));
                    if(flag3 == 0)
                        number1 = result;
                    else if(flag3 == 1)
                        number2 = result;
                    numbertemp = adjust(numbertemp);
                    textView.setText(numbertemp);
                    //BtnBack.setEnabled(false);
                    flag5 = 1;                  //flag5：现在处于按了开方之后的状态，按数字或小数点会清零
                }
                break;
            case R.id.BtnSquare:
                if(num == 0 && flagPoint == 0 && flag3 == 0){         //没按过数，没按过符号，应该对上一次结果做平方
                    numbertemp = number1;
                    flag1 = 1;
                }
                else if(num == 0 && flagPoint == 0 && flag3 == 1) {
                    numbertemp = number1;
                    flag2 = 1;
                }
                d1 = Double.parseDouble(numbertemp);
                if(d1 > 1e8 || d1 < -1e8) {
                    textView.setText("溢出");
                    pause();
                }
                else {
                    BigDecimal f1 = new BigDecimal(numbertemp);
                    BigDecimal f2 = new BigDecimal(numbertemp);
                    result = numbertemp = f1.multiply(f2).toString();
                    if(flag3 == 0)
                        number1 = result;
                    else if(flag3 == 1)
                        number2 = result;
                    numbertemp = adjust(numbertemp);
                    textView.setText(numbertemp);
                    //BtnBack.setEnabled(false);
                    flag5 = 1;                  //flag5：现在处于按了x²之后的状态，按数字或小数点会清零
                }
                break;
            case R.id.BtnReciprocal:
                if(num == 0 && flagPoint == 0 && flag3 == 0){         //没按过数，没按过符号，应该对上一次结果取倒数
                    numbertemp = number1;
                    flag1 = 1;
                }
                else if(num == 0 && flagPoint == 0 && flag3 == 1) {
                    numbertemp = number1;
                    flag2 = 1;
                }
                d1 = Double.parseDouble(numbertemp);
                if(d1 == 0) {
                    textView.setText("除数不能为零");
                    pause();
                }
                else {
                    result = numbertemp = String.valueOf(1.0 / d1);
                    if(flag3 == 0)
                        number1 = result;
                    else if(flag3 == 1)
                        number2 = result;
                    numbertemp = adjust(numbertemp);
                    textView.setText(numbertemp);
                    //BtnBack.setEnabled(false);
                    flag5 = 1;                  //flag5：现在处于按了1/x之后的状态，按数字或小数点会清零
                }
                break;
            case R.id.BtnCE:
                if(flag4 == 1)
                    resume();
                if(flag5 == 1)
                    flag5 = 0;
                textView.setText("0");
                numbertemp = "0";
                if(num == 0 && flagPoint == 0 && flag3 == 0){         //没按过数，没按过符号，应该对上一次结果做平方
                    numbertemp = number1;
                    flag1 = 1;
                }
                else if(num == 0 && flagPoint == 0 && flag3 == 1) {
                    numbertemp = number1;
                    flag2 = 1;
                }
                num = 0;
                result = numbertemp = "0";
                if (flag3 == 0)
                    number1 = result;
                else if (flag3 == 1)
                    number2 = result;
                textView.setText(numbertemp);
                //BtnBack.setEnabled(false);
                break;
            case R.id.BtnC:
                if(flag4 == 1)
                    resume();
                numbertemp = result = "0";
                textView.setText("0");
                flag = flag1 = flag2 = flag3 = flag5 = point = flagPoint = 0;
                num = 0;
                number1 = number2 = number3 = "0";
                d1 = d2 = d3 = 0;
                break;
            case R.id.BtnBack:
                if(flag4 == 1) {
                    resume();
                    textView.setText("0");
                }
                if(flag1 == 0 && flag2 == 0 && flag3 == 0)
                    return;
                if(flag5 == 1)
                    return;
                if(flag4 == 1) {
                    resume();
                    textView.setText("0");
                }
                else {
                    if(numbertemp.equals("0") || numbertemp.equals("-0") || numbertemp.equals("-0.") ||  numbertemp.equals("0.") || numbertemp.length() == 1 || numbertemp.length() == 0) {
                        numbertemp = "0";
                        flagPoint = 0;
                        textView.setText("0");
                        num = 0;
                        return;
                    }
                    else {
                        int length = numbertemp.length();
                        String d = numbertemp.substring(length - 1, length);
                        if(d.equals("."))
                            flagPoint = 0;
                        result = numbertemp = numbertemp.substring(0, length - 1);
                        num = num - 1;
                        if(numbertemp.equals("-"))
                            numbertemp = "0";
                        if(flag3 == 0)
                            number1 = result;
                        else if(flag3 == 1)
                            number2 = result;
                        textView.setText(numbertemp);
                    }
                }
                break;
            case R.id.BtnDivide:
                //if(flag5 == 1)
                    //flag5 = 0;
                if(flag2 != 0)                         //第二操作数有值，需要先计算出结果，再继续
                    equal();
                if(flag3 == 0)
                    flag3 = 1;
                if(flag1 == 1) {                        //结束第一操作数的输入
                    number1 = numbertemp;
                    num = 0;
                    flagPoint = 0;
                    //numbertemp = "0";
                }
                flag5 = 1;
                flag = 4;
                break;
            case R.id.BtnMultiply:
                //if(flag5 == 1)
                    //flag5 = 0;
                if(flag2 != 0)                         //第二操作数有值，需要先计算出结果，再继续
                    equal();
                if(flag3 == 0)
                    flag3 = 1;
                if(flag1 == 1) {                        //结束第一操作数的输入
                    number1 = numbertemp;
                    num = 0;
                    flagPoint = 0;
                }
                flag5 = 1;
                flag = 3;
                break;
            case R.id.BtnMinus:
                //if(flag5 == 1)
                    //flag5 = 0;
                if(flag2 != 0)                         //第二操作数有值，需要先计算出结果，再继续
                    equal();
                if(flag3 == 0)
                    flag3 = 1;
                if(flag1 == 1) {                        //结束第一操作数的输入
                    number1 = numbertemp;
                    num = 0;
                    flagPoint = 0;
                }
                flag5 = 1;
                flag = 2;
                break;
            case R.id.BtnPlus:
                //if(flag5 == 1)
                    //flag5 = 0;
                if(flag2 != 0)                         //第二操作数有值，需要先计算出结果，再继续
                    equal();
                if(flag3 == 0)
                    flag3 = 1;
                if(flag1 == 1) {                        //结束第一操作数的输入
                    number1 = numbertemp;
                    num = 0;
                    flagPoint = 0;
                }
                flag5 = 1;
                flag = 1;
                break;
            case R.id.BtnSign:
                if(num == 0 && flagPoint == 0 && flag3 == 0){         //没按过数，没按过符号，应该对上一次结果加负号
                    numbertemp = number1;
                    flag1 = 1;
                    flag5 = 1;
                }
                else if(num == 0 && flagPoint == 0 && flag3 == 1) {
                    numbertemp = number1;
                    flag2 = 1;
                    flag5 = 1;
                }
                if(numbertemp.equals("0"))
                    return;
                if(Double.parseDouble(numbertemp) < 0) {
                    int length = numbertemp.length();
                    numbertemp = numbertemp.substring(1, length);
                }
                else
                    numbertemp = "-" + numbertemp;
                result = numbertemp;
                if(flag3 == 0)
                    number1 = result;
                else
                    number2 = result;
                int flagPoint2 = 0, k;
                int length = numbertemp.length();
                char[] c = new char[20];
                c = numbertemp.toCharArray();
                for(k = 0; k < length; k++)
                    if(c[k] == '.')
                        flagPoint2 = 1;
                if(flagPoint2 == 1)
                    length = length - 1;
                if(length > 16) {
                    if (flagPoint2 == 1)
                        k = 17;
                    else
                        k = 16;
                    if(Double.parseDouble(numbertemp) < 0)
                        k++;
                    numbertemp = numbertemp.substring(0, k);
                }
                textView.setText(numbertemp);
                break;
            case R.id.BtnPoint:
                if(flag5 == 1) {
                    numbertemp = "0";
                    flag5 = 0;
                }
                if(flagPoint == 0){                            //没小数点可以加小数点
                    if(flag3 == 0 && flag1 == 0){               //还没按过数就加个0
                        flag1 = 1;
                        numbertemp = "0";
                    }
                    else if(flag3 == 1 && flag2 == 0) {
                        flag2 = 1;
                        numbertemp = "0";
                    }
                    numbertemp = numbertemp + ".";
                    flagPoint = 1;                              //现在有小数点了
                    point = num;								//记录小数点的位置,小数点在第point位后
                    textView.setText(numbertemp);
                }
                break;
            case R.id.BtnEqual:
                if(flag4 == 1) {
                    resume();
                    textView.setText("0");
                }
                if(flag5 == 1)
                    flag5 = 0;
                equal();
                break;
        }
    }
    public void equal() {
        //flag不能清零！
        //adjust(number1);
        //adjust(number2);
        if(flag1 == 0 && flag2 == 0 && flag3 == 0)   //实现直接按等号的逻辑，number1已经在上一次计算继承了result的值
            //number1在上一次计算中继承了result的值
            //flag在上一次计算末尾后没有清零
            number2 = number3;
        else if(flag2 == 0 && flag3 == 0) {            //实现2=2的逻辑
            number1 = numbertemp;
            flag = 1;
            number2 = "0";
            /*number1 = numbertemp;
            num = 0;
            flagPoint = 0;
            number2 = number3;
            */
        }
        else if(flag1 == 0 && flag2 == 0)               //实现+=的逻辑
            //number1在上一次计算的末尾被赋值成了result
            //flag已经输入
            number2 = number1;
        else if(flag2 == 0)                              //实现2+=的逻辑
            //number1已经输入（在按符号时赋了值）
            //flag已经输入
            number2 = number1;
        else {                                               //正常情况number2应该继承numbertemp
            //number1已经输入（在按符号时赋了值）
            //flag已经输入
            number2 = numbertemp;
        }
        BigDecimal f1 = new BigDecimal(number1);
        BigDecimal f2 = new BigDecimal(number2);
        d1 = Double.parseDouble(number1);
        d2 = Double.parseDouble(number2);
        switch (flag) {
            case 1:
                result = f1.add(f2).toString();
                d3 = d1 + d2;
                break;
            case 2:
                result = f1.subtract(f2).toString();
                d3 = d1 - d2;
                break;
            case 3:
                result = f1.multiply(f2).toString();
                d3 = d1 * d2;
                break;
            case 4:
                if(number2.equals("0"))
                    if(number1.equals("0")) {
                        textView.setText("结果未定义");
                        pause();
                        return;
                    }
                    else {
                        textView.setText("除数不能为零");
                        pause();
                        return;
                    }
                else{
                    d3 = d1 / d2;
                    result = String.valueOf(d3);
                }
                break;
        }
        if(d3 > 1e17) {
            textView.setText("溢出");
            pause();
        }
        else {
            result = adjust(result);
            number1 = result;                                            //结果暂存到第一操作数中
            number3 = number2;                                           //第二操作数暂时存在number2中
            flag1 = flag2 = flag3 = flagPoint = num = 0;               //清零操作，注意flag不能清零
            numbertemp = "0";
            textView.setText(result);
        }
    }
}
