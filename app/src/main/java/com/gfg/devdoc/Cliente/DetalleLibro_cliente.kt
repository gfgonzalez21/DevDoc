package com.gfg.devdoc.Cliente

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.gfg.devdoc.Administrador.Constantes
import com.gfg.devdoc.Administrador.MisFunciones
import com.gfg.devdoc.LeerLibro
import com.gfg.devdoc.R
import com.gfg.devdoc.databinding.ActivityDetalleLibroClienteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.FileOutputStream

class DetalleLibro_cliente : AppCompatActivity() {

    private lateinit var  binding: ActivityDetalleLibroClienteBinding
    private var idLibro=""
    private var tituloLibro=""
    private var urlLibro=""

    private var esfavorito=false
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetalleLibroClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idLibro=intent.getStringExtra("idLibro")!!

        MisFunciones.incrementarVisitas(idLibro)
        firebaseAuth=FirebaseAuth.getInstance()

        progressDialog=ProgressDialog(this)
        progressDialog.setTitle("Espera por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
        binding.BtnLeerLibro.setOnClickListener {
            val intent= Intent(this@DetalleLibro_cliente, LeerLibro::class.java)
            intent.putExtra("idLibro",idLibro)
            startActivity(intent)
        }
        binding.BtnDescargarCliente.setOnClickListener{
            Toast.makeText(applicationContext,"Descargar documentacion",Toast.LENGTH_SHORT).show()
            descargarLibro()
        }

        binding.BtnFavoritosCliente.setOnClickListener {
            if (esfavorito){
                MisFunciones.EliminarFavoritos(this@DetalleLibro_cliente,idLibro)
            }else{
                AgregarFavoritos()
            }

        }
        comprobarFavorito()
        cargarDetalleLibro()
    }
    private fun comprobarFavorito() {
        val ref =FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!).child("Favoritos").child(idLibro)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    esfavorito=snapshot.exists()
                    if (esfavorito){
                        binding.BtnFavoritosCliente.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            R.drawable.ic_favoritos,
                            0,
                            0
                        )
                        binding.BtnFavoritosCliente.text="Eliminar de favoritos"
                    }else{
                        binding.BtnFavoritosCliente.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            R.drawable.ic_no_favorito,
                            0,
                            0
                        )
                        binding.BtnFavoritosCliente.text="Agregar a favoritos"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun AgregarFavoritos(){
        val tiempo =System.currentTimeMillis()
        val hashMap =HashMap<String,Any>()
        hashMap["id"]=idLibro
        hashMap["tiempo"]=tiempo

        val ref =FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!).child("Favoritos").child(idLibro)
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(applicationContext,"Documentacion agregada a favoritos",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                Toast.makeText(applicationContext,"No se agrego a favoritos debido a ${e.message}",Toast.LENGTH_SHORT).show()
            }

    }

    private fun descargarLibro() {
        progressDialog.setMessage("Descargando documentacion")
        progressDialog.show()
        val storageReference=FirebaseStorage.getInstance().getReferenceFromUrl(urlLibro)
        storageReference.getBytes(Constantes.Maximo_bytes_pdf)
            .addOnSuccessListener {bytes->
                guardarLibroDisp(bytes)
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"${e.message}",
                    Toast.LENGTH_SHORT)
                    .show()
            }

    }

    private fun guardarLibroDisp(bytes:ByteArray) {
        val nombreLibro_extension ="$tituloLibro.pdf"
        try {
            val carpeta =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            carpeta.mkdirs()
            val archivo_ruta = carpeta.path+"/"+nombreLibro_extension
            val out =FileOutputStream(archivo_ruta)
            out.write(bytes)
            out.close()

            Toast.makeText(applicationContext,"Libro guardado con exito",
                Toast.LENGTH_SHORT)
                .show()
            progressDialog.dismiss()
            incrementarNumDes()

        }catch (e:Exception){
            progressDialog.dismiss()
            Toast.makeText(applicationContext,"${e.message}",
                Toast.LENGTH_SHORT)
                .show()
            progressDialog.dismiss()
        }
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
                    tituloLibro ="${snapshot.child("titulo").value}"
                    urlLibro ="${snapshot.child("url").value}"

                    val fecha = MisFunciones.formatoTiempo(tiempo.toLong())
                    MisFunciones.CargarCategoria(barrio,binding.barrioD)

                    MisFunciones.CargarPdfUrl("$urlLibro","$tituloLibro",binding.VisualizadorPDF,binding.progressBar)

                    binding.tituloLibroD.text=tituloLibro
                    binding.descripcionD.text=descripcion
                    binding.vistasD.text=contadorVisitas
                    binding.descargasD.text=contadorDescargas
                    binding.fechaD.text=fecha



                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun incrementarNumDes(){
        val ref =FirebaseDatabase.getInstance().getReference("Libros")
        ref.child(idLibro)
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var contDescarActual = "${snapshot.child("contadorDescargas").value}"
                    if (contDescarActual==""|| contDescarActual=="null"){
                        contDescarActual= "0"

                    }
                    val nuevaDes=contDescarActual.toLong()+1
                    val hashMap=HashMap<String,Any>()
                    hashMap["contadorDescargas"]=nuevaDes

                    val BDRef =FirebaseDatabase.getInstance().getReference("Libros")
                    BDRef.child(idLibro)
                        .updateChildren(hashMap)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

    }
}