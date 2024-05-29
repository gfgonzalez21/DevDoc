package com.gfg.devdoc.Administrador

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gfg.devdoc.MainActivity
import com.gfg.devdoc.R
import com.gfg.devdoc.databinding.ActivityAgregarCategoriaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AgregarCategoria : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarCategoriaBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAgregarCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()
        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.IbRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.AgregarCategoriaBd.setOnClickListener {
            ValidarDatos()
        }

    }



    private var categoria=""
    private fun ValidarDatos() {
        categoria=binding.EtCategoria.text.toString().trim()
        if(categoria.isEmpty()){
            Toast.makeText(applicationContext,"Ingrese un barrio", Toast.LENGTH_SHORT).show()
        }else{
            AgregarCategoriaBd()
        }
    }

    private fun AgregarCategoriaBd() {
        progressDialog.setMessage("Agregando Categoria")
        progressDialog.show()

        val tiempo=System.currentTimeMillis()

        val hashMap=HashMap<String,Any>()

        hashMap["id"]="$tiempo"
        hashMap["categoria"]=categoria
        hashMap["tiempo"]=tiempo
        hashMap["uid"]="${firebaseAuth.uid}"

        val ref= FirebaseDatabase.getInstance().getReference("Categorias")
        ref.child("$tiempo")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"Se agrego la categoria a la BD",Toast.LENGTH_SHORT).show()
                binding.EtCategoria.setText("")
                startActivity(Intent(this@AgregarCategoria,MainActivity::class.java))
                finishAffinity()
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"No se agrego la categoria debiado a ${e.message}",Toast.LENGTH_SHORT).show()

            }
    }
}