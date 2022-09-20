package com.example.pagination_github.ui.auto_loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pagination_github.R
import com.example.pagination_github.data.EmulateResponseManager
import com.example.pagination_github.data.Item
import com.example.pagination_github.databinding.FragmentAutoLoadingBinding
import com.example.pagination_github.untils.auto_loading.AutoLoadingRecyclerView
import com.example.pagination_github.untils.auto_loading.ILoading
import com.example.pagination_github.untils.auto_loading.OffsetAndLimit


class AutoLoadingFragment : Fragment() {

    private val LIMIT = 50
    private var recyclerView: AutoLoadingRecyclerView<Item>? = null
    private var recyclerViewAdapter: LoadingRecyclerViewAdapter? = null

    private var _binding: FragmentAutoLoadingBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAutoLoadingBinding.inflate(inflater, container, false)
        val rootView = binding.root
        retainInstance = true;
        init(rootView, savedInstanceState);
        return rootView;
    }

    private fun init(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById<AutoLoadingRecyclerView>(R.id.RecyclerView)
    }

}