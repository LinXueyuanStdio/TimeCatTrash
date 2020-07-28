package com.timecat.module.main.miniapp.apps;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.timecat.component.alert.ToastUtil;
import com.timecat.module.main.R;
import com.timecat.module.main.app.calculator.CustomFunction;
import com.timecat.module.main.app.calculator.ExpressionBuilder;
import com.timecat.module.main.miniapp.adapters.ListItemAdapter;
import com.timecat.module.main.miniapp.models.ListItemModel;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import com.timecat.plugin.window.StandOutFlags;
import com.timecat.plugin.window.StandOutWindow;
import com.timecat.plugin.window.Window;
import com.timecat.plugin.window.WindowAgreement;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class CalculatorApp extends StandOutWindow {
    public static int id = 2;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_calculator);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_calculator);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_calculator);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.calculator;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_calculator);
    }

    public String getHiddenNotificationMessage(int id) {
        return getString(R.string.main_miniapp_mininized);
    }

    public Intent getHiddenNotificationIntent(int id) {
        return WindowAgreement.getShowIntent(this, getClass(), id);
    }

    public Animation getShowAnimation(int id) {
        if (isExistingId(id)) {
            return AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        }
        return super.getShowAnimation(id);
    }

    public Animation getHideAnimation(int id) {
        return AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
    }

    public StandOutLayoutParams getParams(int id, Window window) {
        int h = SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT").equals("") ? 200 : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT"));
        int w = SettingsUtils.GetValue(window.getContext(), getAppName() + "WIDTH").equals("") ? 200 : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "WIDTH"));
        int x = SettingsUtils.GetValue(window.getContext(), getAppName() + "XPOS").equals("") ? Integer.MIN_VALUE : (int) Float.parseFloat(SettingsUtils.GetValue(window.getContext(), getAppName() + "XPOS"));
        int y = SettingsUtils.GetValue(window.getContext(), getAppName() + "YPOS").equals("") ? Integer.MIN_VALUE : (int) Float.parseFloat(SettingsUtils.GetValue(window.getContext(), getAppName() + "YPOS"));
        if (h < GeneralUtils.GetDP(window.getContext(), 200)) {
            h = GeneralUtils.GetDP(window.getContext(), 200);
        }
        if (w < GeneralUtils.GetDP(window.getContext(), 200)) {
            w = GeneralUtils.GetDP(window.getContext(), 200);
        }
        return new StandOutLayoutParams(id, w, h, x, y, GeneralUtils.GetDP(window.getContext(), 56), GeneralUtils.GetDP(window.getContext(), 56));
    }

    public int getFlags(int id) {
        return (((StandOutFlags.FLAG_DECORATION_SYSTEM | StandOutFlags.FLAG_BODY_MOVE_ENABLE) | StandOutFlags.FLAG_WINDOW_HIDE_ENABLE) | StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP) | StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE;
    }

    public List<DropDownListItem> getDropDownItems(final int id) {
        List<DropDownListItem> items = new ArrayList<>();
        items.add(new DropDownListItem(R.mipmap.menu_standard, getString(R.string.main_miniapp_Standard), new Runnable() {
            public void run() {
                ((CalculatorCreator) GeneralUtils.CalculatorMap.get(id)).awesomePager.setCurrentItem(1);
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_advanced, getString(R.string.main_miniapp_Advanced), new Runnable() {
            public void run() {
                ((CalculatorCreator) GeneralUtils.CalculatorMap.get(id)).awesomePager.setCurrentItem(2);
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_recent_notes, getString(R.string.main_miniapp_History), new Runnable() {
            public void run() {
                ((CalculatorCreator) GeneralUtils.CalculatorMap.get(id)).awesomePager.setCurrentItem(0);
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_delete, getString(R.string.main_miniapp_clearHistory), new Runnable() {
            public void run() {
                SettingsUtils.SetValue(context, "CALC_HISTORY", "");
                ((CalculatorCreator) GeneralUtils.CalculatorMap.get(id)).awesomeAdapter.fillHistory();
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_copy, getString(R.string.main_miniapp_Copy), new Runnable() {
            public void run() {
                if (((CalculatorCreator) GeneralUtils.CalculatorMap.get(id)).textExpr.getText().toString().length() != 0) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Result", ((CalculatorCreator) GeneralUtils.CalculatorMap.get(id)).textExpr.getText().toString());
                    ToastUtil.i("Copied to clipboard");
                    clipboard.setPrimaryClip(clip);
                    return;
                }
                ToastUtil.i("No content to copy");
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_paste, getString(R.string.main_miniapp_Paste), new Runnable() {
            public void run() {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard.hasPrimaryClip()) {
                    ((CalculatorCreator) GeneralUtils.CalculatorMap.get(id)).textExpr.setText(((CalculatorCreator) GeneralUtils.CalculatorMap.get(id)).textExpr.getText().toString() + clipboard.getPrimaryClip().getItemAt(0).getText());
                    return;
                }
                ToastUtil.i("No content to paste");
            }
        }));
        items.add(new DropDownListItem(R.mipmap.menu_share, getString(R.string.main_miniapp_Share), new Runnable() {
            public void run() {
                if (((CalculatorCreator) GeneralUtils.CalculatorMap.get(id)).textExpr.getText().toString().length() != 0) {
                    Intent sharingIntent = new Intent("android.intent.action.SEND");
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra("android.intent.extra.TEXT", ((CalculatorCreator) GeneralUtils.CalculatorMap.get(id)).textExpr.getText().toString());
                    sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(sharingIntent, getString(R.string.main_miniapp_Share)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    return;
                }
                ToastUtil.i("No content to share");
            }
        }));
        return items;
    }

    public void createAndAttachView(int id, FrameLayout frame) {
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_calculator, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        GeneralUtils.CalculatorMap.put(id, new CalculatorCreator());
    }

    public class CalculatorCreator {
        Button btn0;
        Button btn1;
        Button btn2;
        Button btn3;
        Button btn4;
        Button btn5;
        Button btn6;
        Button btn7;
        Button btn8;
        Button btn9;
        Button btnAllMult;
        Button btnBracketsC1;
        Button btnBracketsC2;
        Button btnBracketsO1;
        Button btnBracketsO2;
        RelativeLayout btnClear1;
        RelativeLayout btnClear2;
        Button btnCos;
        Button btnDiv;
        Button btnDot;
        Button btnE;
        RelativeLayout btnEq1;
        RelativeLayout btnEq2;
        Button btnLn;
        Button btnLog;
        Button btnMinus;
        Button btnMode;
        Button btnMult;
        Button btnPerc;
        Button btnPi;
        Button btnPlus;
        Button btnReciprocal;
        Button btnSin;
        Button btnSq;
        Button btnSqrt;
        Button btnTan;
        Button btnXRoot;
        Button btnXSq;
        boolean isNewCalc = true;
        String sciMode = "DEG";
        TextView textExpr;
        private AwesomePagerAdapter awesomeAdapter = new AwesomePagerAdapter();
        private ViewPager awesomePager;
        private ListItemAdapter listAdapter;
        private ArrayList<ListItemModel> listItems;
        private ListView listView;
        private TextView tvNoFiles;

        public CalculatorCreator() {
            this.awesomePager = (ViewPager) publicView.findViewById(R.id.awesomepager);
            this.awesomePager.setAdapter(this.awesomeAdapter);
            this.awesomePager.setOnPageChangeListener(new OnPageChangeListener() {
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                public void onPageSelected(int position) {
                    if (position == 0) {
                        ((CalculatorCreator) GeneralUtils.CalculatorMap.get(publicId)).awesomeAdapter.fillHistory();
                        listAdapter.refreshItems();
                    }
                }

                public void onPageScrollStateChanged(int state) {
                }
            });
            this.awesomePager.setCurrentItem(1);
            this.textExpr = (TextView) publicView.findViewById(R.id.textViewExpr);
            this.textExpr.setMovementMethod(new ScrollingMovementMethod());
            this.sciMode = SettingsUtils.GetValue(context, "CALC_MODE");
        }

        class AwesomePagerAdapter extends PagerAdapter {
            private int NUM_AWESOME_VIEWS = 3;
            private View fragment1;
            private View fragment2;
            private View fragment3;

            public AwesomePagerAdapter() {
                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                this.fragment1 = inflater.inflate(R.layout.app_calculator_fragment1, null);
                this.fragment2 = inflater.inflate(R.layout.app_calculator_fragment2, null);
                this.fragment3 = inflater.inflate(R.layout.app_calculator_fragment3, null);
                listView = (ListView) this.fragment1.findViewById(R.id.listView);
                tvNoFiles = (TextView) this.fragment1.findViewById(R.id.textViewNoFiles);
                listItems = new ArrayList<>();
                listAdapter = new ListItemAdapter(context, listItems);
                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    AwesomePagerAdapter.this.addText(((ListItemModel) parent.getItemAtPosition(position)).getSubtitle());
                    awesomePager.setCurrentItem(1);
                });
                findButtons();
                setClicks();
            }

            public String getExpression() {
                return textExpr.getText().toString();
            }

            void findButtons() {
                btn0 = (Button) this.fragment2.findViewById(R.id.button0);
                btn1 = (Button) this.fragment2.findViewById(R.id.button1);
                btn2 = (Button) this.fragment2.findViewById(R.id.button2);
                btn3 = (Button) this.fragment2.findViewById(R.id.button3);
                btn4 = (Button) this.fragment2.findViewById(R.id.button4);
                btn5 = (Button) this.fragment2.findViewById(R.id.button5);
                btn6 = (Button) this.fragment2.findViewById(R.id.button6);
                btn7 = (Button) this.fragment2.findViewById(R.id.button7);
                btn8 = (Button) this.fragment2.findViewById(R.id.button8);
                btn9 = (Button) this.fragment2.findViewById(R.id.button9);
                btnPlus = (Button) this.fragment2.findViewById(R.id.buttonPlus);
                btnMinus = (Button) this.fragment2.findViewById(R.id.buttonMinus);
                btnMult = (Button) this.fragment2.findViewById(R.id.buttonMultiply);
                btnDiv = (Button) this.fragment2.findViewById(R.id.buttonDivide);
                btnDot = (Button) this.fragment2.findViewById(R.id.buttonDot);
                btnBracketsO1 = (Button) this.fragment2.findViewById(R.id.buttonBrO1);
                btnBracketsC1 = (Button) this.fragment2.findViewById(R.id.buttonBrC1);
                btnClear1 = (RelativeLayout) this.fragment2.findViewById(R.id.buttonBackspace1);
                btnEq1 = (RelativeLayout) this.fragment2.findViewById(R.id.buttonEquals1);
                btnSq = (Button) this.fragment3.findViewById(R.id.buttonSq);
                btnSqrt = (Button) this.fragment3.findViewById(R.id.buttonSqRoot);
                btnMode = (Button) this.fragment3.findViewById(R.id.buttonDR);
                btnXSq = (Button) this.fragment3.findViewById(R.id.buttonXPow);
                btnXRoot = (Button) this.fragment3.findViewById(R.id.buttonXRoot);
                btnReciprocal = (Button) this.fragment3.findViewById(R.id.buttonReciprocal);
                btnAllMult = (Button) this.fragment3.findViewById(R.id.buttonMultiplyAll);
                btnSin = (Button) this.fragment3.findViewById(R.id.buttonSin);
                btnCos = (Button) this.fragment3.findViewById(R.id.buttonCos);
                btnTan = (Button) this.fragment3.findViewById(R.id.buttonTan);
                btnPi = (Button) this.fragment3.findViewById(R.id.buttonPi);
                btnE = (Button) this.fragment3.findViewById(R.id.buttonE);
                btnPerc = (Button) this.fragment3.findViewById(R.id.buttonPerc);
                btnLn = (Button) this.fragment3.findViewById(R.id.buttonLn);
                btnLog = (Button) this.fragment3.findViewById(R.id.buttonLog);
                btnBracketsO2 = (Button) this.fragment3.findViewById(R.id.buttonBrO2);
                btnBracketsC2 = (Button) this.fragment3.findViewById(R.id.buttonBrC2);
                btnClear2 = (RelativeLayout) this.fragment3.findViewById(R.id.buttonBackspace2);
                btnEq2 = (RelativeLayout) this.fragment3.findViewById(R.id.buttonEquals2);
            }

            void setClicks() {
                btn0.setOnClickListener(new C02282());
                btn1.setOnClickListener(new C02293());
                btn2.setOnClickListener(new C02304());
                btn3.setOnClickListener(new C02315());
                btn4.setOnClickListener(new C02326());
                btn5.setOnClickListener(new C02337());
                btn6.setOnClickListener(new C02348());
                btn7.setOnClickListener(new C02359());
                btn8.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("8");
                    }
                });
                btn9.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("9");
                    }
                });
                btnPlus.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("+");
                    }
                });
                btnMinus.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("-");
                    }
                });
                btnMult.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("*");
                    }
                });
                btnDiv.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("/");
                    }
                });
                btnDot.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        String s = textExpr.getText().toString();
                        if (s.length() <= 0) {
                            AwesomePagerAdapter.this.addText("0.");
                        } else if (Character.isDigit(s.charAt(s.length() - 1))) {
                            AwesomePagerAdapter.this.addText(".");
                        } else {
                            AwesomePagerAdapter.this.addText("0.");
                        }
                    }
                });
                btnBracketsO1.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("(");
                    }
                });
                btnBracketsC1.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText(")");
                    }
                });
                btnClear1.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        String s = textExpr.getText().toString();
                        if (s.length() > 0) {
                            textExpr.setText(s.subSequence(0, s.length() - 1));
                        }
                    }
                });
                btnClear1.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        textExpr.setText("");
                        return true;
                    }
                });
                btnEq1.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.evaluate();
                    }
                });
                btnSq.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("^2");
                    }
                });
                btnSqrt.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("^(1/2)");
                    }
                });
                btnMode.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (sciMode.equals("DEG")) {
                            SettingsUtils.SetValue(context, "CALC_MODE", "RAD");
                            sciMode = "RAD";
                            ToastUtil.i("Changed to Radian Mode".toString());
                            return;
                        }
                        SettingsUtils.SetValue(context, "CALC_MODE", "DEG");
                        sciMode = "DEG";
                        ToastUtil.i("Changed to Degree Mode".toString());
                    }
                });
                btnXSq.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("^");
                    }
                });
                btnXRoot.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("^(1/");
                    }
                });
                btnReciprocal.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        textExpr.setText("1/(" + textExpr.getText() + ")");
                    }
                });
                btnAllMult.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        textExpr.setText("(" + textExpr.getText() + ")*");
                    }
                });
                btnSin.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (sciMode.equals("DEG")) {
                            AwesomePagerAdapter.this.addText("sin_d(");
                        } else {
                            AwesomePagerAdapter.this.addText("sin(");
                        }
                    }
                });
                btnCos.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (sciMode.equals("DEG")) {
                            AwesomePagerAdapter.this.addText("cos_d(");
                        } else {
                            AwesomePagerAdapter.this.addText("cos(");
                        }
                    }
                });
                btnTan.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (sciMode.equals("DEG")) {
                            AwesomePagerAdapter.this.addText("tan_d(");
                        } else {
                            AwesomePagerAdapter.this.addText("tan(");
                        }
                    }
                });
                btnPi.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("π");
                    }
                });
                btnE.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("e");
                    }
                });
                btnPerc.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("%");
                    }
                });
                btnLn.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("ln(");
                    }
                });
                btnLog.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("log(");
                    }
                });
                btnBracketsO2.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText("(");
                    }
                });
                btnBracketsC2.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.addText(")");
                    }
                });
                btnClear2.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        String s = textExpr.getText().toString();
                        if (s.length() > 0) {
                            textExpr.setText(s.subSequence(0, s.length() - 1));
                        }
                    }
                });
                btnClear2.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        textExpr.setText("");
                        return true;
                    }
                });
                btnEq2.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        AwesomePagerAdapter.this.evaluate();
                    }
                });
            }

            void addText(String str) {
                if (isNewCalc) {
                    if (Character.isDigit(str.charAt(0))) {
                        textExpr.setText(str);
                    } else {
                        textExpr.setText(textExpr.getText() + str);
                    }
                    isNewCalc = false;
                    return;
                }
                textExpr.setText(textExpr.getText() + str);
            }

            void evaluate() {
                String s = textExpr.getText().toString();
                if (s.length() > 0) {
                    try {
                        s = resolvePerc(s);
                        CustomFunction sinDeg = new CustomFunction("sin_d") {
                            public double applyFunction(double... arg0) {
                                double piVal = 0.017453292519943295d * arg0[0];
                                if (piVal % 3.141592653589793d == 0.0d) {
                                    return 0.0d;
                                }
                                return Math.sin(piVal);
                            }
                        };
                        CustomFunction cosDeg = new CustomFunction("cos_d") {
                            public double applyFunction(double... arg0) {
                                double piVal = 0.017453292519943295d * arg0[0];
                                if (piVal % 3.141592653589793d == 1.5707963267948966d) {
                                    return 0.0d;
                                }
                                return Math.cos(piVal);
                            }
                        };
                        CustomFunction tanDeg = new CustomFunction("tan_d") {
                            public double applyFunction(double... arg0) {
                                double sin;
                                double cos;
                                double piVal = 0.017453292519943295d * arg0[0];
                                if (piVal % 3.141592653589793d == 0.0d) {
                                    sin = 0.0d;
                                } else {
                                    sin = Math.sin(piVal);
                                }
                                if (piVal % 3.141592653589793d == 1.5707963267948966d) {
                                    cos = 0.0d;
                                } else {
                                    cos = Math.cos(piVal);
                                }
                                return sin / cos;
                            }
                        };
                        CustomFunction mysin = new CustomFunction("sin") {
                            public double applyFunction(double... arg0) {
                                if (arg0[0] % 3.141592653589793d == 0.0d) {
                                    return 0.0d;
                                }
                                return Math.sin(arg0[0]);
                            }
                        };
                        CustomFunction mycos = new CustomFunction("cos") {
                            public double applyFunction(double... arg0) {
                                if (arg0[0] % 3.141592653589793d == 1.5707963267948966d) {
                                    return 0.0d;
                                }
                                return Math.cos(arg0[0]);
                            }
                        };
                        double result = new ExpressionBuilder(s).withCustomFunction(sinDeg).withCustomFunction(cosDeg).withCustomFunction(tanDeg).withCustomFunction(mysin).withCustomFunction(mycos).withCustomFunction(new CustomFunction("tan") {
                            public double applyFunction(double... arg0) {
                                double sin;
                                double cos;
                                if (arg0[0] % 3.141592653589793d == 0.0d) {
                                    sin = 0.0d;
                                } else {
                                    sin = Math.sin(arg0[0]);
                                }
                                if (arg0[0] % 3.141592653589793d == 1.5707963267948966d) {
                                    cos = 0.0d;
                                } else {
                                    cos = Math.cos(arg0[0]);
                                }
                                return sin / cos;
                            }
                        }).build().calculate();
                        NumberFormat formatter = new DecimalFormat("#0.##########");
                        NumberFormat formatterE = new DecimalFormat("#0.0#######E0");
                        String resultStr = "";
                        if ((Math.abs(result) <= 1.0E-10d || Math.abs(result) >= 1.0E9d) && result != 0.0d) {
                            resultStr = formatterE.format(result);
                        } else {
                            resultStr = formatter.format(result);
                        }
                        resultStr = resultStr.replace(",", ".");
                        saveCalculation(s, resultStr);
                        textExpr.setText(resultStr);
                        isNewCalc = true;
                    } catch (Exception e) {
                        if (e.getMessage() == null) {
                            ToastUtil.i("Invalid Expression");
                        } else if (e.getMessage().startsWith("Division by zero")) {
                            ToastUtil.i("Ans = INFINITY");
                        } else {
                            ToastUtil.i("Invalid Expression");
                        }
                    }
                }
            }

            String resolvePerc(String s) {
                if (s.length() > 0) {
                    return s.replace("%", "*0.01").replace("π", "(3.141592653589793)").replace("e", "(2.718281828459045)").replace("ln", "LOG").replace("log", "(" + Math.log10(2.718281828459045d) + ")*LOG").toLowerCase();
                }
                return s;
            }

            void saveCalculation(String exp, String result) {
                SettingsUtils.SetValue(context, "CALC_HISTORY", exp + " = " + result + "$" + SettingsUtils.GetValue(context, "CALC_HISTORY"));
            }

            void fillHistory() {
                try {
                    String hist = SettingsUtils.GetValue(context, "CALC_HISTORY");
                    if (hist.contains("$")) {
                        String[] items = hist.split("[$]");
                        listItems.clear();
                        for (String split : items) {
                            String[] parts = split.split("[=]");
                            listItems.add(new ListItemModel((int) R.mipmap.calculator, parts[0].trim(), parts[1].trim()));
                        }
                        listAdapter.refreshItems();
                        if (listItems.size() == 0) {
                            listView.setVisibility(View.INVISIBLE);
                            tvNoFiles.setVisibility(View.VISIBLE);
                            return;
                        }
                        listView.setVisibility(View.VISIBLE);
                        tvNoFiles.setVisibility(View.INVISIBLE);
                        return;
                    }
                    listView.setVisibility(View.INVISIBLE);
                    tvNoFiles.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                }
            }

            public Object instantiateItem(ViewGroup collection, int position) {
                fillHistory();
                listAdapter.refreshItems();
                if (position == 0) {
                    collection.addView(this.fragment1, 0);
                    return this.fragment1;
                } else if (position == 1) {
                    collection.addView(this.fragment2, 0);
                    return this.fragment2;
                } else {
                    collection.addView(this.fragment3, 0);
                    return this.fragment3;
                }
            }

            public int getCount() {
                return this.NUM_AWESOME_VIEWS;
            }

            public void destroyItem(ViewGroup collection, int position, Object view) {
                collection.removeView((View) view);
            }

            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            public void finishUpdate(ViewGroup arg0) {
            }

            public void restoreState(Parcelable arg0, ClassLoader arg1) {
            }

            public Parcelable saveState() {
                return null;
            }

            public void startUpdate(ViewGroup arg0) {
            }

            class C02282 implements OnClickListener {
                C02282() {
                }

                public void onClick(View v) {
                    AwesomePagerAdapter.this.addText("0");
                }
            }

            class C02293 implements OnClickListener {
                C02293() {
                }

                public void onClick(View v) {
                    AwesomePagerAdapter.this.addText("1");
                }
            }

            class C02304 implements OnClickListener {
                C02304() {
                }

                public void onClick(View v) {
                    AwesomePagerAdapter.this.addText("2");
                }
            }

            class C02315 implements OnClickListener {
                C02315() {
                }

                public void onClick(View v) {
                    AwesomePagerAdapter.this.addText("3");
                }
            }

            class C02326 implements OnClickListener {
                C02326() {
                }

                public void onClick(View v) {
                    AwesomePagerAdapter.this.addText("4");
                }
            }

            class C02337 implements OnClickListener {
                C02337() {
                }

                public void onClick(View v) {
                    AwesomePagerAdapter.this.addText("5");
                }
            }

            class C02348 implements OnClickListener {
                C02348() {
                }

                public void onClick(View v) {
                    AwesomePagerAdapter.this.addText("6");
                }
            }

            class C02359 implements OnClickListener {
                C02359() {
                }

                public void onClick(View v) {
                    AwesomePagerAdapter.this.addText("7");
                }
            }
        }
    }
}
