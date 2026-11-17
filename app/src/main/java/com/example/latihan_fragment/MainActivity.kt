package com.example.latihan_fragment

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import android.graphics.Color
import android.widget.LinearLayout


class MainActivity : AppCompatActivity() {
    private lateinit var etAngka1: EditText
    private lateinit var etAngka2: EditText
    private lateinit var tvHasil: TextView
    private val historyTambah = arrayListOf<String>()
    private val historyKurang = arrayListOf<String>()
    private val historyKali = arrayListOf<String>()
    private val historyBagi = arrayListOf<String>()
    private lateinit var mainLayout: LinearLayout
    private var hasilNegatif = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etAngka1 = findViewById(R.id.etAngka1)
        etAngka2 = findViewById(R.id.etAngka2)
        tvHasil = findViewById(R.id.tvHasil)
        mainLayout = findViewById(R.id.main)
        mainLayout.setBackgroundColor(Color.WHITE)


        if (savedInstanceState != null) {
            historyTambah.addAll(
                savedInstanceState.getStringArrayList("historyTambah")
                    ?: arrayListOf()
            )
            historyKurang.addAll(
                savedInstanceState.getStringArrayList("historyKurang")
                    ?: arrayListOf()
            )
            historyKali.addAll(
                savedInstanceState.getStringArrayList("historyKali")
                    ?: arrayListOf()
            )
            historyBagi.addAll(
                savedInstanceState.getStringArrayList("historyBagi")
                    ?: arrayListOf()
            )

            hasilNegatif = savedInstanceState.getBoolean("hasilNegatif", false)
            tvHasil.text = savedInstanceState.getString("teksHasil", "Hasil: -")
        }

        mainLayout.setBackgroundColor(
            if (hasilNegatif) Color.RED
            else Color.WHITE
        )



        findViewById<Button>(R.id.btnKali).setOnClickListener {
            hitung("*")
        }

        findViewById<Button>(R.id.btnBagi).setOnClickListener {
            hitung("/")
        }

        findViewById<Button>(R.id.btnTambah).setOnClickListener {
            hitung("+")
        }

        findViewById<Button>(R.id.btnKurang).setOnClickListener {
            hitung("-")
        }

        if (savedInstanceState == null) {
            replaceFragment("+")
        }
    }

    private fun hitung(operasi: String) {

        val angka1 = etAngka1.text.toString().toDoubleOrNull()
        val angka2 = etAngka2.text.toString().toDoubleOrNull()

        if (angka1 == null || angka2 == null) {
            Toast.makeText(
                this,
                "Masukkan kedua angka terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (operasi == "/" && angka2 == 0.0) {
            Toast.makeText(
                this,
                "Tidak boleh membagi dengan nol",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val hasil = when (operasi) {
            "*" -> angka1 * angka2
            "/" -> angka1 / angka2
            "+" -> angka1 + angka2
            "-" -> angka1 - angka2
            else -> return
        }

        val catatan = "${formatAngka(angka1)} $operasi ${formatAngka(angka2)} = ${formatAngka(hasil)}"

        when (operasi) {
            "*" -> historyKali.add(catatan)
            "/" -> historyBagi.add(catatan)
            "+" -> historyTambah.add(catatan)
            "-" -> historyKurang.add(catatan)
        }

        tvHasil.text = "Hasil: ${formatAngka(hasil)}"

        hasilNegatif = hasil < 0.0
        mainLayout.setBackgroundColor(
            if (hasilNegatif) Color.RED
            else Color.WHITE
        )


        replaceFragment(operasi)
    }

    private fun replaceFragment(operasi: String) {

        val fragment: Fragment
        val history: ArrayList<String>

        when (operasi) {
            "*" -> {
                fragment = KaliFragment()
                history = historyKali
            }

            "/" -> {
                fragment = BagiFragment()
                history = historyBagi
            }

            "-" -> {
                fragment = KurangFragment()
                history = historyKurang
            }

            else -> {
                fragment = TambahFragment()
                history = historyTambah
            }
        }

        val dataKirim = Bundle().apply {
            putString(
                "history",
                if (history.isEmpty()) {
                    "Belum ada riwayat operasi."
                } else {
                    history.joinToString("\n")
                }
            )
        }

        fragment.arguments = dataKirim

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun formatAngka(angka: Double): String {
        return if (
            angka.isFinite() &&
            angka % 1.0 == 0.0 &&
            angka >= Long.MIN_VALUE.toDouble() &&
            angka < Long.MAX_VALUE.toDouble()
        ) {
            angka.toLong().toString()
        } else {
            angka.toString()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList(
            "historyTambah",
            ArrayList(historyTambah)
        )
        outState.putStringArrayList(
            "historyKurang",
            ArrayList(historyKurang)
        )
        outState.putStringArrayList(
            "historyKali",
            ArrayList(historyKali)
        )
        outState.putStringArrayList(
            "historyBagi",
            ArrayList(historyBagi)
        )
        outState.putBoolean("hasilNegatif", hasilNegatif)
        outState.putString(
            "teksHasil",
            tvHasil.text.toString()
        )
        super.onSaveInstanceState(outState)
    }
}
