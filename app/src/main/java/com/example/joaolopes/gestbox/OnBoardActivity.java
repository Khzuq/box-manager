package com.example.joaolopes.gestbox;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnBoardActivity extends AppCompatActivity {
    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;


    private TextView txvCancelar, txvSeguinte;
    private int mCurrentPage;

    private TextView[] mDots;

    private SliderAdapter sliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);

        mSlideViewPager = (ViewPager)findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);



        mSlideViewPager.addOnPageChangeListener(viewListener);

        txvCancelar = (TextView)findViewById(R.id.txvCancelar);
        txvSeguinte = (TextView)findViewById(R.id.txvSeguinte);

        txvSeguinte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //== numero de paginas que vamos ter
                if(mCurrentPage==7){
                    executarActivity(LoginActivity.class);
                }

                mSlideViewPager.setCurrentItem(mCurrentPage+1);

            }
        });

        txvCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage-1);
            }
        });
    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            mCurrentPage = position;

            if(position==0){
                txvSeguinte.setEnabled(true);
                txvCancelar.setEnabled(false);
                txvCancelar.setVisibility(View.INVISIBLE);
            }
            else if(position == 2){
                txvSeguinte.setEnabled(true);
                txvCancelar.setEnabled(true);
                txvCancelar.setVisibility(View.VISIBLE);

                txvSeguinte.setText("Terminar");
                txvCancelar.setText("Voltar");

            }
            else{
                txvSeguinte.setEnabled(true);
                txvCancelar.setEnabled(true);
                txvCancelar.setVisibility(View.VISIBLE);

                txvSeguinte.setText("Seguinte");
                txvCancelar.setText("Voltar");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void executarActivity(Class<?> subAtividade){
        Intent x = new Intent(this, subAtividade);
        startActivity(x);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerMenu);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    moveTaskToBack(true);
                }
                return true;
        }
        return false;
    }
}
