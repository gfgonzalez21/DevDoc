package com.gfg.devdoc.Administrador

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.gfg.devdoc.MainActivity
import com.gfg.devdoc.R
import com.gfg.devdoc.databinding.ActivityLoginAdminBinding
import com.google.firebase.auth.FirebaseAuth

class Login_admin : AppCompatActivity() {

    private lateinit var binding: ActivityLoginAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_login_admin)
        setContentView(binding.root)


        firebaseAuth=FirebaseAuth.getInstance()
        progressDialog=ProgressDialog(this)
        progressDialog.setTitle("Espera por favor")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        binding.BtnLoginAdmin.setOnClickListener{
            ValidarInformacion()
        }



    }
    private var email=""
    private var password=""
    private fun ValidarInformacion() {
        email=binding.EtEmailAdmin.text.toString().trim()
        password=binding.EtPasswordAdmin.text.toString().trim()

        if (email.isEmpty()){
            binding.EtEmailAdmin.error="Ingrese email"
            binding.EtEmailAdmin.requestFocus()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.EtEmailAdmin.error = "Email no valido"
            binding.EtEmailAdmin.requestFocus()
        }
        else if (password.isEmpty()){
            binding.EtPasswordAdmin.error="Ingrese la contraseña"
            binding.EtPasswordAdmin.requestFocus()
        }
        else{
            LoginAdmin()
        }
    }
    private fun LoginAdmin() {
        progressDialog.setMessage("Iniciando sesion")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this@Login_admin,MainActivity::class.java))
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