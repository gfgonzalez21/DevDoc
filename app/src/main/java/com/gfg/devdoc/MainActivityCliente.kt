package com.gfg.devdoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gfg.devdoc.Fragmentos_cliente.Fragment_cliente_cuenta
import com.gfg.devdoc.Fragmentos_cliente.Fragment_cliente_dashboard
import com.gfg.devdoc.Fragmentos_cliente.Fragment_cliente_favoritos
import com.gfg.devdoc.databinding.ActivityMainClienteBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivityCliente : AppCompatActivity() {
    private lateinit var binding: ActivityMainClienteBinding
    private lateinit var firebaseAuth : FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding=ActivityMainClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        ComprobarSesion()


        binding.buttonNvCliente.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Menu_dashboard_cl -> {
                    VerFragmentoDashboard()
                    true
                }
                R.id.Menu_favoritos_cl -> {
                    VerFragmentoFavoritos()
                    true
                }
                R.id.Menu_cuenta_cl -> {
                    VerFragmentoCuenta()
                    true
                }
                else -> false
            }
        }
    }
    private fun VerFragmentoDashboard(){
        val nombre_titulo="Dashboard"
        binding.TituloRLCliente.text=nombre_titulo

        val fragment= Fragment_cliente_dashboard()
        val fragmentTransaction=supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsCliente.id,fragment,"Fragment dashboard")
        fragmentTransaction.commit()

    }
    private fun VerFragmentoFavoritos(){
        val nombre_titulo="Favoritos"
        binding.TituloRLCliente.text=nombre_titulo

        val fragment=Fragment_cliente_favoritos()
        val fragmentTransaction=supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsCliente.id,fragment,"Fragment mi cuenta")
        fragmentTransaction.commit()

    }
    private fun VerFragmentoCuenta(){
        val nombre_titulo="Mi cuenta"
        binding.TituloRLCliente.text=nombre_titulo

        val fragment= Fragment_cliente_cuenta()
        val fragmentTransaction=supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsCliente.id,fragment,"Fragment mi cuenta")
        fragmentTransaction.commit()
    }

    private fun ComprobarSesion() {
        val firebaseUser=firebaseAuth.currentUser
        if (firebaseUser==null){
            startActivity(Intent(this,Elegir_rol::class.java))
            finishAffinity()
        }else{
            Toast.makeText(applicationContext,"Bienvenid@ ${firebaseUser.email}", Toast.LENGTH_SHORT).show()

        }
    }
}