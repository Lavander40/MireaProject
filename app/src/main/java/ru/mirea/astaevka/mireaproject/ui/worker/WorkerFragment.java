package ru.mirea.astaevka.mireaproject.ui.worker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mirea.astaevka.mireaproject.databinding.FragmentSlideshowBinding;
import ru.mirea.astaevka.mireaproject.databinding.FragmentWorkerBinding;
import ru.mirea.astaevka.mireaproject.ui.slideshow.SlideshowViewModel;

public class WorkerFragment extends Fragment {

    private FragmentWorkerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WorkerViewModel slideshowViewModel =
                new ViewModelProvider(this).get(WorkerViewModel.class);

        binding = FragmentWorkerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(UploadWorker.class)
                        .build();
        WorkManager
                .getInstance(this.getContext())
                .enqueue(uploadWorkRequest);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}