package org.yangosoft.cst2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.yangosoft.cst2.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });


        binding.buttonStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               start();
            }
        });

        binding.buttonStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void start()
    {
        android.content.Intent serviceIntent = new android.content.Intent(getActivity(), ConnectorSrv.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        //ContextCompat.startForegroundService(getActivity(), serviceIntent);
        getActivity().startForegroundService(serviceIntent);
    }

    public void stop()
    {
        Intent serviceIntent = new Intent(getActivity(), ConnectorSrv.class);

        getActivity().stopService(serviceIntent);

    }

}