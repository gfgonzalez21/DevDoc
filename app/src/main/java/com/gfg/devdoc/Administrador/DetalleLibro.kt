package com.gfg.devdoc.Administrador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gfg.devdoc.LeerLibro
import com.gfg.devdoc.R
import com.gfg.devdoc.databinding.ActivityActualizarLibroBinding
import com.gfg.devdoc.databinding.ActivityDetalleLibroBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetalleLibro : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleLibroBinding
    private var idLibro=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetalleLibroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idLibro = intent.getStringExtra("idLibro")!!

        binding.IbRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.BtnLeerLibro.setOnClickListener {
            val intent=Intent(this@DetalleLibro,LeerLibro::class.java)
            intent.putExtra("idLibro",idLibro)
            startActivity(intent)
        }
        cargarDetalleLibro()

    }
    private fun cargarDetalleLibro() {
        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.child(idLibro)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val barrio ="${snapshot.child("categoria").value}"
                    val contadorVisitas ="${snapshot.child("contadorVisitas").value}"
                    val contadorDescargas ="${snapshot.child("contadorDescargas").value}"
                    val descripcion="${snapshot.child("descripcion").value}"
                    val tiempo ="${snapshot.child("tiempo").value}"
                    val titulo ="${snapshot.child("titulo").value}"
                    val url ="${snapshot.child("url").value}"

                    val fecha =MisFunciones.formatoTiempo(tiempo.toLong())
                    MisFunciones.CargarCategoria(barrio,binding.barrioD)

                    MisFunciones.CargarPdfUrl("$url","$titulo",binding.VisualizadorPDF,binding.progressBar)

                    binding.tituloLibroD.text=titulo
                    binding.descripcionD.text=descripcion
                    binding.vistasD.text=contadorVisitas
                    binding.descargasD.text=contadorDescargas
                    binding.fechaD.text=fecha



                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}