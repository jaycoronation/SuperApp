package com.application.alphacapital.superapp.pms.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.alphaestatevault.utils.AppUtils;
import com.application.alphacapital.superapp.R;
import com.application.alphacapital.superapp.databinding.ContactFragmentBinding;
import com.application.alphacapital.superapp.pms.network.PortfolioApiClient;
import com.application.alphacapital.superapp.pms.network.PortfolioApiInterface;
import com.application.alphacapital.superapp.pms.utils.SessionManager;

public class ContactFragment extends Fragment
{
    private Activity activity ;
    private ContactFragmentBinding binding;
    private SessionManager sessionManager ;
    private PortfolioApiInterface apiService;
    private AppUtils appUtils;


    public ContactFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activity = getActivity() ;
        sessionManager = new SessionManager(activity);
        appUtils = new AppUtils();
        apiService = PortfolioApiClient.getClient().create(PortfolioApiInterface.class);
        binding = DataBindingUtil.inflate(inflater,R.layout.contact_fragment, container, false);
        initViews();
        return binding.getRoot();
    }

    private void initViews()
    {

        binding.edtFullName.setText(sessionManager.getFirstName() + " " +sessionManager.getLastName());
        binding.edtEmail.setText(sessionManager.getEmail());

        binding.tvbtnsave.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                if(validation())
                {
                    
                }

            }
        });
    }

    private boolean validation()
    {
        if (binding.edtFullName.getText().length() == 0)
        {
            Toast.makeText(activity, "Please Enter a Full name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.edtContactNumber.getText().length() == 0)
        {
            Toast.makeText(activity, "Please Enter Your Contact Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.edtContactNumber.getText().length() < 10)
        {
            Toast.makeText(activity, "Contact Number Must be required 10 digit", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.edtEmail.getText().length() == 0)
        {
            Toast.makeText(activity, "Please Enter Your Email Id.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (appUtils.isEmailValid(binding.edtEmail.getText().toString()))
        {
            Toast.makeText(activity, "Please Enter Valid Email Id", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(binding.edtMessage.getText().length() == 0)
        {
            Toast.makeText(activity, "Please Enter Your Message.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
