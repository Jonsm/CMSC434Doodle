package com.example.jon.cmsc434doodle;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ActionMenuView amvMenu;
    private int mainColor = Color.argb(255,0,0,0);
    private int mainColorTmp = Color.argb(255,0,0,0);
    private int brushSize = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        amvMenu = (ActionMenuView) myToolbar.findViewById(R.id.amvMenu);
        amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.leftalign_menu, amvMenu.getMenu());
        inflater.inflate(R.menu.rightalign_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ActionMenuItemView brush1 = (ActionMenuItemView) findViewById(R.id.brush_1);
        ActionMenuItemView brush2 = (ActionMenuItemView) findViewById(R.id.brush_2);
        ActionMenuItemView brush3 = (ActionMenuItemView) findViewById(R.id.brush_3);

        switch (item.getItemId()) {
            case R.id.clear:
                clearDialog();
                return true;

            case R.id.brush_1:
                brush1.setIcon(getResources().getDrawable(R.drawable.ic_lens_black_12dp));
                brush2.setIcon(getResources().getDrawable(R.drawable.ic_panorama_fish_eye_black_24dp));
                brush3.setIcon(getResources().getDrawable(R.drawable.ic_panorama_fish_eye_black_32dp));
                brushSize = 1;
                return true;

            case R.id.brush_2:
                brush1.setIcon(getResources().getDrawable(R.drawable.ic_panorama_fish_eye_black_12dp));
                brush2.setIcon(getResources().getDrawable(R.drawable.ic_lens_black_24dp));
                brush3.setIcon(getResources().getDrawable(R.drawable.ic_panorama_fish_eye_black_32dp));
                brushSize = 2;
                return true;

            case R.id.brush_3:
                brush1.setIcon(getResources().getDrawable(R.drawable.ic_panorama_fish_eye_black_12dp));
                brush2.setIcon(getResources().getDrawable(R.drawable.ic_panorama_fish_eye_black_24dp));
                brush3.setIcon(getResources().getDrawable(R.drawable.ic_lens_black_32dp));
                brushSize = 3;
                return true;

            case R.id.color:
                colorDialog();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void clearDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Clear Drawing?");

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("cancel");
            }
        });

        alert.setPositiveButton("Clear", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("clear");
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void colorDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Color");
        // this is set the view from XML inside AlertDialog
        View alertLayout = getLayoutInflater().inflate(R.layout.color_popup_layout, null);
        alert.setView(alertLayout);

        View colorIndicator = alertLayout.findViewById(R.id.colorIndicator);
        colorIndicator.setBackgroundColor(mainColor);
        final ActionMenuItemView colorButton = (ActionMenuItemView) findViewById(R.id.color);

        SeekBar barR = (SeekBar) alertLayout.findViewById(R.id.seekBarR);
        barR.setProgress(Color.red(mainColor));
        barR.setOnSeekBarChangeListener(new colorSeekBarListener(2, colorIndicator));
        SeekBar barG = (SeekBar) alertLayout.findViewById(R.id.seekBarG);
        barG.setProgress(Color.green(mainColor));
        barG.setOnSeekBarChangeListener(new colorSeekBarListener(1, colorIndicator));
        SeekBar barB = (SeekBar) alertLayout.findViewById(R.id.seekBarB);
        barB.setProgress(Color.blue(mainColor));
        barB.setOnSeekBarChangeListener(new colorSeekBarListener(0, colorIndicator));
        SeekBar barA = (SeekBar) alertLayout.findViewById(R.id.seekBarA);
        barA.setProgress(Color.alpha(mainColor));
        barA.setOnSeekBarChangeListener(new colorSeekBarListener(3, colorIndicator));

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainColorTmp = mainColor;
            }
        });

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainColor = mainColorTmp;
                changeMenuIconColor(colorButton, mainColor);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void changeMenuIconColor(final ActionMenuItemView icon, final int color) {
        int drawablesCount = icon.getCompoundDrawables().length;
        for (int k = 0; k < drawablesCount; k++) {
            if (icon.getCompoundDrawables()[k] != null) {
                final int finalK = k;
                icon.post(new Runnable() {
                    @Override
                    public void run() {
                        icon.getCompoundDrawables()[finalK].setColorFilter(new LightingColorFilter(Color.BLACK, color));
                    }
                });
            }
        }
    }

    public class colorSeekBarListener implements SeekBar.OnSeekBarChangeListener {
        int power;
        View colorIndicator;

        public colorSeekBarListener(int power, View colorIndicator) {
            this.power = power;
            this.colorIndicator = colorIndicator;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
            mainColorTmp = (mainColorTmp & ~(0xff << 8*power)) | (progressValue << 8*power);
            colorIndicator.setBackgroundColor(mainColorTmp);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
}
