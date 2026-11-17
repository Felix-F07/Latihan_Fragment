
package com.example.latihan_fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class KaliFragment : Fragment(R.layout.fragment_kali) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvHistory = view.findViewById<TextView>(R.id.tvHistory)

        tvHistory.text = arguments?.getString("history") ?: "Belum ada riwayat operasi."
    }
}
