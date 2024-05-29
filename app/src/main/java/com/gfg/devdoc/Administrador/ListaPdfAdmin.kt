package com.gfg.devdoc.Administrador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.gfg.devdoc.R
import com.gfg.devdoc.databinding.ActivityListaPdfAdminBinding
import com.gfg.devdoc.databinding.ItemLibroAdminBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class ListaPdfAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityListaPdfAdminBinding
    private var idCategoria=""
    private var tituloCategoria=""
    private lateinit var pdfArrayList: ArrayList<ModeloPdf>
    private lateinit var adaptadorPdfAdmin: AdaptadorPdfAdmin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityListaPdfAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val intent=intent
        idCategoria = intent.getStringExtra("idCategoria")!!
        tituloCategoria = intent.getStringExtra("tituloCategoria")!!

        binding.TxtCategoriaLibro.text=tituloCategoria

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
        ListarLibros()

    }
    private fun ListarLibros() {
        pdfArrayList= ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.orderByChild("categoria").equalTo(idCategoria)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    pdfArrayList.clear()
                    for (ds in snapshot.children){
                        val modelo = ds.getValue(ModeloPdf::class.java)
                        if (modelo!=null){
                            pdfArrayList.add(modelo)
                        }
                    }
                    adaptadorPdfAdmin=AdaptadorPdfAdmin(this@ListaPdfAdmin,pdfArrayList)
                    binding.RvLibrosAdmin.adapter=adaptadorPdfAdmin
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}