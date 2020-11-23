package com.example.internshipmanagement.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.util.CustomizedLoadingDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {

    private lateinit var loadingDialog: CustomizedLoadingDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getRootLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewOnEventListener()
        setObserverFragment()
        loadingDialog = CustomizedLoadingDialog(requireContext())
    }

    abstract fun getRootLayoutId(): Int
    abstract fun setViewOnEventListener()
    abstract fun setObserverFragment()

    private fun showSnackBarFragment(message: String) {
        activity?.let {
            Snackbar.make(it.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
        }
    }

    fun setBaseObserverFragment(baseViewModel: BaseViewModel) {
        baseViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if(it) {
                loadingDialog.showLoadingDialog()
            } else {
                loadingDialog.dismissDialog()
            }
        })

        baseViewModel.messageResponse.observe(viewLifecycleOwner, Observer {
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

    fun showSnackBar(message: String) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    fun hideBottomNavigationView(activity: FragmentActivity) {
        activity.findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility = View.GONE
    }

    fun showBottomNavigationView(activity: FragmentActivity) {
        activity.findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility = View.VISIBLE
    }

    fun detachFragment(activity: FragmentActivity, fragment: Fragment) {
        activity.supportFragmentManager.beginTransaction().detach(fragment).commit()
    }
}