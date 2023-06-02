package com.example.social_mediavj.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.social_mediavj.AccountSettingActivity
import com.example.social_mediavj.R
import com.example.social_mediavj.databinding.ActivityMainBinding
import com.example.social_mediavj.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.editAccountSettingProfileFra.setOnClickListener{

            startActivity(Intent(context,AccountSettingActivity::class.java))
        }
        return binding.root
    }

}