package com.gfg.devdoc.Cliente

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.gfg.devdoc.Administrador.ModeloPdf
import com.gfg.devdoc.R
import com.gfg.devdoc.databinding.ActivityListaPdfClienteBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class ListaPdfCliente : AppCompatActivity() {

    private lateinit var binding: ActivityListaPdfClienteBinding
    private var idCategoria =""
    private var tituloCategoria=""

    private lateinit var pdfArrayList: ArrayList<ModeloPdf>
    private lateinit var adaptadorPdfCliente: AdaptadorPdfCliente


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityListaPdfClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val intent= intent
        idCategoria = intent.getStringExtra("idCategoria")!!
        tituloCategoria=intent.getStringExtra("tituloCategoria")!!


        binding.TxtCategoriaLibro.text=tituloCategoria

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
        cargarLibros()
        binding.EtBuscarLibroCliente.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(casa: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    adaptadorPdfCliente.filter.filter(casa)
                }catch (e: Exception){

                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

    }
    private fun cargarLibros() {
        pdfArrayList= ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.orderByChild("categoria").equalTo(idCategoria)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    pdfArrayList.clear()
                    for(ds in snapshot.children){
                        val modelo =ds.getValue(ModeloPdf::class.java)
                        if (modelo!=null){
                            pdfArrayList.add(modelo)
                        }
                    }
                    adaptadorPdfCliente=AdaptadorPdfCliente(this@ListaPdfCliente,pdfArrayList)
                    binding.RvLibrosCliente.adapter=adaptadorPdfCliente
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}