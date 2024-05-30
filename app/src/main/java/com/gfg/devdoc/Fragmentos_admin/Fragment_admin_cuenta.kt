package com.gfg.devdoc.Fragmentos_admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gfg.devdoc.Administrador.MisFunciones
import com.gfg.devdoc.Elegir_rol
import com.gfg.devdoc.R
import com.gfg.devdoc.databinding.ActivityRegistrarAdminBinding
import com.gfg.devdoc.databinding.FragmentAdminCuentaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Fragment_admin_cuenta : Fragment() {

private lateinit var binding: FragmentAdminCuentaBinding
private lateinit var firebaseAuth: FirebaseAuth
private lateinit var mcontext: Context

    override fun onAttach(context: Context) {
        mcontext=context
        super.onAttach(context)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=FragmentAdminCuentaBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth=FirebaseAuth.getInstance()

        cargarInformacion()


        binding.CerrarSesionAdmin.setOnClickListener {
            firebaseAuth.signOut()
            firebaseAuth.signOut()
            startActivity(Intent(context,Elegir_rol::class.java))
            activity?.finishAffinity()
        }
    }
    private fun cargarInformacion() {
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child("${firebaseAuth.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombres="${snapshot.child("nombres").value}"
                    val email="${snapshot.child("email").value}"
                    var t_registro="${snapshot.child("tiempo_registro").value}"
                    val rol="${snapshot.child("rol").value}"

                    if (t_registro=="null"){
                        t_registro="0"
                    }
                    val formatoFecha=MisFunciones.formatoTiempo(t_registro.toLong())
                    binding.TxtNombresAdmin.text=nombres
                    binding.TxtEmailAdmin.text=email
                    binding.TxtTiempoRegistroAdmin.text=formatoFecha
                    binding.TxtRolAdmin.text=rol


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}