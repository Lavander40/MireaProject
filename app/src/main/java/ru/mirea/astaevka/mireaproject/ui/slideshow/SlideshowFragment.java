package ru.mirea.astaevka.mireaproject.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.astaevka.mireaproject.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.textView2.setText("Проект восток - 東方Project");
        binding.textView3.setText("Touhou Project — серия компьютерных игр в жанре даммаку (скролл-шутер), созданная японской компанией Team Shanghai Alice, состоящей из одного человека, известного под псевдонимом ZUN. ZUN в одиночку производит графику, музыку и программный код своих игр.\n" +
                "\n" +
                "Сюжет Touhou Project завязан вокруг странных феноменов, происходящих в Генсокё, вымышленной локации, населённой людьми и ёкаями, сверхъестественными существами. До событий в играх Генсокё был изолирован от внешнего мира магическим барьером. Протагонист серии, синтоистская жрица Рэйму Хакурэй, обеспечивает работоспособность барьера и сражается с враждебно настроенными ёкаями. ");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}