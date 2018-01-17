package com.example.joaolopes.gestbox;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by joaolopes on 17/01/18.
 */

public class SliderAdapter extends PagerAdapter{
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.login,
            R.drawable.menu,
            R.drawable.treinos,
            R.drawable.aulas
    };


    public String[] slide_headings={
            "Bem-vindo",
            "Explore",
            "Fique sempre atualizado",
            "Fique sempre atualizado"
    };

    public String[] slide_text={
            "Se gosta de BTT esta é uma app pensada em si!",
            "Explore e fique a conhecer novos percursos através desta comunidade! Faça novos amigos, marque uma volta, isto tudo através da nossa aplicação.",
            "Na nossa aplicação temos duas seções para a divulgação da modalidade quer de noticias quer de eventos.",
            "Na nossa aplicação temos duas seções para a divulgação da modalidade quer de noticias quer de eventos."
    };


    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView)view.findViewById(R.id.SlideImage);
        TextView slideHeading = (TextView)view.findViewById(R.id.SlideHeading);
        TextView slideText = (TextView)view.findViewById(R.id.SlideText);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideText.setText(slide_text[position]);

        container.addView(view);

        return view;
    };

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
