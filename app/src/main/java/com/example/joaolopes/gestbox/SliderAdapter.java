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
            R.drawable.aulas,
            R.drawable.filtro_data,
            R.drawable.add_pr,
            R.drawable.pr,
            R.drawable.mapa
    };


    public String[] slide_headings={
            "Bem-vindo",
            "Explore",
            "Todos os treinos",
            "Fique sempre informado",
            "Use o nosso filtro",
            "Adicione o seu novo recorde",
            "Todos os seus recordes",
            "Partilhe a sua box"
    };

    public String[] slide_text={
            "Esteja sempre a par das novidades da sua box com a nossa APP GestBox",
            "Explore todas as funcionalidades.",
            "Quer saber se vai valer a pena ir à aula do dia de hoje? Veja o plano de treino na nossa APP!",
            "Agora não vai ser problema marcar as aulas.. Com a GestBox pode fazê-lo em qualquer lugar!",
            "Selecione o dia em que quer marcar a sua aula, de forma mais rápida usando o nosso filtro.",
            "Guarde todos os seus recordes pessoais, agora já não há desculpas para se esquecer do seu PR!",
            "Mostre a toda a gente a sua excelente condição física!",
            "Deseja convidar um amigo? Agora é mais simples, partilhe a localização da sua box."
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
