package com.gfg.devdoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gfg.devdoc.Fragmentos_admin.Fragment_admin_cuenta
import com.gfg.devdoc.Fragmentos_admin.Fragment_admin_dashboard
import com.gfg.devdoc.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        ComprobarSesion()
        VerFragmentoDashboard()

        binding.BottonNvAdmin.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Menu_panel -> {
                    VerFragmentoDashboard()
                    true
                }
                R.id.Menu_cuenta -> {
                    VerFragmentoCuenta()
                    true
                }
                else -> false
            }
        }
    }

    private fun VerFragmentoDashboard(){
        val nombre_titulo="Dashboard"
        binding.tituloRlAdmin.text=nombre_titulo

        val fragment=Fragment_admin_dashboard()
        val fragmentTransaction=supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsAdmin.id,fragment,"Fragment dashboard")
        fragmentTransaction.commit()
    }
    private fun VerFragmentoCuenta(){
        val nombre_titulo="Mi cuenta"
        binding.tituloRlAdmin.text=nombre_titulo

        val fragment=Fragment_admin_cuenta()
        val fragmentTransaction=supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsAdmin.id,fragment,"Fragment mi cuenta")
        fragmentTransaction.commit()
    }
    private fun ComprobarSesion(){
        val firebaseUser=firebaseAuth.currentUser
        if (firebaseUser==null){
            startActivity(Intent(this,Elegir_rol::class.java))
            finishAffinity()
        }else{
            //Toast.makeText(applicationContext,"Bienvenid@ ${firebaseUser.email}", Toast.LENGTH_SHORT).show()

        }
    }
}