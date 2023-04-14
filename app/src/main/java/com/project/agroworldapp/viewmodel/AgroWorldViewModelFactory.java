package com.project.agroworldapp.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.project.agroworldapp.ui.repository.AgroWorldRepositoryImpl;

public class AgroWorldViewModelFactory implements ViewModelProvider.Factory {
    private AgroWorldRepositoryImpl agroWorldRepository;
    private Context context;

    public AgroWorldViewModelFactory(AgroWorldRepositoryImpl agroWorldRepository, Context context) {
        this.agroWorldRepository = agroWorldRepository;
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass, @NonNull CreationExtras extras) {
        return (T) new AgroViewModel(agroWorldRepository, context);
    }
}
