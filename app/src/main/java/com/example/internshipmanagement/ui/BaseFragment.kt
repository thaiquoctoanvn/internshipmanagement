package com.example.internshipmanagement.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getRootLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewOnEventListener()
    }

    abstract fun getRootLayoutId(): Int
    abstract fun setViewOnEventListener()

    private fun showSnackBarFragment(message: String) {
        activity?.let {
            Snackbar.make(it.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
        }
    }

    fun setBaseObserverFragment(baseViewModel: BaseViewModel) {
        baseViewModel.getIsLoadingValue().observe(viewLifecycleOwner, Observer {

        })

        baseViewModel.getMessageResponseValue().observe(viewLifecycleOwner, Observer {
            showSnackBarFragment(it)
        })
    }

    fun isMentorAccount(type: String): Boolean {
        // 1 - mentor
        // 2 - mentee
        if(type == "1") {
            return true
        }
        return false
    }

}