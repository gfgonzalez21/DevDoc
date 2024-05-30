package com.gfg.devdoc.Cliente

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.gfg.devdoc.MainActivityCliente
import com.gfg.devdoc.R
import com.gfg.devdoc.databinding.ActivityRegistroClienteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Registro_cliente : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroClienteBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegistroClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()
        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        binding.BtnRegistrarCliente.setOnClickListener {
            validarInformacion()
        }
        binding.TxtTengoCuenta.setOnClickListener {
            startActivity(Intent(this@Registro_cliente,Login_cliente::class.java))
        }

    }
    var nombres=""
    var edad=""
    var email=""
    var password=""
    var r_password=""
    private fun validarInformacion() {
        nombres=binding.EtNombresCliente.text.toString().trim()
        edad=binding.EtEdadCliente.text.toString().trim()
        email=binding.EtEmailCliente.text.toString().trim()
        password=binding.EtPasswordCliente.text.toString().trim()
        r_password=binding.EtRPasswordCliente.text.toString().trim()
        if (nombres.isEmpty()){
            binding.EtNombresCliente.error="Ingrese nombres"
            binding.EtNombresCliente.requestFocus()
        }
        else if (edad.isEmpty()){
            binding.EtEdadCliente.error="Ingrese edad"
            binding.EtEdadCliente.requestFocus()
        }
        else if (email.isEmpty()){
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
        else if (password.length<6){
            binding.EtPasswordCliente.error="La contraseña debe de tener mas de 6 caracteres"
            binding.EtPasswordCliente.requestFocus()
        }
        else if (r_password.isEmpty()){
            binding.EtRPasswordCliente.error="Repita la contraseña"
            binding.EtRPasswordCliente.requestFocus()
        }
        else if (password!=r_password){
            binding.EtRPasswordCliente.error="Las contraseñas no coinciden"
            binding.EtRPasswordCliente.requestFocus()
        }
        else{
            CrearCuentaCliente(email,password)
        }
    }
    private fun CrearCuentaCliente(email: String, password: String) {
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

        val uid=firebaseAuth.uid!!

        val datos_cliente : HashMap<String,Any?> = HashMap()

        datos_cliente["uid"]=uid
        datos_cliente["nombres"]=nombres
        datos_cliente["edad"]=edad
        datos_cliente["email"]=email
        datos_cliente["rol"]="cliente"
        datos_cliente["tiempo_registro"]=tiempo
        datos_cliente["imagen"]=""


        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        reference.child(uid!!)
            .setValue(datos_cliente)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"Cuenta creada",Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this, MainActivityCliente::class.java))
                finishAffinity()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"No se pudo guardar la informacion debido a ${e.message}",Toast.LENGTH_SHORT)
                    .show()
            }


    }
}