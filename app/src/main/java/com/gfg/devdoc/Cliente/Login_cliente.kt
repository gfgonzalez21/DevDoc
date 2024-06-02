package com.gfg.devdoc.Cliente

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.gfg.devdoc.MainActivityCliente
import com.gfg.devdoc.R
import com.gfg.devdoc.databinding.ActivityLoginClienteBinding
import com.google.firebase.auth.FirebaseAuth

class Login_cliente : AppCompatActivity() {

    private lateinit var binding: ActivityLoginClienteBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {

        binding=ActivityLoginClienteBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()
        progressDialog=ProgressDialog(this)
        progressDialog.setTitle("Espera por favor")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
        binding.BtnLoginCliente.setOnClickListener{
            ValidarInformacion()
        }


    }
    private var email=""
    private var password=""
    private var rol=""
    private fun ValidarInformacion() {
        email=binding.EtEmailCliente.text.toString().trim()
        password=binding.EtPasswordCliente.text.toString().trim()

        if (email.isEmpty()){
            binding.EtEmailCliente.error="Ingrese email"
            binding.EtEmailCliente.requestFocus()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.EtEmailCliente.error = "Email no valido"
            binding.EtEmailCliente.requestFocus()
        }
        else if (password.isEmpty()){
            binding.EtPasswordCliente.error="Ingrese la contraseña"
            binding.EtPasswordCliente.requestFocus()
        }
        else{
            LoginCliente()
        }
    }
    private fun LoginCliente() {
        progressDialog.setMessage("Iniciando sesion")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this@Login_cliente, MainActivityCliente::class.java))
                finishAffinity()
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"No se pudo iniciar sesion  debido a ${e.message}",
                    Toast.LENGTH_SHORT)
                    .show()
            }
    }
}