package com.kashmirinfo.yesstudyinmalaysia;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class Ui {
    public static final int GREEN = Color.rgb(11,107,87);
    public static final int GREEN_DARK = Color.rgb(7,61,53);
    public static final int GOLD = Color.rgb(244,201,93);
    public static final int TEXT = Color.rgb(25,42,39);
    public static final int MUTED = Color.rgb(92,109,106);
    public static final int BG = Color.rgb(245,249,248);
    public static final int WHITE = Color.WHITE;
    public static final int BORDER = Color.rgb(218,230,227);
    public static int dp(Context c,int n){return Math.round(n*c.getResources().getDisplayMetrics().density);}
    public static GradientDrawable box(int fill,float radius,int strokeColor){
        GradientDrawable g=new GradientDrawable();g.setColor(fill);g.setCornerRadius(radius);if(strokeColor!=0)g.setStroke(1,strokeColor);return g;
    }
    public static TextView text(Context c,String s,int sp,boolean bold){
        TextView v=new TextView(c);v.setText(s==null?"":s);v.setTextSize(sp);v.setTextColor(TEXT);v.setLineSpacing(0,1.12f);if(bold)v.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return v;
    }
    public static TextView muted(Context c,String s,int sp){TextView v=text(c,s,sp,false);v.setTextColor(MUTED);return v;}
    public static LinearLayout column(Context c){LinearLayout l=new LinearLayout(c);l.setOrientation(LinearLayout.VERTICAL);return l;}
    public static LinearLayout card(Context c){LinearLayout l=column(c);l.setPadding(dp(c,16),dp(c,16),dp(c,16),dp(c,16));l.setBackground(box(WHITE,dp(c,18),BORDER));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(0,0,0,dp(c,12));l.setLayoutParams(p);return l;}
    public static Button button(Context c,String s,boolean primary){
        Button b=new Button(c);b.setText(s);b.setAllCaps(false);b.setTextSize(15);b.setTextColor(primary?Color.WHITE:GREEN_DARK);b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);b.setBackground(box(primary?GREEN:Color.WHITE,dp(c,12),primary?0:BORDER));b.setPadding(dp(c,14),dp(c,10),dp(c,14),dp(c,10));return b;
    }
    public static EditText input(Context c,String hint){EditText e=new EditText(c);e.setHint(hint);e.setTextSize(15);e.setTextColor(TEXT);e.setHintTextColor(Color.rgb(139,153,150));e.setSingleLine(true);e.setPadding(dp(c,14),dp(c,12),dp(c,14),dp(c,12));e.setBackground(box(Color.WHITE,dp(c,12),BORDER));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(0,0,0,dp(c,10));e.setLayoutParams(p);return e;}
    public static EditText password(Context c,String hint){EditText e=input(c,hint);e.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);return e;}
    public static EditText multiline(Context c,String hint){EditText e=input(c,hint);e.setSingleLine(false);e.setMinLines(3);e.setGravity(Gravity.TOP);return e;}
    public static void space(LinearLayout l,Context c,int h){View v=new View(c);l.addView(v,new LinearLayout.LayoutParams(1,dp(c,h)));}
    public static void addLabel(LinearLayout l,Context c,String s){TextView v=muted(c,s,13);v.setTypeface(Typeface.DEFAULT,Typeface.BOLD);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(0,dp(c,4),0,dp(c,5));l.addView(v,p);}
    public static TextView pill(Context c,String s){TextView v=text(c,s,12,true);v.setTextColor(GREEN_DARK);v.setPadding(dp(c,10),dp(c,6),dp(c,10),dp(c,6));v.setBackground(box(Color.rgb(231,245,241),dp(c,30),0));return v;}
    private Ui(){}
}