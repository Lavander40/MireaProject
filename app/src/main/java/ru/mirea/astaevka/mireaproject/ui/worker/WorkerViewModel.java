package ru.mirea.astaevka.mireaproject.ui.worker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WorkerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public WorkerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is worker fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
