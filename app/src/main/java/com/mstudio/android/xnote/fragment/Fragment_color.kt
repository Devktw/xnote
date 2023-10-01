package com.mstudio.android.xnote.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mstudio.android.xnote.R

class Fragment_color : Fragment() {
    private lateinit var recycleview : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_color, container, false)
        recycleview = view.findViewById(R.id.recycleview)

        return view
    }
}