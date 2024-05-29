package com.gfg.devdoc.Administrador

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.gfg.devdoc.MainActivity
import com.gfg.devdoc.R
import com.gfg.devdoc.databinding.ActivityElegirRolBinding
import com.gfg.devdoc.databinding.ActivityRegistrarAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Registrar_admin : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarAdminBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegistrarAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()
        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        binding.BtnRegistrarAdmin.setOnClickListener {
            validarInformacion()
        }
        binding.TxtTengoCuenta.setOnClickListener {
           startActivity(Intent(this@Registrar_admin,Login_admin::class.java))
        }









    }
    var nombres=""
    var email=""
    var password=""
    var r_password=""

    private fun validarInformacion() {
        nombres=binding.EtNombresAdmin.text.toString().trim()
        email=binding.EtEmailAdmin.text.toString().trim()
        password=binding.EtPasswordAdmin.text.toString().trim()
        r_password=binding.EtRPasswordAdmin.text.toString().trim()

        if (nombres.isEmpty()){
            binding.EtNombresAdmin.error="Ingrese nombres"
            binding.EtNombresAdmin.requestFocus()
        }
        else if (email.isEmpty()){
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
        else if (password.length<6){
            binding.EtPasswordAdmin.error="La contraseña debe de tener mas de 6 caracteres"
            binding.EtPasswordAdmin.requestFocus()
        }
        else if (r_password.isEmpty()){
            binding.EtRPasswordAdmin.error="Repita la contraseña"
            binding.EtRPasswordAdmin.requestFocus()
        }
        else if (password!=r_password){
            binding.EtRPasswordAdmin.error="Las contraseñas no coinciden"
            binding.EtRPasswordAdmin.requestFocus()
        }
        else{
            CrearCuentaAdmin(email,password)
        }
    }
    private fun CrearCuentaAdmin(email: String, password: String) {
        progressDialog.setMessage("Creando cuenta")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                AgregarInfoDb()
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"Ha fallado la creacion de la cuemta debido a ${e.message}",
                    Toast.LENGTH_SHORT)
                    .show()
            }
    }
    private fun AgregarInfoDb() {
        progressDialog.setMessage("Guardando informacion")
        val tiempo =System.currentTimeMillis()
        val uid=firebaseAuth.uid

        val datos_admin : HashMap<String,Any?> = HashMap()
        datos_admin["uid"]=uid
        datos_admin["nombres"]=nombres
        datos_admin["email"]=email
        datos_admin["rol"]="admin"
        datos_admin["tiempo_registro"]=tiempo
        datos_admin["imagen"]=""


        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        reference.child(uid!!)
            .setValue(datos_admin)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"Cuenta creada",Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this,MainActivity::class.java))
                finishAffinity()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"No se pudo guardar la informacion debido a ${e.message}",Toast.LENGTH_SHORT)
                    .show()
            }


    }
}