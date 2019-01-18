package com.apps.brayan.surveyapp.coreapp.fallback;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.brayan.surveyapp.R;

public class FallbackManager {
    private ViewGroup parentView;
    private ConstraintLayout childView;

    public void injectFallback(FallbackCase fallbackCase, ViewGroup parentView){
        if(parentView == null)
            throw new RuntimeException("Debes enviar una vista donde el fallback se pueda alojar");
        if(fallbackCase == null)
            throw  new RuntimeException("Debes enviar un caso de fallback para saber qué información debe saber el usuario (ver clase FallbackCase)");

        if(childView!=null && this.parentView !=null && this.parentView == parentView){
            detachCurrentFallback();
        }

        this.parentView = parentView;
        childView = (ConstraintLayout) LayoutInflater.from(parentView.getContext()).inflate(R.layout.fallback_generic,parentView,false);
        TextView textFallback = childView.findViewById(R.id.error_message);
        textFallback.setText(fallbackCase.getMessage());
        parentView.addView(childView);
    }

    public void detachCurrentFallback(){
        if(childView != null){
            parentView.removeView(childView);
            childView = null;
        }
    }
}
